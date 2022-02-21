package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions()).onSuccess(vertx -> {
      vertx.deployVerticle(new APIVerticle());
//      vertx.deployVerticle(new RestEasyVerticle());
      vertx.deployVerticle(new DatabaseVerticle());
      logger.info("Main app is running!");
    }).onFailure(failure -> {
      logger.error("Main app is not running. Error: " + failure);
    });
  }
}
