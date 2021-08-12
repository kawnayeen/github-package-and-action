package com.kawnayeen;

public class HelloConsumer {
  public String sayHello() {
    var mavenLib = new HelloLib();
    return "Using : " + mavenLib.sayHello();
  }
}
