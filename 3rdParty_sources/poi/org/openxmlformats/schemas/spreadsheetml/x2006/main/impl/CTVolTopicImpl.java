/*
 * XML Type:  CT_VolTopic
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_VolTopic(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTVolTopicImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic {
    private static final long serialVersionUID = 1L;

    public CTVolTopicImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "v"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "stp"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "tr"),
        new QName("", "t"),
    };


    /**
     * Gets the "v" element
     */
    @Override
    public java.lang.String getV() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "v" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetV() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "v" element
     */
    @Override
    public void setV(java.lang.String v) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(v);
        }
    }

    /**
     * Sets (as xml) the "v" element
     */
    @Override
    public void xsetV(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring v) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(v);
        }
    }

    /**
     * Gets a List of "stp" elements
     */
    @Override
    public java.util.List<java.lang.String> getStpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getStpArray,
                this::setStpArray,
                this::insertStp,
                this::removeStp,
                this::sizeOfStpArray
            );
        }
    }

    /**
     * Gets array of all "stp" elements
     */
    @Override
    public java.lang.String[] getStpArray() {
        return getObjectArray(PROPERTY_QNAME[1], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "stp" element
     */
    @Override
    public java.lang.String getStpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "stp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring> xgetStpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetStpArray,
                this::xsetStpArray,
                this::insertNewStp,
                this::removeStp,
                this::sizeOfStpArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "stp" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[] xgetStpArray() {
        return xgetArray(PROPERTY_QNAME[1], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[]::new);
    }

    /**
     * Gets (as xml) ith "stp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetStpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "stp" element
     */
    @Override
    public int sizeOfStpArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "stp" element
     */
    @Override
    public void setStpArray(java.lang.String[] stpArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(stpArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets ith "stp" element
     */
    @Override
    public void setStpArray(int i, java.lang.String stp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(stp);
        }
    }

    /**
     * Sets (as xml) array of all "stp" element
     */
    @Override
    public void xsetStpArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[]stpArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(stpArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets (as xml) ith "stp" element
     */
    @Override
    public void xsetStpArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring stp) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(stp);
        }
    }

    /**
     * Inserts the value as the ith "stp" element
     */
    @Override
    public void insertStp(int i, java.lang.String stp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            target.setStringValue(stp);
        }
    }

    /**
     * Appends the value as the last "stp" element
     */
    @Override
    public void addStp(java.lang.String stp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            target.setStringValue(stp);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "stp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring insertNewStp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "stp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring addNewStp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "stp" element
     */
    @Override
    public void removeStp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "tr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef> getTrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTrArray,
                this::setTrArray,
                this::insertNewTr,
                this::removeTr,
                this::sizeOfTrArray
            );
        }
    }

    /**
     * Gets array of all "tr" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef[] getTrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef[0]);
    }

    /**
     * Gets ith "tr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef getTrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tr" element
     */
    @Override
    public int sizeOfTrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "tr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTrArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef[] trArray) {
        check_orphaned();
        arraySetterHelper(trArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "tr" element
     */
    @Override
    public void setTrArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef tr) {
        generatedSetterHelperImpl(tr, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef insertNewTr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef addNewTr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "tr" element
     */
    @Override
    public void removeTr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets the "t" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType.Enum getT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "t" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType xgetT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "t" attribute
     */
    @Override
    public boolean isSetT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "t" attribute
     */
    @Override
    public void setT(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType.Enum t) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(t);
        }
    }

    /**
     * Sets (as xml) the "t" attribute
     */
    @Override
    public void xsetT(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType t) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(t);
        }
    }

    /**
     * Unsets the "t" attribute
     */
    @Override
    public void unsetT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
