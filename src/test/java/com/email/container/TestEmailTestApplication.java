package com.email.container;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(TestContainersConfig.class)
public class TestEmailTestApplication {

  public static void main(String[] args) {
    SpringApplication.from(EmailTestApplication::main)
        .with(TestEmailTestApplication.class)
        .run(args);
  }
}
