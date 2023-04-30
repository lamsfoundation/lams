/*
 * XML Type:  CT_TblPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblPrChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblprchangef5e2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tblPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase getTblPr();

    /**
     * Sets the "tblPr" element
     */
    void setTblPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase tblPr);

    /**
     * Appends and returns a new empty "tblPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase addNewTblPr();
}
