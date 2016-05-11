

package org.jgroups.util;


import java.io.*;



public class Range implements Externalizable, Streamable {
    public long low=-1;  // first msg to be retransmitted
    public long high=-1; // last msg to be retransmitted



    /** For externalization */
    public Range() {
    }

    public Range(long low, long high) {
        this.low=low; this.high=high;
    }



    public String toString() {
        return "[" + low + " : " + high + ']';
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(low);
        out.writeLong(high);
    }



    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        low=in.readLong();
        high=in.readLong();
    }


    public void writeTo(DataOutputStream out) throws IOException {
        out.writeLong(low);
        out.writeLong(high);
    }

    public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
        low=in.readLong();
        high=in.readLong();
    }
}
