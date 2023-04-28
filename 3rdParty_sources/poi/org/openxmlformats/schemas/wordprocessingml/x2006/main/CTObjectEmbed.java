/*
 * XML Type:  CT_ObjectEmbed
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ObjectEmbed(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTObjectEmbed extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctobjectembedb578type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "drawAspect" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectDrawAspect.Enum getDrawAspect();

    /**
     * Gets (as xml) the "drawAspect" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectDrawAspect xgetDrawAspect();

    /**
     * True if has "drawAspect" attribute
     */
    boolean isSetDrawAspect();

    /**
     * Sets the "drawAspect" attribute
     */
    void setDrawAspect(org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectDrawAspect.Enum drawAspect);

    /**
     * Sets (as xml) the "drawAspect" attribute
     */
    void xsetDrawAspect(org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectDrawAspect drawAspect);

    /**
     * Unsets the "drawAspect" attribute
     */
    void unsetDrawAspect();

    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id);

    /**
     * Gets the "progId" attribute
     */
    java.lang.String getProgId();

    /**
     * Gets (as xml) the "progId" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetProgId();

    /**
     * True if has "progId" attribute
     */
    boolean isSetProgId();

    /**
     * Sets the "progId" attribute
     */
    void setProgId(java.lang.String progId);

    /**
     * Sets (as xml) the "progId" attribute
     */
    void xsetProgId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString progId);

    /**
     * Unsets the "progId" attribute
     */
    void unsetProgId();

    /**
     * Gets the "shapeId" attribute
     */
    java.lang.String getShapeId();

    /**
     * Gets (as xml) the "shapeId" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetShapeId();

    /**
     * True if has "shapeId" attribute
     */
    boolean isSetShapeId();

    /**
     * Sets the "shapeId" attribute
     */
    void setShapeId(java.lang.String shapeId);

    /**
     * Sets (as xml) the "shapeId" attribute
     */
    void xsetShapeId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString shapeId);

    /**
     * Unsets the "shapeId" attribute
     */
    void unsetShapeId();

    /**
     * Gets the "fieldCodes" attribute
     */
    java.lang.String getFieldCodes();

    /**
     * Gets (as xml) the "fieldCodes" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetFieldCodes();

    /**
     * True if has "fieldCodes" attribute
     */
    boolean isSetFieldCodes();

    /**
     * Sets the "fieldCodes" attribute
     */
    void setFieldCodes(java.lang.String fieldCodes);

    /**
     * Sets (as xml) the "fieldCodes" attribute
     */
    void xsetFieldCodes(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString fieldCodes);

    /**
     * Unsets the "fieldCodes" attribute
     */
    void unsetFieldCodes();
}
