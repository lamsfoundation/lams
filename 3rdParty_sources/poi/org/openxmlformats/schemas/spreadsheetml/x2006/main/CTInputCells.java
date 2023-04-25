/*
 * XML Type:  CT_InputCells
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTInputCells
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_InputCells(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTInputCells extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTInputCells> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctinputcells3470type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
     * Gets the "deleted" attribute
     */
    boolean getDeleted();

    /**
     * Gets (as xml) the "deleted" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDeleted();

    /**
     * True if has "deleted" attribute
     */
    boolean isSetDeleted();

    /**
     * Sets the "deleted" attribute
     */
    void setDeleted(boolean deleted);

    /**
     * Sets (as xml) the "deleted" attribute
     */
    void xsetDeleted(org.apache.xmlbeans.XmlBoolean deleted);

    /**
     * Unsets the "deleted" attribute
     */
    void unsetDeleted();

    /**
     * Gets the "undone" attribute
     */
    boolean getUndone();

    /**
     * Gets (as xml) the "undone" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUndone();

    /**
     * True if has "undone" attribute
     */
    boolean isSetUndone();

    /**
     * Sets the "undone" attribute
     */
    void setUndone(boolean undone);

    /**
     * Sets (as xml) the "undone" attribute
     */
    void xsetUndone(org.apache.xmlbeans.XmlBoolean undone);

    /**
     * Unsets the "undone" attribute
     */
    void unsetUndone();

    /**
     * Gets the "val" attribute
     */
    java.lang.String getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(java.lang.String val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring val);

    /**
     * Gets the "numFmtId" attribute
     */
    long getNumFmtId();

    /**
     * Gets (as xml) the "numFmtId" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId xgetNumFmtId();

    /**
     * True if has "numFmtId" attribute
     */
    boolean isSetNumFmtId();

    /**
     * Sets the "numFmtId" attribute
     */
    void setNumFmtId(long numFmtId);

    /**
     * Sets (as xml) the "numFmtId" attribute
     */
    void xsetNumFmtId(org.openxmlformats.schemas.spreadsheetml.x2006.main.STNumFmtId numFmtId);

    /**
     * Unsets the "numFmtId" attribute
     */
    void unsetNumFmtId();
}
