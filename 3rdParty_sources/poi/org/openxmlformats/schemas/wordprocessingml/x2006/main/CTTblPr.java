/*
 * XML Type:  CT_TblPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblPr extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblpr5b72type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tblPrChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange getTblPrChange();

    /**
     * True if has "tblPrChange" element
     */
    boolean isSetTblPrChange();

    /**
     * Sets the "tblPrChange" element
     */
    void setTblPrChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange tblPrChange);

    /**
     * Appends and returns a new empty "tblPrChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange addNewTblPrChange();

    /**
     * Unsets the "tblPrChange" element
     */
    void unsetTblPrChange();
}
