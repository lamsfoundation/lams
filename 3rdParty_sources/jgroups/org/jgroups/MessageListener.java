

package org.jgroups;

/**
 * Allows a listener to be notified when a message arrives. 
 * Contrary to the pull-style of channels, some building blocks 
 * (e.g., {@link org.jgroups.blocks.PullPushAdapter}) provide an
 * event-like, push-style message delivery model. 
 * In this case, the entity to be notified of message reception needs to 
 * provide a callback to be invoked whenever a message has been received. 
 * The MessageListener interface provides a method to do so. 
 */
public interface MessageListener {
	/**
	 * Called when a message is received. 
	 * @param msg
	 */
    void          receive(Message msg);
    /**
     * Answers the group state; e.g., when joining.
     * @return byte[] 
     */
    byte[]        getState();
    /**
     * Sets the group state; e.g., when joining.
     * @param state
     */
    void          setState(byte[] state);
}
