package com.generics;

import com.utils.PrintUtil;
import org.junit.jupiter.api.Test;

public class GeneratorTest {
    @Test
    void test() {
        DerivedGetter d = new DerivedGetter() {
            @Override
            public Derived get() {
                return null;
            }
        };
        Derived result1 = d.get(); // 调用的DerivedGetter.get()
        Base result2 = d.get(); // 也调用的DerivedGetter.get()
    }
}

class Base {
}

class Derived extends Base {
    static {
        PrintUtil.print("load Derived");
    }
}

interface OrdinaryGetter {
    Base get();
}

interface DerivedGetter extends OrdinaryGetter {
    // DerivedGetter.get()覆盖了OrdinaryGetter.get()
    @Override
    Derived get();
}


