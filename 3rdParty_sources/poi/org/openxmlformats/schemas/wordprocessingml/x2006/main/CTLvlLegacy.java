/*
 * XML Type:  CT_LvlLegacy
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvlLegacy
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LvlLegacy(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLvlLegacy extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvlLegacy> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlvllegacy10a7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "legacy" attribute
     */
    java.lang.Object getLegacy();

    /**
     * Gets (as xml) the "legacy" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetLegacy();

    /**
     * True if has "legacy" attribute
     */
    boolean isSetLegacy();

    /**
     * Sets the "legacy" attribute
     */
    void setLegacy(java.lang.Object legacy);

    /**
     * Sets (as xml) the "legacy" attribute
     */
    void xsetLegacy(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff legacy);

    /**
     * Unsets the "legacy" attribute
     */
    void unsetLegacy();

    /**
     * Gets the "legacySpace" attribute
     */
    java.lang.Object getLegacySpace();

    /**
     * Gets (as xml) the "legacySpace" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetLegacySpace();

    /**
     * True if has "legacySpace" attribute
     */
    boolean isSetLegacySpace();

    /**
     * Sets the "legacySpace" attribute
     */
    void setLegacySpace(java.lang.Object legacySpace);

    /**
     * Sets (as xml) the "legacySpace" attribute
     */
    void xsetLegacySpace(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure legacySpace);

    /**
     * Unsets the "legacySpace" attribute
     */
    void unsetLegacySpace();

    /**
     * Gets the "legacyIndent" attribute
     */
    java.lang.Object getLegacyIndent();

    /**
     * Gets (as xml) the "legacyIndent" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetLegacyIndent();

    /**
     * True if has "legacyIndent" attribute
     */
    boolean isSetLegacyIndent();

    /**
     * Sets the "legacyIndent" attribute
     */
    void setLegacyIndent(java.lang.Object legacyIndent);

    /**
     * Sets (as xml) the "legacyIndent" attribute
     */
    void xsetLegacyIndent(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure legacyIndent);

    /**
     * Unsets the "legacyIndent" attribute
     */
    void unsetLegacyIndent();
}
