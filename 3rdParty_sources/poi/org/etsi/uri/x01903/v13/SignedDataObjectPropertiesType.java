/*
 * XML Type:  SignedDataObjectPropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SignedDataObjectPropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface SignedDataObjectPropertiesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "signeddataobjectpropertiestype19a6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "DataObjectFormat" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.DataObjectFormatType> getDataObjectFormatList();

    /**
     * Gets array of all "DataObjectFormat" elements
     */
    org.etsi.uri.x01903.v13.DataObjectFormatType[] getDataObjectFormatArray();

    /**
     * Gets ith "DataObjectFormat" element
     */
    org.etsi.uri.x01903.v13.DataObjectFormatType getDataObjectFormatArray(int i);

    /**
     * Returns number of "DataObjectFormat" element
     */
    int sizeOfDataObjectFormatArray();

    /**
     * Sets array of all "DataObjectFormat" element
     */
    void setDataObjectFormatArray(org.etsi.uri.x01903.v13.DataObjectFormatType[] dataObjectFormatArray);

    /**
     * Sets ith "DataObjectFormat" element
     */
    void setDataObjectFormatArray(int i, org.etsi.uri.x01903.v13.DataObjectFormatType dataObjectFormat);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "DataObjectFormat" element
     */
    org.etsi.uri.x01903.v13.DataObjectFormatType insertNewDataObjectFormat(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "DataObjectFormat" element
     */
    org.etsi.uri.x01903.v13.DataObjectFormatType addNewDataObjectFormat();

    /**
     * Removes the ith "DataObjectFormat" element
     */
    void removeDataObjectFormat(int i);

    /**
     * Gets a List of "CommitmentTypeIndication" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CommitmentTypeIndicationType> getCommitmentTypeIndicationList();

    /**
     * Gets array of all "CommitmentTypeIndication" elements
     */
    org.etsi.uri.x01903.v13.CommitmentTypeIndicationType[] getCommitmentTypeIndicationArray();

    /**
     * Gets ith "CommitmentTypeIndication" element
     */
    org.etsi.uri.x01903.v13.CommitmentTypeIndicationType getCommitmentTypeIndicationArray(int i);

    /**
     * Returns number of "CommitmentTypeIndication" element
     */
    int sizeOfCommitmentTypeIndicationArray();

    /**
     * Sets array of all "CommitmentTypeIndication" element
     */
    void setCommitmentTypeIndicationArray(org.etsi.uri.x01903.v13.CommitmentTypeIndicationType[] commitmentTypeIndicationArray);

    /**
     * Sets ith "CommitmentTypeIndication" element
     */
    void setCommitmentTypeIndicationArray(int i, org.etsi.uri.x01903.v13.CommitmentTypeIndicationType commitmentTypeIndication);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CommitmentTypeIndication" element
     */
    org.etsi.uri.x01903.v13.CommitmentTypeIndicationType insertNewCommitmentTypeIndication(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "CommitmentTypeIndication" element
     */
    org.etsi.uri.x01903.v13.CommitmentTypeIndicationType addNewCommitmentTypeIndication();

    /**
     * Removes the ith "CommitmentTypeIndication" element
     */
    void removeCommitmentTypeIndication(int i);

    /**
     * Gets a List of "AllDataObjectsTimeStamp" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getAllDataObjectsTimeStampList();

    /**
     * Gets array of all "AllDataObjectsTimeStamp" elements
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType[] getAllDataObjectsTimeStampArray();

    /**
     * Gets ith "AllDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getAllDataObjectsTimeStampArray(int i);

    /**
     * Returns number of "AllDataObjectsTimeStamp" element
     */
    int sizeOfAllDataObjectsTimeStampArray();

    /**
     * Sets array of all "AllDataObjectsTimeStamp" element
     */
    void setAllDataObjectsTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] allDataObjectsTimeStampArray);

    /**
     * Sets ith "AllDataObjectsTimeStamp" element
     */
    void setAllDataObjectsTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType allDataObjectsTimeStamp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AllDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewAllDataObjectsTimeStamp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "AllDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewAllDataObjectsTimeStamp();

    /**
     * Removes the ith "AllDataObjectsTimeStamp" element
     */
    void removeAllDataObjectsTimeStamp(int i);

    /**
     * Gets a List of "IndividualDataObjectsTimeStamp" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getIndividualDataObjectsTimeStampList();

    /**
     * Gets array of all "IndividualDataObjectsTimeStamp" elements
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType[] getIndividualDataObjectsTimeStampArray();

    /**
     * Gets ith "IndividualDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType getIndividualDataObjectsTimeStampArray(int i);

    /**
     * Returns number of "IndividualDataObjectsTimeStamp" element
     */
    int sizeOfIndividualDataObjectsTimeStampArray();

    /**
     * Sets array of all "IndividualDataObjectsTimeStamp" element
     */
    void setIndividualDataObjectsTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] individualDataObjectsTimeStampArray);

    /**
     * Sets ith "IndividualDataObjectsTimeStamp" element
     */
    void setIndividualDataObjectsTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType individualDataObjectsTimeStamp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "IndividualDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewIndividualDataObjectsTimeStamp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "IndividualDataObjectsTimeStamp" element
     */
    org.etsi.uri.x01903.v13.XAdESTimeStampType addNewIndividualDataObjectsTimeStamp();

    /**
     * Removes the ith "IndividualDataObjectsTimeStamp" element
     */
    void removeIndividualDataObjectsTimeStamp(int i);

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
