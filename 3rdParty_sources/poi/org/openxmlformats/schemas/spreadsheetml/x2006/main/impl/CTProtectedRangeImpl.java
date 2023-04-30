/*
 * XML Type:  CT_ProtectedRange
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ProtectedRange(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTProtectedRangeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange {
    private static final long serialVersionUID = 1L;

    public CTProtectedRangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "securityDescriptor"),
        new QName("", "password"),
        new QName("", "sqref"),
        new QName("", "name"),
        new QName("", "securityDescriptor"),
        new QName("", "algorithmName"),
        new QName("", "hashValue"),
        new QName("", "saltValue"),
        new QName("", "spinCount"),
    };


    /**
     * Gets a List of "securityDescriptor" elements
     */
    @Override
    public java.util.List<java.lang.String> getSecurityDescriptorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getSecurityDescriptorArray,
                this::setSecurityDescriptorArray,
                this::insertSecurityDescriptor,
                this::removeSecurityDescriptor,
                this::sizeOfSecurityDescriptorArray
            );
        }
    }

    /**
     * Gets array of all "securityDescriptor" elements
     */
    @Override
    public java.lang.String[] getSecurityDescriptorArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "securityDescriptor" element
     */
    @Override
    public java.lang.String getSecurityDescriptorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "securityDescriptor" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlString> xgetSecurityDescriptorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetSecurityDescriptorArray,
                this::xsetSecurityDescriptorArray,
                this::insertNewSecurityDescriptor,
                this::removeSecurityDescriptor,
                this::sizeOfSecurityDescriptorArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "securityDescriptor" elements
     */
    @Override
    public org.apache.xmlbeans.XmlString[] xgetSecurityDescriptorArray() {
        return xgetArray(PROPERTY_QNAME[0], org.apache.xmlbeans.XmlString[]::new);
    }

    /**
     * Gets (as xml) ith "securityDescriptor" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetSecurityDescriptorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "securityDescriptor" element
     */
    @Override
    public int sizeOfSecurityDescriptorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "securityDescriptor" element
     */
    @Override
    public void setSecurityDescriptorArray(java.lang.String[] securityDescriptorArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(securityDescriptorArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "securityDescriptor" element
     */
    @Override
    public void setSecurityDescriptorArray(int i, java.lang.String securityDescriptor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(securityDescriptor);
        }
    }

    /**
     * Sets (as xml) array of all "securityDescriptor" element
     */
    @Override
    public void xsetSecurityDescriptorArray(org.apache.xmlbeans.XmlString[]securityDescriptorArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(securityDescriptorArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "securityDescriptor" element
     */
    @Override
    public void xsetSecurityDescriptorArray(int i, org.apache.xmlbeans.XmlString securityDescriptor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(securityDescriptor);
        }
    }

    /**
     * Inserts the value as the ith "securityDescriptor" element
     */
    @Override
    public void insertSecurityDescriptor(int i, java.lang.String securityDescriptor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setStringValue(securityDescriptor);
        }
    }

    /**
     * Appends the value as the last "securityDescriptor" element
     */
    @Override
    public void addSecurityDescriptor(java.lang.String securityDescriptor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setStringValue(securityDescriptor);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "securityDescriptor" element
     */
    @Override
    public org.apache.xmlbeans.XmlString insertNewSecurityDescriptor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "securityDescriptor" element
     */
    @Override
    public org.apache.xmlbeans.XmlString addNewSecurityDescriptor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "securityDescriptor" element
     */
    @Override
    public void removeSecurityDescriptor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "password" attribute
     */
    @Override
    public byte[] getPassword() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "password" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex xgetPassword() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "password" attribute
     */
    @Override
    public boolean isSetPassword() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "password" attribute
     */
    @Override
    public void setPassword(byte[] password) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setByteArrayValue(password);
        }
    }

    /**
     * Sets (as xml) the "password" attribute
     */
    @Override
    public void xsetPassword(org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex password) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(password);
        }
    }

    /**
     * Unsets the "password" attribute
     */
    @Override
    public void unsetPassword() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "sqref" attribute
     */
    @Override
    public java.util.List getSqref() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getListValue();
        }
    }

    /**
     * Gets (as xml) the "sqref" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref xgetSqref() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "sqref" attribute
     */
    @Override
    public void setSqref(java.util.List sqref) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setListValue(sqref);
        }
    }

    /**
     * Sets (as xml) the "sqref" attribute
     */
    @Override
    public void xsetSqref(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref sqref) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(sqref);
        }
    }

    /**
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(name);
        }
    }

    /**
     * Gets the "securityDescriptor" attribute
     */
    @Override
    public java.lang.String getSecurityDescriptor2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "securityDescriptor" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetSecurityDescriptor2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "securityDescriptor" attribute
     */
    @Override
    public boolean isSetSecurityDescriptor2() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "securityDescriptor" attribute
     */
    @Override
    public void setSecurityDescriptor2(java.lang.String securityDescriptor2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setStringValue(securityDescriptor2);
        }
    }

    /**
     * Sets (as xml) the "securityDescriptor" attribute
     */
    @Override
    public void xsetSecurityDescriptor2(org.apache.xmlbeans.XmlString securityDescriptor2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(securityDescriptor2);
        }
    }

    /**
     * Unsets the "securityDescriptor" attribute
     */
    @Override
    public void unsetSecurityDescriptor2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "algorithmName" attribute
     */
    @Override
    public java.lang.String getAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "algorithmName" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "algorithmName" attribute
     */
    @Override
    public boolean isSetAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "algorithmName" attribute
     */
    @Override
    public void setAlgorithmName(java.lang.String algorithmName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setStringValue(algorithmName);
        }
    }

    /**
     * Sets (as xml) the "algorithmName" attribute
     */
    @Override
    public void xsetAlgorithmName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring algorithmName) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(algorithmName);
        }
    }

    /**
     * Unsets the "algorithmName" attribute
     */
    @Override
    public void unsetAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "hashValue" attribute
     */
    @Override
    public byte[] getHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "hashValue" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "hashValue" attribute
     */
    @Override
    public boolean isSetHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "hashValue" attribute
     */
    @Override
    public void setHashValue(byte[] hashValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setByteArrayValue(hashValue);
        }
    }

    /**
     * Sets (as xml) the "hashValue" attribute
     */
    @Override
    public void xsetHashValue(org.apache.xmlbeans.XmlBase64Binary hashValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(hashValue);
        }
    }

    /**
     * Unsets the "hashValue" attribute
     */
    @Override
    public void unsetHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "saltValue" attribute
     */
    @Override
    public byte[] getSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "saltValue" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "saltValue" attribute
     */
    @Override
    public boolean isSetSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "saltValue" attribute
     */
    @Override
    public void setSaltValue(byte[] saltValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setByteArrayValue(saltValue);
        }
    }

    /**
     * Sets (as xml) the "saltValue" attribute
     */
    @Override
    public void xsetSaltValue(org.apache.xmlbeans.XmlBase64Binary saltValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(saltValue);
        }
    }

    /**
     * Unsets the "saltValue" attribute
     */
    @Override
    public void unsetSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "spinCount" attribute
     */
    @Override
    public long getSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "spinCount" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "spinCount" attribute
     */
    @Override
    public boolean isSetSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "spinCount" attribute
     */
    @Override
    public void setSpinCount(long spinCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setLongValue(spinCount);
        }
    }

    /**
     * Sets (as xml) the "spinCount" attribute
     */
    @Override
    public void xsetSpinCount(org.apache.xmlbeans.XmlUnsignedInt spinCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(spinCount);
        }
    }

    /**
     * Unsets the "spinCount" attribute
     */
    @Override
    public void unsetSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }
}
