package no.slash.brave.example;

import com.github.kristofa.brave.jersey.ServletTraceFilter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class GuiceWebModule extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new MyJerseyServletModule(), new BraveTraceModule());
    }

    private static class MyJerseyServletModule extends JerseyServletModule {
        @Override
        protected void configureServlets() {
            install(new MyServletModule());
        }
    }

    private static class MyServletModule extends ServletModule {
        @Override
        protected void configureServlets() {
            bind(TestResource.class).in(Singleton.class);

            // Install filter which will grab headers from request and create server span.
            filter("/*").through(ServletTraceFilter.class);

            // Set init params for Jersey
            Map<String, String> params = new HashMap<String, String>();
            params.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

            // Route api requests through GuiceContainer
            serve("/api/*").with(GuiceContainer.class, params);
        }
    }
}
