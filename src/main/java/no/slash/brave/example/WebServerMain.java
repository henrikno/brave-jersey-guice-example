package no.slash.brave.example;

import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;

public class WebServerMain {
    private static final Logger logger = LoggerFactory.getLogger(WebServerMain.class);

    public static void main(String[] args) throws Exception {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        Server server = new Server();

        final ServerConnector connector = new ServerConnector(server);
        connector.setIdleTimeout(1000 * 60 * 60);
        connector.setPort(8082);
        server.setConnectors(new Connector[] {connector});

        final WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/");
        context.setWar("src/main/webapp");
        context.addEventListener(new GuiceWebModule());
        context.addFilter(GuiceFilter.class, "/*", null);
        context.addServlet(DefaultServlet.class, "/");

        server.setHandler(context);

        try {
            server.start();
            logger.info("Started jetty on port " + connector.getPort());
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to start server.", e);
        }
        server.join();
    }
}

