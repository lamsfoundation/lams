/*
 * XML Type:  CT_PageMargins
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PageMargins(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTPageMargins extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpagemarginsb730type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "l" attribute
     */
    double getL();

    /**
     * Gets (as xml) the "l" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetL();

    /**
     * Sets the "l" attribute
     */
    void setL(double l);

    /**
     * Sets (as xml) the "l" attribute
     */
    void xsetL(org.apache.xmlbeans.XmlDouble l);

    /**
     * Gets the "r" attribute
     */
    double getR();

    /**
     * Gets (as xml) the "r" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetR();

    /**
     * Sets the "r" attribute
     */
    void setR(double r);

    /**
     * Sets (as xml) the "r" attribute
     */
    void xsetR(org.apache.xmlbeans.XmlDouble r);

    /**
     * Gets the "t" attribute
     */
    double getT();

    /**
     * Gets (as xml) the "t" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetT();

    /**
     * Sets the "t" attribute
     */
    void setT(double t);

    /**
     * Sets (as xml) the "t" attribute
     */
    void xsetT(org.apache.xmlbeans.XmlDouble t);

    /**
     * Gets the "b" attribute
     */
    double getB();

    /**
     * Gets (as xml) the "b" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetB();

    /**
     * Sets the "b" attribute
     */
    void setB(double b);

    /**
     * Sets (as xml) the "b" attribute
     */
    void xsetB(org.apache.xmlbeans.XmlDouble b);

    /**
     * Gets the "header" attribute
     */
    double getHeader();

    /**
     * Gets (as xml) the "header" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetHeader();

    /**
     * Sets the "header" attribute
     */
    void setHeader(double header);

    /**
     * Sets (as xml) the "header" attribute
     */
    void xsetHeader(org.apache.xmlbeans.XmlDouble header);

    /**
     * Gets the "footer" attribute
     */
    double getFooter();

    /**
     * Gets (as xml) the "footer" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetFooter();

    /**
     * Sets the "footer" attribute
     */
    void setFooter(double footer);

    /**
     * Sets (as xml) the "footer" attribute
     */
    void xsetFooter(org.apache.xmlbeans.XmlDouble footer);
}
