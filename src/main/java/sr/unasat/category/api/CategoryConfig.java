package sr.unasat.category.api;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


public class CategoryConfig {
    public static void main(String[] args) throws Exception {
        // Optional: Disable JAXB optimization
        System.setProperty("org.glassfish.jaxb.runtime.v2.runtime.JAXBContextImpl.fastBoot", "true");

        Server server = new Server(8080);

        // Set up context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Configure Jersey
        ResourceConfig config = new ResourceConfig();
        config.register(JacksonFeature.class); // Enable JSON support
        config.packages("sr.unasat.category.api.controllers"); // Package with your REST controllers

        // Add Jersey servlet
        ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));
        context.addServlet(servletHolder, "/api/*");

        // Register custom CORS filter
        FilterHolder corsFilter = new FilterHolder(new CORSFilter());
        context.addFilter(corsFilter, "/*", null);

        // Set context handler and start server
        server.setHandler(context);
        server.start();
        server.join();
    }
}
