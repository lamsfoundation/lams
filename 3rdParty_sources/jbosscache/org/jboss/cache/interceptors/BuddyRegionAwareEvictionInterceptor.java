/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cache.interceptors;

import org.jboss.cache.Fqn;
import org.jboss.cache.Region;
import org.jboss.cache.buddyreplication.BuddyFqnTransformer;
import org.jboss.cache.factories.annotations.Inject;

/**
 * A subclass of EvictionInterceptor that is aware of and able to deal with buddy regions.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
public class BuddyRegionAwareEvictionInterceptor extends EvictionInterceptor
{
   private BuddyFqnTransformer buddyFqnTransformer;

   @Inject
   public void initialize(BuddyFqnTransformer transformer)
   {
      this.buddyFqnTransformer = transformer;
   }

   @Override
   protected Region getRegion(Fqn fqn)
   {
      Region r = super.getRegion(fqn);
      if (r != null)
         return r;
      else if (buddyFqnTransformer.isBackupFqn(fqn))
      {
         // try and grab a backup region, creating one if need be.
         Fqn actualFqn = buddyFqnTransformer.getActualFqn(fqn);
         Fqn backupRoot = buddyFqnTransformer.getBackupRootFromFqn(fqn);

         // the actual region could be a few levels higher than actualFqn
         Region actualRegion = regionManager.getRegion(actualFqn, Region.Type.EVICTION, false);

         if (actualRegion == null) return null;

         //create a new region for this backup
         Region newRegion = regionManager.getRegion(Fqn.fromRelativeFqn(backupRoot, actualRegion.getFqn()), Region.Type.EVICTION, true);
         newRegion.setEvictionRegionConfig(actualRegion.getEvictionRegionConfig());

         return newRegion;
      }
      else return null;
   }
}
