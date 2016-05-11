

package org.jgroups.protocols;


import org.jgroups.Message;

import java.io.Serializable;



/**
 * Lighweight representation of the VectorTime clock suitable for network transport
 *
 * @author Vladimir Blagojevic vladimir@cs.yorku.ca
 * @version $Revision$
 */
public class TransportedVectorTime implements Serializable
{
   /**
    * index of the sender
    */
   int senderPosition;

   /**
    * values represented as an array
    */
   int[] values;

   /**
    * Message associated with this vector clock
    */
   private transient Message m;
    private static final long serialVersionUID = 5857647322589533545L;

    /**
     *
     */
    public TransportedVectorTime()
    {
    }

   /**
    * Constructs TransportedVectorTime with sender index and vector values
    *
    * @param senderIndex index of the sender of the message
    * @param values vector values
    */
   public TransportedVectorTime(int senderIndex, int[] values)
   {
      this.values = values;
      this.senderPosition = senderIndex;
   }

   /**
    * Returns sender index
    * @return sender index position
    */
   public int getSenderIndex()
   {
      return senderPosition;
   }

   /**
    * Returns vector values
    * @return an array of vector values
    */
   public int[] getValues()
   {
      return values;
   }

   /**
    * Returns size of this vector timestamp  i.e number of process group members
    * @return vector timestamp size
    */
   public int size()
   {
      return values.length;
   }

   /**
    * Sets a message associated with this vector timestamp
    * @param owner Message that is associated with this vector timestamp
    */
   public void setAssociatedMessage(Message owner)
   {
      m = owner;
   }

   /**
    * Returns a message associated with this vector timestamp.
    * @return Message associated with this vector timestamp
    */
   public Message getAssociatedMessage()
   {
      return m;
   }


   /**
    *<p>
    *Checks if this TransportedVectorTime is less than or equal to the the specified TransportedVectorTime.
    *The check is done as follows:
    *</p>
    * <p>
    * VT1<=VT2 iff for every i:1..k VT1[i]<=VT2[i]
    * </p>
    * @param other TransportedVectorTimebeing compared with this.
    * @return true if this TransportedVectorTimeis less than or equal from
    * other, false othwerwise
    */
   public boolean lessThanOrEqual(TransportedVectorTime other)
   {
      int[] b = other.getValues();
      int[] a = values;
      for (int k = 0; k < a.length; k++)
      {
          if (a[k] < b[k])
              return true;
          else if (a[k] > b[k])
              return false;
      }
       return true; // equals ?!?
   }

   /**
    * <p>
    * Checks if this TransportedVectorTimeis equal to the specified TransportedVectorTime.
    * The check is done as follows:
    * </p>
    * <p>
    *  VT1==VT2 iff for every i:1..k VT1[i]==VT2[i]
    * @param other TransportedVectorTimebeing compared with this.
    * @return true if the equation given above is true, false otherwise
    */
   public boolean equals(Object other)
   {
      int a [] = getValues();
      int b [] = ((TransportedVectorTime)other).getValues();

      for (int i = 0; i < a.length; i++)
         if (a[i] != b[i]) return false;

      return true;
   }

   /**
    * Returns String representation of this vector timestamp
    * @return String representing this vetor timestamp
    */
   public String toString()
   {
      String classType = "TransportedVectorTime[";
      int bufferLength = classType.length() + values.length * 2 + 1;
      StringBuilder buf = new StringBuilder(bufferLength);
      buf.append(classType);
      for (int i = 0; i < values.length - 1; i++)
      {
         buf.append(values[i]).append(',');
      }
      buf.append(values[values.length - 1]);
      buf.append(']');
      return buf.toString();
   }
}
