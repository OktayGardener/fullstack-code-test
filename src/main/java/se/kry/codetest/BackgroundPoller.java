package se.kry.codetest;

import io.vertx.core.Future;

import java.util.List;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class BackgroundPoller {

  private Logger LOGGER = LoggerFactory.getLogger(BackgroundPoller.class);
  private static String URL = "url";
  private static String STATUS = "status";
  private static String OK = "OK";
  private static String FAIL = "FAIL";

  private DBConnector dbConnector = new DBConnector(Vertx.vertx());

  public Future<List<JsonObject>> pollServices(List<JsonObject> services, Vertx vertx) {
    WebClient client = WebClient.create(vertx);
    for(JsonObject service : services) {
      String url = (String)service.getValue("url");
      LOGGER.info("Polling "  + url);
      client.getAbs(url).send(ar -> {
        if (ar.succeeded()) {
          service.put(STATUS, OK);
        } else {
          LOGGER.info(ar.cause());
          service.put(STATUS, FAIL);
        }
      });
    }
    System.out.println(services);
    return Future.succeededFuture();
  }




}
