/*
 * XML Type:  CT_SRgbColor
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SRgbColor(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTSRgbColorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor {
    private static final long serialVersionUID = 1L;

    public CTSRgbColorImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tint"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "shade"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "comp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "inv"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gray"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alpha"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaOff"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaMod"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hue"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hueOff"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hueMod"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sat"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "satOff"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "satMod"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lum"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lumOff"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lumMod"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "red"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "redOff"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "redMod"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "green"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "greenOff"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "greenMod"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blue"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blueOff"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blueMod"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gamma"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "invGamma"),
        new QName("", "val"),
    };


    /**
     * Gets a List of "tint" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage> getTintList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTintArray,
                this::setTintArray,
                this::insertNewTint,
                this::removeTint,
                this::sizeOfTintArray
            );
        }
    }

    /**
     * Gets array of all "tint" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage[] getTintArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage[0]);
    }

    /**
     * Gets ith "tint" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage getTintArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tint" element
     */
    @Override
    public int sizeOfTintArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "tint" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTintArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage[] tintArray) {
        check_orphaned();
        arraySetterHelper(tintArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "tint" element
     */
    @Override
    public void setTintArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage tint) {
        generatedSetterHelperImpl(tint, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tint" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage insertNewTint(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tint" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage addNewTint() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "tint" element
     */
    @Override
    public void removeTint(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "shade" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage> getShadeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getShadeArray,
                this::setShadeArray,
                this::insertNewShade,
                this::removeShade,
                this::sizeOfShadeArray
            );
        }
    }

    /**
     * Gets array of all "shade" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage[] getShadeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage[0]);
    }

    /**
     * Gets ith "shade" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage getShadeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "shade" element
     */
    @Override
    public int sizeOfShadeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "shade" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setShadeArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage[] shadeArray) {
        check_orphaned();
        arraySetterHelper(shadeArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "shade" element
     */
    @Override
    public void setShadeArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage shade) {
        generatedSetterHelperImpl(shade, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "shade" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage insertNewShade(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "shade" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage addNewShade() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "shade" element
     */
    @Override
    public void removeShade(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "comp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform> getCompList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCompArray,
                this::setCompArray,
                this::insertNewComp,
                this::removeComp,
                this::sizeOfCompArray
            );
        }
    }

    /**
     * Gets array of all "comp" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform[] getCompArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform[0]);
    }

    /**
     * Gets ith "comp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform getCompArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "comp" element
     */
    @Override
    public int sizeOfCompArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "comp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCompArray(org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform[] compArray) {
        check_orphaned();
        arraySetterHelper(compArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "comp" element
     */
    @Override
    public void setCompArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform comp) {
        generatedSetterHelperImpl(comp, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "comp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform insertNewComp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "comp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform addNewComp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTComplementTransform)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "comp" element
     */
    @Override
    public void removeComp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "inv" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform> getInvList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getInvArray,
                this::setInvArray,
                this::insertNewInv,
                this::removeInv,
                this::sizeOfInvArray
            );
        }
    }

    /**
     * Gets array of all "inv" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform[] getInvArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform[0]);
    }

    /**
     * Gets ith "inv" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform getInvArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "inv" element
     */
    @Override
    public int sizeOfInvArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "inv" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setInvArray(org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform[] invArray) {
        check_orphaned();
        arraySetterHelper(invArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "inv" element
     */
    @Override
    public void setInvArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform inv) {
        generatedSetterHelperImpl(inv, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "inv" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform insertNewInv(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "inv" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform addNewInv() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTInverseTransform)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "inv" element
     */
    @Override
    public void removeInv(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "gray" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform> getGrayList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGrayArray,
                this::setGrayArray,
                this::insertNewGray,
                this::removeGray,
                this::sizeOfGrayArray
            );
        }
    }

    /**
     * Gets array of all "gray" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform[] getGrayArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform[0]);
    }

    /**
     * Gets ith "gray" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform getGrayArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "gray" element
     */
    @Override
    public int sizeOfGrayArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "gray" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGrayArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform[] grayArray) {
        check_orphaned();
        arraySetterHelper(grayArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "gray" element
     */
    @Override
    public void setGrayArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform gray) {
        generatedSetterHelperImpl(gray, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gray" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform insertNewGray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "gray" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform addNewGray() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleTransform)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "gray" element
     */
    @Override
    public void removeGray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "alpha" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage> getAlphaList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlphaArray,
                this::setAlphaArray,
                this::insertNewAlpha,
                this::removeAlpha,
                this::sizeOfAlphaArray
            );
        }
    }

    /**
     * Gets array of all "alpha" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage[] getAlphaArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage[0]);
    }

    /**
     * Gets ith "alpha" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage getAlphaArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alpha" element
     */
    @Override
    public int sizeOfAlphaArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "alpha" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage[] alphaArray) {
        check_orphaned();
        arraySetterHelper(alphaArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "alpha" element
     */
    @Override
    public void setAlphaArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage alpha) {
        generatedSetterHelperImpl(alpha, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alpha" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage insertNewAlpha(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alpha" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage addNewAlpha() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedPercentage)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "alpha" element
     */
    @Override
    public void removeAlpha(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "alphaOff" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage> getAlphaOffList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlphaOffArray,
                this::setAlphaOffArray,
                this::insertNewAlphaOff,
                this::removeAlphaOff,
                this::sizeOfAlphaOffArray
            );
        }
    }

    /**
     * Gets array of all "alphaOff" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage[] getAlphaOffArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage[0]);
    }

    /**
     * Gets ith "alphaOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage getAlphaOffArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alphaOff" element
     */
    @Override
    public int sizeOfAlphaOffArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "alphaOff" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaOffArray(org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage[] alphaOffArray) {
        check_orphaned();
        arraySetterHelper(alphaOffArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "alphaOff" element
     */
    @Override
    public void setAlphaOffArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage alphaOff) {
        generatedSetterHelperImpl(alphaOff, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alphaOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage insertNewAlphaOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alphaOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage addNewAlphaOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFixedPercentage)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "alphaOff" element
     */
    @Override
    public void removeAlphaOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "alphaMod" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage> getAlphaModList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlphaModArray,
                this::setAlphaModArray,
                this::insertNewAlphaMod,
                this::removeAlphaMod,
                this::sizeOfAlphaModArray
            );
        }
    }

    /**
     * Gets array of all "alphaMod" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage[] getAlphaModArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage[0]);
    }

    /**
     * Gets ith "alphaMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage getAlphaModArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alphaMod" element
     */
    @Override
    public int sizeOfAlphaModArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "alphaMod" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaModArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage[] alphaModArray) {
        check_orphaned();
        arraySetterHelper(alphaModArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "alphaMod" element
     */
    @Override
    public void setAlphaModArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage alphaMod) {
        generatedSetterHelperImpl(alphaMod, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alphaMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage insertNewAlphaMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alphaMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage addNewAlphaMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "alphaMod" element
     */
    @Override
    public void removeAlphaMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "hue" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle> getHueList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHueArray,
                this::setHueArray,
                this::insertNewHue,
                this::removeHue,
                this::sizeOfHueArray
            );
        }
    }

    /**
     * Gets array of all "hue" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle[] getHueArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle[0]);
    }

    /**
     * Gets ith "hue" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle getHueArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "hue" element
     */
    @Override
    public int sizeOfHueArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "hue" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHueArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle[] hueArray) {
        check_orphaned();
        arraySetterHelper(hueArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "hue" element
     */
    @Override
    public void setHueArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle hue) {
        generatedSetterHelperImpl(hue, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "hue" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle insertNewHue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "hue" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle addNewHue() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveFixedAngle)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "hue" element
     */
    @Override
    public void removeHue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "hueOff" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAngle> getHueOffList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHueOffArray,
                this::setHueOffArray,
                this::insertNewHueOff,
                this::removeHueOff,
                this::sizeOfHueOffArray
            );
        }
    }

    /**
     * Gets array of all "hueOff" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAngle[] getHueOffArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.drawingml.x2006.main.CTAngle[0]);
    }

    /**
     * Gets ith "hueOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAngle getHueOffArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAngle)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "hueOff" element
     */
    @Override
    public int sizeOfHueOffArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "hueOff" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHueOffArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAngle[] hueOffArray) {
        check_orphaned();
        arraySetterHelper(hueOffArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "hueOff" element
     */
    @Override
    public void setHueOffArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAngle hueOff) {
        generatedSetterHelperImpl(hueOff, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "hueOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAngle insertNewHueOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAngle)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "hueOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAngle addNewHueOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAngle)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "hueOff" element
     */
    @Override
    public void removeHueOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "hueMod" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage> getHueModList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHueModArray,
                this::setHueModArray,
                this::insertNewHueMod,
                this::removeHueMod,
                this::sizeOfHueModArray
            );
        }
    }

    /**
     * Gets array of all "hueMod" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage[] getHueModArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage[0]);
    }

    /**
     * Gets ith "hueMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage getHueModArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "hueMod" element
     */
    @Override
    public int sizeOfHueModArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "hueMod" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHueModArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage[] hueModArray) {
        check_orphaned();
        arraySetterHelper(hueModArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "hueMod" element
     */
    @Override
    public void setHueModArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage hueMod) {
        generatedSetterHelperImpl(hueMod, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "hueMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage insertNewHueMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "hueMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage addNewHueMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositivePercentage)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "hueMod" element
     */
    @Override
    public void removeHueMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "sat" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getSatList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSatArray,
                this::setSatArray,
                this::insertNewSat,
                this::removeSat,
                this::sizeOfSatArray
            );
        }
    }

    /**
     * Gets array of all "sat" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getSatArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "sat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getSatArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sat" element
     */
    @Override
    public int sizeOfSatArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "sat" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSatArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] satArray) {
        check_orphaned();
        arraySetterHelper(satArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "sat" element
     */
    @Override
    public void setSatArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage sat) {
        generatedSetterHelperImpl(sat, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewSat(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewSat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "sat" element
     */
    @Override
    public void removeSat(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "satOff" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getSatOffList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSatOffArray,
                this::setSatOffArray,
                this::insertNewSatOff,
                this::removeSatOff,
                this::sizeOfSatOffArray
            );
        }
    }

    /**
     * Gets array of all "satOff" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getSatOffArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "satOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getSatOffArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "satOff" element
     */
    @Override
    public int sizeOfSatOffArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "satOff" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSatOffArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] satOffArray) {
        check_orphaned();
        arraySetterHelper(satOffArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "satOff" element
     */
    @Override
    public void setSatOffArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage satOff) {
        generatedSetterHelperImpl(satOff, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "satOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewSatOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "satOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewSatOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "satOff" element
     */
    @Override
    public void removeSatOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }

    /**
     * Gets a List of "satMod" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getSatModList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSatModArray,
                this::setSatModArray,
                this::insertNewSatMod,
                this::removeSatMod,
                this::sizeOfSatModArray
            );
        }
    }

    /**
     * Gets array of all "satMod" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getSatModArray() {
        return getXmlObjectArray(PROPERTY_QNAME[13], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "satMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getSatModArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "satMod" element
     */
    @Override
    public int sizeOfSatModArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "satMod" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSatModArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] satModArray) {
        check_orphaned();
        arraySetterHelper(satModArray, PROPERTY_QNAME[13]);
    }

    /**
     * Sets ith "satMod" element
     */
    @Override
    public void setSatModArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage satMod) {
        generatedSetterHelperImpl(satMod, PROPERTY_QNAME[13], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "satMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewSatMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "satMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewSatMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Removes the ith "satMod" element
     */
    @Override
    public void removeSatMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], i);
        }
    }

    /**
     * Gets a List of "lum" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getLumList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLumArray,
                this::setLumArray,
                this::insertNewLum,
                this::removeLum,
                this::sizeOfLumArray
            );
        }
    }

    /**
     * Gets array of all "lum" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getLumArray() {
        return getXmlObjectArray(PROPERTY_QNAME[14], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "lum" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getLumArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lum" element
     */
    @Override
    public int sizeOfLumArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "lum" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLumArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] lumArray) {
        check_orphaned();
        arraySetterHelper(lumArray, PROPERTY_QNAME[14]);
    }

    /**
     * Sets ith "lum" element
     */
    @Override
    public void setLumArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage lum) {
        generatedSetterHelperImpl(lum, PROPERTY_QNAME[14], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lum" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewLum(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lum" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewLum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Removes the ith "lum" element
     */
    @Override
    public void removeLum(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], i);
        }
    }

    /**
     * Gets a List of "lumOff" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getLumOffList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLumOffArray,
                this::setLumOffArray,
                this::insertNewLumOff,
                this::removeLumOff,
                this::sizeOfLumOffArray
            );
        }
    }

    /**
     * Gets array of all "lumOff" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getLumOffArray() {
        return getXmlObjectArray(PROPERTY_QNAME[15], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "lumOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getLumOffArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lumOff" element
     */
    @Override
    public int sizeOfLumOffArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets array of all "lumOff" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLumOffArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] lumOffArray) {
        check_orphaned();
        arraySetterHelper(lumOffArray, PROPERTY_QNAME[15]);
    }

    /**
     * Sets ith "lumOff" element
     */
    @Override
    public void setLumOffArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage lumOff) {
        generatedSetterHelperImpl(lumOff, PROPERTY_QNAME[15], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lumOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewLumOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[15], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lumOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewLumOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Removes the ith "lumOff" element
     */
    @Override
    public void removeLumOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], i);
        }
    }

    /**
     * Gets a List of "lumMod" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getLumModList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLumModArray,
                this::setLumModArray,
                this::insertNewLumMod,
                this::removeLumMod,
                this::sizeOfLumModArray
            );
        }
    }

    /**
     * Gets array of all "lumMod" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getLumModArray() {
        return getXmlObjectArray(PROPERTY_QNAME[16], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "lumMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getLumModArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lumMod" element
     */
    @Override
    public int sizeOfLumModArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "lumMod" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLumModArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] lumModArray) {
        check_orphaned();
        arraySetterHelper(lumModArray, PROPERTY_QNAME[16]);
    }

    /**
     * Sets ith "lumMod" element
     */
    @Override
    public void setLumModArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage lumMod) {
        generatedSetterHelperImpl(lumMod, PROPERTY_QNAME[16], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lumMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewLumMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lumMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewLumMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Removes the ith "lumMod" element
     */
    @Override
    public void removeLumMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], i);
        }
    }

    /**
     * Gets a List of "red" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getRedList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRedArray,
                this::setRedArray,
                this::insertNewRed,
                this::removeRed,
                this::sizeOfRedArray
            );
        }
    }

    /**
     * Gets array of all "red" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getRedArray() {
        return getXmlObjectArray(PROPERTY_QNAME[17], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "red" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getRedArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[17], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "red" element
     */
    @Override
    public int sizeOfRedArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Sets array of all "red" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRedArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] redArray) {
        check_orphaned();
        arraySetterHelper(redArray, PROPERTY_QNAME[17]);
    }

    /**
     * Sets ith "red" element
     */
    @Override
    public void setRedArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage red) {
        generatedSetterHelperImpl(red, PROPERTY_QNAME[17], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "red" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewRed(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[17], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "red" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewRed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Removes the ith "red" element
     */
    @Override
    public void removeRed(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], i);
        }
    }

    /**
     * Gets a List of "redOff" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getRedOffList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRedOffArray,
                this::setRedOffArray,
                this::insertNewRedOff,
                this::removeRedOff,
                this::sizeOfRedOffArray
            );
        }
    }

    /**
     * Gets array of all "redOff" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getRedOffArray() {
        return getXmlObjectArray(PROPERTY_QNAME[18], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "redOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getRedOffArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[18], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "redOff" element
     */
    @Override
    public int sizeOfRedOffArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Sets array of all "redOff" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRedOffArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] redOffArray) {
        check_orphaned();
        arraySetterHelper(redOffArray, PROPERTY_QNAME[18]);
    }

    /**
     * Sets ith "redOff" element
     */
    @Override
    public void setRedOffArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage redOff) {
        generatedSetterHelperImpl(redOff, PROPERTY_QNAME[18], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "redOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewRedOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[18], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "redOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewRedOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Removes the ith "redOff" element
     */
    @Override
    public void removeRedOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], i);
        }
    }

    /**
     * Gets a List of "redMod" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getRedModList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRedModArray,
                this::setRedModArray,
                this::insertNewRedMod,
                this::removeRedMod,
                this::sizeOfRedModArray
            );
        }
    }

    /**
     * Gets array of all "redMod" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getRedModArray() {
        return getXmlObjectArray(PROPERTY_QNAME[19], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "redMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getRedModArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[19], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "redMod" element
     */
    @Override
    public int sizeOfRedModArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Sets array of all "redMod" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRedModArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] redModArray) {
        check_orphaned();
        arraySetterHelper(redModArray, PROPERTY_QNAME[19]);
    }

    /**
     * Sets ith "redMod" element
     */
    @Override
    public void setRedModArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage redMod) {
        generatedSetterHelperImpl(redMod, PROPERTY_QNAME[19], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "redMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewRedMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[19], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "redMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewRedMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Removes the ith "redMod" element
     */
    @Override
    public void removeRedMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], i);
        }
    }

    /**
     * Gets a List of "green" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getGreenList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGreenArray,
                this::setGreenArray,
                this::insertNewGreen,
                this::removeGreen,
                this::sizeOfGreenArray
            );
        }
    }

    /**
     * Gets array of all "green" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getGreenArray() {
        return getXmlObjectArray(PROPERTY_QNAME[20], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "green" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getGreenArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[20], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "green" element
     */
    @Override
    public int sizeOfGreenArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Sets array of all "green" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGreenArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] greenArray) {
        check_orphaned();
        arraySetterHelper(greenArray, PROPERTY_QNAME[20]);
    }

    /**
     * Sets ith "green" element
     */
    @Override
    public void setGreenArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage green) {
        generatedSetterHelperImpl(green, PROPERTY_QNAME[20], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "green" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewGreen(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[20], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "green" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewGreen() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Removes the ith "green" element
     */
    @Override
    public void removeGreen(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], i);
        }
    }

    /**
     * Gets a List of "greenOff" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getGreenOffList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGreenOffArray,
                this::setGreenOffArray,
                this::insertNewGreenOff,
                this::removeGreenOff,
                this::sizeOfGreenOffArray
            );
        }
    }

    /**
     * Gets array of all "greenOff" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getGreenOffArray() {
        return getXmlObjectArray(PROPERTY_QNAME[21], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "greenOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getGreenOffArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "greenOff" element
     */
    @Override
    public int sizeOfGreenOffArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets array of all "greenOff" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGreenOffArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] greenOffArray) {
        check_orphaned();
        arraySetterHelper(greenOffArray, PROPERTY_QNAME[21]);
    }

    /**
     * Sets ith "greenOff" element
     */
    @Override
    public void setGreenOffArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage greenOff) {
        generatedSetterHelperImpl(greenOff, PROPERTY_QNAME[21], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "greenOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewGreenOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[21], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "greenOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewGreenOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Removes the ith "greenOff" element
     */
    @Override
    public void removeGreenOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], i);
        }
    }

    /**
     * Gets a List of "greenMod" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getGreenModList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGreenModArray,
                this::setGreenModArray,
                this::insertNewGreenMod,
                this::removeGreenMod,
                this::sizeOfGreenModArray
            );
        }
    }

    /**
     * Gets array of all "greenMod" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getGreenModArray() {
        return getXmlObjectArray(PROPERTY_QNAME[22], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "greenMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getGreenModArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[22], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "greenMod" element
     */
    @Override
    public int sizeOfGreenModArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Sets array of all "greenMod" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGreenModArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] greenModArray) {
        check_orphaned();
        arraySetterHelper(greenModArray, PROPERTY_QNAME[22]);
    }

    /**
     * Sets ith "greenMod" element
     */
    @Override
    public void setGreenModArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage greenMod) {
        generatedSetterHelperImpl(greenMod, PROPERTY_QNAME[22], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "greenMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewGreenMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[22], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "greenMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewGreenMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Removes the ith "greenMod" element
     */
    @Override
    public void removeGreenMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], i);
        }
    }

    /**
     * Gets a List of "blue" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getBlueList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBlueArray,
                this::setBlueArray,
                this::insertNewBlue,
                this::removeBlue,
                this::sizeOfBlueArray
            );
        }
    }

    /**
     * Gets array of all "blue" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getBlueArray() {
        return getXmlObjectArray(PROPERTY_QNAME[23], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "blue" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getBlueArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[23], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "blue" element
     */
    @Override
    public int sizeOfBlueArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Sets array of all "blue" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBlueArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] blueArray) {
        check_orphaned();
        arraySetterHelper(blueArray, PROPERTY_QNAME[23]);
    }

    /**
     * Sets ith "blue" element
     */
    @Override
    public void setBlueArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage blue) {
        generatedSetterHelperImpl(blue, PROPERTY_QNAME[23], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "blue" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewBlue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[23], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "blue" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewBlue() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Removes the ith "blue" element
     */
    @Override
    public void removeBlue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], i);
        }
    }

    /**
     * Gets a List of "blueOff" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getBlueOffList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBlueOffArray,
                this::setBlueOffArray,
                this::insertNewBlueOff,
                this::removeBlueOff,
                this::sizeOfBlueOffArray
            );
        }
    }

    /**
     * Gets array of all "blueOff" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getBlueOffArray() {
        return getXmlObjectArray(PROPERTY_QNAME[24], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "blueOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getBlueOffArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[24], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "blueOff" element
     */
    @Override
    public int sizeOfBlueOffArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Sets array of all "blueOff" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBlueOffArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] blueOffArray) {
        check_orphaned();
        arraySetterHelper(blueOffArray, PROPERTY_QNAME[24]);
    }

    /**
     * Sets ith "blueOff" element
     */
    @Override
    public void setBlueOffArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage blueOff) {
        generatedSetterHelperImpl(blueOff, PROPERTY_QNAME[24], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "blueOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewBlueOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[24], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "blueOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewBlueOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * Removes the ith "blueOff" element
     */
    @Override
    public void removeBlueOff(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], i);
        }
    }

    /**
     * Gets a List of "blueMod" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage> getBlueModList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBlueModArray,
                this::setBlueModArray,
                this::insertNewBlueMod,
                this::removeBlueMod,
                this::sizeOfBlueModArray
            );
        }
    }

    /**
     * Gets array of all "blueMod" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] getBlueModArray() {
        return getXmlObjectArray(PROPERTY_QNAME[25], new org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[0]);
    }

    /**
     * Gets ith "blueMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage getBlueModArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().find_element_user(PROPERTY_QNAME[25], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "blueMod" element
     */
    @Override
    public int sizeOfBlueModArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Sets array of all "blueMod" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBlueModArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage[] blueModArray) {
        check_orphaned();
        arraySetterHelper(blueModArray, PROPERTY_QNAME[25]);
    }

    /**
     * Sets ith "blueMod" element
     */
    @Override
    public void setBlueModArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage blueMod) {
        generatedSetterHelperImpl(blueMod, PROPERTY_QNAME[25], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "blueMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage insertNewBlueMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().insert_element_user(PROPERTY_QNAME[25], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "blueMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage addNewBlueMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPercentage)get_store().add_element_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * Removes the ith "blueMod" element
     */
    @Override
    public void removeBlueMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], i);
        }
    }

    /**
     * Gets a List of "gamma" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform> getGammaList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGammaArray,
                this::setGammaArray,
                this::insertNewGamma,
                this::removeGamma,
                this::sizeOfGammaArray
            );
        }
    }

    /**
     * Gets array of all "gamma" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform[] getGammaArray() {
        return getXmlObjectArray(PROPERTY_QNAME[26], new org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform[0]);
    }

    /**
     * Gets ith "gamma" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform getGammaArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform)get_store().find_element_user(PROPERTY_QNAME[26], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "gamma" element
     */
    @Override
    public int sizeOfGammaArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Sets array of all "gamma" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGammaArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform[] gammaArray) {
        check_orphaned();
        arraySetterHelper(gammaArray, PROPERTY_QNAME[26]);
    }

    /**
     * Sets ith "gamma" element
     */
    @Override
    public void setGammaArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform gamma) {
        generatedSetterHelperImpl(gamma, PROPERTY_QNAME[26], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gamma" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform insertNewGamma(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform)get_store().insert_element_user(PROPERTY_QNAME[26], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "gamma" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform addNewGamma() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGammaTransform)get_store().add_element_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * Removes the ith "gamma" element
     */
    @Override
    public void removeGamma(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], i);
        }
    }

    /**
     * Gets a List of "invGamma" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform> getInvGammaList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getInvGammaArray,
                this::setInvGammaArray,
                this::insertNewInvGamma,
                this::removeInvGamma,
                this::sizeOfInvGammaArray
            );
        }
    }

    /**
     * Gets array of all "invGamma" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform[] getInvGammaArray() {
        return getXmlObjectArray(PROPERTY_QNAME[27], new org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform[0]);
    }

    /**
     * Gets ith "invGamma" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform getInvGammaArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform)get_store().find_element_user(PROPERTY_QNAME[27], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "invGamma" element
     */
    @Override
    public int sizeOfInvGammaArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Sets array of all "invGamma" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setInvGammaArray(org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform[] invGammaArray) {
        check_orphaned();
        arraySetterHelper(invGammaArray, PROPERTY_QNAME[27]);
    }

    /**
     * Sets ith "invGamma" element
     */
    @Override
    public void setInvGammaArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform invGamma) {
        generatedSetterHelperImpl(invGamma, PROPERTY_QNAME[27], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "invGamma" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform insertNewInvGamma(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform)get_store().insert_element_user(PROPERTY_QNAME[27], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "invGamma" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform addNewInvGamma() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTInverseGammaTransform)get_store().add_element_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * Removes the ith "invGamma" element
     */
    @Override
    public void removeInvGamma(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], i);
        }
    }

    /**
     * Gets the "val" attribute
     */
    @Override
    public byte[] getVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "val" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB xgetVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Sets the "val" attribute
     */
    @Override
    public void setVal(byte[] val) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.setByteArrayValue(val);
        }
    }

    /**
     * Sets (as xml) the "val" attribute
     */
    @Override
    public void xsetVal(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB val) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STHexColorRGB)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.set(val);
        }
    }
}
