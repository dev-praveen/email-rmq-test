package com.email.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@TestConfiguration(proxyBeanMethods = false)
class TestContainersConfig {

  @Bean
  GenericContainer<?> mailPitContainer(DynamicPropertyRegistry properties) {

    final var container =
        new GenericContainer<>("axllent/mailpit:v1.19")
            .withExposedPorts(1025, 8025)
            .waitingFor(Wait.forLogMessage(".*accessible via.*", 1));
    properties.add("spring.mail.host", container::getHost);
    properties.add("spring.mail.port", container::getFirstMappedPort);
    properties.add("mailpit.web.port", () -> container.getMappedPort(8025));
    return container;
  }

  @Bean
  public RabbitMQContainer rabbitmq(DynamicPropertyRegistry properties) {

    final RabbitMQContainer container =
        new RabbitMQContainer("rabbitmq:3.13.3-management-alpine")
            .withExposedPorts(5672, 15672) // Expose RabbitMQ ports
            .withEnv("RABBITMQ_DEFAULT_USER", "guest") // Set RabbitMQ default user
            .withEnv("RABBITMQ_DEFAULT_PASS", "guest") // Set RabbitMQ default password
            .waitingFor(Wait.forLogMessage(".*Server startup complete.*", 1));

    properties.add("spring.rabbitmq.host", container::getHost);
    properties.add("spring.rabbitmq.port", container::getFirstMappedPort);
    properties.add(
        "rabbitmq.management.port", () -> container.getMappedPort(15672)); // Management UI port
    return container;
  }

  @Bean
  public ApplicationRunner logMailPitWebPort(
      @Value("${spring.mail.host}") String host,
      @Value("${mailpit.web.port}") int port,
      @Value("${spring.rabbitmq.host}") String mqHost,
      @Value("${rabbitmq.management.port}") int mqPort) {
    Logger log = LoggerFactory.getLogger(getClass());
    return args -> {
      log.info("Mailpit accessible through http://{}:{}", host, port);
      log.info("Rabbit accessible through http://{}:{}", mqHost, mqPort);
    };
  }
}
