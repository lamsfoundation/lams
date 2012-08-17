package org.jboss.cache.marshall;

import org.jboss.cache.commands.ReplicableCommand;

/**
 * This is a hook for observing remotely replicated commands on this instance. Mainly used for unit testing.
 *
 * @author Mircea.Markus@jboss.com
 */
public interface ReplicationObserver
{
   public void afterExecutingCommand(ReplicableCommand command);
}
