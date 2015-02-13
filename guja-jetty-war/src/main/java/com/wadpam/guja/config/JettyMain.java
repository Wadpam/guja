package com.wadpam.guja.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by sosandstrom on 2015-02-13.
 */
public class JettyMain {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("target/guja-jetty-war.war");

        server.setHandler(webapp);
        server.start();
        server.join();
    }
}
