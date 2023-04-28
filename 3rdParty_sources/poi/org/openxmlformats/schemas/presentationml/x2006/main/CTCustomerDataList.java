/*
 * XML Type:  CT_CustomerDataList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CustomerDataList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCustomerDataList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcustomerdatalist8b7ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "custData" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData> getCustDataList();

    /**
     * Gets array of all "custData" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData[] getCustDataArray();

    /**
     * Gets ith "custData" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData getCustDataArray(int i);

    /**
     * Returns number of "custData" element
     */
    int sizeOfCustDataArray();

    /**
     * Sets array of all "custData" element
     */
    void setCustDataArray(org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData[] custDataArray);

    /**
     * Sets ith "custData" element
     */
    void setCustDataArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData custData);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "custData" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData insertNewCustData(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "custData" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData addNewCustData();

    /**
     * Removes the ith "custData" element
     */
    void removeCustData(int i);

    /**
     * Gets the "tags" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData getTags();

    /**
     * True if has "tags" element
     */
    boolean isSetTags();

    /**
     * Sets the "tags" element
     */
    void setTags(org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData tags);

    /**
     * Appends and returns a new empty "tags" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData addNewTags();

    /**
     * Unsets the "tags" element
     */
    void unsetTags();
}
