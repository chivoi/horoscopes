package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.Random;
import java.util.UUID;

public class SensorVerticle extends AbstractVerticle {
  private static final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "9999"));

  private final String uuid = UUID.randomUUID().toString();
  private final Random random = new Random();
  private double temperature = 21.0;

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.setPeriodic(2000, this::updateTemperature);

    Router router = Router.router(vertx);
    router.get("/data").handler(this::getData);
//    router.get("/hello").handler(result -> {
//      System.out.println(result);
//      System.out.println("hello");
//    });

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(httpPort)
      .onSuccess(ok -> {
        System.out.println("http server running: http://127.0.0.1" + httpPort);
        startPromise.complete();
      })
      .onFailure(startPromise::fail);
  }

  public void getData(RoutingContext context) {
    System.out.println("Processing HTTP request from: " + context.request().remoteAddress());
    JsonObject payload = getPayload();

    context.response()
      .putHeader("Content-Type", "application/json")
      .setStatusCode(200)
      .end(payload.encode());
  }

  private JsonObject getPayload() {
    return new JsonObject()
      .put("uuid", uuid)
      .put("temperature", temperature)
      .put("timestamp", System.currentTimeMillis());
  }

  private void updateTemperature(Long id) {
    temperature = temperature + (random.nextGaussian() / 2.0d);
    System.out.println("The temperature updated: " + temperature);

    vertx.eventBus().publish("temperature.updates", getPayload());
  }
}
