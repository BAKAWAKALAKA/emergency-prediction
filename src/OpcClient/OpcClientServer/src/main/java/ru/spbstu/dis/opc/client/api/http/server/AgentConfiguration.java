package ru.spbstu.dis.opc.client.api.http.server;

import io.dropwizard.Configuration;

public class AgentConfiguration
    extends Configuration {
  private String testField;

  public String getTestField() {
    return testField;
  }

  public void setTestField(String name) {
    this.testField = name;
  }
}