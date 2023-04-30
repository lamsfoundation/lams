/*
 * XML Type:  CT_PCDSCPage
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSCPage
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PCDSCPage(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPCDSCPage extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSCPage> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpcdscpage70aftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "pageItem" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageItem> getPageItemList();

    /**
     * Gets array of all "pageItem" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageItem[] getPageItemArray();

    /**
     * Gets ith "pageItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageItem getPageItemArray(int i);

    /**
     * Returns number of "pageItem" element
     */
    int sizeOfPageItemArray();

    /**
     * Sets array of all "pageItem" element
     */
    void setPageItemArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageItem[] pageItemArray);

    /**
     * Sets ith "pageItem" element
     */
    void setPageItemArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageItem pageItem);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pageItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageItem insertNewPageItem(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "pageItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageItem addNewPageItem();

    /**
     * Removes the ith "pageItem" element
     */
    void removePageItem(int i);

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
