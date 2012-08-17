package org.jboss.cache;

/**
 * Defines lifecycle operations for various components
 *
 * @author Manik Surtani
 */
public interface Lifecycle
{
   void create() throws Exception;

   void start() throws Exception;

   void stop();

   void destroy();
}
