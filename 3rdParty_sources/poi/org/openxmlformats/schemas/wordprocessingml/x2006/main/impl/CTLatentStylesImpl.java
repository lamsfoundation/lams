/*
 * XML Type:  CT_LatentStyles
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_LatentStyles(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTLatentStylesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles {
    private static final long serialVersionUID = 1L;

    public CTLatentStylesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lsdException"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "defLockedState"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "defUIPriority"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "defSemiHidden"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "defUnhideWhenUsed"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "defQFormat"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "count"),
    };


    /**
     * Gets a List of "lsdException" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException> getLsdExceptionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLsdExceptionArray,
                this::setLsdExceptionArray,
                this::insertNewLsdException,
                this::removeLsdException,
                this::sizeOfLsdExceptionArray
            );
        }
    }

    /**
     * Gets array of all "lsdException" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException[] getLsdExceptionArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException[0]);
    }

    /**
     * Gets ith "lsdException" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException getLsdExceptionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lsdException" element
     */
    @Override
    public int sizeOfLsdExceptionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "lsdException" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLsdExceptionArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException[] lsdExceptionArray) {
        check_orphaned();
        arraySetterHelper(lsdExceptionArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "lsdException" element
     */
    @Override
    public void setLsdExceptionArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException lsdException) {
        generatedSetterHelperImpl(lsdException, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lsdException" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException insertNewLsdException(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lsdException" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException addNewLsdException() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "lsdException" element
     */
    @Override
    public void removeLsdException(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "defLockedState" attribute
     */
    @Override
    public java.lang.Object getDefLockedState() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "defLockedState" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDefLockedState() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "defLockedState" attribute
     */
    @Override
    public boolean isSetDefLockedState() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "defLockedState" attribute
     */
    @Override
    public void setDefLockedState(java.lang.Object defLockedState) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(defLockedState);
        }
    }

    /**
     * Sets (as xml) the "defLockedState" attribute
     */
    @Override
    public void xsetDefLockedState(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff defLockedState) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(defLockedState);
        }
    }

    /**
     * Unsets the "defLockedState" attribute
     */
    @Override
    public void unsetDefLockedState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "defUIPriority" attribute
     */
    @Override
    public java.math.BigInteger getDefUIPriority() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "defUIPriority" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetDefUIPriority() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "defUIPriority" attribute
     */
    @Override
    public boolean isSetDefUIPriority() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "defUIPriority" attribute
     */
    @Override
    public void setDefUIPriority(java.math.BigInteger defUIPriority) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBigIntegerValue(defUIPriority);
        }
    }

    /**
     * Sets (as xml) the "defUIPriority" attribute
     */
    @Override
    public void xsetDefUIPriority(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber defUIPriority) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(defUIPriority);
        }
    }

    /**
     * Unsets the "defUIPriority" attribute
     */
    @Override
    public void unsetDefUIPriority() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "defSemiHidden" attribute
     */
    @Override
    public java.lang.Object getDefSemiHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "defSemiHidden" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDefSemiHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "defSemiHidden" attribute
     */
    @Override
    public boolean isSetDefSemiHidden() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "defSemiHidden" attribute
     */
    @Override
    public void setDefSemiHidden(java.lang.Object defSemiHidden) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(defSemiHidden);
        }
    }

    /**
     * Sets (as xml) the "defSemiHidden" attribute
     */
    @Override
    public void xsetDefSemiHidden(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff defSemiHidden) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(defSemiHidden);
        }
    }

    /**
     * Unsets the "defSemiHidden" attribute
     */
    @Override
    public void unsetDefSemiHidden() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "defUnhideWhenUsed" attribute
     */
    @Override
    public java.lang.Object getDefUnhideWhenUsed() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "defUnhideWhenUsed" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDefUnhideWhenUsed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "defUnhideWhenUsed" attribute
     */
    @Override
    public boolean isSetDefUnhideWhenUsed() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "defUnhideWhenUsed" attribute
     */
    @Override
    public void setDefUnhideWhenUsed(java.lang.Object defUnhideWhenUsed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setObjectValue(defUnhideWhenUsed);
        }
    }

    /**
     * Sets (as xml) the "defUnhideWhenUsed" attribute
     */
    @Override
    public void xsetDefUnhideWhenUsed(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff defUnhideWhenUsed) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(defUnhideWhenUsed);
        }
    }

    /**
     * Unsets the "defUnhideWhenUsed" attribute
     */
    @Override
    public void unsetDefUnhideWhenUsed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "defQFormat" attribute
     */
    @Override
    public java.lang.Object getDefQFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "defQFormat" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDefQFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "defQFormat" attribute
     */
    @Override
    public boolean isSetDefQFormat() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "defQFormat" attribute
     */
    @Override
    public void setDefQFormat(java.lang.Object defQFormat) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setObjectValue(defQFormat);
        }
    }

    /**
     * Sets (as xml) the "defQFormat" attribute
     */
    @Override
    public void xsetDefQFormat(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff defQFormat) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(defQFormat);
        }
    }

    /**
     * Unsets the "defQFormat" attribute
     */
    @Override
    public void unsetDefQFormat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "count" attribute
     */
    @Override
    public java.math.BigInteger getCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "count" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "count" attribute
     */
    @Override
    public boolean isSetCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "count" attribute
     */
    @Override
    public void setCount(java.math.BigInteger count) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBigIntegerValue(count);
        }
    }

    /**
     * Sets (as xml) the "count" attribute
     */
    @Override
    public void xsetCount(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber count) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(count);
        }
    }

    /**
     * Unsets the "count" attribute
     */
    @Override
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}
