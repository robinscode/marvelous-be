package ca.h8tch.robin.db;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

public class ExecutionContext extends CustomExecutionContext {

    @Inject
    public ExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "database.ec");
    }

}
