/*
 * XML Type:  UnsignedSignaturePropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML UnsignedSignaturePropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface UnsignedSignaturePropertiesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "unsignedsignaturepropertiestypecf32type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "CounterSignature" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CounterSignatureType> getCounterSignatureList();

    /**
     * Gets array of all "CounterSignature" elements
     */
    org.etsi.uri.x01903.v13.CounterSignatureType[] getCounterSignatureArray();

    /**
     * Gets ith "CounterSignature" element
     */
    org.etsi.uri.x01903.v13.CounterSignatureType getCounterSignatureArray(int i);

    /**
     * Returns number of "CounterSignature" element
     */
    int sizeOfCounterSignatureArray();

    /**
     * Sets array of all "CounterSignature" element
     */
    void setCounterSignatureArray(org.etsi.uri.x01903.v13.CounterSignatureType[] counterSignatureArray);

    /**
     * Sets ith "CounterSignature" element
     */
    void setCounterSignatureArray(int i, org.etsi.uri.x01903.v13.CounterSignatureType counterSignature);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CounterSignature" element
     */
    org.etsi.uri.x01903.v13.CounterSignatureType insertNewCounterSignature(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "CounterSignature" element
     */
    org.etsi.uri.x01903.v13.CounterSignatureType addNewCounterSignature();

    /**
     * Removes the ith "CounterSignature" element
     */
    void removeCounterSignature(int i);

    /**
     * Gets a List of "SignatureTimeStamp" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getSignatureTimeStampList();

    /**
     * Gets array of all "SignatureTimeStamp" elements
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType[] getSignatureTimeStampArray();

    /**
     * Gets ith "SignatureTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getSignatureTimeStampArray(int i);

    /**
     * Returns number of "SignatureTimeStamp" element
     */
    int sizeOfSignatureTimeStampArray();

    /**
     * Sets array of all "SignatureTimeStamp" element
     */
    void setSignatureTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] signatureTimeStampArray);

    /**
     * Sets ith "SignatureTimeStamp" element
     */
    void setSignatureTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType signatureTimeStamp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SignatureTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewSignatureTimeStamp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "SignatureTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewSignatureTimeStamp();

    /**
     * Removes the ith "SignatureTimeStamp" element
     */
    void removeSignatureTimeStamp(int i);

    /**
     * Gets a List of "CompleteCertificateRefs" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CompleteCertificateRefsType> getCompleteCertificateRefsList();

    /**
     * Gets array of all "CompleteCertificateRefs" elements
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType[] getCompleteCertificateRefsArray();

    /**
     * Gets ith "CompleteCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType getCompleteCertificateRefsArray(int i);

    /**
     * Returns number of "CompleteCertificateRefs" element
     */
    int sizeOfCompleteCertificateRefsArray();

    /**
     * Sets array of all "CompleteCertificateRefs" element
     */
    void setCompleteCertificateRefsArray(org.etsi.uri.x01903.v13.CompleteCertificateRefsType[] completeCertificateRefsArray);

    /**
     * Sets ith "CompleteCertificateRefs" element
     */
    void setCompleteCertificateRefsArray(int i, org.etsi.uri.x01903.v13.CompleteCertificateRefsType completeCertificateRefs);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CompleteCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType insertNewCompleteCertificateRefs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "CompleteCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType addNewCompleteCertificateRefs();

    /**
     * Removes the ith "CompleteCertificateRefs" element
     */
    void removeCompleteCertificateRefs(int i);

    /**
     * Gets a List of "CompleteRevocationRefs" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CompleteRevocationRefsType> getCompleteRevocationRefsList();

    /**
     * Gets array of all "CompleteRevocationRefs" elements
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType[] getCompleteRevocationRefsArray();

    /**
     * Gets ith "CompleteRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType getCompleteRevocationRefsArray(int i);

    /**
     * Returns number of "CompleteRevocationRefs" element
     */
    int sizeOfCompleteRevocationRefsArray();

    /**
     * Sets array of all "CompleteRevocationRefs" element
     */
    void setCompleteRevocationRefsArray(org.etsi.uri.x01903.v13.CompleteRevocationRefsType[] completeRevocationRefsArray);

    /**
     * Sets ith "CompleteRevocationRefs" element
     */
    void setCompleteRevocationRefsArray(int i, org.etsi.uri.x01903.v13.CompleteRevocationRefsType completeRevocationRefs);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CompleteRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType insertNewCompleteRevocationRefs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "CompleteRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType addNewCompleteRevocationRefs();

    /**
     * Removes the ith "CompleteRevocationRefs" element
     */
    void removeCompleteRevocationRefs(int i);

    /**
     * Gets a List of "AttributeCertificateRefs" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CompleteCertificateRefsType> getAttributeCertificateRefsList();

    /**
     * Gets array of all "AttributeCertificateRefs" elements
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType[] getAttributeCertificateRefsArray();

    /**
     * Gets ith "AttributeCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType getAttributeCertificateRefsArray(int i);

    /**
     * Returns number of "AttributeCertificateRefs" element
     */
    int sizeOfAttributeCertificateRefsArray();

    /**
     * Sets array of all "AttributeCertificateRefs" element
     */
    void setAttributeCertificateRefsArray(org.etsi.uri.x01903.v13.CompleteCertificateRefsType[] attributeCertificateRefsArray);

    /**
     * Sets ith "AttributeCertificateRefs" element
     */
    void setAttributeCertificateRefsArray(int i, org.etsi.uri.x01903.v13.CompleteCertificateRefsType attributeCertificateRefs);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AttributeCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType insertNewAttributeCertificateRefs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "AttributeCertificateRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteCertificateRefsType addNewAttributeCertificateRefs();

    /**
     * Removes the ith "AttributeCertificateRefs" element
     */
    void removeAttributeCertificateRefs(int i);

    /**
     * Gets a List of "AttributeRevocationRefs" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CompleteRevocationRefsType> getAttributeRevocationRefsList();

    /**
     * Gets array of all "AttributeRevocationRefs" elements
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType[] getAttributeRevocationRefsArray();

    /**
     * Gets ith "AttributeRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType getAttributeRevocationRefsArray(int i);

    /**
     * Returns number of "AttributeRevocationRefs" element
     */
    int sizeOfAttributeRevocationRefsArray();

    /**
     * Sets array of all "AttributeRevocationRefs" element
     */
    void setAttributeRevocationRefsArray(org.etsi.uri.x01903.v13.CompleteRevocationRefsType[] attributeRevocationRefsArray);

    /**
     * Sets ith "AttributeRevocationRefs" element
     */
    void setAttributeRevocationRefsArray(int i, org.etsi.uri.x01903.v13.CompleteRevocationRefsType attributeRevocationRefs);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AttributeRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType insertNewAttributeRevocationRefs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "AttributeRevocationRefs" element
     */
    org.etsi.uri.x01903.v13.CompleteRevocationRefsType addNewAttributeRevocationRefs();

    /**
     * Removes the ith "AttributeRevocationRefs" element
     */
    void removeAttributeRevocationRefs(int i);

    /**
     * Gets a List of "SigAndRefsTimeStamp" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getSigAndRefsTimeStampList();

    /**
     * Gets array of all "SigAndRefsTimeStamp" elements
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType[] getSigAndRefsTimeStampArray();

    /**
     * Gets ith "SigAndRefsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getSigAndRefsTimeStampArray(int i);

    /**
     * Returns number of "SigAndRefsTimeStamp" element
     */
    int sizeOfSigAndRefsTimeStampArray();

    /**
     * Sets array of all "SigAndRefsTimeStamp" element
     */
    void setSigAndRefsTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] sigAndRefsTimeStampArray);

    /**
     * Sets ith "SigAndRefsTimeStamp" element
     */
    void setSigAndRefsTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType sigAndRefsTimeStamp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SigAndRefsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewSigAndRefsTimeStamp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "SigAndRefsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewSigAndRefsTimeStamp();

    /**
     * Removes the ith "SigAndRefsTimeStamp" element
     */
    void removeSigAndRefsTimeStamp(int i);

    /**
     * Gets a List of "RefsOnlyTimeStamp" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getRefsOnlyTimeStampList();

    /**
     * Gets array of all "RefsOnlyTimeStamp" elements
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType[] getRefsOnlyTimeStampArray();

    /**
     * Gets ith "RefsOnlyTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getRefsOnlyTimeStampArray(int i);

    /**
     * Returns number of "RefsOnlyTimeStamp" element
     */
    int sizeOfRefsOnlyTimeStampArray();

    /**
     * Sets array of all "RefsOnlyTimeStamp" element
     */
    void setRefsOnlyTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] refsOnlyTimeStampArray);

    /**
     * Sets ith "RefsOnlyTimeStamp" element
     */
    void setRefsOnlyTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType refsOnlyTimeStamp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "RefsOnlyTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewRefsOnlyTimeStamp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "RefsOnlyTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewRefsOnlyTimeStamp();

    /**
     * Removes the ith "RefsOnlyTimeStamp" element
     */
    void removeRefsOnlyTimeStamp(int i);

    /**
     * Gets a List of "CertificateValues" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CertificateValuesType> getCertificateValuesList();

    /**
     * Gets array of all "CertificateValues" elements
     */
    org.etsi.uri.x01903.v13.CertificateValuesType[] getCertificateValuesArray();

    /**
     * Gets ith "CertificateValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType getCertificateValuesArray(int i);

    /**
     * Returns number of "CertificateValues" element
     */
    int sizeOfCertificateValuesArray();

    /**
     * Sets array of all "CertificateValues" element
     */
    void setCertificateValuesArray(org.etsi.uri.x01903.v13.CertificateValuesType[] certificateValuesArray);

    /**
     * Sets ith "CertificateValues" element
     */
    void setCertificateValuesArray(int i, org.etsi.uri.x01903.v13.CertificateValuesType certificateValues);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CertificateValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType insertNewCertificateValues(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "CertificateValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType addNewCertificateValues();

    /**
     * Removes the ith "CertificateValues" element
     */
    void removeCertificateValues(int i);

    /**
     * Gets a List of "RevocationValues" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.RevocationValuesType> getRevocationValuesList();

    /**
     * Gets array of all "RevocationValues" elements
     */
    org.etsi.uri.x01903.v13.RevocationValuesType[] getRevocationValuesArray();

    /**
     * Gets ith "RevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType getRevocationValuesArray(int i);

    /**
     * Returns number of "RevocationValues" element
     */
    int sizeOfRevocationValuesArray();

    /**
     * Sets array of all "RevocationValues" element
     */
    void setRevocationValuesArray(org.etsi.uri.x01903.v13.RevocationValuesType[] revocationValuesArray);

    /**
     * Sets ith "RevocationValues" element
     */
    void setRevocationValuesArray(int i, org.etsi.uri.x01903.v13.RevocationValuesType revocationValues);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "RevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType insertNewRevocationValues(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "RevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType addNewRevocationValues();

    /**
     * Removes the ith "RevocationValues" element
     */
    void removeRevocationValues(int i);

    /**
     * Gets a List of "AttrAuthoritiesCertValues" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CertificateValuesType> getAttrAuthoritiesCertValuesList();

    /**
     * Gets array of all "AttrAuthoritiesCertValues" elements
     */
    org.etsi.uri.x01903.v13.CertificateValuesType[] getAttrAuthoritiesCertValuesArray();

    /**
     * Gets ith "AttrAuthoritiesCertValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType getAttrAuthoritiesCertValuesArray(int i);

    /**
     * Returns number of "AttrAuthoritiesCertValues" element
     */
    int sizeOfAttrAuthoritiesCertValuesArray();

    /**
     * Sets array of all "AttrAuthoritiesCertValues" element
     */
    void setAttrAuthoritiesCertValuesArray(org.etsi.uri.x01903.v13.CertificateValuesType[] attrAuthoritiesCertValuesArray);

    /**
     * Sets ith "AttrAuthoritiesCertValues" element
     */
    void setAttrAuthoritiesCertValuesArray(int i, org.etsi.uri.x01903.v13.CertificateValuesType attrAuthoritiesCertValues);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AttrAuthoritiesCertValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType insertNewAttrAuthoritiesCertValues(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "AttrAuthoritiesCertValues" element
     */
    org.etsi.uri.x01903.v13.CertificateValuesType addNewAttrAuthoritiesCertValues();

    /**
     * Removes the ith "AttrAuthoritiesCertValues" element
     */
    void removeAttrAuthoritiesCertValues(int i);

    /**
     * Gets a List of "AttributeRevocationValues" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.RevocationValuesType> getAttributeRevocationValuesList();

    /**
     * Gets array of all "AttributeRevocationValues" elements
     */
    org.etsi.uri.x01903.v13.RevocationValuesType[] getAttributeRevocationValuesArray();

    /**
     * Gets ith "AttributeRevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType getAttributeRevocationValuesArray(int i);

    /**
     * Returns number of "AttributeRevocationValues" element
     */
    int sizeOfAttributeRevocationValuesArray();

    /**
     * Sets array of all "AttributeRevocationValues" element
     */
    void setAttributeRevocationValuesArray(org.etsi.uri.x01903.v13.RevocationValuesType[] attributeRevocationValuesArray);

    /**
     * Sets ith "AttributeRevocationValues" element
     */
    void setAttributeRevocationValuesArray(int i, org.etsi.uri.x01903.v13.RevocationValuesType attributeRevocationValues);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AttributeRevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType insertNewAttributeRevocationValues(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "AttributeRevocationValues" element
     */
    org.etsi.uri.x01903.v13.RevocationValuesType addNewAttributeRevocationValues();

    /**
     * Removes the ith "AttributeRevocationValues" element
     */
    void removeAttributeRevocationValues(int i);

    /**
     * Gets a List of "ArchiveTimeStamp" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getArchiveTimeStampList();

    /**
     * Gets array of all "ArchiveTimeStamp" elements
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType[] getArchiveTimeStampArray();

    /**
     * Gets ith "ArchiveTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getArchiveTimeStampArray(int i);

    /**
     * Returns number of "ArchiveTimeStamp" element
     */
    int sizeOfArchiveTimeStampArray();

    /**
     * Sets array of all "ArchiveTimeStamp" element
     */
    void setArchiveTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] archiveTimeStampArray);

    /**
     * Sets ith "ArchiveTimeStamp" element
     */
    void setArchiveTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType archiveTimeStamp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ArchiveTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewArchiveTimeStamp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ArchiveTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewArchiveTimeStamp();

    /**
     * Removes the ith "ArchiveTimeStamp" element
     */
    void removeArchiveTimeStamp(int i);

    /**
     * Gets the "Id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "Id" attribute
     */
    org.apache.xmlbeans.XmlID xgetId();

    /**
     * True if has "Id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "Id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "Id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlID id);

    /**
     * Unsets the "Id" attribute
     */
    void unsetId();
}
