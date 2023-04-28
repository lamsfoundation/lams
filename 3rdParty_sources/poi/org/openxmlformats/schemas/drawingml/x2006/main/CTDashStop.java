/*
 * XML Type:  CT_DashStop
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DashStop(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDashStop extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdashstopdc4ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "d" attribute
     */
    java.lang.Object getD();

    /**
     * Gets (as xml) the "d" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage xgetD();

    /**
     * Sets the "d" attribute
     */
    void setD(java.lang.Object d);

    /**
     * Sets (as xml) the "d" attribute
     */
    void xsetD(org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage d);

    /**
     * Gets the "sp" attribute
     */
    java.lang.Object getSp();

    /**
     * Gets (as xml) the "sp" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage xgetSp();

    /**
     * Sets the "sp" attribute
     */
    void setSp(java.lang.Object sp);

    /**
     * Sets (as xml) the "sp" attribute
     */
    void xsetSp(org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage sp);
}
