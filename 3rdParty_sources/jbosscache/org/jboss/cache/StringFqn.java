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
package org.jboss.cache;

import org.jboss.cache.annotations.Experimental;

import java.util.Arrays;
import java.util.List;

/**
 * An optimisation of Fqn that does more efficient equals() and hashcode() computations.  This is returned by default when
 * the factory method {@link Fqn#fromString(String)} is used, or when any of the other factory methods on {@link Fqn} are
 * passed only String elements.
 * <p/>
 * <b>Note</b> that the "/" character is illegal in any Fqn String element and if encountered may be used to split Fqn elements.
 * Expect indeterminate behaviour until proper String escaping is in place.
 * <p/>
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.2.0
 */
// TODO: 3.0.0: Implement proper String escaping.
@Experimental
public final class StringFqn extends Fqn
{
   protected StringFqn()
   {
      super();
      stringRepresentation = SEPARATOR;
   }

   @SuppressWarnings("unchecked")
   protected StringFqn(StringFqn base, List<String> elements)
   {
      super(base, (List) elements);
      String elementStringRep = getStringRepresentation((List) elements);
      stringRepresentation = base.isRoot() ? elementStringRep : base.stringRepresentation + elementStringRep;
   }

   protected StringFqn(StringFqn base, StringFqn relative)
   {
      super(base, relative.elements);
      if (base.isRoot())
      {
         if (relative.isRoot())
            stringRepresentation = SEPARATOR;
         else
            stringRepresentation = relative.stringRepresentation;
      }
      else
      {
         if (relative.isRoot())
            stringRepresentation = base.stringRepresentation;
         else
            stringRepresentation = base.stringRepresentation + relative.stringRepresentation;
      }
   }

   @SuppressWarnings("unchecked")
   protected StringFqn(List<String> stringElements)
   {
      super((List) stringElements, false);
      stringRepresentation = getStringRepresentation(elements);
   }

   protected StringFqn(String stringRep)
   {
      this(Arrays.asList(stringRep.split("/")));
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this) return true;
      if (other == null) return false;
      if (other.getClass().equals(StringFqn.class))
      {
         return stringRepresentation.equals(((StringFqn) other).stringRepresentation);
      }
      else
      {
         return super.equals(other);
      }
   }

   @Override
   protected int calculateHashCode()
   {
      return stringRepresentation.hashCode();
   }

   @Override
   @SuppressWarnings(value = "unchecked")
   public boolean isChildOrEquals(Fqn parentFqn)
   {
      if (parentFqn.getClass().equals(StringFqn.class))
      {
         StringFqn stringParentFqn = (StringFqn) parentFqn;
         return stringRepresentation.startsWith(stringParentFqn.stringRepresentation);
      }
      else
      {
         return super.isChildOrEquals(parentFqn);
      }
   }

   public String getStringRepresentation()
   {
      return stringRepresentation;
   }
}
