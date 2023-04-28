/*
 * XML Type:  CT_ModifyVerifier
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ModifyVerifier(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTModifyVerifierImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier {
    private static final long serialVersionUID = 1L;

    public CTModifyVerifierImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "algorithmName"),
        new QName("", "hashValue"),
        new QName("", "saltValue"),
        new QName("", "spinValue"),
        new QName("", "cryptProviderType"),
        new QName("", "cryptAlgorithmClass"),
        new QName("", "cryptAlgorithmType"),
        new QName("", "cryptAlgorithmSid"),
        new QName("", "spinCount"),
        new QName("", "saltData"),
        new QName("", "hashData"),
        new QName("", "cryptProvider"),
        new QName("", "algIdExt"),
        new QName("", "algIdExtSource"),
        new QName("", "cryptProviderTypeExt"),
        new QName("", "cryptProviderTypeExtSource"),
    };


    /**
     * Gets the "algorithmName" attribute
     */
    @Override
    public java.lang.String getAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "algorithmName" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(algorithmName);
        }
    }

    /**
     * Sets (as xml) the "algorithmName" attribute
     */
    @Override
    public void xsetAlgorithmName(org.apache.xmlbeans.XmlString algorithmName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[0]);
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
            get_store().remove_attribute(PROPERTY_QNAME[0]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[1]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            get_store().remove_attribute(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[2]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[2]);
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
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "spinValue" attribute
     */
    @Override
    public long getSpinValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "spinValue" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetSpinValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "spinValue" attribute
     */
    @Override
    public boolean isSetSpinValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "spinValue" attribute
     */
    @Override
    public void setSpinValue(long spinValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setLongValue(spinValue);
        }
    }

    /**
     * Sets (as xml) the "spinValue" attribute
     */
    @Override
    public void xsetSpinValue(org.apache.xmlbeans.XmlUnsignedInt spinValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(spinValue);
        }
    }

    /**
     * Unsets the "spinValue" attribute
     */
    @Override
    public void unsetSpinValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "cryptProviderType" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv.Enum getCryptProviderType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "cryptProviderType" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv xgetCryptProviderType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "cryptProviderType" attribute
     */
    @Override
    public boolean isSetCryptProviderType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "cryptProviderType" attribute
     */
    @Override
    public void setCryptProviderType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv.Enum cryptProviderType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(cryptProviderType);
        }
    }

    /**
     * Sets (as xml) the "cryptProviderType" attribute
     */
    @Override
    public void xsetCryptProviderType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv cryptProviderType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(cryptProviderType);
        }
    }

    /**
     * Unsets the "cryptProviderType" attribute
     */
    @Override
    public void unsetCryptProviderType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "cryptAlgorithmClass" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass.Enum getCryptAlgorithmClass() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "cryptAlgorithmClass" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass xgetCryptAlgorithmClass() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "cryptAlgorithmClass" attribute
     */
    @Override
    public boolean isSetCryptAlgorithmClass() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "cryptAlgorithmClass" attribute
     */
    @Override
    public void setCryptAlgorithmClass(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass.Enum cryptAlgorithmClass) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(cryptAlgorithmClass);
        }
    }

    /**
     * Sets (as xml) the "cryptAlgorithmClass" attribute
     */
    @Override
    public void xsetCryptAlgorithmClass(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass cryptAlgorithmClass) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(cryptAlgorithmClass);
        }
    }

    /**
     * Unsets the "cryptAlgorithmClass" attribute
     */
    @Override
    public void unsetCryptAlgorithmClass() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "cryptAlgorithmType" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType.Enum getCryptAlgorithmType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "cryptAlgorithmType" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType xgetCryptAlgorithmType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "cryptAlgorithmType" attribute
     */
    @Override
    public boolean isSetCryptAlgorithmType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "cryptAlgorithmType" attribute
     */
    @Override
    public void setCryptAlgorithmType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType.Enum cryptAlgorithmType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(cryptAlgorithmType);
        }
    }

    /**
     * Sets (as xml) the "cryptAlgorithmType" attribute
     */
    @Override
    public void xsetCryptAlgorithmType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType cryptAlgorithmType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(cryptAlgorithmType);
        }
    }

    /**
     * Unsets the "cryptAlgorithmType" attribute
     */
    @Override
    public void unsetCryptAlgorithmType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "cryptAlgorithmSid" attribute
     */
    @Override
    public long getCryptAlgorithmSid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "cryptAlgorithmSid" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCryptAlgorithmSid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "cryptAlgorithmSid" attribute
     */
    @Override
    public boolean isSetCryptAlgorithmSid() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "cryptAlgorithmSid" attribute
     */
    @Override
    public void setCryptAlgorithmSid(long cryptAlgorithmSid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setLongValue(cryptAlgorithmSid);
        }
    }

    /**
     * Sets (as xml) the "cryptAlgorithmSid" attribute
     */
    @Override
    public void xsetCryptAlgorithmSid(org.apache.xmlbeans.XmlUnsignedInt cryptAlgorithmSid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(cryptAlgorithmSid);
        }
    }

    /**
     * Unsets the "cryptAlgorithmSid" attribute
     */
    @Override
    public void unsetCryptAlgorithmSid() {
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

    /**
     * Gets the "saltData" attribute
     */
    @Override
    public byte[] getSaltData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "saltData" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetSaltData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "saltData" attribute
     */
    @Override
    public boolean isSetSaltData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "saltData" attribute
     */
    @Override
    public void setSaltData(byte[] saltData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setByteArrayValue(saltData);
        }
    }

    /**
     * Sets (as xml) the "saltData" attribute
     */
    @Override
    public void xsetSaltData(org.apache.xmlbeans.XmlBase64Binary saltData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(saltData);
        }
    }

    /**
     * Unsets the "saltData" attribute
     */
    @Override
    public void unsetSaltData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "hashData" attribute
     */
    @Override
    public byte[] getHashData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "hashData" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetHashData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "hashData" attribute
     */
    @Override
    public boolean isSetHashData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "hashData" attribute
     */
    @Override
    public void setHashData(byte[] hashData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setByteArrayValue(hashData);
        }
    }

    /**
     * Sets (as xml) the "hashData" attribute
     */
    @Override
    public void xsetHashData(org.apache.xmlbeans.XmlBase64Binary hashData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(hashData);
        }
    }

    /**
     * Unsets the "hashData" attribute
     */
    @Override
    public void unsetHashData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "cryptProvider" attribute
     */
    @Override
    public java.lang.String getCryptProvider() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "cryptProvider" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetCryptProvider() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "cryptProvider" attribute
     */
    @Override
    public boolean isSetCryptProvider() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "cryptProvider" attribute
     */
    @Override
    public void setCryptProvider(java.lang.String cryptProvider) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setStringValue(cryptProvider);
        }
    }

    /**
     * Sets (as xml) the "cryptProvider" attribute
     */
    @Override
    public void xsetCryptProvider(org.apache.xmlbeans.XmlString cryptProvider) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(cryptProvider);
        }
    }

    /**
     * Unsets the "cryptProvider" attribute
     */
    @Override
    public void unsetCryptProvider() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "algIdExt" attribute
     */
    @Override
    public long getAlgIdExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "algIdExt" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetAlgIdExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "algIdExt" attribute
     */
    @Override
    public boolean isSetAlgIdExt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "algIdExt" attribute
     */
    @Override
    public void setAlgIdExt(long algIdExt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setLongValue(algIdExt);
        }
    }

    /**
     * Sets (as xml) the "algIdExt" attribute
     */
    @Override
    public void xsetAlgIdExt(org.apache.xmlbeans.XmlUnsignedInt algIdExt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(algIdExt);
        }
    }

    /**
     * Unsets the "algIdExt" attribute
     */
    @Override
    public void unsetAlgIdExt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "algIdExtSource" attribute
     */
    @Override
    public java.lang.String getAlgIdExtSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "algIdExtSource" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetAlgIdExtSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * True if has "algIdExtSource" attribute
     */
    @Override
    public boolean isSetAlgIdExtSource() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "algIdExtSource" attribute
     */
    @Override
    public void setAlgIdExtSource(java.lang.String algIdExtSource) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setStringValue(algIdExtSource);
        }
    }

    /**
     * Sets (as xml) the "algIdExtSource" attribute
     */
    @Override
    public void xsetAlgIdExtSource(org.apache.xmlbeans.XmlString algIdExtSource) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(algIdExtSource);
        }
    }

    /**
     * Unsets the "algIdExtSource" attribute
     */
    @Override
    public void unsetAlgIdExtSource() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "cryptProviderTypeExt" attribute
     */
    @Override
    public long getCryptProviderTypeExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "cryptProviderTypeExt" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCryptProviderTypeExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * True if has "cryptProviderTypeExt" attribute
     */
    @Override
    public boolean isSetCryptProviderTypeExt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "cryptProviderTypeExt" attribute
     */
    @Override
    public void setCryptProviderTypeExt(long cryptProviderTypeExt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setLongValue(cryptProviderTypeExt);
        }
    }

    /**
     * Sets (as xml) the "cryptProviderTypeExt" attribute
     */
    @Override
    public void xsetCryptProviderTypeExt(org.apache.xmlbeans.XmlUnsignedInt cryptProviderTypeExt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(cryptProviderTypeExt);
        }
    }

    /**
     * Unsets the "cryptProviderTypeExt" attribute
     */
    @Override
    public void unsetCryptProviderTypeExt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "cryptProviderTypeExtSource" attribute
     */
    @Override
    public java.lang.String getCryptProviderTypeExtSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "cryptProviderTypeExtSource" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetCryptProviderTypeExtSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * True if has "cryptProviderTypeExtSource" attribute
     */
    @Override
    public boolean isSetCryptProviderTypeExtSource() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "cryptProviderTypeExtSource" attribute
     */
    @Override
    public void setCryptProviderTypeExtSource(java.lang.String cryptProviderTypeExtSource) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setStringValue(cryptProviderTypeExtSource);
        }
    }

    /**
     * Sets (as xml) the "cryptProviderTypeExtSource" attribute
     */
    @Override
    public void xsetCryptProviderTypeExtSource(org.apache.xmlbeans.XmlString cryptProviderTypeExtSource) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(cryptProviderTypeExtSource);
        }
    }

    /**
     * Unsets the "cryptProviderTypeExtSource" attribute
     */
    @Override
    public void unsetCryptProviderTypeExtSource() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }
}
