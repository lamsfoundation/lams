/*
 * XML Type:  CT_MemberProperties
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMemberProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MemberProperties(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMemberProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMemberProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmemberpropertiesd2f6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "mp" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMemberProperty> getMpList();

    /**
     * Gets array of all "mp" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMemberProperty[] getMpArray();

    /**
     * Gets ith "mp" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMemberProperty getMpArray(int i);

    /**
     * Returns number of "mp" element
     */
    int sizeOfMpArray();

    /**
     * Sets array of all "mp" element
     */
    void setMpArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMemberProperty[] mpArray);

    /**
     * Sets ith "mp" element
     */
    void setMpArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMemberProperty mp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "mp" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMemberProperty insertNewMp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "mp" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMemberProperty addNewMp();

    /**
     * Removes the ith "mp" element
     */
    void removeMp(int i);

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
