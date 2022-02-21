package com.zendesk.horoscopes;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.web.VertxWebClientExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({VertxExtension.class, VertxWebClientExtension.class})
public class TestAPIVerticle {
  Vertx vertx = Vertx.vertx();

  @BeforeEach
  void setup(TestContext ctx) {
    Async async = ctx.async();
    this.vertx = Vertx.vertx();

    vertx.deployVerticle(APIVerticle.class.getName(), h -> {
      if (h.succeeded()) {
        async.complete();
      } else {
        ctx.fail();
      }
    });
  }
}

//  @Test
//  void test_web_client(WebClient client, VertxTestContext testContext) {
//    testRequest(client, HttpMethod.POST, "/")
//      .with(
//        queryParam("sign", "aries"),
//        requestHeader("Accept", "application/json")
//      )
//      .expect(
//        // Assert that response is a JSON with a specific body
//        jsonBodyResponse(new JsonObject().put("value", "Hello Francesco!")),
//        // Assert that response contains a particular header
//        responseHeader("Accept", "application/json")
//      )
//      .send(testContext); // Complete (or fail) the VertxTestContext
//  }
//}

//  @Test
//  void test_production(TestContext ctx) {
//    Async async = ctx.async();
////    doAnswer(args -> {
////      async.complete();
////      return null;
////    }).when(delegate).invokeMethod(anyString(), anyString());
//
//    EventBus eventBus = vertx.eventBus();
//
//    String jsonResponse = "{\"current_date\":\"June 23, 2017\",\"compatibility\":\" Cancer\",\"lucky_time\":\" 7am\",\"lucky_number\":\" 64\",\"color\":\" Spring Green\",\"date_range\":\"Mar 21 - Apr 20\",\"mood\":\" Relaxed\",\"description\":\"It's finally time for you to think about just one thing: what makes you happy. Fortunately, that happens to be a person who feels the same way.Give yourself the evening off.Refuse to be put in charge of anything.\"}";
//
//    JsonObject apiResponse = new JsonObject(jsonResponse);
//    vertx.eventBus().publish("horoscope.updates", apiResponse);
//  }
