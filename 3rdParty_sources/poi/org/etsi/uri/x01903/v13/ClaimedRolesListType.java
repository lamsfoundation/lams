/*
 * XML Type:  ClaimedRolesListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ClaimedRolesListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ClaimedRolesListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface ClaimedRolesListType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.ClaimedRolesListType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "claimedroleslisttypef16etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "ClaimedRole" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.AnyType> getClaimedRoleList();

    /**
     * Gets array of all "ClaimedRole" elements
     */
    org.etsi.uri.x01903.v13.AnyType[] getClaimedRoleArray();

    /**
     * Gets ith "ClaimedRole" element
     */
    org.etsi.uri.x01903.v13.AnyType getClaimedRoleArray(int i);

    /**
     * Returns number of "ClaimedRole" element
     */
    int sizeOfClaimedRoleArray();

    /**
     * Sets array of all "ClaimedRole" element
     */
    void setClaimedRoleArray(org.etsi.uri.x01903.v13.AnyType[] claimedRoleArray);

    /**
     * Sets ith "ClaimedRole" element
     */
    void setClaimedRoleArray(int i, org.etsi.uri.x01903.v13.AnyType claimedRole);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ClaimedRole" element
     */
    org.etsi.uri.x01903.v13.AnyType insertNewClaimedRole(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ClaimedRole" element
     */
    org.etsi.uri.x01903.v13.AnyType addNewClaimedRole();

    /**
     * Removes the ith "ClaimedRole" element
     */
    void removeClaimedRole(int i);
}
