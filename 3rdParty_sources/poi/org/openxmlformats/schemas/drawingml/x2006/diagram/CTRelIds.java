/*
 * XML Type:  CT_RelIds
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RelIds(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTRelIds extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrelidsfef2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dm" attribute
     */
    java.lang.String getDm();

    /**
     * Gets (as xml) the "dm" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetDm();

    /**
     * Sets the "dm" attribute
     */
    void setDm(java.lang.String dm);

    /**
     * Sets (as xml) the "dm" attribute
     */
    void xsetDm(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId dm);

    /**
     * Gets the "lo" attribute
     */
    java.lang.String getLo();

    /**
     * Gets (as xml) the "lo" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetLo();

    /**
     * Sets the "lo" attribute
     */
    void setLo(java.lang.String lo);

    /**
     * Sets (as xml) the "lo" attribute
     */
    void xsetLo(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId lo);

    /**
     * Gets the "qs" attribute
     */
    java.lang.String getQs();

    /**
     * Gets (as xml) the "qs" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetQs();

    /**
     * Sets the "qs" attribute
     */
    void setQs(java.lang.String qs);

    /**
     * Sets (as xml) the "qs" attribute
     */
    void xsetQs(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId qs);

    /**
     * Gets the "cs" attribute
     */
    java.lang.String getCs();

    /**
     * Gets (as xml) the "cs" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetCs();

    /**
     * Sets the "cs" attribute
     */
    void setCs(java.lang.String cs);

    /**
     * Sets (as xml) the "cs" attribute
     */
    void xsetCs(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId cs);
}
