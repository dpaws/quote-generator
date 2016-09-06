package com.pluralsight.dpaws.quotegenerator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.File;

/**
 * Created by jmenga on 5/09/16.
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        // Get environment configuration
        JsonObject environment = new JsonObject();
        putString("MARKET_DATA_ADDRESS", environment);
        putInteger("HTTP_PORT", environment);

        // Merge configuration
        JsonObject config = config().mergeIn(environment);

        // Deploy REST Quote API Verticle
        vertx.deployVerticle(RestQuoteAPIVerticle.class.getName(), new DeploymentOptions().setConfig(config));

        // Deploy Market Data Verticles
        JsonArray quotes = config.getJsonArray("companies");
        for (Object q : quotes) {
            JsonObject company = (JsonObject) q;
            company.put("MARKET_DATA_ADDRESS", config.getString("MARKET_DATA_ADDRESS"));
            vertx.deployVerticle(MarketDataVerticle.class.getName(), new DeploymentOptions().setConfig(company));
        }
    }

    private void putString(String key, JsonObject env) {
        String value = System.getenv(key);
        if (value != null) {
            env.put(key, value);
        }
    }

    private void putInteger(String key, JsonObject env) {
        String value = System.getenv(key);
        if (value != null) {
            env.put(key, Integer.parseInt(value));
        }
    }
}
