//import io.vertx.core.AbstractVerticle;
//import io.vertx.core.Promise;
//import io.vertx.core.Vertx;
//import io.vertx.core.VertxOptions;
//import io.vertx.core.eventbus.Message;
//import io.vertx.core.json.JsonArray;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.web.Route;
//import io.vertx.ext.web.Router;
//import io.vertx.ext.web.RoutingContext;
//import io.vertx.pgclient.PgConnectOptions;
//import io.vertx.pgclient.PgPool;
//import io.vertx.sqlclient.PoolOptions;
//import io.vertx.sqlclient.Row;
//import io.vertx.sqlclient.RowSet;
//import io.vertx.sqlclient.Tuple;
//
//import java.sql.Timestamp;

import com.sensors.sensors.DatabaseVerticle;

public class ServerVerticle extends AbstractVerticle {
  private static final int httpPort = Integer.parseInt(System.getenv().getOrDefault("HTTP_PORT", "9999"));

  public static void main(String[] args) {

  }

  @Override
  public void start(Promise<Void> startPromise) {
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
}
