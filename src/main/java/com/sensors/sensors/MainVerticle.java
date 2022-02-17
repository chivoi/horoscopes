package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions()).onSuccess(vertx -> {
      vertx.deployVerticle(new DatabaseVerticle());
      vertx.deployVerticle(new APIVerticle());
//      vertx.deployVerticle(new SensorVerticle());
      System.out.println("Main app is running!");
    }).onFailure(failure -> {
      System.out.println("Main app is not running. Error: " + failure);
    });
  }
}
