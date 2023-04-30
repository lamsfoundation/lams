/*
 * XML Type:  CT_TLSubShapeId
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLSubShapeId(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLSubShapeId extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlsubshapeidaaf1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "spid" attribute
     */
    java.lang.String getSpid();

    /**
     * Gets (as xml) the "spid" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STShapeID xgetSpid();

    /**
     * Sets the "spid" attribute
     */
    void setSpid(java.lang.String spid);

    /**
     * Sets (as xml) the "spid" attribute
     */
    void xsetSpid(org.openxmlformats.schemas.drawingml.x2006.main.STShapeID spid);
}
