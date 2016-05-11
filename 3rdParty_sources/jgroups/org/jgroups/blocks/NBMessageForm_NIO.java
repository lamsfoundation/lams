

package org.jgroups.blocks;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NBMessageForm - Message form for non-blocking message reads.
 * @author akbollu
 * @author Bela Ban
 */
public class NBMessageForm_NIO
{
	ByteBuffer headerBuffer = null;
	ByteBuffer dataBuffer = null;
	static final int HEADER_SIZE = 4;
	final boolean isComplete = false;
	int messageSize = 0;
	boolean w_in_p = false;
	SocketChannel channel = null;



	public NBMessageForm_NIO(int dataBuffSize, SocketChannel ch)
	{
		headerBuffer = ByteBuffer.allocate(HEADER_SIZE);
		dataBuffer = ByteBuffer.allocate(dataBuffSize);
		channel = ch;
	}



    public ByteBuffer readCompleteMsgBuffer() throws IOException
    {

        int rt;

        try {
            rt = channel.read(headerBuffer);
            if ( (rt == 0) || (rt == -1) )
            {
                channel.close();
                return null;
            }
            if (rt == HEADER_SIZE)
            {
                headerBuffer.flip();
                messageSize = headerBuffer.getInt();
                if(dataBuffer.capacity() < messageSize)
                {
                    dataBuffer = ByteBuffer.allocate(messageSize);
                }
            }
            else {
                return null;
            }
        }
        catch(IOException ex) {
            channel.close();
            throw ex;
        }


        //rt == 0 need not be checked twice in the same event
        channel.read(dataBuffer);
        if(isComplete())
        {
            dataBuffer.flip();
            return dataBuffer;
        }
        return null;
    }



	public void reset()
	{
		dataBuffer.clear();
		headerBuffer.clear();
		messageSize = 0;
		w_in_p = false;
	}
	
	private boolean isComplete()
	{
		return ( dataBuffer.position() == messageSize );
	}	
}
