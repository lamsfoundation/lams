/*
 * XML Type:  CT_Br
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Br(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbr7dd8type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "clear" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrClear.Enum getClear();

    /**
     * Gets (as xml) the "clear" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrClear xgetClear();

    /**
     * True if has "clear" attribute
     */
    boolean isSetClear();

    /**
     * Sets the "clear" attribute
     */
    void setClear(org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrClear.Enum clear);

    /**
     * Sets (as xml) the "clear" attribute
     */
    void xsetClear(org.openxmlformats.schemas.wordprocessingml.x2006.main.STBrClear clear);

    /**
     * Unsets the "clear" attribute
     */
    void unsetClear();
}
