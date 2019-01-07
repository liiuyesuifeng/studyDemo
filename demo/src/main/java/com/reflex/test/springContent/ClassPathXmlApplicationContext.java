package com.reflex.test.springContent;

import com.demo.domain.proxy.DynamicProxyHandle;
import com.utils.DOMUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassPathXmlApplicationContext implements ApplicationContext {
    private static ClassPathXmlApplicationContext classContext;
    private static final String FILE_NAME = "applicationContext.xml";//读取的xml文件的名称
    private static SAXReader reader;//xml文件读取对象
    private static final String CURRENT_PATH = System.getProperty("user.dir");//读取当前工路径
    private static Document doc = null;//xml文档本身
    private static boolean isProxy = false;
    private List<Class<?>> classList = new ArrayList<>();

    static {
        reader = new SAXReader();
    }

    private ClassPathXmlApplicationContext() {
        try {

            doc = reader.read(new File(CURRENT_PATH + "\\src\\main\\resources\\" + FILE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static ClassPathXmlApplicationContext getInstance() {
        if (classContext == null) {
            classContext = new ClassPathXmlApplicationContext();
        }
        return classContext;
    }

    @Override
    public Object getBean(String beanid) {
        try {
            //获取主节点
            Element beanNode = DOMUtil.getBeanNode(doc, beanid);
            //获取class中的属性值
            String className = beanNode.attributeValue("class");
            //反射获取class中的字节码文件
            Class<?> aClass = Class.forName(className);
            //获取当前节点下所有property节点
            List<Element> property = (List<Element>) beanNode.selectNodes("property");
            //判断是否使用代理模式，代理实现类
            if (aClass == DynamicProxyHandle.class) {
                isProxy = true;
            }
            Object ob = null;
            //判断property节点是否为空
            if (property != null && property.size() > 0) {
                //遍历property节点
                for (Element propertyNode : property) {
                    //获取当前节点下所有value节点，value节点为存放当前对象所实现的接口信息
                    List<Element> values = (List<Element>) propertyNode.selectNodes("value");
                    if (values != null && values.size() > 0) {
                        for (Element value : values) {
                            //获取接口文件的信息，并存放在List中，为后面反射所需内容
                            Class<?> interClass = Class.forName(value.getText());
                            classList.add(interClass);
                        }
                    } else {
                        //创建当前对象全
                        String di_ref = propertyNode.attributeValue("ref");
                        //判断当前对象是否存在依赖
                        if (StringUtils.isEmpty(di_ref)) {
                            ob = injectionFormObject(beanid);
                        } else {
                            ob = injectionFormObject(di_ref);
                        }
                    }
                }
            } else {
                ob = injectionFormObject(beanid);
            }
            if (isProxy) {
                //结合代理模式，获取代理接口的所有信息
                Class<?>[] interfaces = new Class<?>[classList.size()];
                classList.toArray(interfaces);
                return Proxy.newProxyInstance(ob.getClass().getClassLoader(), interfaces, new DynamicProxyHandle(ob));
            } else {
                return ob;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    public static  void main(String [] args){
//        try{
//
//            doc=reader.read(new File(CURRENT_PATH+"\\src\\main\\resources\\"+FILE_NAME));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        Element beanNode= DOMUtil.getBeanNode(doc,"proxyBean");
//        List<Element> property = (List<Element>)beanNode.selectNodes("property");
//        for(Element proty:property){
//            Element valueProty= (Element) proty.selectSingleNode("value");
//            String value=valueProty.getText();
//            String protyName=proty.attributeValue("name");
//            String protyRef=proty.attributeValue("ref");
//            System.out.println(protyName+"**********"+protyRef+"*******"+value);
//        }
//
//    }
    private Object injectionFormObject(String beanid) {
        //xml文档
        Object obj = null;//目标表创建出来的实例
        try {
            Element beanNode = DOMUtil.getBeanNode(doc, beanid);
            Element propertyNode = DOMUtil.getPropertyNode(beanNode, "property");
            //获取class中的属性值
            String className = beanNode.attributeValue("class");
            Class<?> aClass = Class.forName(className);
            obj = aClass.newInstance();
            //获取property中实力话对象节点
            if (propertyNode != null) {
                System.out.println("当前bean有属性需要注入");
                //获取当前name中的属性值
                String propertyName = propertyNode.attributeValue("name");
                System.out.println("当前bean需要注入的属性为" + propertyName);
                //拼接出注入方法
                String setMethod = "set" + (propertyName.substring(0, 1)).toUpperCase() + propertyName.substring(1, propertyName.length());
                System.out.println("自动调用注入方法" + setMethod);
                //获取节点为ref的值
                String set_object_name = propertyNode.attributeValue("ref");
                System.out.println("需要注入的对象名" + set_object_name);
                Object di_object = injectionFormObject(set_object_name);
                System.out.println("注入的对象实例" + di_object);
                Method[] methods = obj.getClass().getMethods();
                for (Method m : methods) {
                    if (setMethod.equals(m.getName())) {
                        m.invoke(obj, di_object);
                        break;
                    }
                }
            } else {
                System.out.println("当前bean没有属性，无需注入直接结束");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
