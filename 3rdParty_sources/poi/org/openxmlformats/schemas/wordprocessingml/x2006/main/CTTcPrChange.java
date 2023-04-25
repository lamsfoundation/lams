/*
 * XML Type:  CT_TcPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TcPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTcPrChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttcprchangea0e7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tcPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner getTcPr();

    /**
     * Sets the "tcPr" element
     */
    void setTcPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner tcPr);

    /**
     * Appends and returns a new empty "tcPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner addNewTcPr();
}
