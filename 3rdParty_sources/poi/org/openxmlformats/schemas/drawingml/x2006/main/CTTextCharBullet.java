/*
 * XML Type:  CT_TextCharBullet
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextCharBullet(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextCharBullet extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharBullet> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextcharbullet3c20type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "char" attribute
     */
    java.lang.String getChar();

    /**
     * Gets (as xml) the "char" attribute
     */
    org.apache.xmlbeans.XmlString xgetChar();

    /**
     * Sets the "char" attribute
     */
    void setChar(java.lang.String xchar);

    /**
     * Sets (as xml) the "char" attribute
     */
    void xsetChar(org.apache.xmlbeans.XmlString xchar);
}
