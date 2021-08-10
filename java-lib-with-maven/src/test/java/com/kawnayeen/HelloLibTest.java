package com.kawnayeen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloLibTest {
  @Test
  public void testSayHello() {
    var helloLib = new HelloLib();
    Assertions.assertEquals("Java library with maven build system", helloLib.sayHello());
  }
}
