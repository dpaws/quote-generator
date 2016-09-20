# Docker in Production using AWS - Quote Generator

This service is part of the sample application included with the Pluralsight course Docker in Production using Amazon Web Services.

## Quick Start

To run tests and create an application "fat" JAR:

```
$ make test
```

This will build a development image, run tests and create a fat JAR that is output to the local `target` folder.

To run the fat JAR:

```
$ java -jar target/quote-generator-20160918203018.5bb4888-fat.jar -conf=src/conf/config.json
Sep 07, 2016 1:12:32 AM io.vertx.core.impl.launcher.commands.VertxIsolatedDeployer
INFO: Succeeded in deploying verticle
Server started

```

By default the quote generator will run on port 35000.  

> You can set the `HTTP_PORT` environment variable to change this port.

In another terminal you can test the endpoint as follows:

```
$ curl localhost:35000
{
  "MacroHard" : {
    "volume" : 95000,
    "shares" : 47776,
    "symbol" : "MCH",
    "name" : "MacroHard",
    "ask" : 553.0,
    "exchange" : "vert.x stock exchange",
    "bid" : 525.0,
    "open" : 600.0
  },
  "Black Coat" : {
    "volume" : 90000,
    "shares" : 45229,
    "symbol" : "BCT",
    "name" : "Black Coat",
    "ask" : 741.0,
    "exchange" : "vert.x stock exchange",
    "bid" : 737.0,
    "open" : 550.0
  },
  "Divinator" : {
    "volume" : 98000,
    "shares" : 49358,
    "symbol" : "DVN",
    "name" : "Divinator",
    "ask" : 630.0,
    "exchange" : "vert.x stock exchange",
    "bid" : 634.0,
    "open" : 650.0
  }
}
```