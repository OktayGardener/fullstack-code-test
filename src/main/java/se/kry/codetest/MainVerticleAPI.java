package se.kry.codetest;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
import java.util.List;

public class MainVerticleAPI extends AbstractVerticle {

  private List<JsonObject> services = new ArrayList<>();
  private DBConnector connector;
  private BackgroundPoller poller = new BackgroundPoller();

  @Override
  public void start(Future<Void> startFuture) {
    connector = new DBConnector(vertx);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    vertx.setPeriodic(1000 * 60, timerId -> poller.pollServices(services, vertx));
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
      req.response().putHeader("content-type", "application/json").end(new JsonArray(services).encode());
    });

    router.post("/service").handler(req -> {
      JsonObject jsonBody = req.getBodyAsJson();
      createService(jsonBody);
      req.response().putHeader("content-type", "text/plain").end("OK");
      getServices();
    });

    router.put("/service").handler(req -> {
      JsonObject jsonBody = req.getBodyAsJson();
      updateService(jsonBody);
      req.response().putHeader("content-type", "text/plain").end("OK");
      getServices();
    });

    router.delete("/service/:serviceName").handler(req -> {
      deleteService(req.pathParam("url"));
      req.response().putHeader("content-type", "text/plain").end("OK");
      getServices();
    });
  }


  public void createService(JsonObject json) {
    connector.createService(json);
  }

  public void getServices() {
    connector.getServices().setHandler(done -> {
      services.clear();
      services.addAll(done.result().getRows());
    });
  }

  public void updateService(JsonObject json) {
    connector.updateService(json);
  }

  public void deleteService(String url) {
    connector.deleteService(url);
  }

}



