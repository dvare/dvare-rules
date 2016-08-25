package org.dvare.rest.api;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class App {

    public static void main(String[] args) throws Exception {

        ResourceConfig config = new ResourceConfig();
        config.packages("org.dvare.rest.resource");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8090);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        HashSessionIdManager idmanager = new HashSessionIdManager();
        server.setSessionIdManager(idmanager);


        // Create the SessionHandler (wrapper) to handle the sessions
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        context.setSessionHandler(sessions);


        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }


    }
}
