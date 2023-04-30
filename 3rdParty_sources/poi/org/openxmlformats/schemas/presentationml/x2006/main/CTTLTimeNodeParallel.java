/*
 * XML Type:  CT_TLTimeNodeParallel
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLTimeNodeParallel(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLTimeNodeParallel extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttltimenodeparallelf917type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cTn" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData getCTn();

    /**
     * Sets the "cTn" element
     */
    void setCTn(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData cTn);

    /**
     * Appends and returns a new empty "cTn" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData addNewCTn();
}
