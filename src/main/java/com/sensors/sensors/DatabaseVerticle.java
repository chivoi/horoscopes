package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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

  public static void main(String[] args) {
  }

  @Override
  public void start(Promise<Void> startPromise) {
    pgPool = PgPool.pool(vertx, new PgConnectOptions()
      .setHost("127.0.0.1")
      .setUser("agoh")
      .setDatabase("postgres")
      .setPassword("vertx-in-action"), new PoolOptions());

    vertx.eventBus().consumer("temperature.updates", this::recordTemperature);
  }

  private void recordTemperature(Message<JsonObject> message) {
    System.out.println("+++RECORDING TEMP+++");
    System.out.println(message.body().toString());

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
