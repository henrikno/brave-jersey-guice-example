package no.slash.brave.example;

import com.github.kristofa.brave.*;
import com.github.kristofa.brave.jersey.JerseyClientTraceFilter;
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector;
import com.github.kristofa.brave.zipkin.ZipkinSpanCollectorParams;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.sun.jersey.api.client.Client;

import java.util.Arrays;

public class BraveTraceModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    public SpanCollector spanCollector() {
        return new LoggingSpanCollectorImpl();
//        ZipkinSpanCollectorParams params = new ZipkinSpanCollectorParams();
//        params.setFailOnSetup(false);
//        return new ZipkinSpanCollector("127.0.0.1", 9410, params);
    }

    @Provides
    public ClientTracer getClientTracer(SpanCollector spanCollector, TraceFilters traceFilters) {
        return Brave.getClientTracer(spanCollector, traceFilters.getTraceFilters());
    }

    @Provides
    public TraceFilters getTraceFilters() {
        return new TraceFilters(Arrays.<TraceFilter>asList(new FixedSampleRateTraceFilter(1)));
    }

    @Provides
    public EndPointSubmitter getEndpointSubmitter() {
        return Brave.getEndPointSubmitter();
    }

    @Provides
    public ServerTracer getServerTracer(SpanCollector spanCollector, TraceFilters traceFilters) {
        return Brave.getServerTracer(spanCollector, traceFilters.getTraceFilters());
    }

    @Provides
    public Client getClient(ClientTracer clientTracer) {
        Client client = Client.create();
        client.addFilter(new JerseyClientTraceFilter(clientTracer));
        return client;
    }
}
