package org.jgroups.util;

/**
 * An object that manages threads and provides callbacks to a 
 * {@link ThreadDecorator} to allow it to alter their state.
 * 
 * @author Brian Stansberry
 *
 */
public interface ThreadManager {
   /**
    * Gets the ThreadDecorator associated with this manager.
    * @return the ThreadDecorator, or <code>null</code> if there is none.
    */
   ThreadDecorator getThreadDecorator();
   
   /**
    * Sets the ThreadDecorator associated this manager should use.
    * @param decorator the ThreadDecorator, or <code>null</code>.
    */
   void setThreadDecorator(ThreadDecorator decorator);
}
