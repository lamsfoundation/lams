/*
 * XML Type:  CT_FldChar
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FldChar(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFldCharImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar {
    private static final long serialVersionUID = 1L;

    public CTFldCharImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fldData"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ffData"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numberingChange"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fldCharType"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fldLock"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dirty"),
    };


    /**
     * Gets the "fldData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText getFldData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fldData" element
     */
    @Override
    public boolean isSetFldData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "fldData" element
     */
    @Override
    public void setFldData(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText fldData) {
        generatedSetterHelperImpl(fldData, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fldData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText addNewFldData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "fldData" element
     */
    @Override
    public void unsetFldData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "ffData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData getFfData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ffData" element
     */
    @Override
    public boolean isSetFfData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "ffData" element
     */
    @Override
    public void setFfData(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData ffData) {
        generatedSetterHelperImpl(ffData, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ffData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData addNewFfData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "ffData" element
     */
    @Override
    public void unsetFfData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "numberingChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering getNumberingChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numberingChange" element
     */
    @Override
    public boolean isSetNumberingChange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "numberingChange" element
     */
    @Override
    public void setNumberingChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering numberingChange) {
        generatedSetterHelperImpl(numberingChange, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numberingChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering addNewNumberingChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "numberingChange" element
     */
    @Override
    public void unsetNumberingChange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "fldCharType" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType.Enum getFldCharType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "fldCharType" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType xgetFldCharType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "fldCharType" attribute
     */
    @Override
    public void setFldCharType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType.Enum fldCharType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(fldCharType);
        }
    }

    /**
     * Sets (as xml) the "fldCharType" attribute
     */
    @Override
    public void xsetFldCharType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType fldCharType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(fldCharType);
        }
    }

    /**
     * Gets the "fldLock" attribute
     */
    @Override
    public java.lang.Object getFldLock() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "fldLock" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetFldLock() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "fldLock" attribute
     */
    @Override
    public boolean isSetFldLock() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "fldLock" attribute
     */
    @Override
    public void setFldLock(java.lang.Object fldLock) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setObjectValue(fldLock);
        }
    }

    /**
     * Sets (as xml) the "fldLock" attribute
     */
    @Override
    public void xsetFldLock(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff fldLock) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(fldLock);
        }
    }

    /**
     * Unsets the "fldLock" attribute
     */
    @Override
    public void unsetFldLock() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "dirty" attribute
     */
    @Override
    public java.lang.Object getDirty() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "dirty" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "dirty" attribute
     */
    @Override
    public boolean isSetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "dirty" attribute
     */
    @Override
    public void setDirty(java.lang.Object dirty) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setObjectValue(dirty);
        }
    }

    /**
     * Sets (as xml) the "dirty" attribute
     */
    @Override
    public void xsetDirty(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff dirty) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(dirty);
        }
    }

    /**
     * Unsets the "dirty" attribute
     */
    @Override
    public void unsetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }
}
