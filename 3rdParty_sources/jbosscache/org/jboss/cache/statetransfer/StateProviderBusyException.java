package org.jboss.cache.statetransfer;

import org.jboss.cache.CacheException;

/**
 * Thrown when a state provider is busy
 *
 * @author Manik Surtani
 * @since 3.1
 */
public class StateProviderBusyException extends CacheException
{
   public StateProviderBusyException()
   {
   }

   public StateProviderBusyException(String message)
   {
      super(message);
   }

   public StateProviderBusyException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public StateProviderBusyException(Throwable cause)
   {
      super(cause);
   }
}
