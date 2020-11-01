package foo.bar.poller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/")
public class PollResource {

    @Inject
    @Channel("voting-booth")
    Emitter<String> votingBooth;

    @Inject
    @Channel("poll-results")
    Publisher<Vote> votes;

    @Inject
    Template index;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        // get index page
        Map<String, String> data = new HashMap<>();
        return index.data(data);
    }

    @POST
    @Path("vote")
    public void vote(String item) {
        votingBooth.send(item.trim());
    }

    @GET
    @Path("/vote-stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<Vote> liveResults() {
        return votes;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/votes")
    public List<Vote> getAllVotes() {
        return Vote.listAll();
    }

}