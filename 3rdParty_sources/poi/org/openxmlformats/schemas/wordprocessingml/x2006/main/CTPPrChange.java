/*
 * XML Type:  CT_PPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPPrChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpprchange29b0type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase getPPr();

    /**
     * Sets the "pPr" element
     */
    void setPPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase pPr);

    /**
     * Appends and returns a new empty "pPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase addNewPPr();
}
