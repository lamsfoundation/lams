/*
 * XML Type:  CT_XmlColumnPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlColumnPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_XmlColumnPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTXmlColumnPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXmlColumnPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctxmlcolumnprc14etype");
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
     * Gets the "denormalized" attribute
     */
    boolean getDenormalized();

    /**
     * Gets (as xml) the "denormalized" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDenormalized();

    /**
     * True if has "denormalized" attribute
     */
    boolean isSetDenormalized();

    /**
     * Sets the "denormalized" attribute
     */
    void setDenormalized(boolean denormalized);

    /**
     * Sets (as xml) the "denormalized" attribute
     */
    void xsetDenormalized(org.apache.xmlbeans.XmlBoolean denormalized);

    /**
     * Unsets the "denormalized" attribute
     */
    void unsetDenormalized();

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
