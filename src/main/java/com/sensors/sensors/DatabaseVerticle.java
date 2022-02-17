package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
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

import java.sql.Timestamp;

public class DatabaseVerticle extends AbstractVerticle {
  private PgPool pgPool;
  private static final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "9999"));


  public static void main(String[] args) {
  }

  @Override
  public void start(Promise<Void> startPromise) {
    pgPool = PgPool.pool(vertx, new PgConnectOptions()
      .setHost("127.0.0.1")
      .setUser("ana.lastoviria")
      .setDatabase("postgres")
      .setPassword("vertx-in-action"), new PoolOptions());

    vertx.eventBus().consumer("temperature.updates", this::recordTemperature);
    vertx.eventBus().consumer("horoscope.updates", this::recordHoroscope);

    Router router = Router.router(vertx);

    router.get("/all").handler(this::getAllData);

    vertx.createHttpServer().requestHandler(router).listen(httpPort).onSuccess(ok -> {
      System.out.println("http server running at port: " + httpPort);
      startPromise.complete();
    }).onFailure(fail -> {
      startPromise.fail("No server because " + fail);
    });
  }

  private void recordTemperature(Message<JsonObject> message) {
    System.out.println("+++RECORDING TEMP+++");

    String uuid = message.body().getString("uuid");
    double temperature = message.body().getDouble("temperature");
    Timestamp tstamp = new Timestamp(message.body().getLong("timestamp"));

    pgPool.preparedQuery("insert into postgres2(uuid, value, tstamp) values($1, $2, $3)")
      .execute(Tuple.of(uuid, temperature, tstamp.toLocalDateTime()), ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          System.out.println(rows.value().toString());
        } else {
          System.out.println("Failure: " + ar.cause().getMessage());
        }
      });
  }

  private void recordHoroscope(Message<JsonObject> message) {
    System.out.println("+++RECORDING HOROSCOPE+++");

    String date = message.body().getString("current_date");
    String compatability  = message.body().getString("compatibility");
    String color  = message.body().getString("color");
    String luckynumber  = message.body().getString("lucky_number");
    String description  = message.body().getString("description");

    pgPool.preparedQuery("insert into horoscopes(date, compatability, color, luckynumber, description) values($1, $2, $3, $4, $5)")
      .execute(Tuple.of(date, compatability, color, luckynumber, description), ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          System.out.println(rows.value().toString());
        } else {
          System.out.println("Failure: " + ar.cause().getMessage());
        }
      });

  }

  public void getAllData(RoutingContext context) {
    System.out.println("Requesting all data from " + context.request().remoteAddress());
    String query = "select * from postgres2";
    pgPool.preparedQuery(query)
      .execute()
      .onSuccess(rows -> {
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
      .onFailure(failure -> {
        System.out.println("Woops: " + failure);
        context.fail(500);
      });
  }
}
