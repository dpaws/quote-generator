package com.pluralsight.dockerproductionaws.quotegenerator;

import com.pluralsight.dockerproductionaws.common.MicroserviceVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by jmenga on 13/09/16.
 */
public class MainVerticle extends MicroserviceVerticle {

    @Override
    public void start() {
        super.start();

        // Deploy Market Data Verticles
        JsonArray quotes = config().getJsonArray("companies");
        for (Object q : quotes) {
            JsonObject company = (JsonObject) q;
            company.put("MARKET_DATA_ADDRESS", config().getString("MARKET_DATA_ADDRESS"));
            vertx.deployVerticle(MarketDataVerticle.class.getName(), new DeploymentOptions().setConfig(company));
        }

        // Deploy REST Quote API Verticle
        vertx.deployVerticle(RestQuoteAPIVerticle.class.getName(), new DeploymentOptions().setConfig(config()));

        // Publish the services in the discovery infrastructure.
        publishMessageSource("market-data", config().getString("MARKET_DATA_ADDRESS"), rec -> {
            if (!rec.succeeded()) {
                rec.cause().printStackTrace();
            }
            System.out.println("Market data service published : " + rec.succeeded());
        });

        publishHttpEndpoint("quotes", "localhost", config().getInteger("http.port", 8080), ar -> {
            if (ar.failed()) {
                ar.cause().printStackTrace();
            } else {
                System.out.println("Quotes (Rest endpoint) service published : " + ar.succeeded());
            }
        });
    }
}
