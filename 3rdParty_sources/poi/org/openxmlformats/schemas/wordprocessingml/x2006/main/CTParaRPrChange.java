/*
 * XML Type:  CT_ParaRPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ParaRPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTParaRPrChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpararprchange986etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal getRPr();

    /**
     * Sets the "rPr" element
     */
    void setRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal rPr);

    /**
     * Appends and returns a new empty "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal addNewRPr();
}
