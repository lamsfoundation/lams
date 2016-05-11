

package org.jgroups.protocols.pbcast;

import org.jgroups.Address;
import org.jgroups.util.Digest;

import java.io.Serializable;
import java.util.Vector;
import java.net.UnknownHostException;


public class Gossip implements Serializable {
    Address sender=null;
    long id=-1;
    Digest digest=null;
    Vector not_seen=null;     // members who haven't seen this specific gossip yet
    Vector seen=new Vector(11); // members who have seen the gossip already
    private static final long serialVersionUID = 7954243278668401185L;


    public Gossip(Address obj, long id) {
        sender=obj;
        this.id=id;
    }

    public Gossip(Address obj, long id, Digest d, Vector not_seen) {
        sender=obj;
        this.id=id;
        digest=d;
        this.not_seen=not_seen;
    }


    /**
     * Removes obj from not_seen list
     */
    public void removeFromNotSeenList(Address mbr) {
        if(not_seen != null && mbr != null)
            not_seen.removeElement(mbr);
    }

    public void addToSeenList(Address mbr) {
        if(mbr != null && !seen.contains(mbr))
            seen.addElement(mbr);
    }


    public int sizeOfNotSeenList() {
        return not_seen == null ? 0 : not_seen.size();
    }


    public Vector getNotSeenList() {
        return not_seen;
    }

    public Vector getSeenList() {
        return seen;
    }


    public boolean equals(Object o) {
        Gossip other=null;

        if(sender != null && o != null) {
            other=(Gossip)o;
            return sender.equals(other.sender) && id == other.id;
        }
        return false;
    }


    public int hashCode() {
        if(sender != null)
            return sender.hashCode() + (int)id;
        else
            return (int)id;
    }


    public Gossip copy() {
        Gossip ret=new Gossip(sender, id);
        if(digest != null)
            ret.digest=digest.copy();
        if(not_seen != null)
            ret.not_seen=(Vector)not_seen.clone();
        if(seen != null)
            ret.seen=(Vector)seen.clone();
        return ret;
    }


    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("sender=");
        if(sender != null)
            sb.append(sender);
        else
            sb.append("<null>");
        if(digest != null) sb.append(", digest=" + digest);
        if(not_seen != null) sb.append(", not_seen=" + not_seen);
        if(seen != null) sb.append(", seen=" + seen);
        sb.append(", id=" + id);
        return sb.toString();
    }


    public String shortForm() {
        StringBuilder sb=new StringBuilder();
        if(sender != null)
            sb.append(sender);
        else
            sb.append("<null>");
        sb.append("#" + id);
        return sb.toString();
    }


    public static void main(String[] args) throws UnknownHostException {
        Gossip id1, id2;

        id1=new Gossip(new org.jgroups.stack.IpAddress("daddy", 4567), 23);
        id2=new Gossip(new org.jgroups.stack.IpAddress("133.164.130.19", 4567), 23);

        System.out.println(id1.equals(id2));
    }
}
