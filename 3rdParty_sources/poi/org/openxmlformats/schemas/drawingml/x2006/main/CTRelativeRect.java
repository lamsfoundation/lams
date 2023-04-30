/*
 * XML Type:  CT_RelativeRect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RelativeRect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRelativeRect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrelativerecta4ebtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "l" attribute
     */
    java.lang.Object getL();

    /**
     * Gets (as xml) the "l" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetL();

    /**
     * True if has "l" attribute
     */
    boolean isSetL();

    /**
     * Sets the "l" attribute
     */
    void setL(java.lang.Object l);

    /**
     * Sets (as xml) the "l" attribute
     */
    void xsetL(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage l);

    /**
     * Unsets the "l" attribute
     */
    void unsetL();

    /**
     * Gets the "t" attribute
     */
    java.lang.Object getT();

    /**
     * Gets (as xml) the "t" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetT();

    /**
     * True if has "t" attribute
     */
    boolean isSetT();

    /**
     * Sets the "t" attribute
     */
    void setT(java.lang.Object t);

    /**
     * Sets (as xml) the "t" attribute
     */
    void xsetT(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage t);

    /**
     * Unsets the "t" attribute
     */
    void unsetT();

    /**
     * Gets the "r" attribute
     */
    java.lang.Object getR();

    /**
     * Gets (as xml) the "r" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetR();

    /**
     * True if has "r" attribute
     */
    boolean isSetR();

    /**
     * Sets the "r" attribute
     */
    void setR(java.lang.Object r);

    /**
     * Sets (as xml) the "r" attribute
     */
    void xsetR(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage r);

    /**
     * Unsets the "r" attribute
     */
    void unsetR();

    /**
     * Gets the "b" attribute
     */
    java.lang.Object getB();

    /**
     * Gets (as xml) the "b" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetB();

    /**
     * True if has "b" attribute
     */
    boolean isSetB();

    /**
     * Sets the "b" attribute
     */
    void setB(java.lang.Object b);

    /**
     * Sets (as xml) the "b" attribute
     */
    void xsetB(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage b);

    /**
     * Unsets the "b" attribute
     */
    void unsetB();
}
