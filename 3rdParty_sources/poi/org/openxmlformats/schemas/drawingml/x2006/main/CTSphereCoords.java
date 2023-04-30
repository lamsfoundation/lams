/*
 * XML Type:  CT_SphereCoords
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SphereCoords(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSphereCoords extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTSphereCoords> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctspherecoords8226type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "lat" attribute
     */
    int getLat();

    /**
     * Gets (as xml) the "lat" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle xgetLat();

    /**
     * Sets the "lat" attribute
     */
    void setLat(int lat);

    /**
     * Sets (as xml) the "lat" attribute
     */
    void xsetLat(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle lat);

    /**
     * Gets the "lon" attribute
     */
    int getLon();

    /**
     * Gets (as xml) the "lon" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle xgetLon();

    /**
     * Sets the "lon" attribute
     */
    void setLon(int lon);

    /**
     * Sets (as xml) the "lon" attribute
     */
    void xsetLon(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle lon);

    /**
     * Gets the "rev" attribute
     */
    int getRev();

    /**
     * Gets (as xml) the "rev" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle xgetRev();

    /**
     * Sets the "rev" attribute
     */
    void setRev(int rev);

    /**
     * Sets (as xml) the "rev" attribute
     */
    void xsetRev(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle rev);
}
