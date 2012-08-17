package org.jgroups;

/**
 * Defines the callbacks that are invoked when messages, views etc are received on a channel
 * @author Bela Ban
 * @version $Id$
 */
public interface Receiver extends MessageListener, MembershipListener {
}
