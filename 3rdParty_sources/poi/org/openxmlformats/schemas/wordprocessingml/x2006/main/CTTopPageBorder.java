/*
 * XML Type:  CT_TopPageBorder
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TopPageBorder(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTopPageBorder extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageBorder {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTopPageBorder> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttoppageborder3c02type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "topLeft" attribute
     */
    java.lang.String getTopLeft();

    /**
     * Gets (as xml) the "topLeft" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetTopLeft();

    /**
     * True if has "topLeft" attribute
     */
    boolean isSetTopLeft();

    /**
     * Sets the "topLeft" attribute
     */
    void setTopLeft(java.lang.String topLeft);

    /**
     * Sets (as xml) the "topLeft" attribute
     */
    void xsetTopLeft(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId topLeft);

    /**
     * Unsets the "topLeft" attribute
     */
    void unsetTopLeft();

    /**
     * Gets the "topRight" attribute
     */
    java.lang.String getTopRight();

    /**
     * Gets (as xml) the "topRight" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetTopRight();

    /**
     * True if has "topRight" attribute
     */
    boolean isSetTopRight();

    /**
     * Sets the "topRight" attribute
     */
    void setTopRight(java.lang.String topRight);

    /**
     * Sets (as xml) the "topRight" attribute
     */
    void xsetTopRight(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId topRight);

    /**
     * Unsets the "topRight" attribute
     */
    void unsetTopRight();
}
