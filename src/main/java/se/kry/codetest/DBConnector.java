package se.kry.codetest;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import java.time.Instant;

public class DBConnector {

  private final String DB_PATH = "poller.db";
  private final SQLClient client;

  public DBConnector(Vertx vertx){
    JsonObject config = new JsonObject()
        .put("url", "jdbc:sqlite:" + DB_PATH)
        .put("driver_class", "org.sqlite.JDBC")
        .put("max_pool_size", 30);

    client = JDBCClient.createShared(vertx, config);
  }

  public Future<ResultSet> query(String query) {
    return query(query, new JsonArray());
  }


  public Future<ResultSet> query(String query, JsonArray params) {
    if(query == null || query.isEmpty()) {
      return Future.failedFuture("Query is null or empty");
    }
    if(!query.endsWith(";")) {
      query = query + ";";
    }

    Future<ResultSet> queryResultFuture = Future.future();

    client.queryWithParams(query, params, result -> {
      if(result.failed()){
        queryResultFuture.fail(result.cause());
      } else {
        queryResultFuture.complete(result.result());
      }
    });
    return queryResultFuture;
  }

  public Future<ResultSet> getServices() {
    return this.query("SELECT * FROM service");
  }

  public Future<ResultSet> createService(JsonObject json) {
  	String url = json.getValue("url").toString();
  	url = checkURL(url);
  	return this.query("INSERT INTO service VALUES ('" + url + "','" + json.getValue("name") + "','" + Instant.now().getEpochSecond() + "')");
  }

  public Future<ResultSet> updateService(JsonObject json) {
    return this.query("UPDATE service SET url = '" + json.getValue("url") + "'" + " WHERE name='" + json.getValue("name") + "'");
  }

  public Future<ResultSet> deleteService(String name) {
  	name = checkSqlInjection(name);
    return this.query("DELETE FROM service WHERE name = '" + name + "'");
  }


  private String checkURL(String url) {
  	if(url.startsWith("www.")) {
  		url = "http://" + url;
  	} else if(!url.startsWith("www.") && !url.startsWith("http://")) {
  		url = "http://www." + url;
  	}
  	return url;
  }

  private String checkSqlInjection(String param) {
  	return param == null ? null : param.replace(";", "");
  }

}
