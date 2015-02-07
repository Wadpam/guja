[![Build Status](https://travis-ci.org/Wadpam/guja.svg?branch=master)](https://travis-ci.org/Wadpam/guja)

# guja

Guice and JAX-RS in symbiosis, with the GPLv3 license.

See [https://github.com/Wadpam/guja/wiki] for documentation.

## Running in GAE devserver

    mvn clean install -DskipTests && mvn appengine:devserver -pl :guja-appengine-ear

## Running in Embedded Jetty

    mvn clean install -DskipTests && java -jar guja-jetty-war/target/guja-jetty-war-*.war

# References

* [Google Guice](http://)
* [Jersey](http://)
