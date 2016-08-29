/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.rest.api;


import org.apache.commons.cli.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class App {

    public static void main(String[] args) throws Exception {

        Options options = new Options();


        Option option = new Option("port", true, "server port");
        option.setArgs(1);
        options.addOption(option);

        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Server server = null;
        if (cmd.hasOption("port")) {
            String port = cmd.getOptionValue("port", "8080");
            if (port != null) {
                server = new Server(Integer.parseInt(port));
            }
        } else {

            String port = System.getProperty("server.port");
            if (port != null) {
                server = new Server(Integer.parseInt(port));
            } else {
                System.exit(1);
            }

        }
        ServletHolder servlet = new ServletHolder(new ServletContainer(new ApplicationConfig()));


        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/api/*");

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
