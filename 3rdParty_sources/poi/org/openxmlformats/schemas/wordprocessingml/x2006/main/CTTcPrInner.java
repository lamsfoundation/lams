/*
 * XML Type:  CT_TcPrInner
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TcPrInner(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTcPrInner extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrBase {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttcprinnerc56dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cellIns" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCellIns();

    /**
     * True if has "cellIns" element
     */
    boolean isSetCellIns();

    /**
     * Sets the "cellIns" element
     */
    void setCellIns(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange cellIns);

    /**
     * Appends and returns a new empty "cellIns" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewCellIns();

    /**
     * Unsets the "cellIns" element
     */
    void unsetCellIns();

    /**
     * Gets the "cellDel" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getCellDel();

    /**
     * True if has "cellDel" element
     */
    boolean isSetCellDel();

    /**
     * Sets the "cellDel" element
     */
    void setCellDel(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange cellDel);

    /**
     * Appends and returns a new empty "cellDel" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewCellDel();

    /**
     * Unsets the "cellDel" element
     */
    void unsetCellDel();

    /**
     * Gets the "cellMerge" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCellMergeTrackChange getCellMerge();

    /**
     * True if has "cellMerge" element
     */
    boolean isSetCellMerge();

    /**
     * Sets the "cellMerge" element
     */
    void setCellMerge(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCellMergeTrackChange cellMerge);

    /**
     * Appends and returns a new empty "cellMerge" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCellMergeTrackChange addNewCellMerge();

    /**
     * Unsets the "cellMerge" element
     */
    void unsetCellMerge();
}
