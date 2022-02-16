package com.sensors.sensors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;
import java.util.UUID;

public class SensorVerticle extends AbstractVerticle {

  private static Logger logger = LogManager.getLogger(SensorVerticle.class);
  private final String uuid = UUID.randomUUID().toString();
  private final Random random = new Random();
  private double temperature = 21.0;

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.setPeriodic(2000, this::updateTemperature);
  }

  private JsonObject getPayload() {
    return new JsonObject()
      .put("uuid", uuid)
      .put("temperature", temperature)
      .put("timestamp", System.currentTimeMillis());
  }

  private void updateTemperature(Long id) {
//    TODO: Get data from external endpoint
    temperature = temperature + (random.nextGaussian() / 2.0d);
    logger.info("The temperature updated: ", temperature);

    vertx.eventBus().publish("temperature.updates", getPayload());
  }
}

// Create endpoints via RestEasy
// Get data from other, external endpoints via vertx httpclient, use futures
// Tests with junit

// SensorVerticle - gets data from external API and publishes it to event bus

// Functionality:
// - Get data from external API [on start, get 5 lots of data] - SensorVerticle
// - Save that data to a database - DatabaseVerticle
// - Create endpoints that serve database data - ServerVerticle

