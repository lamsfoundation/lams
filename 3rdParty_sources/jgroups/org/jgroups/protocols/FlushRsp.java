

package org.jgroups.protocols;

import java.util.Vector;




public class FlushRsp {
    public boolean  result=true;
    public Vector   unstable_msgs=new Vector();
    public Vector   failed_mbrs=null;           // when result is false

    public FlushRsp() {}

    public FlushRsp(boolean result, Vector unstable_msgs, Vector failed_mbrs) {
	this.result=result;
	this.unstable_msgs=unstable_msgs;
	this.failed_mbrs=failed_mbrs;
    }

    public String toString() {
	return "result=" + result + "\nunstable_msgs=" + unstable_msgs + "\nfailed_mbrs=" + failed_mbrs;
    }
}
