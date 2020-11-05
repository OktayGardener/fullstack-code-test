package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MainVerticleAPI extends AbstractVerticle {

  private List<JsonObject> services = new ArrayList<>();
  private DBConnector connector;
  private BackgroundPoller poller = new BackgroundPoller();
  private MainVerticle mainVerticle;
  private Logger LOGGER = LoggerFactory.getLogger(BackgroundPoller.class);


	@Override
  public void start(Future<Void> startFuture) {
    connector = new DBConnector(vertx);
    mainVerticle = new MainVerticle(vertx);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());


    services = mainVerticle.getServices(services);

    vertx.setPeriodic(1000 * 10, timerId -> poller.pollServices(services, vertx));
    vertx.setPeriodic(1000 * 10, timerId -> System.out.println(services));
    setRoutes(router);

    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(8080, result -> {
          if (result.succeeded()) {
            System.out.println("KRY code test service started");
            startFuture.complete();
          } else {
            startFuture.fail(result.cause());
          }
        });
  }

  private void setRoutes(Router router){
    router.route("/*").handler(StaticHandler.create());

    router.get("/service").handler(req -> {
		LOGGER.info("GET request");
		req.response().putHeader("content-type", "application/json").end(new JsonArray(services).encode());
    });

    router.post("/service").handler(req -> {
    	JsonObject jsonBody = req.getBodyAsJson();
		LOGGER.info("POST request with params: \n" + jsonBody);
    	mainVerticle.createService(jsonBody);
    	req.response().putHeader("content-type", "application/json").end("OK");
    	services = mainVerticle.getServices(services);
    });

    router.put("/service").handler(req -> {
    	JsonObject jsonBody = req.getBodyAsJson();
		LOGGER.info("PUT request with params: \n" + jsonBody);
    	mainVerticle.updateService(jsonBody);
    	req.response().putHeader("content-type", "application/json").end("OK");
    	services = mainVerticle.getServices(services);
    });

    router.delete("/service/:name").handler(req -> {
    	String name = req.request().getParam("name");
		LOGGER.info("DELETE request for url: " + name);
    	mainVerticle.deleteService(name);
    	req.response().putHeader("content-type", "application/text").end("OK");
    	services = mainVerticle.getServices(services);
    });
  }

}



