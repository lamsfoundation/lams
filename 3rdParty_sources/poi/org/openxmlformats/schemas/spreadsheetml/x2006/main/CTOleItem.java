/*
 * XML Type:  CT_OleItem
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OleItem(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOleItem extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItem> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoleiteme3fatype");
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
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name);

    /**
     * Gets the "icon" attribute
     */
    boolean getIcon();

    /**
     * Gets (as xml) the "icon" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetIcon();

    /**
     * True if has "icon" attribute
     */
    boolean isSetIcon();

    /**
     * Sets the "icon" attribute
     */
    void setIcon(boolean icon);

    /**
     * Sets (as xml) the "icon" attribute
     */
    void xsetIcon(org.apache.xmlbeans.XmlBoolean icon);

    /**
     * Unsets the "icon" attribute
     */
    void unsetIcon();

    /**
     * Gets the "advise" attribute
     */
    boolean getAdvise();

    /**
     * Gets (as xml) the "advise" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAdvise();

    /**
     * True if has "advise" attribute
     */
    boolean isSetAdvise();

    /**
     * Sets the "advise" attribute
     */
    void setAdvise(boolean advise);

    /**
     * Sets (as xml) the "advise" attribute
     */
    void xsetAdvise(org.apache.xmlbeans.XmlBoolean advise);

    /**
     * Unsets the "advise" attribute
     */
    void unsetAdvise();

    /**
     * Gets the "preferPic" attribute
     */
    boolean getPreferPic();

    /**
     * Gets (as xml) the "preferPic" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetPreferPic();

    /**
     * True if has "preferPic" attribute
     */
    boolean isSetPreferPic();

    /**
     * Sets the "preferPic" attribute
     */
    void setPreferPic(boolean preferPic);

    /**
     * Sets (as xml) the "preferPic" attribute
     */
    void xsetPreferPic(org.apache.xmlbeans.XmlBoolean preferPic);

    /**
     * Unsets the "preferPic" attribute
     */
    void unsetPreferPic();
}
