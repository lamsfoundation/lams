/*
 * XML Type:  CT_ProtectedRange
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ProtectedRange(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTProtectedRange extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctprotectedrange7078type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "securityDescriptor" elements
     */
    java.util.List<java.lang.String> getSecurityDescriptorList();

    /**
     * Gets array of all "securityDescriptor" elements
     */
    java.lang.String[] getSecurityDescriptorArray();

    /**
     * Gets ith "securityDescriptor" element
     */
    java.lang.String getSecurityDescriptorArray(int i);

    /**
     * Gets (as xml) a List of "securityDescriptor" elements
     */
    java.util.List<org.apache.xmlbeans.XmlString> xgetSecurityDescriptorList();

    /**
     * Gets (as xml) array of all "securityDescriptor" elements
     */
    org.apache.xmlbeans.XmlString[] xgetSecurityDescriptorArray();

    /**
     * Gets (as xml) ith "securityDescriptor" element
     */
    org.apache.xmlbeans.XmlString xgetSecurityDescriptorArray(int i);

    /**
     * Returns number of "securityDescriptor" element
     */
    int sizeOfSecurityDescriptorArray();

    /**
     * Sets array of all "securityDescriptor" element
     */
    void setSecurityDescriptorArray(java.lang.String[] securityDescriptorArray);

    /**
     * Sets ith "securityDescriptor" element
     */
    void setSecurityDescriptorArray(int i, java.lang.String securityDescriptor);

    /**
     * Sets (as xml) array of all "securityDescriptor" element
     */
    void xsetSecurityDescriptorArray(org.apache.xmlbeans.XmlString[] securityDescriptorArray);

    /**
     * Sets (as xml) ith "securityDescriptor" element
     */
    void xsetSecurityDescriptorArray(int i, org.apache.xmlbeans.XmlString securityDescriptor);

    /**
     * Inserts the value as the ith "securityDescriptor" element
     */
    void insertSecurityDescriptor(int i, java.lang.String securityDescriptor);

    /**
     * Appends the value as the last "securityDescriptor" element
     */
    void addSecurityDescriptor(java.lang.String securityDescriptor);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "securityDescriptor" element
     */
    org.apache.xmlbeans.XmlString insertNewSecurityDescriptor(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "securityDescriptor" element
     */
    org.apache.xmlbeans.XmlString addNewSecurityDescriptor();

    /**
     * Removes the ith "securityDescriptor" element
     */
    void removeSecurityDescriptor(int i);

    /**
     * Gets the "password" attribute
     */
    byte[] getPassword();

    /**
     * Gets (as xml) the "password" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex xgetPassword();

    /**
     * True if has "password" attribute
     */
    boolean isSetPassword();

    /**
     * Sets the "password" attribute
     */
    void setPassword(byte[] password);

    /**
     * Sets (as xml) the "password" attribute
     */
    void xsetPassword(org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex password);

    /**
     * Unsets the "password" attribute
     */
    void unsetPassword();

    /**
     * Gets the "sqref" attribute
     */
    java.util.List getSqref();

    /**
     * Gets (as xml) the "sqref" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref xgetSqref();

    /**
     * Sets the "sqref" attribute
     */
    void setSqref(java.util.List sqref);

    /**
     * Sets (as xml) the "sqref" attribute
     */
    void xsetSqref(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref sqref);

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name);

    /**
     * Gets the "securityDescriptor" attribute
     */
    java.lang.String getSecurityDescriptor2();

    /**
     * Gets (as xml) the "securityDescriptor" attribute
     */
    org.apache.xmlbeans.XmlString xgetSecurityDescriptor2();

    /**
     * True if has "securityDescriptor" attribute
     */
    boolean isSetSecurityDescriptor2();

    /**
     * Sets the "securityDescriptor" attribute
     */
    void setSecurityDescriptor2(java.lang.String securityDescriptor2);

    /**
     * Sets (as xml) the "securityDescriptor" attribute
     */
    void xsetSecurityDescriptor2(org.apache.xmlbeans.XmlString securityDescriptor2);

    /**
     * Unsets the "securityDescriptor" attribute
     */
    void unsetSecurityDescriptor2();

    /**
     * Gets the "algorithmName" attribute
     */
    java.lang.String getAlgorithmName();

    /**
     * Gets (as xml) the "algorithmName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAlgorithmName();

    /**
     * True if has "algorithmName" attribute
     */
    boolean isSetAlgorithmName();

    /**
     * Sets the "algorithmName" attribute
     */
    void setAlgorithmName(java.lang.String algorithmName);

    /**
     * Sets (as xml) the "algorithmName" attribute
     */
    void xsetAlgorithmName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring algorithmName);

    /**
     * Unsets the "algorithmName" attribute
     */
    void unsetAlgorithmName();

    /**
     * Gets the "hashValue" attribute
     */
    byte[] getHashValue();

    /**
     * Gets (as xml) the "hashValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetHashValue();

    /**
     * True if has "hashValue" attribute
     */
    boolean isSetHashValue();

    /**
     * Sets the "hashValue" attribute
     */
    void setHashValue(byte[] hashValue);

    /**
     * Sets (as xml) the "hashValue" attribute
     */
    void xsetHashValue(org.apache.xmlbeans.XmlBase64Binary hashValue);

    /**
     * Unsets the "hashValue" attribute
     */
    void unsetHashValue();

    /**
     * Gets the "saltValue" attribute
     */
    byte[] getSaltValue();

    /**
     * Gets (as xml) the "saltValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetSaltValue();

    /**
     * True if has "saltValue" attribute
     */
    boolean isSetSaltValue();

    /**
     * Sets the "saltValue" attribute
     */
    void setSaltValue(byte[] saltValue);

    /**
     * Sets (as xml) the "saltValue" attribute
     */
    void xsetSaltValue(org.apache.xmlbeans.XmlBase64Binary saltValue);

    /**
     * Unsets the "saltValue" attribute
     */
    void unsetSaltValue();

    /**
     * Gets the "spinCount" attribute
     */
    long getSpinCount();

    /**
     * Gets (as xml) the "spinCount" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetSpinCount();

    /**
     * True if has "spinCount" attribute
     */
    boolean isSetSpinCount();

    /**
     * Sets the "spinCount" attribute
     */
    void setSpinCount(long spinCount);

    /**
     * Sets (as xml) the "spinCount" attribute
     */
    void xsetSpinCount(org.apache.xmlbeans.XmlUnsignedInt spinCount);

    /**
     * Unsets the "spinCount" attribute
     */
    void unsetSpinCount();
}
