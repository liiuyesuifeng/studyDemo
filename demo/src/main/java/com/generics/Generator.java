package com.generics;

import java.util.Collection;

public interface Generator<T> {
    T next();
}

class Generators {
    public static <T> Collection<T> fill(Collection<T> coll, Generator<T> gan, int n) {
        for (int i = 0; i < n; i++) {
            coll.add(gan.next());
        }
        return coll;
    }
}
