/*
 * XML Type:  OtherCertStatusRefsType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OtherCertStatusRefsType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML OtherCertStatusRefsType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface OtherCertStatusRefsType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.OtherCertStatusRefsType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "othercertstatusrefstype6922type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "OtherRef" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.AnyType> getOtherRefList();

    /**
     * Gets array of all "OtherRef" elements
     */
    org.etsi.uri.x01903.v13.AnyType[] getOtherRefArray();

    /**
     * Gets ith "OtherRef" element
     */
    org.etsi.uri.x01903.v13.AnyType getOtherRefArray(int i);

    /**
     * Returns number of "OtherRef" element
     */
    int sizeOfOtherRefArray();

    /**
     * Sets array of all "OtherRef" element
     */
    void setOtherRefArray(org.etsi.uri.x01903.v13.AnyType[] otherRefArray);

    /**
     * Sets ith "OtherRef" element
     */
    void setOtherRefArray(int i, org.etsi.uri.x01903.v13.AnyType otherRef);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "OtherRef" element
     */
    org.etsi.uri.x01903.v13.AnyType insertNewOtherRef(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "OtherRef" element
     */
    org.etsi.uri.x01903.v13.AnyType addNewOtherRef();

    /**
     * Removes the ith "OtherRef" element
     */
    void removeOtherRef(int i);
}
