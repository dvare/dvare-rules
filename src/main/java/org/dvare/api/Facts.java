package org.dvare.api;

import java.util.HashMap;

public class Facts extends HashMap<String, Object> {

    public Facts() {

    }

    public Facts(HashMap<? extends String, ?> m) {
        super(m);
    }

    public void add(String age, Object value) {
        put(age, value);
    }
}
