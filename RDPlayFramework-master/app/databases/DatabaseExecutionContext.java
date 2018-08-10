package databases;

import com.google.inject.Inject;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

// TODO: Auto-generated Javadoc
/**
 * The Class DatabaseExecutionContext.
 */
public class DatabaseExecutionContext extends CustomExecutionContext {

    /**
     * Instantiates a new database execution context.
     *
     * @param actorSystem the actor system
     */
    @Inject
    public DatabaseExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "database.dispatcher");
    }
}

