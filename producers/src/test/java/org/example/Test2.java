package org.example;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class Test2 {

  @Test
  void name() {

    var pattern = "BL";

    var s = "BucketPizza";

    var sb = new StringBuilder();
    for (String s1 : pattern.split("")) {
      sb.append(s1)
        .append(".*");
    }

    var collect = Arrays.stream(pattern.split(""))
      .collect(Collectors.joining(".*"));

    System.out.println(collect);

    System.out.println(s.split(collect).length);
  }
}
