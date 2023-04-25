/*
 * XML Type:  CT_TLTemplate
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLTemplate(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLTemplate extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttltemplate3ab3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tnLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList getTnLst();

    /**
     * Sets the "tnLst" element
     */
    void setTnLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList tnLst);

    /**
     * Appends and returns a new empty "tnLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList addNewTnLst();

    /**
     * Gets the "lvl" attribute
     */
    long getLvl();

    /**
     * Gets (as xml) the "lvl" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetLvl();

    /**
     * True if has "lvl" attribute
     */
    boolean isSetLvl();

    /**
     * Sets the "lvl" attribute
     */
    void setLvl(long lvl);

    /**
     * Sets (as xml) the "lvl" attribute
     */
    void xsetLvl(org.apache.xmlbeans.XmlUnsignedInt lvl);

    /**
     * Unsets the "lvl" attribute
     */
    void unsetLvl();
}
