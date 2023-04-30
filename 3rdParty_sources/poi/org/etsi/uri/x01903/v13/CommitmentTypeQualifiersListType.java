/*
 * XML Type:  CommitmentTypeQualifiersListType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CommitmentTypeQualifiersListType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CommitmentTypeQualifiersListType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "commitmenttypequalifierslisttype2d24type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "CommitmentTypeQualifier" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.AnyType> getCommitmentTypeQualifierList();

    /**
     * Gets array of all "CommitmentTypeQualifier" elements
     */
    org.etsi.uri.x01903.v13.AnyType[] getCommitmentTypeQualifierArray();

    /**
     * Gets ith "CommitmentTypeQualifier" element
     */
    org.etsi.uri.x01903.v13.AnyType getCommitmentTypeQualifierArray(int i);

    /**
     * Returns number of "CommitmentTypeQualifier" element
     */
    int sizeOfCommitmentTypeQualifierArray();

    /**
     * Sets array of all "CommitmentTypeQualifier" element
     */
    void setCommitmentTypeQualifierArray(org.etsi.uri.x01903.v13.AnyType[] commitmentTypeQualifierArray);

    /**
     * Sets ith "CommitmentTypeQualifier" element
     */
    void setCommitmentTypeQualifierArray(int i, org.etsi.uri.x01903.v13.AnyType commitmentTypeQualifier);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CommitmentTypeQualifier" element
     */
    org.etsi.uri.x01903.v13.AnyType insertNewCommitmentTypeQualifier(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "CommitmentTypeQualifier" element
     */
    org.etsi.uri.x01903.v13.AnyType addNewCommitmentTypeQualifier();

    /**
     * Removes the ith "CommitmentTypeQualifier" element
     */
    void removeCommitmentTypeQualifier(int i);
}
