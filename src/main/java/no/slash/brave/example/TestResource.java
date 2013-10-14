package no.slash.brave.example;

import com.github.kristofa.brave.ServerTracer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Random;

@Path("/test")
@Produces("application/json")
public class TestResource {

    private static final Logger logger = LoggerFactory.getLogger(TestResource.class);
    private final ServerTracer serverTracer;
    private final Client client;

    @Inject
    public TestResource(Client client, ServerTracer serverTracer) {
        this.client = client;
        this.serverTracer = serverTracer;
    }

    @GET
    @Path("/foo")
    public String getFoo(@Context UriInfo uriInfo) throws InterruptedException {
        Random random = new Random();
        Thread.sleep(random.nextInt(1000));
        WebResource resource = client.resource(uriInfo.getRequestUri().resolve("/api/test/bar"));
        ClientResponse clientResponse1 = resource.get(ClientResponse.class);
        ClientResponse clientResponse2 = resource.get(ClientResponse.class);
        return "Done! Now look at zipkin or inspect the logs";
    }

    @GET
    @Path("/bar")
    public Response bar() throws InterruptedException {
        Random random = new Random();
        long startTime = System.currentTimeMillis();
        int sleep = random.nextInt(1000);
        Thread.sleep(sleep);
        serverTracer.submitAnnotation("sleep", startTime, System.currentTimeMillis());
        return Response.ok().build();
    }
}
