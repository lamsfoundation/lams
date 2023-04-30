/*
 * XML Type:  CT_OutlinePr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OutlinePr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOutlinePr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoutlineprc483type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "applyStyles" attribute
     */
    boolean getApplyStyles();

    /**
     * Gets (as xml) the "applyStyles" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetApplyStyles();

    /**
     * True if has "applyStyles" attribute
     */
    boolean isSetApplyStyles();

    /**
     * Sets the "applyStyles" attribute
     */
    void setApplyStyles(boolean applyStyles);

    /**
     * Sets (as xml) the "applyStyles" attribute
     */
    void xsetApplyStyles(org.apache.xmlbeans.XmlBoolean applyStyles);

    /**
     * Unsets the "applyStyles" attribute
     */
    void unsetApplyStyles();

    /**
     * Gets the "summaryBelow" attribute
     */
    boolean getSummaryBelow();

    /**
     * Gets (as xml) the "summaryBelow" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetSummaryBelow();

    /**
     * True if has "summaryBelow" attribute
     */
    boolean isSetSummaryBelow();

    /**
     * Sets the "summaryBelow" attribute
     */
    void setSummaryBelow(boolean summaryBelow);

    /**
     * Sets (as xml) the "summaryBelow" attribute
     */
    void xsetSummaryBelow(org.apache.xmlbeans.XmlBoolean summaryBelow);

    /**
     * Unsets the "summaryBelow" attribute
     */
    void unsetSummaryBelow();

    /**
     * Gets the "summaryRight" attribute
     */
    boolean getSummaryRight();

    /**
     * Gets (as xml) the "summaryRight" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetSummaryRight();

    /**
     * True if has "summaryRight" attribute
     */
    boolean isSetSummaryRight();

    /**
     * Sets the "summaryRight" attribute
     */
    void setSummaryRight(boolean summaryRight);

    /**
     * Sets (as xml) the "summaryRight" attribute
     */
    void xsetSummaryRight(org.apache.xmlbeans.XmlBoolean summaryRight);

    /**
     * Unsets the "summaryRight" attribute
     */
    void unsetSummaryRight();

    /**
     * Gets the "showOutlineSymbols" attribute
     */
    boolean getShowOutlineSymbols();

    /**
     * Gets (as xml) the "showOutlineSymbols" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowOutlineSymbols();

    /**
     * True if has "showOutlineSymbols" attribute
     */
    boolean isSetShowOutlineSymbols();

    /**
     * Sets the "showOutlineSymbols" attribute
     */
    void setShowOutlineSymbols(boolean showOutlineSymbols);

    /**
     * Sets (as xml) the "showOutlineSymbols" attribute
     */
    void xsetShowOutlineSymbols(org.apache.xmlbeans.XmlBoolean showOutlineSymbols);

    /**
     * Unsets the "showOutlineSymbols" attribute
     */
    void unsetShowOutlineSymbols();
}
