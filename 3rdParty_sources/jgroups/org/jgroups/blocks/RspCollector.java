

package org.jgroups.blocks;

import org.jgroups.Address;
import org.jgroups.Message;
import org.jgroups.View;


public interface RspCollector {
    void receiveResponse(Object response_value, Address sender);
    void suspect(Address mbr);
    void viewChange(View new_view);
}
