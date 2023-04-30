/*
 * XML Type:  CT_TableStyleInfo
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TableStyleInfo(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTableStyleInfo extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttablestyleinfo499atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName();

    /**
     * True if has "name" attribute
     */
    boolean isSetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name);

    /**
     * Unsets the "name" attribute
     */
    void unsetName();

    /**
     * Gets the "showFirstColumn" attribute
     */
    boolean getShowFirstColumn();

    /**
     * Gets (as xml) the "showFirstColumn" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowFirstColumn();

    /**
     * True if has "showFirstColumn" attribute
     */
    boolean isSetShowFirstColumn();

    /**
     * Sets the "showFirstColumn" attribute
     */
    void setShowFirstColumn(boolean showFirstColumn);

    /**
     * Sets (as xml) the "showFirstColumn" attribute
     */
    void xsetShowFirstColumn(org.apache.xmlbeans.XmlBoolean showFirstColumn);

    /**
     * Unsets the "showFirstColumn" attribute
     */
    void unsetShowFirstColumn();

    /**
     * Gets the "showLastColumn" attribute
     */
    boolean getShowLastColumn();

    /**
     * Gets (as xml) the "showLastColumn" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowLastColumn();

    /**
     * True if has "showLastColumn" attribute
     */
    boolean isSetShowLastColumn();

    /**
     * Sets the "showLastColumn" attribute
     */
    void setShowLastColumn(boolean showLastColumn);

    /**
     * Sets (as xml) the "showLastColumn" attribute
     */
    void xsetShowLastColumn(org.apache.xmlbeans.XmlBoolean showLastColumn);

    /**
     * Unsets the "showLastColumn" attribute
     */
    void unsetShowLastColumn();

    /**
     * Gets the "showRowStripes" attribute
     */
    boolean getShowRowStripes();

    /**
     * Gets (as xml) the "showRowStripes" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowRowStripes();

    /**
     * True if has "showRowStripes" attribute
     */
    boolean isSetShowRowStripes();

    /**
     * Sets the "showRowStripes" attribute
     */
    void setShowRowStripes(boolean showRowStripes);

    /**
     * Sets (as xml) the "showRowStripes" attribute
     */
    void xsetShowRowStripes(org.apache.xmlbeans.XmlBoolean showRowStripes);

    /**
     * Unsets the "showRowStripes" attribute
     */
    void unsetShowRowStripes();

    /**
     * Gets the "showColumnStripes" attribute
     */
    boolean getShowColumnStripes();

    /**
     * Gets (as xml) the "showColumnStripes" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowColumnStripes();

    /**
     * True if has "showColumnStripes" attribute
     */
    boolean isSetShowColumnStripes();

    /**
     * Sets the "showColumnStripes" attribute
     */
    void setShowColumnStripes(boolean showColumnStripes);

    /**
     * Sets (as xml) the "showColumnStripes" attribute
     */
    void xsetShowColumnStripes(org.apache.xmlbeans.XmlBoolean showColumnStripes);

    /**
     * Unsets the "showColumnStripes" attribute
     */
    void unsetShowColumnStripes();
}
