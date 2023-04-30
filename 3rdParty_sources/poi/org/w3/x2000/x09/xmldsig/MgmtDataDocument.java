/*
 * An XML document type.
 * Localname: MgmtData
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.MgmtDataDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one MgmtData(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface MgmtDataDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.MgmtDataDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "mgmtdatabce4doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "MgmtData" element
     */
    java.lang.String getMgmtData();

    /**
     * Gets (as xml) the "MgmtData" element
     */
    org.apache.xmlbeans.XmlString xgetMgmtData();

    /**
     * Sets the "MgmtData" element
     */
    void setMgmtData(java.lang.String mgmtData);

    /**
     * Sets (as xml) the "MgmtData" element
     */
    void xsetMgmtData(org.apache.xmlbeans.XmlString mgmtData);
}
