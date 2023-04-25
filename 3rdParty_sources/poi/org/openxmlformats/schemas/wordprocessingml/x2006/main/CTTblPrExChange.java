/*
 * XML Type:  CT_TblPrExChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblPrExChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblPrExChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblprexchange12eftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tblPrEx" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase getTblPrEx();

    /**
     * Sets the "tblPrEx" element
     */
    void setTblPrEx(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase tblPrEx);

    /**
     * Appends and returns a new empty "tblPrEx" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase addNewTblPrEx();
}
