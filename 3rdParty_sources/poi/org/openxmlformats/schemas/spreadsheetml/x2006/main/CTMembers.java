/*
 * XML Type:  CT_Members
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMembers
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Members(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMembers extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMembers> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmembers9a38type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "member" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMember> getMemberList();

    /**
     * Gets array of all "member" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMember[] getMemberArray();

    /**
     * Gets ith "member" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMember getMemberArray(int i);

    /**
     * Returns number of "member" element
     */
    int sizeOfMemberArray();

    /**
     * Sets array of all "member" element
     */
    void setMemberArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMember[] memberArray);

    /**
     * Sets ith "member" element
     */
    void setMemberArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMember member);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "member" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMember insertNewMember(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "member" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMember addNewMember();

    /**
     * Removes the ith "member" element
     */
    void removeMember(int i);

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

    /**
     * Gets the "level" attribute
     */
    long getLevel();

    /**
     * Gets (as xml) the "level" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetLevel();

    /**
     * True if has "level" attribute
     */
    boolean isSetLevel();

    /**
     * Sets the "level" attribute
     */
    void setLevel(long level);

    /**
     * Sets (as xml) the "level" attribute
     */
    void xsetLevel(org.apache.xmlbeans.XmlUnsignedInt level);

    /**
     * Unsets the "level" attribute
     */
    void unsetLevel();
}
