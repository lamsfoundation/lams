/*
 * XML Type:  CT_RecipientData
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RecipientData(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRecipientData extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrecipientdata064ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "active" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getActive();

    /**
     * True if has "active" element
     */
    boolean isSetActive();

    /**
     * Sets the "active" element
     */
    void setActive(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff active);

    /**
     * Appends and returns a new empty "active" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewActive();

    /**
     * Unsets the "active" element
     */
    void unsetActive();

    /**
     * Gets the "column" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getColumn();

    /**
     * Sets the "column" element
     */
    void setColumn(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber column);

    /**
     * Appends and returns a new empty "column" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewColumn();

    /**
     * Gets the "uniqueTag" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBase64Binary getUniqueTag();

    /**
     * Sets the "uniqueTag" element
     */
    void setUniqueTag(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBase64Binary uniqueTag);

    /**
     * Appends and returns a new empty "uniqueTag" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBase64Binary addNewUniqueTag();
}
