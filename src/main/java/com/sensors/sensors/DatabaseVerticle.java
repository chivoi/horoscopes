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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.Objects;

public class DatabaseVerticle extends AbstractVerticle {
  private static final Logger logger = LogManager.getLogger(DatabaseVerticle.class);
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

    router.get("/temperature").handler(this::getTemperatureData);
    router.get("/horoscope").handler(this::getHoroscopeData);
    router.get("/one-horoscope").handler(this::getHoroscopeData);

    vertx.createHttpServer().requestHandler(router).listen(httpPort).onSuccess(ok -> {
      logger.info("http server running at port: " + httpPort);
      startPromise.complete();
    }).onFailure(fail -> {
      startPromise.fail("No server because " + fail);
    });
  }

  private void recordTemperature(Message<JsonObject> message) {
    logger.info("+++RECORDING TEMP+++");

    String uuid = message.body().getString("uuid");
    double temperature = message.body().getDouble("temperature");
    Timestamp tstamp = new Timestamp(message.body().getLong("timestamp"));

    pgPool.preparedQuery("insert into postgres2(uuid, value, tstamp) values($1, $2, $3)")
      .execute(Tuple.of(uuid, temperature, tstamp.toLocalDateTime()), ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          logger.info(rows.value().toString());
        } else {
          logger.error("Failure: " + ar.cause().getMessage());
        }
      });
  }

  private void recordHoroscope(Message<JsonObject> message) {
    logger.info("+++RECORDING HOROSCOPE+++");

    String date = message.body().getString("current_date");
    String compatability  = message.body().getString("compatibility");
    String color  = message.body().getString("color");
    String luckynumber  = message.body().getString("lucky_number");
    String description  = message.body().getString("description");

    pgPool.preparedQuery("insert into horoscopes(date, compatability, color, luckynumber, description) values($1, $2, $3, $4, $5)")
      .execute(Tuple.of(date, compatability, color, luckynumber, description), ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          logger.info("Horoscope recorded!");
        } else {
          logger.info("Failure: " + ar.cause().getMessage());
        }
      });

  }

  public void getHoroscopeData(RoutingContext context){
    String query = null;
    if(Objects.equals(context.normalizedPath(), "/horoscope")){
      query = "select * from horoscopes";
    }else if(Objects.equals(context.normalizedPath(), "/one-horoscope")){
      query = "select * from horoscopes order by id desc limit 1";
    }

    pgPool.preparedQuery(query)
      .execute()
      .onSuccess(rows -> {
        JsonArray array = new JsonArray();
        for (Row row : rows) {
          array.add(new JsonObject()
            .put("date", row.getString("date"))
            .put("compatibility", row.getString("compatability"))
            .put("color", row.getString("color"))
            .put("luckynumber", row.getString("luckynumber"))
            .put("description", row.getString("description")));
        }
        context.response()
          .putHeader("Content-Type", "application/json")
          .end(new JsonObject().put("data", array).encode());
      })
      .onFailure(failure -> {
        logger.info("Whoops: " + failure);
        context.fail(500);
      });
  }

  public void getTemperatureData(RoutingContext context) {
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
        logger.info("Whoops: " + failure);
        context.fail(500);
      });
  }
}
