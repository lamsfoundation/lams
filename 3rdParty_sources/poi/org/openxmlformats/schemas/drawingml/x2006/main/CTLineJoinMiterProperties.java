/*
 * XML Type:  CT_LineJoinMiterProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LineJoinMiterProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLineJoinMiterProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlinejoinmiterproperties02abtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "lim" attribute
     */
    java.lang.Object getLim();

    /**
     * Gets (as xml) the "lim" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage xgetLim();

    /**
     * True if has "lim" attribute
     */
    boolean isSetLim();

    /**
     * Sets the "lim" attribute
     */
    void setLim(java.lang.Object lim);

    /**
     * Sets (as xml) the "lim" attribute
     */
    void xsetLim(org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage lim);

    /**
     * Unsets the "lim" attribute
     */
    void unsetLim();
}
