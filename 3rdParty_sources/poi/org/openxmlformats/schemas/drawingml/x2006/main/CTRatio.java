/*
 * XML Type:  CT_Ratio
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTRatio
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Ratio(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRatio extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTRatio> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctratioc3bctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "n" attribute
     */
    long getN();

    /**
     * Gets (as xml) the "n" attribute
     */
    org.apache.xmlbeans.XmlLong xgetN();

    /**
     * Sets the "n" attribute
     */
    void setN(long n);

    /**
     * Sets (as xml) the "n" attribute
     */
    void xsetN(org.apache.xmlbeans.XmlLong n);

    /**
     * Gets the "d" attribute
     */
    long getD();

    /**
     * Gets (as xml) the "d" attribute
     */
    org.apache.xmlbeans.XmlLong xgetD();

    /**
     * Sets the "d" attribute
     */
    void setD(long d);

    /**
     * Sets (as xml) the "d" attribute
     */
    void xsetD(org.apache.xmlbeans.XmlLong d);
}
