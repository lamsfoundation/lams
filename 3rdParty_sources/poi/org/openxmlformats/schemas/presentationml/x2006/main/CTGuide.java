/*
 * XML Type:  CT_Guide
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTGuide
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Guide(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGuide extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTGuide> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctguide4f93type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "orient" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum getOrient();

    /**
     * Gets (as xml) the "orient" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STDirection xgetOrient();

    /**
     * True if has "orient" attribute
     */
    boolean isSetOrient();

    /**
     * Sets the "orient" attribute
     */
    void setOrient(org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum orient);

    /**
     * Sets (as xml) the "orient" attribute
     */
    void xsetOrient(org.openxmlformats.schemas.presentationml.x2006.main.STDirection orient);

    /**
     * Unsets the "orient" attribute
     */
    void unsetOrient();

    /**
     * Gets the "pos" attribute
     */
    java.lang.Object getPos();

    /**
     * Gets (as xml) the "pos" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetPos();

    /**
     * True if has "pos" attribute
     */
    boolean isSetPos();

    /**
     * Sets the "pos" attribute
     */
    void setPos(java.lang.Object pos);

    /**
     * Sets (as xml) the "pos" attribute
     */
    void xsetPos(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 pos);

    /**
     * Unsets the "pos" attribute
     */
    void unsetPos();
}
