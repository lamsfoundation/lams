/*
 * XML Type:  CT_Pages
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Pages(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPages extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpages27e3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "page" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSCPage> getPageList();

    /**
     * Gets array of all "page" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSCPage[] getPageArray();

    /**
     * Gets ith "page" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSCPage getPageArray(int i);

    /**
     * Returns number of "page" element
     */
    int sizeOfPageArray();

    /**
     * Sets array of all "page" element
     */
    void setPageArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSCPage[] pageArray);

    /**
     * Sets ith "page" element
     */
    void setPageArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSCPage page);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "page" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSCPage insertNewPage(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "page" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSCPage addNewPage();

    /**
     * Removes the ith "page" element
     */
    void removePage(int i);

    /**
     * Gets the "count" attribute
     */
    long getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCount();

    /**
     * True if has "count" attribute
     */
    boolean isSetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(long count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count);

    /**
     * Unsets the "count" attribute
     */
    void unsetCount();
}
