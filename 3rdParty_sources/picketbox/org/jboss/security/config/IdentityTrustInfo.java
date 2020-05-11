/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.security.config;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.security.identitytrust.config.IdentityTrustModuleEntry;

/**
 *  Identity Trust Info
 *  
 *  @author <a href="mailto:Anil.Saldhana@redhat.com">Anil Saldhana</a>
 *  @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 *  @version $Revision$
 *  @since  July 25, 2007
 */
public class IdentityTrustInfo extends BaseSecurityInfo<IdentityTrustModuleEntry>
{  
   public IdentityTrustInfo(String name)
   { 
      super(name);
   } 
   
   public IdentityTrustModuleEntry[] getIdentityTrustModuleEntry()
   {
      SecurityManager sm = System.getSecurityManager();
      if( sm != null )
         sm.checkPermission(GET_CONFIG_ENTRY_PERM); 
      IdentityTrustModuleEntry[] entries = new IdentityTrustModuleEntry[moduleEntries.size()];
      moduleEntries.toArray(entries);
      return entries;
   }

   @Override
   protected BaseSecurityInfo<IdentityTrustModuleEntry> create(String name)
   { 
      return new IdentityTrustInfo(name);
   }
   
   /**
    * Write element content. The start element is already written.
    * 
    * @param writer
    * @throws XMLStreamException
    */
   public void writeContent(XMLStreamWriter writer) throws XMLStreamException
   {
      for (int i = 0; i < moduleEntries.size(); i++)
      {
         IdentityTrustModuleEntry entry = moduleEntries.get(i);
         writer.writeStartElement(Element.TRUST_MODULE.getLocalName());
         writer.writeAttribute(Attribute.CODE.getLocalName(), entry.getName());
         writer.writeAttribute(Attribute.FLAG.getLocalName(), entry.getControlFlag().toString().toLowerCase(Locale.ENGLISH));
         Map<String, ?> options = entry.getOptions();
         if (options != null && options.size() > 0)
         {
            for (Entry<String, ?> option : options.entrySet())
            {
               writer.writeStartElement(Element.MODULE_OPTION.getLocalName());
               writer.writeAttribute(Attribute.NAME.getLocalName(), option.getKey());
               writer.writeAttribute(Attribute.VALUE.getLocalName(), option.getValue().toString());
               writer.writeEndElement();
            }
         }
         writer.writeEndElement();
      }
      writer.writeEndElement();
   }
}