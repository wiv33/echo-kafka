package org.example;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleProducerTest {


    @Test
    void name() {


//        System.out.println(s.length);
//        System.out.println(s[5]);
        var input = "([]){}";

        var stack = new Stack<String>();
        var open = "{([";
        var mapping = new HashMap<String, String>();
        mapping.put("}", "{");
        mapping.put("]", "[");
        mapping.put(")", "(");
        var s = input.split("");

        for (int i = 0; i < s.length; i++) {
            var c = s[i];
            System.out.println("c = " + c);
            if (open.contains(c)) {
                stack.push(c);
            } else {
                var pop = stack.pop();
                if (!pop.equals(mapping.get(c))) {
                    System.out.println(" false ::::: pop = " + pop + ", close = " + mapping.get(c));
                }
            }
        }

        int[] answer = new int[10];
        System.out.println(stack.size() == 0);
    }
}