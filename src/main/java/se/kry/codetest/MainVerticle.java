package se.kry.codetest;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.util.List;

public class MainVerticle extends AbstractVerticle {
	private final DBConnector connector;
	public MainVerticle(Vertx vertx) {
		connector = new DBConnector(vertx);
	}

	public void createService(JsonObject json) {
		connector.createService(json);
	}

	public List<JsonObject> getServices(List<JsonObject> services) {
		connector.getServices().setHandler(done -> {
			services.clear();
			services.addAll(done.result().getRows());
		});

		return services;
	}

	public void updateService(JsonObject json) {
		connector.updateService(json);
	}

	public void deleteService(String name) {
		connector.deleteService(name);
	}
}
