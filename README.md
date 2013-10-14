brave-jersey-guice-example
==========================

Example application that shows how to use [brave](https://github.com/kristofa/brave) with jersey server and client with Guice.

run `mvn:exec` to run the example, or just run the main method from your IDE.

The example will log spans to the console by default. To hook it up to zipkin, just uncomment the three lines in `BraveTraceModule`.
