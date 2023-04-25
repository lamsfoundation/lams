/*
 * XML Type:  CT_TblGrid
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblGrid(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblGrid extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblgrid2eeetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tblGridChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange getTblGridChange();

    /**
     * True if has "tblGridChange" element
     */
    boolean isSetTblGridChange();

    /**
     * Sets the "tblGridChange" element
     */
    void setTblGridChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange tblGridChange);

    /**
     * Appends and returns a new empty "tblGridChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange addNewTblGridChange();

    /**
     * Unsets the "tblGridChange" element
     */
    void unsetTblGridChange();
}
