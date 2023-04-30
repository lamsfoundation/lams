/*
 * XML Type:  CT_CellMergeTrackChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCellMergeTrackChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CellMergeTrackChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCellMergeTrackChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCellMergeTrackChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcellmergetrackchange3683type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "vMerge" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum getVMerge();

    /**
     * Gets (as xml) the "vMerge" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge xgetVMerge();

    /**
     * True if has "vMerge" attribute
     */
    boolean isSetVMerge();

    /**
     * Sets the "vMerge" attribute
     */
    void setVMerge(org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum vMerge);

    /**
     * Sets (as xml) the "vMerge" attribute
     */
    void xsetVMerge(org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge vMerge);

    /**
     * Unsets the "vMerge" attribute
     */
    void unsetVMerge();

    /**
     * Gets the "vMergeOrig" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum getVMergeOrig();

    /**
     * Gets (as xml) the "vMergeOrig" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge xgetVMergeOrig();

    /**
     * True if has "vMergeOrig" attribute
     */
    boolean isSetVMergeOrig();

    /**
     * Sets the "vMergeOrig" attribute
     */
    void setVMergeOrig(org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum vMergeOrig);

    /**
     * Sets (as xml) the "vMergeOrig" attribute
     */
    void xsetVMergeOrig(org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge vMergeOrig);

    /**
     * Unsets the "vMergeOrig" attribute
     */
    void unsetVMergeOrig();
}
