package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class MainVerticle extends AbstractVerticle {

  private static final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "9999"));

  private PgPool pgPool;

  @Override
  public void start(Promise<Void> startPromise) {
    pgPool = PgPool.pool(vertx, new PgConnectOptions()
      .setHost("127.0.0.1")
      .setUser("ana.lastoviria")
      .setDatabase("postgres")
      .setPassword("vertx-in-action"), new PoolOptions());

    vertx.eventBus().consumer("temperature.updates", this::recordTemperature);

    Router router = Router.router(vertx);
    router.get("/all").handler(this::getAllData);
    router.get("/for/:uuid").handler(this::getData);
    router.get("/last-5-minutes").handler(this::getLastFiveMinutes);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(httpPort)
      .onSuccess(ok -> {
        System.out.println("http server running: " + httpPort);
        startPromise.complete();
      })
      .onFailure(startPromise::fail);
  }

  private void getAllData(RoutingContext context){
    System.out.println("Requesting all data from " + context.request().remoteAddress());
    String query = "select * from postgres2";
    pgPool.preparedQuery(query)
      .execute()
      .onSuccess(rows ->{
        JsonArray array = new JsonArray();
        for (Row row : rows) {
          array.add(new JsonObject()
            .put("uuid", row.getString("uuid"))
            .put("temperature", row.getDouble("value"))
            .put("timestamp", row.getTemporal("tstamp").toString()));
        }
        context.response()
          .putHeader("Content-Type", "application/json")
          .end(new JsonObject().put("data", array).encode());
      })
      .onFailure(failure ->{
        System.out.println("Woops " + failure);
        context.fail(500);
      });
  }

  private void getLastFiveMinutes(RoutingContext context){

  }

  private void getData(RoutingContext context){

  }

//  private void recordTemperature(RoutingContext context, Message<JsonObject> message){
//    System.out.println("+++RECORDING TEMP+++");
//    System.out.println(message.body().toString());
//    String query = String.format("insert into postgres2(uuid, value) values(%s, %s)", message.body().getString("uuid"), message.body().getDouble("value") );
//    pgPool.preparedQuery(query)
//      .execute()
//      .onSuccess(x ->{
//        System.out.println("-----THIS IS X--------");
//        System.out.println(x);
//        context.response()
//          .putHeader("Content-Type", "application/json")
//          .end();
//      })
//      .onFailure(failure ->{
//        System.out.println("Recording failed" + failure);
//        context.fail(500);
//      });
//  }

  private void recordTemperature(Message<JsonObject> message){
    System.out.println("+++RECORDING TEMP+++");
    System.out.println(message.body().toString());
    pgPool.preparedQuery("insert into postgres2(uuid, value) values($1, $2)")
      .execute(Tuple.of(message.body().getString("uuid"), message.body().getDouble("temperature")), ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          System.out.println(rows.value().toString());
        } else {
          System.out.println("Failure: " + ar.cause().getMessage());
        }
    });
  }


  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions())
      .onSuccess(vertx -> {
        vertx.deployVerticle(new MainVerticle());
        vertx.deployVerticle(new SensorVerticle());
        System.out.println("Running");
      })
      .onFailure(failure -> {
        System.out.println("Not running: " + failure);
      });
  }

}
