/*
 * XML Type:  CT_SingleXmlCell
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SingleXmlCell(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSingleXmlCell extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCell> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsinglexmlcell7790type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "xmlCellPr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlCellPr getXmlCellPr();

    /**
     * Sets the "xmlCellPr" element
     */
    void setXmlCellPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlCellPr xmlCellPr);

    /**
     * Appends and returns a new empty "xmlCellPr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlCellPr addNewXmlCellPr();

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
     * Gets the "id" attribute
     */
    long getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(long id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlUnsignedInt id);

    /**
     * Gets the "r" attribute
     */
    java.lang.String getR();

    /**
     * Gets (as xml) the "r" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef xgetR();

    /**
     * Sets the "r" attribute
     */
    void setR(java.lang.String r);

    /**
     * Sets (as xml) the "r" attribute
     */
    void xsetR(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef r);

    /**
     * Gets the "connectionId" attribute
     */
    long getConnectionId();

    /**
     * Gets (as xml) the "connectionId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetConnectionId();

    /**
     * Sets the "connectionId" attribute
     */
    void setConnectionId(long connectionId);

    /**
     * Sets (as xml) the "connectionId" attribute
     */
    void xsetConnectionId(org.apache.xmlbeans.XmlUnsignedInt connectionId);
}
