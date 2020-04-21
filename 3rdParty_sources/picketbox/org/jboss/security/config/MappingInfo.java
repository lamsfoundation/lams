/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.security.config;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.security.mapping.config.MappingModuleEntry;

/**
 *  Mapping Info
 *  
 *  @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 *  @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 *  @version $Revision$
 *  @since  Aug 28, 2006
 */
public class MappingInfo extends BaseSecurityInfo<MappingModuleEntry>
{  
   public MappingInfo()
   {
      super();
   }
   
   public MappingInfo(String name)
   {
      super(name);
   }  

   public MappingModuleEntry[] getMappingModuleEntry()
   {
      SecurityManager sm = System.getSecurityManager();
      if( sm != null )
         sm.checkPermission(GET_CONFIG_ENTRY_PERM); 
      MappingModuleEntry[] entries = new MappingModuleEntry[moduleEntries.size()];
      moduleEntries.toArray(entries);
      return entries;
   }

   @Override
   protected BaseSecurityInfo<MappingModuleEntry> create(String name)
   { 
      return new MappingInfo(name);
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
         MappingModuleEntry entry = moduleEntries.get(i);
         writer.writeStartElement(Element.MAPPING_MODULE.getLocalName());
         writer.writeAttribute(Attribute.CODE.getLocalName(), entry.getMappingModuleName());
         writer.writeAttribute(Attribute.TYPE.getLocalName(), entry.getMappingModuleType());
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