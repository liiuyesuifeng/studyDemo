package com.test;

import com.utils.PrintUtil;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;

public enum OzWitch {
    WEST("MISS Gulh"),NORTH("AAAAA"),EAST("BBBBBB"),SOUTH("CCCCCC");
    private String descirtion;
    private OzWitch(String descirtion){
        this.descirtion = descirtion;
    }

    public String getDescirtion() {
        return descirtion;
    }
    public static void  main(String [] agrs){
        for(OzWitch oz : OzWitch.values()){
            PrintUtil.print(oz.getDescirtion());
        }
        analyze(OzWitch.class);
    }
    public static void analyze (Class <? extends Enum>  enumClass){
       PrintUtil.print("--------开始 解析--------");
       PrintUtil.print("-----接口解析----------");
        Type[] genericInterfaces = enumClass.getGenericInterfaces();
        for(Type tyep : genericInterfaces){
            PrintUtil.print(tyep.getTypeName());
            PrintUtil.print("-----接口解析----------");
        }

        Method[] methods = enumClass.getMethods();
        for(Method method : methods){
            PrintUtil.print(method.getName());
            PrintUtil.print("------解析方法-----");
        }
    }

}

