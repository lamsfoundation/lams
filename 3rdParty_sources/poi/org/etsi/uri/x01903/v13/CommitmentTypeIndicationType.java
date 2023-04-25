/*
 * XML Type:  CommitmentTypeIndicationType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CommitmentTypeIndicationType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CommitmentTypeIndicationType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CommitmentTypeIndicationType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CommitmentTypeIndicationType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "commitmenttypeindicationtypef179type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CommitmentTypeId" element
     */
    org.etsi.uri.x01903.v13.ObjectIdentifierType getCommitmentTypeId();

    /**
     * Sets the "CommitmentTypeId" element
     */
    void setCommitmentTypeId(org.etsi.uri.x01903.v13.ObjectIdentifierType commitmentTypeId);

    /**
     * Appends and returns a new empty "CommitmentTypeId" element
     */
    org.etsi.uri.x01903.v13.ObjectIdentifierType addNewCommitmentTypeId();

    /**
     * Gets a List of "ObjectReference" elements
     */
    java.util.List<java.lang.String> getObjectReferenceList();

    /**
     * Gets array of all "ObjectReference" elements
     */
    java.lang.String[] getObjectReferenceArray();

    /**
     * Gets ith "ObjectReference" element
     */
    java.lang.String getObjectReferenceArray(int i);

    /**
     * Gets (as xml) a List of "ObjectReference" elements
     */
    java.util.List<org.apache.xmlbeans.XmlAnyURI> xgetObjectReferenceList();

    /**
     * Gets (as xml) array of all "ObjectReference" elements
     */
    org.apache.xmlbeans.XmlAnyURI[] xgetObjectReferenceArray();

    /**
     * Gets (as xml) ith "ObjectReference" element
     */
    org.apache.xmlbeans.XmlAnyURI xgetObjectReferenceArray(int i);

    /**
     * Returns number of "ObjectReference" element
     */
    int sizeOfObjectReferenceArray();

    /**
     * Sets array of all "ObjectReference" element
     */
    void setObjectReferenceArray(java.lang.String[] objectReferenceArray);

    /**
     * Sets ith "ObjectReference" element
     */
    void setObjectReferenceArray(int i, java.lang.String objectReference);

    /**
     * Sets (as xml) array of all "ObjectReference" element
     */
    void xsetObjectReferenceArray(org.apache.xmlbeans.XmlAnyURI[] objectReferenceArray);

    /**
     * Sets (as xml) ith "ObjectReference" element
     */
    void xsetObjectReferenceArray(int i, org.apache.xmlbeans.XmlAnyURI objectReference);

    /**
     * Inserts the value as the ith "ObjectReference" element
     */
    void insertObjectReference(int i, java.lang.String objectReference);

    /**
     * Appends the value as the last "ObjectReference" element
     */
    void addObjectReference(java.lang.String objectReference);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ObjectReference" element
     */
    org.apache.xmlbeans.XmlAnyURI insertNewObjectReference(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ObjectReference" element
     */
    org.apache.xmlbeans.XmlAnyURI addNewObjectReference();

    /**
     * Removes the ith "ObjectReference" element
     */
    void removeObjectReference(int i);

    /**
     * Gets the "AllSignedDataObjects" element
     */
    org.apache.xmlbeans.XmlObject getAllSignedDataObjects();

    /**
     * True if has "AllSignedDataObjects" element
     */
    boolean isSetAllSignedDataObjects();

    /**
     * Sets the "AllSignedDataObjects" element
     */
    void setAllSignedDataObjects(org.apache.xmlbeans.XmlObject allSignedDataObjects);

    /**
     * Appends and returns a new empty "AllSignedDataObjects" element
     */
    org.apache.xmlbeans.XmlObject addNewAllSignedDataObjects();

    /**
     * Unsets the "AllSignedDataObjects" element
     */
    void unsetAllSignedDataObjects();

    /**
     * Gets the "CommitmentTypeQualifiers" element
     */
    org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType getCommitmentTypeQualifiers();

    /**
     * True if has "CommitmentTypeQualifiers" element
     */
    boolean isSetCommitmentTypeQualifiers();

    /**
     * Sets the "CommitmentTypeQualifiers" element
     */
    void setCommitmentTypeQualifiers(org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType commitmentTypeQualifiers);

    /**
     * Appends and returns a new empty "CommitmentTypeQualifiers" element
     */
    org.etsi.uri.x01903.v13.CommitmentTypeQualifiersListType addNewCommitmentTypeQualifiers();

    /**
     * Unsets the "CommitmentTypeQualifiers" element
     */
    void unsetCommitmentTypeQualifiers();
}
