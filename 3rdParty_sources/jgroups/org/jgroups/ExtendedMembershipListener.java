package org.jgroups;

/**
 * @author Bela Ban
 *
 */
public interface ExtendedMembershipListener extends MembershipListener {

    /**
     * Called <em>after</em> the FLUSH protocol has unblocked previously blocked senders, and messages can be sent again. This
     * callback only needs to be implemented if we require a notification of that.
     */
    void unblock();
}
