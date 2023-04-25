/*
 * XML Type:  CT_FillStyleList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FillStyleList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFillStyleListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList {
    private static final long serialVersionUID = 1L;

    public CTFillStyleListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "noFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "solidFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gradFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blipFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pattFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "grpFill"),
    };


    /**
     * Gets a List of "noFill" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties> getNoFillList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getNoFillArray,
                this::setNoFillArray,
                this::insertNewNoFill,
                this::removeNoFill,
                this::sizeOfNoFillArray
            );
        }
    }

    /**
     * Gets array of all "noFill" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties[] getNoFillArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties[0]);
    }

    /**
     * Gets ith "noFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties getNoFillArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "noFill" element
     */
    @Override
    public int sizeOfNoFillArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "noFill" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setNoFillArray(org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties[] noFillArray) {
        check_orphaned();
        arraySetterHelper(noFillArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "noFill" element
     */
    @Override
    public void setNoFillArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties noFill) {
        generatedSetterHelperImpl(noFill, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "noFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties insertNewNoFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "noFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties addNewNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "noFill" element
     */
    @Override
    public void removeNoFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "solidFill" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties> getSolidFillList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSolidFillArray,
                this::setSolidFillArray,
                this::insertNewSolidFill,
                this::removeSolidFill,
                this::sizeOfSolidFillArray
            );
        }
    }

    /**
     * Gets array of all "solidFill" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties[] getSolidFillArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties[0]);
    }

    /**
     * Gets ith "solidFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties getSolidFillArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "solidFill" element
     */
    @Override
    public int sizeOfSolidFillArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "solidFill" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSolidFillArray(org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties[] solidFillArray) {
        check_orphaned();
        arraySetterHelper(solidFillArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "solidFill" element
     */
    @Override
    public void setSolidFillArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties solidFill) {
        generatedSetterHelperImpl(solidFill, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "solidFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties insertNewSolidFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "solidFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties addNewSolidFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "solidFill" element
     */
    @Override
    public void removeSolidFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "gradFill" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties> getGradFillList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGradFillArray,
                this::setGradFillArray,
                this::insertNewGradFill,
                this::removeGradFill,
                this::sizeOfGradFillArray
            );
        }
    }

    /**
     * Gets array of all "gradFill" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties[] getGradFillArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties[0]);
    }

    /**
     * Gets ith "gradFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties getGradFillArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "gradFill" element
     */
    @Override
    public int sizeOfGradFillArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "gradFill" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGradFillArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties[] gradFillArray) {
        check_orphaned();
        arraySetterHelper(gradFillArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "gradFill" element
     */
    @Override
    public void setGradFillArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties gradFill) {
        generatedSetterHelperImpl(gradFill, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gradFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties insertNewGradFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "gradFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties addNewGradFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "gradFill" element
     */
    @Override
    public void removeGradFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "blipFill" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties> getBlipFillList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBlipFillArray,
                this::setBlipFillArray,
                this::insertNewBlipFill,
                this::removeBlipFill,
                this::sizeOfBlipFillArray
            );
        }
    }

    /**
     * Gets array of all "blipFill" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties[] getBlipFillArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties[0]);
    }

    /**
     * Gets ith "blipFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties getBlipFillArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "blipFill" element
     */
    @Override
    public int sizeOfBlipFillArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "blipFill" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBlipFillArray(org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties[] blipFillArray) {
        check_orphaned();
        arraySetterHelper(blipFillArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "blipFill" element
     */
    @Override
    public void setBlipFillArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties blipFill) {
        generatedSetterHelperImpl(blipFill, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "blipFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties insertNewBlipFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "blipFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties addNewBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "blipFill" element
     */
    @Override
    public void removeBlipFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "pattFill" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties> getPattFillList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPattFillArray,
                this::setPattFillArray,
                this::insertNewPattFill,
                this::removePattFill,
                this::sizeOfPattFillArray
            );
        }
    }

    /**
     * Gets array of all "pattFill" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties[] getPattFillArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties[0]);
    }

    /**
     * Gets ith "pattFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties getPattFillArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pattFill" element
     */
    @Override
    public int sizeOfPattFillArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "pattFill" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPattFillArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties[] pattFillArray) {
        check_orphaned();
        arraySetterHelper(pattFillArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "pattFill" element
     */
    @Override
    public void setPattFillArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties pattFill) {
        generatedSetterHelperImpl(pattFill, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pattFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties insertNewPattFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pattFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties addNewPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "pattFill" element
     */
    @Override
    public void removePattFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "grpFill" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties> getGrpFillList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGrpFillArray,
                this::setGrpFillArray,
                this::insertNewGrpFill,
                this::removeGrpFill,
                this::sizeOfGrpFillArray
            );
        }
    }

    /**
     * Gets array of all "grpFill" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties[] getGrpFillArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties[0]);
    }

    /**
     * Gets ith "grpFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties getGrpFillArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "grpFill" element
     */
    @Override
    public int sizeOfGrpFillArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "grpFill" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGrpFillArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties[] grpFillArray) {
        check_orphaned();
        arraySetterHelper(grpFillArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "grpFill" element
     */
    @Override
    public void setGrpFillArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties grpFill) {
        generatedSetterHelperImpl(grpFill, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "grpFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties insertNewGrpFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "grpFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties addNewGrpFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "grpFill" element
     */
    @Override
    public void removeGrpFill(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }
}
