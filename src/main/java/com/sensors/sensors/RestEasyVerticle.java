package com.sensors.sensors;

import com.zandero.rest.RestBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RestEasyVerticle extends AbstractVerticle {
  Logger logger = LogManager.getLogger(RestEasyVerticle.class);

  public static void main(String[] args) {
  }

  @Override
  public void start(Promise<Void> startPromise) {
//  public void start() throws Exception {
//    VertxResteasyDeployment deployment = new VertxResteasyDeployment();
//    ResteasyDeployment deployment = new ResteasyDeployment();
//    deployment.start();
//    deployment.getProviderFactory().register(RestEasyVerticle.class);
//    deployment.getRegistry().addPerInstanceResource(HelloWorldService.class);
//    deployment.getProviderFactory().register(HelloWorldService.class);

//    Router router = RestRouter.register(vertx, HelloWorldService.class);
    Router router = new RestBuilder(vertx).register(HelloWorldService.class).build();
    vertx.createHttpServer().requestHandler(router).listen(9999).onSuccess(ok -> {
      logger.warn("http server running at port: " + 9999);
      startPromise.complete();
    }).onFailure(fail -> {
      logger.warn("FAIL!!!!");
      startPromise.fail("No server because " + fail);
    });

//    vertx.createHttpServer()
//      .requestHandler(new VertxRequestHandler(vertx, deployment))
//      .listen(9999
////        , ar -> {
////        System.out.println("Server started on port " + ar.result().actualPort());
////      }
//      ).onSuccess(ok -> {
//        logger.warn("http server running at port: " + 9999);
//        startPromise.complete();
//      }).onFailure(fail -> {
//        logger.warn("FAIL!!!!");
//        startPromise.fail("No server because " + fail);
//      });
  }
}
