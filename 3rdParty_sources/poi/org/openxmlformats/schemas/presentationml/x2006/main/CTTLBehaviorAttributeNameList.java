/*
 * XML Type:  CT_TLBehaviorAttributeNameList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLBehaviorAttributeNameList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLBehaviorAttributeNameList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlbehaviorattributenamelist0494type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "attrName" elements
     */
    java.util.List<java.lang.String> getAttrNameList();

    /**
     * Gets array of all "attrName" elements
     */
    java.lang.String[] getAttrNameArray();

    /**
     * Gets ith "attrName" element
     */
    java.lang.String getAttrNameArray(int i);

    /**
     * Gets (as xml) a List of "attrName" elements
     */
    java.util.List<org.apache.xmlbeans.XmlString> xgetAttrNameList();

    /**
     * Gets (as xml) array of all "attrName" elements
     */
    org.apache.xmlbeans.XmlString[] xgetAttrNameArray();

    /**
     * Gets (as xml) ith "attrName" element
     */
    org.apache.xmlbeans.XmlString xgetAttrNameArray(int i);

    /**
     * Returns number of "attrName" element
     */
    int sizeOfAttrNameArray();

    /**
     * Sets array of all "attrName" element
     */
    void setAttrNameArray(java.lang.String[] attrNameArray);

    /**
     * Sets ith "attrName" element
     */
    void setAttrNameArray(int i, java.lang.String attrName);

    /**
     * Sets (as xml) array of all "attrName" element
     */
    void xsetAttrNameArray(org.apache.xmlbeans.XmlString[] attrNameArray);

    /**
     * Sets (as xml) ith "attrName" element
     */
    void xsetAttrNameArray(int i, org.apache.xmlbeans.XmlString attrName);

    /**
     * Inserts the value as the ith "attrName" element
     */
    void insertAttrName(int i, java.lang.String attrName);

    /**
     * Appends the value as the last "attrName" element
     */
    void addAttrName(java.lang.String attrName);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "attrName" element
     */
    org.apache.xmlbeans.XmlString insertNewAttrName(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "attrName" element
     */
    org.apache.xmlbeans.XmlString addNewAttrName();

    /**
     * Removes the ith "attrName" element
     */
    void removeAttrName(int i);
}
