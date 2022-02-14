package com.sensors.sensors;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Main {

  public static void main(String[] args) {
//    Vertx vertx = Vertx.vertx();
//    vertx.deployVerticle(new SensorVerticle());
//    vertx.deployVerticle("sensors.SensorVerticle", new DeploymentOptions().setInstances(1));

//    vertx.eventBus().consumer("temperature.updates", message -> {
//      System.out.println(">>>>" + message.body());
//    });


    Vertx.clusteredVertx(new VertxOptions())
      .onSuccess(vertx -> {
        vertx.deployVerticle(new SensorVerticle());
      })
      .onFailure(failure -> {
        System.out.println("Woops " + failure);
      });
  }
}
