/*
 * XML Type:  CT_XmlPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_XmlPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTXmlPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctxmlpr2c58type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
     * Gets the "mapId" attribute
     */
    long getMapId();

    /**
     * Gets (as xml) the "mapId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetMapId();

    /**
     * Sets the "mapId" attribute
     */
    void setMapId(long mapId);

    /**
     * Sets (as xml) the "mapId" attribute
     */
    void xsetMapId(org.apache.xmlbeans.XmlUnsignedInt mapId);

    /**
     * Gets the "xpath" attribute
     */
    java.lang.String getXpath();

    /**
     * Gets (as xml) the "xpath" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetXpath();

    /**
     * Sets the "xpath" attribute
     */
    void setXpath(java.lang.String xpath);

    /**
     * Sets (as xml) the "xpath" attribute
     */
    void xsetXpath(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xpath);

    /**
     * Gets the "xmlDataType" attribute
     */
    java.lang.String getXmlDataType();

    /**
     * Gets (as xml) the "xmlDataType" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STXmlDataType xgetXmlDataType();

    /**
     * Sets the "xmlDataType" attribute
     */
    void setXmlDataType(java.lang.String xmlDataType);

    /**
     * Sets (as xml) the "xmlDataType" attribute
     */
    void xsetXmlDataType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STXmlDataType xmlDataType);
}
