/*
 * XML Type:  CT_TblGridChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblGridChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblGridChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblgridchange3c5etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tblGrid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase getTblGrid();

    /**
     * Sets the "tblGrid" element
     */
    void setTblGrid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase tblGrid);

    /**
     * Appends and returns a new empty "tblGrid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase addNewTblGrid();
}
