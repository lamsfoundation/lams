

package org.jgroups;

/**
 * Defines a very small subset of the functionality of a channel, 
 * essentially only the methods for sending and receiving messages. 
 * Many building blocks require nothing else than a 
 * bare-bones facility to send and receive messages; therefore the Transport 
 * interface was created. It increases the genericness and portability of 
 * building blocks: being so simple, the Transport interface can easily be 
 * ported to a different toolkit, without requiring any modifications to 
 * building blocks.
 */
public interface Transport {    
    void     send(Message msg) throws Exception;
    Object   receive(long timeout) throws Exception;
}
