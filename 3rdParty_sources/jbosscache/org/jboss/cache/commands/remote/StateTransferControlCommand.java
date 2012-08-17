package org.jboss.cache.commands.remote;

import org.jboss.cache.InvocationContext;
import org.jboss.cache.RPCManager;
import org.jboss.cache.commands.ReplicableCommand;

/**
 * A control command for communication between peers for non-blocking state transfer
 *
 * @author Manik Surtani
 */
public class StateTransferControlCommand implements ReplicableCommand
{
   public static final int METHOD_ID = 49;
   RPCManager rpcManager;
   boolean enabled;

   public StateTransferControlCommand()
   {
   }

   public StateTransferControlCommand(boolean enabled)
   {
      this.enabled = enabled;
   }

   public void init(RPCManager rpcManager)
   {
      this.rpcManager = rpcManager;
   }

   public Object perform(InvocationContext ctx) throws Throwable
   {
      if (enabled)
         rpcManager.getFlushTracker().block();
      else
         rpcManager.getFlushTracker().unblock();
      return null;
   }

   public int getCommandId()
   {
      return METHOD_ID;
   }

   public Object[] getParameters()
   {
      return new Object[]{enabled};
   }

   public void setParameters(int commandId, Object[] parameters)
   {
      enabled = (Boolean) parameters[0];
   }

   @Override
   public String toString()
   {
      return "StateTransferControlCommand{" +
            "enabled=" + enabled +
            '}';
   }
}
