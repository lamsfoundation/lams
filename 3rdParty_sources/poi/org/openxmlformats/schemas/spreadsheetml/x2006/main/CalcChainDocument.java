/*
 * An XML document type.
 * Localname: calcChain
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one calcChain(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface CalcChainDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "calcchainfc37doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "calcChain" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain getCalcChain();

    /**
     * Sets the "calcChain" element
     */
    void setCalcChain(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain calcChain);

    /**
     * Appends and returns a new empty "calcChain" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain addNewCalcChain();
}
