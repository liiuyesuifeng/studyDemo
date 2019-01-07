package com.test;

import com.utils.PrintUtil;
import org.junit.Test;

public class EnumTest {
    enum Shrubbery{
        GROUP,CARWLING,HANGING
    }
    @Test
    public void test1(){
        for(Shrubbery s : Shrubbery.values()){
            PrintUtil.print(s.ordinal());
            PrintUtil.print(s.compareTo(Shrubbery.CARWLING));
            PrintUtil.print(s.equals(Shrubbery.CARWLING));
            PrintUtil.print(s.getDeclaringClass());
            PrintUtil.print(s.name());
            PrintUtil.print("-----------------");
        }
        for(String s : "GROUP CARWLING HANGING".split(" ")){
            Shrubbery sh = Enum.valueOf(Shrubbery.class,s);
            PrintUtil.print(sh);
        }
    }

}


