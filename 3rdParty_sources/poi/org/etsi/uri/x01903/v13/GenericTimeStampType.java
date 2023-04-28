/*
 * XML Type:  GenericTimeStampType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.GenericTimeStampType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML GenericTimeStampType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface GenericTimeStampType extends org.apache.xmlbeans.XmlObject {
    AbstractDocumentFactory<org.etsi.uri.x01903.v13.GenericTimeStampType> Factory = new AbstractDocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "generictimestamptypecdadtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "Include" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.IncludeType> getIncludeList();

    /**
     * Gets array of all "Include" elements
     */
    org.etsi.uri.x01903.v13.IncludeType[] getIncludeArray();

    /**
     * Gets ith "Include" element
     */
    org.etsi.uri.x01903.v13.IncludeType getIncludeArray(int i);

    /**
     * Returns number of "Include" element
     */
    int sizeOfIncludeArray();

    /**
     * Sets array of all "Include" element
     */
    void setIncludeArray(org.etsi.uri.x01903.v13.IncludeType[] includeArray);

    /**
     * Sets ith "Include" element
     */
    void setIncludeArray(int i, org.etsi.uri.x01903.v13.IncludeType include);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Include" element
     */
    org.etsi.uri.x01903.v13.IncludeType insertNewInclude(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "Include" element
     */
    org.etsi.uri.x01903.v13.IncludeType addNewInclude();

    /**
     * Removes the ith "Include" element
     */
    void removeInclude(int i);

    /**
     * Gets a List of "ReferenceInfo" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.ReferenceInfoType> getReferenceInfoList();

    /**
     * Gets array of all "ReferenceInfo" elements
     */
    org.etsi.uri.x01903.v13.ReferenceInfoType[] getReferenceInfoArray();

    /**
     * Gets ith "ReferenceInfo" element
     */
    org.etsi.uri.x01903.v13.ReferenceInfoType getReferenceInfoArray(int i);

    /**
     * Returns number of "ReferenceInfo" element
     */
    int sizeOfReferenceInfoArray();

    /**
     * Sets array of all "ReferenceInfo" element
     */
    void setReferenceInfoArray(org.etsi.uri.x01903.v13.ReferenceInfoType[] referenceInfoArray);

    /**
     * Sets ith "ReferenceInfo" element
     */
    void setReferenceInfoArray(int i, org.etsi.uri.x01903.v13.ReferenceInfoType referenceInfo);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ReferenceInfo" element
     */
    org.etsi.uri.x01903.v13.ReferenceInfoType insertNewReferenceInfo(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ReferenceInfo" element
     */
    org.etsi.uri.x01903.v13.ReferenceInfoType addNewReferenceInfo();

    /**
     * Removes the ith "ReferenceInfo" element
     */
    void removeReferenceInfo(int i);

    /**
     * Gets the "CanonicalizationMethod" element
     */
    org.w3.x2000.x09.xmldsig.CanonicalizationMethodType getCanonicalizationMethod();

    /**
     * True if has "CanonicalizationMethod" element
     */
    boolean isSetCanonicalizationMethod();

    /**
     * Sets the "CanonicalizationMethod" element
     */
    void setCanonicalizationMethod(org.w3.x2000.x09.xmldsig.CanonicalizationMethodType canonicalizationMethod);

    /**
     * Appends and returns a new empty "CanonicalizationMethod" element
     */
    org.w3.x2000.x09.xmldsig.CanonicalizationMethodType addNewCanonicalizationMethod();

    /**
     * Unsets the "CanonicalizationMethod" element
     */
    void unsetCanonicalizationMethod();

    /**
     * Gets a List of "EncapsulatedTimeStamp" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getEncapsulatedTimeStampList();

    /**
     * Gets array of all "EncapsulatedTimeStamp" elements
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getEncapsulatedTimeStampArray();

    /**
     * Gets ith "EncapsulatedTimeStamp" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedTimeStampArray(int i);

    /**
     * Returns number of "EncapsulatedTimeStamp" element
     */
    int sizeOfEncapsulatedTimeStampArray();

    /**
     * Sets array of all "EncapsulatedTimeStamp" element
     */
    void setEncapsulatedTimeStampArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] encapsulatedTimeStampArray);

    /**
     * Sets ith "EncapsulatedTimeStamp" element
     */
    void setEncapsulatedTimeStampArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedTimeStamp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "EncapsulatedTimeStamp" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewEncapsulatedTimeStamp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "EncapsulatedTimeStamp" element
     */
    org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedTimeStamp();

    /**
     * Removes the ith "EncapsulatedTimeStamp" element
     */
    void removeEncapsulatedTimeStamp(int i);

    /**
     * Gets a List of "XMLTimeStamp" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.AnyType> getXMLTimeStampList();

    /**
     * Gets array of all "XMLTimeStamp" elements
     */
    org.etsi.uri.x01903.v13.AnyType[] getXMLTimeStampArray();

    /**
     * Gets ith "XMLTimeStamp" element
     */
    org.etsi.uri.x01903.v13.AnyType getXMLTimeStampArray(int i);

    /**
     * Returns number of "XMLTimeStamp" element
     */
    int sizeOfXMLTimeStampArray();

    /**
     * Sets array of all "XMLTimeStamp" element
     */
    void setXMLTimeStampArray(org.etsi.uri.x01903.v13.AnyType[] xmlTimeStampArray);

    /**
     * Sets ith "XMLTimeStamp" element
     */
    void setXMLTimeStampArray(int i, org.etsi.uri.x01903.v13.AnyType xmlTimeStamp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "XMLTimeStamp" element
     */
    org.etsi.uri.x01903.v13.AnyType insertNewXMLTimeStamp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "XMLTimeStamp" element
     */
    org.etsi.uri.x01903.v13.AnyType addNewXMLTimeStamp();

    /**
     * Removes the ith "XMLTimeStamp" element
     */
    void removeXMLTimeStamp(int i);

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
