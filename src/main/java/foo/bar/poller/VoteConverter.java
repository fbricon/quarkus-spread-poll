package foo.bar.poller;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class VoteConverter {

    @Incoming("voting-ballot")
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    @Outgoing("poll-results")
    @Blocking
    @Transactional
    @Broadcast
    public Vote process(String item) {
        Vote vote = Vote.findByName(item).orElse(new Vote(item));
        vote.tally = vote.tally+1;
        vote.persist();
        System.err.println(vote);
        return vote;
    }
}
