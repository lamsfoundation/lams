/*
 * XML Type:  CT_TblStylePr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblStylePr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblStylePr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblstylepreb53type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral getPPr();

    /**
     * True if has "pPr" element
     */
    boolean isSetPPr();

    /**
     * Sets the "pPr" element
     */
    void setPPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral pPr);

    /**
     * Appends and returns a new empty "pPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral addNewPPr();

    /**
     * Unsets the "pPr" element
     */
    void unsetPPr();

    /**
     * Gets the "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr getRPr();

    /**
     * True if has "rPr" element
     */
    boolean isSetRPr();

    /**
     * Sets the "rPr" element
     */
    void setRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr rPr);

    /**
     * Appends and returns a new empty "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr addNewRPr();

    /**
     * Unsets the "rPr" element
     */
    void unsetRPr();

    /**
     * Gets the "tblPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase getTblPr();

    /**
     * True if has "tblPr" element
     */
    boolean isSetTblPr();

    /**
     * Sets the "tblPr" element
     */
    void setTblPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase tblPr);

    /**
     * Appends and returns a new empty "tblPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase addNewTblPr();

    /**
     * Unsets the "tblPr" element
     */
    void unsetTblPr();

    /**
     * Gets the "trPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr getTrPr();

    /**
     * True if has "trPr" element
     */
    boolean isSetTrPr();

    /**
     * Sets the "trPr" element
     */
    void setTrPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr trPr);

    /**
     * Appends and returns a new empty "trPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr addNewTrPr();

    /**
     * Unsets the "trPr" element
     */
    void unsetTrPr();

    /**
     * Gets the "tcPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr getTcPr();

    /**
     * True if has "tcPr" element
     */
    boolean isSetTcPr();

    /**
     * Sets the "tcPr" element
     */
    void setTcPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr tcPr);

    /**
     * Appends and returns a new empty "tcPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr addNewTcPr();

    /**
     * Unsets the "tcPr" element
     */
    void unsetTcPr();

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType xgetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType type);
}
