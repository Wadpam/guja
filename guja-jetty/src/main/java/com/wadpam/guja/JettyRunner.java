package com.wadpam.guja;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URL;
import java.security.ProtectionDomain;

public class JettyRunner {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        WebAppContext root = new WebAppContext();
        root.setContextPath("/");

        ProtectionDomain protectionDomain = JettyRunner.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        root.setWar(location.toExternalForm());

        server.setHandler(root);

        server.start();
        server.join();
    }

}
