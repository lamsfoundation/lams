/*
 * XML Type:  SigPolicyQualifiersListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SigPolicyQualifiersListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SigPolicyQualifiersListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface SigPolicyQualifiersListType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SigPolicyQualifiersListType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sigpolicyqualifierslisttype3266type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "SigPolicyQualifier" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.AnyType> getSigPolicyQualifierList();

    /**
     * Gets array of all "SigPolicyQualifier" elements
     */
    org.etsi.uri.x01903.v13.AnyType[] getSigPolicyQualifierArray();

    /**
     * Gets ith "SigPolicyQualifier" element
     */
    org.etsi.uri.x01903.v13.AnyType getSigPolicyQualifierArray(int i);

    /**
     * Returns number of "SigPolicyQualifier" element
     */
    int sizeOfSigPolicyQualifierArray();

    /**
     * Sets array of all "SigPolicyQualifier" element
     */
    void setSigPolicyQualifierArray(org.etsi.uri.x01903.v13.AnyType[] sigPolicyQualifierArray);

    /**
     * Sets ith "SigPolicyQualifier" element
     */
    void setSigPolicyQualifierArray(int i, org.etsi.uri.x01903.v13.AnyType sigPolicyQualifier);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SigPolicyQualifier" element
     */
    org.etsi.uri.x01903.v13.AnyType insertNewSigPolicyQualifier(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "SigPolicyQualifier" element
     */
    org.etsi.uri.x01903.v13.AnyType addNewSigPolicyQualifier();

    /**
     * Removes the ith "SigPolicyQualifier" element
     */
    void removeSigPolicyQualifier(int i);
}
