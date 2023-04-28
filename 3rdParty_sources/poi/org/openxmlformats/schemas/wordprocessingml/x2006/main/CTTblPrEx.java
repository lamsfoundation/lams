/*
 * XML Type:  CT_TblPrEx
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrEx
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblPrEx(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblPrEx extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrEx> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblprex863ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tblPrExChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange getTblPrExChange();

    /**
     * True if has "tblPrExChange" element
     */
    boolean isSetTblPrExChange();

    /**
     * Sets the "tblPrExChange" element
     */
    void setTblPrExChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange tblPrExChange);

    /**
     * Appends and returns a new empty "tblPrExChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange addNewTblPrExChange();

    /**
     * Unsets the "tblPrExChange" element
     */
    void unsetTblPrExChange();
}
