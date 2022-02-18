package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class APIVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(APIVerticle.class);

  public static void main(String[] args) {
  }

  @Override
  public void start(Promise<Void> startPromise) {
    WebClient client = WebClient.create(vertx);
    client
      .post(443, "aztro.sameerkumar.website","/")
      .addQueryParam("sign", "gemini")
      .addQueryParam("day", "today")
      .ssl(true)
      .putHeader("Accept", "application/json")
      .as(BodyCodec.jsonObject())
      .expect(ResponsePredicate.SC_OK)
      .send()
      .onSuccess(response -> {
        System.out.println(response.body());
        vertx.eventBus().publish("horoscope.updates", response.body());
      })
      .onFailure(err -> System.out.println("Something went wrong " + err.getMessage()));
  }

}
