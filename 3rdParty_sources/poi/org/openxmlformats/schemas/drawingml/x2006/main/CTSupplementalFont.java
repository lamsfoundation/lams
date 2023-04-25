/*
 * XML Type:  CT_SupplementalFont
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SupplementalFont(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSupplementalFont extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTSupplementalFont> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsupplementalfonta06etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "script" attribute
     */
    java.lang.String getScript();

    /**
     * Gets (as xml) the "script" attribute
     */
    org.apache.xmlbeans.XmlString xgetScript();

    /**
     * Sets the "script" attribute
     */
    void setScript(java.lang.String script);

    /**
     * Sets (as xml) the "script" attribute
     */
    void xsetScript(org.apache.xmlbeans.XmlString script);

    /**
     * Gets the "typeface" attribute
     */
    java.lang.String getTypeface();

    /**
     * Gets (as xml) the "typeface" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface xgetTypeface();

    /**
     * Sets the "typeface" attribute
     */
    void setTypeface(java.lang.String typeface);

    /**
     * Sets (as xml) the "typeface" attribute
     */
    void xsetTypeface(org.openxmlformats.schemas.drawingml.x2006.main.STTextTypeface typeface);
}
