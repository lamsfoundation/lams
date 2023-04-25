/*
 * XML Type:  CT_NonVisualPictureProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NonVisualPictureProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNonVisualPictureProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnonvisualpicturepropertiesee3ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "picLocks" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking getPicLocks();

    /**
     * True if has "picLocks" element
     */
    boolean isSetPicLocks();

    /**
     * Sets the "picLocks" element
     */
    void setPicLocks(org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking picLocks);

    /**
     * Appends and returns a new empty "picLocks" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking addNewPicLocks();

    /**
     * Unsets the "picLocks" element
     */
    void unsetPicLocks();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "preferRelativeResize" attribute
     */
    boolean getPreferRelativeResize();

    /**
     * Gets (as xml) the "preferRelativeResize" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetPreferRelativeResize();

    /**
     * True if has "preferRelativeResize" attribute
     */
    boolean isSetPreferRelativeResize();

    /**
     * Sets the "preferRelativeResize" attribute
     */
    void setPreferRelativeResize(boolean preferRelativeResize);

    /**
     * Sets (as xml) the "preferRelativeResize" attribute
     */
    void xsetPreferRelativeResize(org.apache.xmlbeans.XmlBoolean preferRelativeResize);

    /**
     * Unsets the "preferRelativeResize" attribute
     */
    void unsetPreferRelativeResize();
}
