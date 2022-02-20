package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class APIVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(APIVerticle.class);

  public static void main(String[] args) {
  }

  @Override
  public void start(Promise<Void> startPromise) {
    WebClient client = WebClient.create(vertx);
    client
      .post(443, "aztro.sameerkumar.website","/")
      .addQueryParam("sign", getRandomSign())
      .addQueryParam("day", "today")
      .ssl(true)
      .putHeader("Accept", "application/json")
      .as(BodyCodec.jsonObject())
      .expect(ResponsePredicate.SC_OK)
      .send()
      .onSuccess(response -> {
        vertx.eventBus().publish("horoscope.updates", response.body());
      })
      .onFailure(err -> logger.info("Something went wrong " + err.getMessage()));
  }

  private String getRandomSign(){
    List <String> signs = Arrays.stream(new String [] {
      "aries",
      "taurus",
      "gemini",
      "cancer",
      "leo",
      "virgo",
      "libra",
      "scorpio",
      "sagittarius",
      "capricorn",
      "aquarius",
      "pisces"
  }).collect(Collectors.toList());

    Random rand = new Random();
    return signs.get(rand.nextInt(signs.size()));
  }
}
