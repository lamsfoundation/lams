/*
 * XML Type:  CT_DPr
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTDPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DPr(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public interface CTDPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.math.CTDPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdpr2596type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "begChr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTChar getBegChr();

    /**
     * True if has "begChr" element
     */
    boolean isSetBegChr();

    /**
     * Sets the "begChr" element
     */
    void setBegChr(org.openxmlformats.schemas.officeDocument.x2006.math.CTChar begChr);

    /**
     * Appends and returns a new empty "begChr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTChar addNewBegChr();

    /**
     * Unsets the "begChr" element
     */
    void unsetBegChr();

    /**
     * Gets the "sepChr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTChar getSepChr();

    /**
     * True if has "sepChr" element
     */
    boolean isSetSepChr();

    /**
     * Sets the "sepChr" element
     */
    void setSepChr(org.openxmlformats.schemas.officeDocument.x2006.math.CTChar sepChr);

    /**
     * Appends and returns a new empty "sepChr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTChar addNewSepChr();

    /**
     * Unsets the "sepChr" element
     */
    void unsetSepChr();

    /**
     * Gets the "endChr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTChar getEndChr();

    /**
     * True if has "endChr" element
     */
    boolean isSetEndChr();

    /**
     * Sets the "endChr" element
     */
    void setEndChr(org.openxmlformats.schemas.officeDocument.x2006.math.CTChar endChr);

    /**
     * Appends and returns a new empty "endChr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTChar addNewEndChr();

    /**
     * Unsets the "endChr" element
     */
    void unsetEndChr();

    /**
     * Gets the "grow" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff getGrow();

    /**
     * True if has "grow" element
     */
    boolean isSetGrow();

    /**
     * Sets the "grow" element
     */
    void setGrow(org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff grow);

    /**
     * Appends and returns a new empty "grow" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff addNewGrow();

    /**
     * Unsets the "grow" element
     */
    void unsetGrow();

    /**
     * Gets the "shp" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTShp getShp();

    /**
     * True if has "shp" element
     */
    boolean isSetShp();

    /**
     * Sets the "shp" element
     */
    void setShp(org.openxmlformats.schemas.officeDocument.x2006.math.CTShp shp);

    /**
     * Appends and returns a new empty "shp" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTShp addNewShp();

    /**
     * Unsets the "shp" element
     */
    void unsetShp();

    /**
     * Gets the "ctrlPr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr getCtrlPr();

    /**
     * True if has "ctrlPr" element
     */
    boolean isSetCtrlPr();

    /**
     * Sets the "ctrlPr" element
     */
    void setCtrlPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr ctrlPr);

    /**
     * Appends and returns a new empty "ctrlPr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr addNewCtrlPr();

    /**
     * Unsets the "ctrlPr" element
     */
    void unsetCtrlPr();
}
