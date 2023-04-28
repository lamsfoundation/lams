/*
 * XML Type:  CT_TrPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TrPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTrPrChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttrprchange4a38type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "trPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrBase getTrPr();

    /**
     * Sets the "trPr" element
     */
    void setTrPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrBase trPr);

    /**
     * Appends and returns a new empty "trPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrBase addNewTrPr();
}
