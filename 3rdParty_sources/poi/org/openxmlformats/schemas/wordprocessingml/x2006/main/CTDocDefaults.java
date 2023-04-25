/*
 * XML Type:  CT_DocDefaults
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocDefaults(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocDefaults extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocdefaults2ea8type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rPrDefault" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault getRPrDefault();

    /**
     * True if has "rPrDefault" element
     */
    boolean isSetRPrDefault();

    /**
     * Sets the "rPrDefault" element
     */
    void setRPrDefault(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault rPrDefault);

    /**
     * Appends and returns a new empty "rPrDefault" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault addNewRPrDefault();

    /**
     * Unsets the "rPrDefault" element
     */
    void unsetRPrDefault();

    /**
     * Gets the "pPrDefault" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault getPPrDefault();

    /**
     * True if has "pPrDefault" element
     */
    boolean isSetPPrDefault();

    /**
     * Sets the "pPrDefault" element
     */
    void setPPrDefault(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault pPrDefault);

    /**
     * Appends and returns a new empty "pPrDefault" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault addNewPPrDefault();

    /**
     * Unsets the "pPrDefault" element
     */
    void unsetPPrDefault();
}
