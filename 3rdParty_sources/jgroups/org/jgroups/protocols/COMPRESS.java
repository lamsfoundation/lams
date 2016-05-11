package org.jgroups.protocols;

import org.jgroups.Event;
import org.jgroups.Global;
import org.jgroups.Header;
import org.jgroups.Message;
import org.jgroups.stack.Protocol;
import org.jgroups.util.Streamable;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Compresses the payload of a message. Goal is to reduce the number of messages sent across the wire.
 * Should ideally be layered somewhere above a fragmentation protocol (e.g. FRAG).
 * @author Bela Ban
 *
 */
public class COMPRESS extends Protocol {
    BlockingQueue<Deflater> deflater_pool=null;
    BlockingQueue<Inflater> inflater_pool=null;


    /** Values are from 0-9 (0=no compression, 9=best compression) */
    int compression_level=Deflater.BEST_COMPRESSION; // this is 9

    /** Minimal payload size of a message (in bytes) for compression to kick in */
    long min_size=500;

    /** Number of inflaters/deflaters, for concurrency, increase this to the max number of concurrent requests possible */
    int pool_size=2;


    final static String name="COMPRESS";

    public String getName() {
        return name;
    }


    public void init() throws Exception {
        deflater_pool=new ArrayBlockingQueue<Deflater>(pool_size);
        for(int i=0; i < pool_size; i++) {
            deflater_pool.add(new Deflater(compression_level));
        }
        inflater_pool=new ArrayBlockingQueue<Inflater>(pool_size);
        for(int i=0; i < pool_size; i++) {
            inflater_pool.add(new Inflater());
        }
    }

    public void destroy() {
        for(Deflater deflater: deflater_pool)
            deflater.end();
        for(Inflater inflater: inflater_pool)
            inflater.end();
    }


    public boolean setProperties(Properties props) {
        String str;

        super.setProperties(props);
        str=props.getProperty("compression_level");
        if(str != null) {
            compression_level=Integer.parseInt(str);
            props.remove("compression_level");
        }

        str=props.getProperty("min_size");
        if(str != null) {
            min_size=Long.parseLong(str);
            props.remove("min_size");
        }

        str=props.getProperty("pool_size");
        if(str != null) {
            pool_size=Integer.parseInt(str);
            if(pool_size <= 0) {
                log.warn("pool_size must be > 0, setting it to 1");
                pool_size=1;
            }
            props.remove("pool_size");
        }

        if(!props.isEmpty()) {
            log.error("the following properties are not recognized: " + props);
            return false;
        }
        return true;
    }



    /**
     * We compress the payload if it is larger than <code>min_size</code>. In this case we add a header containing
     * the original size before compression. Otherwise we add no header.<br/>
     * Note that we compress either the entire buffer (if offset/length are not used), or a subset (if offset/length
     * are used)
     * @param evt
     */
    public Object down(Event evt) {
        if(evt.getType() == Event.MSG) {
            Message msg=(Message)evt.getArg();
            int length=msg.getLength(); // takes offset/length (if set) into account
            if(length >= min_size) {
                byte[] payload=msg.getRawBuffer(); // here we get the ref so we can avoid copying
                byte[] compressed_payload=new byte[length];
                int compressed_size;
                Deflater deflater=null;
                try {
                    deflater=deflater_pool.take();
                    deflater.reset();
                    deflater.setInput(payload, msg.getOffset(), length);
                    deflater.finish();
                    deflater.deflate(compressed_payload);
                    compressed_size=deflater.getTotalOut();
                    byte[] new_payload=new byte[compressed_size];
                    System.arraycopy(compressed_payload, 0, new_payload, 0, compressed_size);
                    msg.setBuffer(new_payload);
                    msg.putHeader(name, new CompressHeader(length));
                    if(log.isTraceEnabled())
                        log.trace("compressed payload from " + length + " bytes to " + compressed_size + " bytes");
                }
                catch(InterruptedException e) {
                    Thread.currentThread().interrupt(); // set interrupt flag again
                    throw new RuntimeException(e);
                }
                finally {
                    if(deflater != null)
                        deflater_pool.offer(deflater);
                }
            }
        }
        return down_prot.down(evt);
    }



    /**
     * If there is no header, we pass the message up. Otherwise we uncompress the payload to its original size.
     * @param evt
     */
    public Object up(Event evt) {
        if(evt.getType() == Event.MSG) {
            Message msg=(Message)evt.getArg();
            CompressHeader hdr=(CompressHeader)msg.getHeader(name);
            if(hdr != null) {
                byte[] compressed_payload=msg.getRawBuffer();
                if(compressed_payload != null && compressed_payload.length > 0) {
                    int original_size=hdr.original_size;
                    byte[] uncompressed_payload=new byte[original_size];
                    Inflater inflater=null;
                    try {
                        inflater=inflater_pool.take();
                        inflater.reset();
                        inflater.setInput(compressed_payload, msg.getOffset(), msg.getLength());
                        try {
                            inflater.inflate(uncompressed_payload);
                            if(log.isTraceEnabled())
                                log.trace("uncompressed " + compressed_payload.length + " bytes to " + original_size +
                                        " bytes");
                            // we need to copy: https://jira.jboss.org/jira/browse/JGRP-867
                            Message copy=msg.copy(false);
                            copy.setBuffer(uncompressed_payload);
                            return up_prot.up(new Event(Event.MSG, copy));
                            // msg.setBuffer(uncompressed_payload);
                        }
                        catch(DataFormatException e) {
                            if(log.isErrorEnabled()) log.error("exception on uncompression", e);
                        }
                    }
                    catch(InterruptedException e) {
                        Thread.currentThread().interrupt(); // set the interrupt bit again, so caller can handle it
                    }
                    finally {
                        if(inflater != null)
                            inflater_pool.offer(inflater);
                    }

                }
            }
        }
        return up_prot.up(evt);
    }






    public static class CompressHeader extends Header implements Streamable {
        int original_size=0;

        public CompressHeader() {
            super();
        }

        public CompressHeader(int s) {
            original_size=s;
        }


        public int size() {
            return Global.INT_SIZE;
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(original_size);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            original_size=in.readInt();
        }

        public void writeTo(DataOutputStream out) throws IOException {
            out.writeInt(original_size);
        }

        public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
            original_size=in.readInt();
        }
    }
}
