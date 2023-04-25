/*
 * XML Type:  CT_WebPublishItems
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WebPublishItems(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTWebPublishItems extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwebpublishitemsdc84type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "webPublishItem" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItem> getWebPublishItemList();

    /**
     * Gets array of all "webPublishItem" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItem[] getWebPublishItemArray();

    /**
     * Gets ith "webPublishItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItem getWebPublishItemArray(int i);

    /**
     * Returns number of "webPublishItem" element
     */
    int sizeOfWebPublishItemArray();

    /**
     * Sets array of all "webPublishItem" element
     */
    void setWebPublishItemArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItem[] webPublishItemArray);

    /**
     * Sets ith "webPublishItem" element
     */
    void setWebPublishItemArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItem webPublishItem);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "webPublishItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItem insertNewWebPublishItem(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "webPublishItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItem addNewWebPublishItem();

    /**
     * Removes the ith "webPublishItem" element
     */
    void removeWebPublishItem(int i);

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
