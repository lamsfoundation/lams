/*
 * XML Type:  CT_HeaderFooter
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_HeaderFooter(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHeaderFooter extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctheaderfooterb29dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "sldNum" attribute
     */
    boolean getSldNum();

    /**
     * Gets (as xml) the "sldNum" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetSldNum();

    /**
     * True if has "sldNum" attribute
     */
    boolean isSetSldNum();

    /**
     * Sets the "sldNum" attribute
     */
    void setSldNum(boolean sldNum);

    /**
     * Sets (as xml) the "sldNum" attribute
     */
    void xsetSldNum(org.apache.xmlbeans.XmlBoolean sldNum);

    /**
     * Unsets the "sldNum" attribute
     */
    void unsetSldNum();

    /**
     * Gets the "hdr" attribute
     */
    boolean getHdr();

    /**
     * Gets (as xml) the "hdr" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetHdr();

    /**
     * True if has "hdr" attribute
     */
    boolean isSetHdr();

    /**
     * Sets the "hdr" attribute
     */
    void setHdr(boolean hdr);

    /**
     * Sets (as xml) the "hdr" attribute
     */
    void xsetHdr(org.apache.xmlbeans.XmlBoolean hdr);

    /**
     * Unsets the "hdr" attribute
     */
    void unsetHdr();

    /**
     * Gets the "ftr" attribute
     */
    boolean getFtr();

    /**
     * Gets (as xml) the "ftr" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFtr();

    /**
     * True if has "ftr" attribute
     */
    boolean isSetFtr();

    /**
     * Sets the "ftr" attribute
     */
    void setFtr(boolean ftr);

    /**
     * Sets (as xml) the "ftr" attribute
     */
    void xsetFtr(org.apache.xmlbeans.XmlBoolean ftr);

    /**
     * Unsets the "ftr" attribute
     */
    void unsetFtr();

    /**
     * Gets the "dt" attribute
     */
    boolean getDt();

    /**
     * Gets (as xml) the "dt" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDt();

    /**
     * True if has "dt" attribute
     */
    boolean isSetDt();

    /**
     * Sets the "dt" attribute
     */
    void setDt(boolean dt);

    /**
     * Sets (as xml) the "dt" attribute
     */
    void xsetDt(org.apache.xmlbeans.XmlBoolean dt);

    /**
     * Unsets the "dt" attribute
     */
    void unsetDt();
}
