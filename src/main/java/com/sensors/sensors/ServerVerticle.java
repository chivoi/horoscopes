package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class ServerVerticle extends AbstractVerticle {
  private static final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "9999"));

  public static void main(String[] args) {
    Router router = Router.router(vertx);
    DatabaseVerticle db = new DatabaseVerticle();

    router.get("/all").handler(db::getAllData);
//    router.get("/for/:uuid").handler(this::getData);
//    router.get("/last-5-minutes").handler(this::getLastFiveMinutes);

    vertx.createHttpServer().requestHandler(router).listen(httpPort).onSuccess(ok -> {
      System.out.println("http server running at port: " + httpPort);
      startPromise.complete();
    }).onFailure(startPromise::fail);
  }

//  @Override
//  public void start() {
//    start();
//  }
//
//  @Override
//  public void start(Promise<Void> startPromise) {
//    Router router = Router.router(vertx);
//    DatabaseVerticle db = new DatabaseVerticle();
//
//    router.get("/all").handler(db::getAllData);
////    router.get("/for/:uuid").handler(this::getData);
////    router.get("/last-5-minutes").handler(this::getLastFiveMinutes);
//
//    vertx.createHttpServer().requestHandler(router).listen(httpPort).onSuccess(ok -> {
//      System.out.println("http server running at port: " + httpPort);
//      startPromise.complete();
//    }).onFailure(startPromise::fail);
//  }
}

