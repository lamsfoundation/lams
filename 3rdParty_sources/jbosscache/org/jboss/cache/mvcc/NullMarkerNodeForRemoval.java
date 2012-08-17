package org.jboss.cache.mvcc;

import org.jboss.cache.DataContainer;
import org.jboss.cache.Fqn;
import org.jboss.cache.InternalNode;
import org.jboss.cache.InvocationContext;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * A specific type of null marker node, used for removal of nodes that don't exist
 *
 * @author Manik Surtani
 * @since 3.1.0
 */
public class NullMarkerNodeForRemoval extends RepeatableReadNode
{
   private Fqn fqn;
   
   public NullMarkerNodeForRemoval(InternalNode parent, Fqn fqn)
   {
      super(null, parent);
      this.fqn = fqn;
   }

   @Override
   public Fqn getFqn()
   {
      return fqn;
   }

   /**
    * @return always returns true
    */
   @Override
   public boolean isNullNode()
   {
      return true;
   }

   /**
    * @return always returns true so that any get commands, upon getting this node, will ignore the node as though it were invalid.
    */
   @Override
   public boolean isValid()
   {
      return false;
   }

   /**
    * @return always returns true so that any get commands, upon getting this node, will ignore the node as though it were removed.
    */
   @Override
   public boolean isDeleted()
   {
      return true;
   }

   @Override
   protected void updateNode(Fqn fqn, InvocationContext ctx, DataContainer dataContainer)
   {
      // no-op since the only updates that are allowed to happen here are the removal of the node, which only affects the parent.
   }

   @Override
   public Map getDataDirect()
   {
      return Collections.emptyMap();
   }

   @Override
   public Set getChildrenNamesDirect()
   {
      return Collections.emptySet();
   }

   @Override
   public Set getChildrenDirect()
   {
      return Collections.emptySet();
   }

   @Override
   public void setValid(boolean valid, boolean recursive)
   {
      // no-op
   }
}
