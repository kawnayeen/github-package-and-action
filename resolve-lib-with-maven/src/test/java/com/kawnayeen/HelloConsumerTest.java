package com.kawnayeen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloConsumerTest {

  @Test
  void sayHello() {
    var helloConsumer = new HelloConsumer();
    Assertions.assertEquals("Using : Java library with maven build system", helloConsumer.sayHello());
  }
}