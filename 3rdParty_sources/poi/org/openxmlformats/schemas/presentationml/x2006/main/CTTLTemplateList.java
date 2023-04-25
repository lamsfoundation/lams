/*
 * XML Type:  CT_TLTemplateList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplateList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLTemplateList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLTemplateList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplateList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttltemplatelist7ab5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "tmpl" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate> getTmplList();

    /**
     * Gets array of all "tmpl" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate[] getTmplArray();

    /**
     * Gets ith "tmpl" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate getTmplArray(int i);

    /**
     * Returns number of "tmpl" element
     */
    int sizeOfTmplArray();

    /**
     * Sets array of all "tmpl" element
     */
    void setTmplArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate[] tmplArray);

    /**
     * Sets ith "tmpl" element
     */
    void setTmplArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate tmpl);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tmpl" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate insertNewTmpl(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "tmpl" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate addNewTmpl();

    /**
     * Removes the ith "tmpl" element
     */
    void removeTmpl(int i);
}
