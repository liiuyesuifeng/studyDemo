package com.utils;

import org.dom4j.Document;
import org.dom4j.Element;

public class DOMUtil {
    //获取BeanNode操作节点
    public static Element getBeanNode(Document doc, String beanid) {
        String xpath = "/beans/bean[@id='" + beanid + "']";
        //去取这个ID下的node对象
        Element beanNode = (Element) doc.selectSingleNode(xpath);
        return beanNode;
    }

    public static Element getPropertyNode(Element beanNode, String propertyName) {
        Element propertyNode = (Element) beanNode.selectSingleNode(propertyName);
        return propertyNode;
    }
}
