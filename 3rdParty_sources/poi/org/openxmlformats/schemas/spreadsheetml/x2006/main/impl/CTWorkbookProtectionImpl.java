/*
 * XML Type:  CT_WorkbookProtection
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_WorkbookProtection(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTWorkbookProtectionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection {
    private static final long serialVersionUID = 1L;

    public CTWorkbookProtectionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "workbookPassword"),
        new QName("", "workbookPasswordCharacterSet"),
        new QName("", "revisionsPassword"),
        new QName("", "revisionsPasswordCharacterSet"),
        new QName("", "lockStructure"),
        new QName("", "lockWindows"),
        new QName("", "lockRevision"),
        new QName("", "revisionsAlgorithmName"),
        new QName("", "revisionsHashValue"),
        new QName("", "revisionsSaltValue"),
        new QName("", "revisionsSpinCount"),
        new QName("", "workbookAlgorithmName"),
        new QName("", "workbookHashValue"),
        new QName("", "workbookSaltValue"),
        new QName("", "workbookSpinCount"),
    };


    /**
     * Gets the "workbookPassword" attribute
     */
    @Override
    public byte[] getWorkbookPassword() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "workbookPassword" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex xgetWorkbookPassword() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "workbookPassword" attribute
     */
    @Override
    public boolean isSetWorkbookPassword() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "workbookPassword" attribute
     */
    @Override
    public void setWorkbookPassword(byte[] workbookPassword) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setByteArrayValue(workbookPassword);
        }
    }

    /**
     * Sets (as xml) the "workbookPassword" attribute
     */
    @Override
    public void xsetWorkbookPassword(org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex workbookPassword) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(workbookPassword);
        }
    }

    /**
     * Unsets the "workbookPassword" attribute
     */
    @Override
    public void unsetWorkbookPassword() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "workbookPasswordCharacterSet" attribute
     */
    @Override
    public java.lang.String getWorkbookPasswordCharacterSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "workbookPasswordCharacterSet" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetWorkbookPasswordCharacterSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "workbookPasswordCharacterSet" attribute
     */
    @Override
    public boolean isSetWorkbookPasswordCharacterSet() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "workbookPasswordCharacterSet" attribute
     */
    @Override
    public void setWorkbookPasswordCharacterSet(java.lang.String workbookPasswordCharacterSet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(workbookPasswordCharacterSet);
        }
    }

    /**
     * Sets (as xml) the "workbookPasswordCharacterSet" attribute
     */
    @Override
    public void xsetWorkbookPasswordCharacterSet(org.apache.xmlbeans.XmlString workbookPasswordCharacterSet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(workbookPasswordCharacterSet);
        }
    }

    /**
     * Unsets the "workbookPasswordCharacterSet" attribute
     */
    @Override
    public void unsetWorkbookPasswordCharacterSet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "revisionsPassword" attribute
     */
    @Override
    public byte[] getRevisionsPassword() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "revisionsPassword" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex xgetRevisionsPassword() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "revisionsPassword" attribute
     */
    @Override
    public boolean isSetRevisionsPassword() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "revisionsPassword" attribute
     */
    @Override
    public void setRevisionsPassword(byte[] revisionsPassword) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setByteArrayValue(revisionsPassword);
        }
    }

    /**
     * Sets (as xml) the "revisionsPassword" attribute
     */
    @Override
    public void xsetRevisionsPassword(org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex revisionsPassword) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(revisionsPassword);
        }
    }

    /**
     * Unsets the "revisionsPassword" attribute
     */
    @Override
    public void unsetRevisionsPassword() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "revisionsPasswordCharacterSet" attribute
     */
    @Override
    public java.lang.String getRevisionsPasswordCharacterSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "revisionsPasswordCharacterSet" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetRevisionsPasswordCharacterSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "revisionsPasswordCharacterSet" attribute
     */
    @Override
    public boolean isSetRevisionsPasswordCharacterSet() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "revisionsPasswordCharacterSet" attribute
     */
    @Override
    public void setRevisionsPasswordCharacterSet(java.lang.String revisionsPasswordCharacterSet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(revisionsPasswordCharacterSet);
        }
    }

    /**
     * Sets (as xml) the "revisionsPasswordCharacterSet" attribute
     */
    @Override
    public void xsetRevisionsPasswordCharacterSet(org.apache.xmlbeans.XmlString revisionsPasswordCharacterSet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(revisionsPasswordCharacterSet);
        }
    }

    /**
     * Unsets the "revisionsPasswordCharacterSet" attribute
     */
    @Override
    public void unsetRevisionsPasswordCharacterSet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "lockStructure" attribute
     */
    @Override
    public boolean getLockStructure() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "lockStructure" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetLockStructure() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "lockStructure" attribute
     */
    @Override
    public boolean isSetLockStructure() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "lockStructure" attribute
     */
    @Override
    public void setLockStructure(boolean lockStructure) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(lockStructure);
        }
    }

    /**
     * Sets (as xml) the "lockStructure" attribute
     */
    @Override
    public void xsetLockStructure(org.apache.xmlbeans.XmlBoolean lockStructure) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(lockStructure);
        }
    }

    /**
     * Unsets the "lockStructure" attribute
     */
    @Override
    public void unsetLockStructure() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "lockWindows" attribute
     */
    @Override
    public boolean getLockWindows() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "lockWindows" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetLockWindows() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "lockWindows" attribute
     */
    @Override
    public boolean isSetLockWindows() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "lockWindows" attribute
     */
    @Override
    public void setLockWindows(boolean lockWindows) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setBooleanValue(lockWindows);
        }
    }

    /**
     * Sets (as xml) the "lockWindows" attribute
     */
    @Override
    public void xsetLockWindows(org.apache.xmlbeans.XmlBoolean lockWindows) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(lockWindows);
        }
    }

    /**
     * Unsets the "lockWindows" attribute
     */
    @Override
    public void unsetLockWindows() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "lockRevision" attribute
     */
    @Override
    public boolean getLockRevision() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "lockRevision" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetLockRevision() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "lockRevision" attribute
     */
    @Override
    public boolean isSetLockRevision() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "lockRevision" attribute
     */
    @Override
    public void setLockRevision(boolean lockRevision) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBooleanValue(lockRevision);
        }
    }

    /**
     * Sets (as xml) the "lockRevision" attribute
     */
    @Override
    public void xsetLockRevision(org.apache.xmlbeans.XmlBoolean lockRevision) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(lockRevision);
        }
    }

    /**
     * Unsets the "lockRevision" attribute
     */
    @Override
    public void unsetLockRevision() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "revisionsAlgorithmName" attribute
     */
    @Override
    public java.lang.String getRevisionsAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "revisionsAlgorithmName" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetRevisionsAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "revisionsAlgorithmName" attribute
     */
    @Override
    public boolean isSetRevisionsAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "revisionsAlgorithmName" attribute
     */
    @Override
    public void setRevisionsAlgorithmName(java.lang.String revisionsAlgorithmName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setStringValue(revisionsAlgorithmName);
        }
    }

    /**
     * Sets (as xml) the "revisionsAlgorithmName" attribute
     */
    @Override
    public void xsetRevisionsAlgorithmName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring revisionsAlgorithmName) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(revisionsAlgorithmName);
        }
    }

    /**
     * Unsets the "revisionsAlgorithmName" attribute
     */
    @Override
    public void unsetRevisionsAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "revisionsHashValue" attribute
     */
    @Override
    public byte[] getRevisionsHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "revisionsHashValue" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetRevisionsHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "revisionsHashValue" attribute
     */
    @Override
    public boolean isSetRevisionsHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "revisionsHashValue" attribute
     */
    @Override
    public void setRevisionsHashValue(byte[] revisionsHashValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setByteArrayValue(revisionsHashValue);
        }
    }

    /**
     * Sets (as xml) the "revisionsHashValue" attribute
     */
    @Override
    public void xsetRevisionsHashValue(org.apache.xmlbeans.XmlBase64Binary revisionsHashValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(revisionsHashValue);
        }
    }

    /**
     * Unsets the "revisionsHashValue" attribute
     */
    @Override
    public void unsetRevisionsHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "revisionsSaltValue" attribute
     */
    @Override
    public byte[] getRevisionsSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "revisionsSaltValue" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetRevisionsSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "revisionsSaltValue" attribute
     */
    @Override
    public boolean isSetRevisionsSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "revisionsSaltValue" attribute
     */
    @Override
    public void setRevisionsSaltValue(byte[] revisionsSaltValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setByteArrayValue(revisionsSaltValue);
        }
    }

    /**
     * Sets (as xml) the "revisionsSaltValue" attribute
     */
    @Override
    public void xsetRevisionsSaltValue(org.apache.xmlbeans.XmlBase64Binary revisionsSaltValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(revisionsSaltValue);
        }
    }

    /**
     * Unsets the "revisionsSaltValue" attribute
     */
    @Override
    public void unsetRevisionsSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "revisionsSpinCount" attribute
     */
    @Override
    public long getRevisionsSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "revisionsSpinCount" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetRevisionsSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "revisionsSpinCount" attribute
     */
    @Override
    public boolean isSetRevisionsSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "revisionsSpinCount" attribute
     */
    @Override
    public void setRevisionsSpinCount(long revisionsSpinCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setLongValue(revisionsSpinCount);
        }
    }

    /**
     * Sets (as xml) the "revisionsSpinCount" attribute
     */
    @Override
    public void xsetRevisionsSpinCount(org.apache.xmlbeans.XmlUnsignedInt revisionsSpinCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(revisionsSpinCount);
        }
    }

    /**
     * Unsets the "revisionsSpinCount" attribute
     */
    @Override
    public void unsetRevisionsSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "workbookAlgorithmName" attribute
     */
    @Override
    public java.lang.String getWorkbookAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "workbookAlgorithmName" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetWorkbookAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "workbookAlgorithmName" attribute
     */
    @Override
    public boolean isSetWorkbookAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "workbookAlgorithmName" attribute
     */
    @Override
    public void setWorkbookAlgorithmName(java.lang.String workbookAlgorithmName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setStringValue(workbookAlgorithmName);
        }
    }

    /**
     * Sets (as xml) the "workbookAlgorithmName" attribute
     */
    @Override
    public void xsetWorkbookAlgorithmName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring workbookAlgorithmName) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(workbookAlgorithmName);
        }
    }

    /**
     * Unsets the "workbookAlgorithmName" attribute
     */
    @Override
    public void unsetWorkbookAlgorithmName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "workbookHashValue" attribute
     */
    @Override
    public byte[] getWorkbookHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "workbookHashValue" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetWorkbookHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "workbookHashValue" attribute
     */
    @Override
    public boolean isSetWorkbookHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "workbookHashValue" attribute
     */
    @Override
    public void setWorkbookHashValue(byte[] workbookHashValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setByteArrayValue(workbookHashValue);
        }
    }

    /**
     * Sets (as xml) the "workbookHashValue" attribute
     */
    @Override
    public void xsetWorkbookHashValue(org.apache.xmlbeans.XmlBase64Binary workbookHashValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(workbookHashValue);
        }
    }

    /**
     * Unsets the "workbookHashValue" attribute
     */
    @Override
    public void unsetWorkbookHashValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "workbookSaltValue" attribute
     */
    @Override
    public byte[] getWorkbookSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "workbookSaltValue" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetWorkbookSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * True if has "workbookSaltValue" attribute
     */
    @Override
    public boolean isSetWorkbookSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "workbookSaltValue" attribute
     */
    @Override
    public void setWorkbookSaltValue(byte[] workbookSaltValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setByteArrayValue(workbookSaltValue);
        }
    }

    /**
     * Sets (as xml) the "workbookSaltValue" attribute
     */
    @Override
    public void xsetWorkbookSaltValue(org.apache.xmlbeans.XmlBase64Binary workbookSaltValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(workbookSaltValue);
        }
    }

    /**
     * Unsets the "workbookSaltValue" attribute
     */
    @Override
    public void unsetWorkbookSaltValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "workbookSpinCount" attribute
     */
    @Override
    public long getWorkbookSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "workbookSpinCount" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetWorkbookSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * True if has "workbookSpinCount" attribute
     */
    @Override
    public boolean isSetWorkbookSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "workbookSpinCount" attribute
     */
    @Override
    public void setWorkbookSpinCount(long workbookSpinCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setLongValue(workbookSpinCount);
        }
    }

    /**
     * Sets (as xml) the "workbookSpinCount" attribute
     */
    @Override
    public void xsetWorkbookSpinCount(org.apache.xmlbeans.XmlUnsignedInt workbookSpinCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(workbookSpinCount);
        }
    }

    /**
     * Unsets the "workbookSpinCount" attribute
     */
    @Override
    public void unsetWorkbookSpinCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }
}
