/*
 * XML Type:  CT_BottomPageBorder
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BottomPageBorder(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBottomPageBorder extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBottomPageBorder> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbottompageborderde82type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bottomLeft" attribute
     */
    java.lang.String getBottomLeft();

    /**
     * Gets (as xml) the "bottomLeft" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetBottomLeft();

    /**
     * True if has "bottomLeft" attribute
     */
    boolean isSetBottomLeft();

    /**
     * Sets the "bottomLeft" attribute
     */
    void setBottomLeft(java.lang.String bottomLeft);

    /**
     * Sets (as xml) the "bottomLeft" attribute
     */
    void xsetBottomLeft(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId bottomLeft);

    /**
     * Unsets the "bottomLeft" attribute
     */
    void unsetBottomLeft();

    /**
     * Gets the "bottomRight" attribute
     */
    java.lang.String getBottomRight();

    /**
     * Gets (as xml) the "bottomRight" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetBottomRight();

    /**
     * True if has "bottomRight" attribute
     */
    boolean isSetBottomRight();

    /**
     * Sets the "bottomRight" attribute
     */
    void setBottomRight(java.lang.String bottomRight);

    /**
     * Sets (as xml) the "bottomRight" attribute
     */
    void xsetBottomRight(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId bottomRight);

    /**
     * Unsets the "bottomRight" attribute
     */
    void unsetBottomRight();
}
