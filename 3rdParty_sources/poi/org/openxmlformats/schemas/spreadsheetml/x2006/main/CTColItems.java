/*
 * XML Type:  CT_colItems
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_colItems(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTColItems extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcolitemsa0c9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "i" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTI> getIList();

    /**
     * Gets array of all "i" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTI[] getIArray();

    /**
     * Gets ith "i" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTI getIArray(int i);

    /**
     * Returns number of "i" element
     */
    int sizeOfIArray();

    /**
     * Sets array of all "i" element
     */
    void setIArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTI[] iValueArray);

    /**
     * Sets ith "i" element
     */
    void setIArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTI iValue);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "i" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTI insertNewI(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "i" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTI addNewI();

    /**
     * Removes the ith "i" element
     */
    void removeI(int i);

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
