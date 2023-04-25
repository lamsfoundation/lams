/*
 * XML Type:  CT_CalculatedItem
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItem
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CalculatedItem(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCalculatedItem extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItem> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcalculateditem29batype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pivotArea" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotArea getPivotArea();

    /**
     * Sets the "pivotArea" element
     */
    void setPivotArea(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotArea pivotArea);

    /**
     * Appends and returns a new empty "pivotArea" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotArea addNewPivotArea();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "field" attribute
     */
    long getField();

    /**
     * Gets (as xml) the "field" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetField();

    /**
     * True if has "field" attribute
     */
    boolean isSetField();

    /**
     * Sets the "field" attribute
     */
    void setField(long field);

    /**
     * Sets (as xml) the "field" attribute
     */
    void xsetField(org.apache.xmlbeans.XmlUnsignedInt field);

    /**
     * Unsets the "field" attribute
     */
    void unsetField();

    /**
     * Gets the "formula" attribute
     */
    java.lang.String getFormula();

    /**
     * Gets (as xml) the "formula" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetFormula();

    /**
     * True if has "formula" attribute
     */
    boolean isSetFormula();

    /**
     * Sets the "formula" attribute
     */
    void setFormula(java.lang.String formula);

    /**
     * Sets (as xml) the "formula" attribute
     */
    void xsetFormula(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring formula);

    /**
     * Unsets the "formula" attribute
     */
    void unsetFormula();
}
