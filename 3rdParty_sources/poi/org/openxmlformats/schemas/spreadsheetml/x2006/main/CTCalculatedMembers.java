/*
 * XML Type:  CT_CalculatedMembers
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMembers
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CalculatedMembers(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCalculatedMembers extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMembers> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcalculatedmembersfa5atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "calculatedMember" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMember> getCalculatedMemberList();

    /**
     * Gets array of all "calculatedMember" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMember[] getCalculatedMemberArray();

    /**
     * Gets ith "calculatedMember" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMember getCalculatedMemberArray(int i);

    /**
     * Returns number of "calculatedMember" element
     */
    int sizeOfCalculatedMemberArray();

    /**
     * Sets array of all "calculatedMember" element
     */
    void setCalculatedMemberArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMember[] calculatedMemberArray);

    /**
     * Sets ith "calculatedMember" element
     */
    void setCalculatedMemberArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMember calculatedMember);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "calculatedMember" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMember insertNewCalculatedMember(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "calculatedMember" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMember addNewCalculatedMember();

    /**
     * Removes the ith "calculatedMember" element
     */
    void removeCalculatedMember(int i);

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
