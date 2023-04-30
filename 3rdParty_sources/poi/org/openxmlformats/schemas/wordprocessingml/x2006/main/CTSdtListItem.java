/*
 * XML Type:  CT_SdtListItem
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtListItem
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SdtListItem(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSdtListItem extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtListItem> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsdtlistitem705etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "displayText" attribute
     */
    java.lang.String getDisplayText();

    /**
     * Gets (as xml) the "displayText" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetDisplayText();

    /**
     * True if has "displayText" attribute
     */
    boolean isSetDisplayText();

    /**
     * Sets the "displayText" attribute
     */
    void setDisplayText(java.lang.String displayText);

    /**
     * Sets (as xml) the "displayText" attribute
     */
    void xsetDisplayText(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString displayText);

    /**
     * Unsets the "displayText" attribute
     */
    void unsetDisplayText();

    /**
     * Gets the "value" attribute
     */
    java.lang.String getValue();

    /**
     * Gets (as xml) the "value" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetValue();

    /**
     * True if has "value" attribute
     */
    boolean isSetValue();

    /**
     * Sets the "value" attribute
     */
    void setValue(java.lang.String value);

    /**
     * Sets (as xml) the "value" attribute
     */
    void xsetValue(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString value);

    /**
     * Unsets the "value" attribute
     */
    void unsetValue();
}
