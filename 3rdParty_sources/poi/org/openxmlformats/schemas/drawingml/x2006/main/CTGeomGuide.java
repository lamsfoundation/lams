/*
 * XML Type:  CT_GeomGuide
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GeomGuide(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGeomGuide extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgeomguidef191type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName name);

    /**
     * Gets the "fmla" attribute
     */
    java.lang.String getFmla();

    /**
     * Gets (as xml) the "fmla" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideFormula xgetFmla();

    /**
     * Sets the "fmla" attribute
     */
    void setFmla(java.lang.String fmla);

    /**
     * Sets (as xml) the "fmla" attribute
     */
    void xsetFmla(org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideFormula fmla);
}
