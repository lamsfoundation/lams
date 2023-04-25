/*
 * XML Type:  CT_Blip
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTBlip
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Blip(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTBlipImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTBlip {
    private static final long serialVersionUID = 1L;

    public CTBlipImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaBiLevel"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaCeiling"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaFloor"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaInv"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaMod"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaModFix"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "alphaRepl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "biLevel"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blur"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "clrChange"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "clrRepl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "duotone"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fillOverlay"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "grayscl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hsl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lum"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tint"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "embed"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "link"),
        new QName("", "cstate"),
    };


    /**
     * Gets a List of "alphaBiLevel" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect> getAlphaBiLevelList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlphaBiLevelArray,
                this::setAlphaBiLevelArray,
                this::insertNewAlphaBiLevel,
                this::removeAlphaBiLevel,
                this::sizeOfAlphaBiLevelArray
            );
        }
    }

    /**
     * Gets array of all "alphaBiLevel" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect[] getAlphaBiLevelArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect[0]);
    }

    /**
     * Gets ith "alphaBiLevel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect getAlphaBiLevelArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alphaBiLevel" element
     */
    @Override
    public int sizeOfAlphaBiLevelArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "alphaBiLevel" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaBiLevelArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect[] alphaBiLevelArray) {
        check_orphaned();
        arraySetterHelper(alphaBiLevelArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "alphaBiLevel" element
     */
    @Override
    public void setAlphaBiLevelArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect alphaBiLevel) {
        generatedSetterHelperImpl(alphaBiLevel, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alphaBiLevel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect insertNewAlphaBiLevel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alphaBiLevel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect addNewAlphaBiLevel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "alphaBiLevel" element
     */
    @Override
    public void removeAlphaBiLevel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "alphaCeiling" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect> getAlphaCeilingList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlphaCeilingArray,
                this::setAlphaCeilingArray,
                this::insertNewAlphaCeiling,
                this::removeAlphaCeiling,
                this::sizeOfAlphaCeilingArray
            );
        }
    }

    /**
     * Gets array of all "alphaCeiling" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect[] getAlphaCeilingArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect[0]);
    }

    /**
     * Gets ith "alphaCeiling" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect getAlphaCeilingArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alphaCeiling" element
     */
    @Override
    public int sizeOfAlphaCeilingArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "alphaCeiling" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaCeilingArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect[] alphaCeilingArray) {
        check_orphaned();
        arraySetterHelper(alphaCeilingArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "alphaCeiling" element
     */
    @Override
    public void setAlphaCeilingArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect alphaCeiling) {
        generatedSetterHelperImpl(alphaCeiling, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alphaCeiling" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect insertNewAlphaCeiling(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alphaCeiling" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect addNewAlphaCeiling() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaCeilingEffect)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "alphaCeiling" element
     */
    @Override
    public void removeAlphaCeiling(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "alphaFloor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect> getAlphaFloorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlphaFloorArray,
                this::setAlphaFloorArray,
                this::insertNewAlphaFloor,
                this::removeAlphaFloor,
                this::sizeOfAlphaFloorArray
            );
        }
    }

    /**
     * Gets array of all "alphaFloor" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect[] getAlphaFloorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect[0]);
    }

    /**
     * Gets ith "alphaFloor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect getAlphaFloorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alphaFloor" element
     */
    @Override
    public int sizeOfAlphaFloorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "alphaFloor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaFloorArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect[] alphaFloorArray) {
        check_orphaned();
        arraySetterHelper(alphaFloorArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "alphaFloor" element
     */
    @Override
    public void setAlphaFloorArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect alphaFloor) {
        generatedSetterHelperImpl(alphaFloor, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alphaFloor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect insertNewAlphaFloor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alphaFloor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect addNewAlphaFloor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaFloorEffect)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "alphaFloor" element
     */
    @Override
    public void removeAlphaFloor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "alphaInv" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect> getAlphaInvList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlphaInvArray,
                this::setAlphaInvArray,
                this::insertNewAlphaInv,
                this::removeAlphaInv,
                this::sizeOfAlphaInvArray
            );
        }
    }

    /**
     * Gets array of all "alphaInv" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect[] getAlphaInvArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect[0]);
    }

    /**
     * Gets ith "alphaInv" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect getAlphaInvArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alphaInv" element
     */
    @Override
    public int sizeOfAlphaInvArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "alphaInv" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaInvArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect[] alphaInvArray) {
        check_orphaned();
        arraySetterHelper(alphaInvArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "alphaInv" element
     */
    @Override
    public void setAlphaInvArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect alphaInv) {
        generatedSetterHelperImpl(alphaInv, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alphaInv" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect insertNewAlphaInv(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alphaInv" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect addNewAlphaInv() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaInverseEffect)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "alphaInv" element
     */
    @Override
    public void removeAlphaInv(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "alphaMod" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect> getAlphaModList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect[] getAlphaModArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect[0]);
    }

    /**
     * Gets ith "alphaMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect getAlphaModArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect)get_store().find_element_user(PROPERTY_QNAME[4], i);
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
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "alphaMod" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaModArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect[] alphaModArray) {
        check_orphaned();
        arraySetterHelper(alphaModArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "alphaMod" element
     */
    @Override
    public void setAlphaModArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect alphaMod) {
        generatedSetterHelperImpl(alphaMod, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alphaMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect insertNewAlphaMod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alphaMod" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect addNewAlphaMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateEffect)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "alphaModFix" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect> getAlphaModFixList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlphaModFixArray,
                this::setAlphaModFixArray,
                this::insertNewAlphaModFix,
                this::removeAlphaModFix,
                this::sizeOfAlphaModFixArray
            );
        }
    }

    /**
     * Gets array of all "alphaModFix" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect[] getAlphaModFixArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect[0]);
    }

    /**
     * Gets ith "alphaModFix" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect getAlphaModFixArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alphaModFix" element
     */
    @Override
    public int sizeOfAlphaModFixArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "alphaModFix" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaModFixArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect[] alphaModFixArray) {
        check_orphaned();
        arraySetterHelper(alphaModFixArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "alphaModFix" element
     */
    @Override
    public void setAlphaModFixArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect alphaModFix) {
        generatedSetterHelperImpl(alphaModFix, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alphaModFix" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect insertNewAlphaModFix(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alphaModFix" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect addNewAlphaModFix() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "alphaModFix" element
     */
    @Override
    public void removeAlphaModFix(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "alphaRepl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect> getAlphaReplList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlphaReplArray,
                this::setAlphaReplArray,
                this::insertNewAlphaRepl,
                this::removeAlphaRepl,
                this::sizeOfAlphaReplArray
            );
        }
    }

    /**
     * Gets array of all "alphaRepl" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect[] getAlphaReplArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect[0]);
    }

    /**
     * Gets ith "alphaRepl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect getAlphaReplArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alphaRepl" element
     */
    @Override
    public int sizeOfAlphaReplArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "alphaRepl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlphaReplArray(org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect[] alphaReplArray) {
        check_orphaned();
        arraySetterHelper(alphaReplArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "alphaRepl" element
     */
    @Override
    public void setAlphaReplArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect alphaRepl) {
        generatedSetterHelperImpl(alphaRepl, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alphaRepl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect insertNewAlphaRepl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alphaRepl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect addNewAlphaRepl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "alphaRepl" element
     */
    @Override
    public void removeAlphaRepl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "biLevel" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect> getBiLevelList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBiLevelArray,
                this::setBiLevelArray,
                this::insertNewBiLevel,
                this::removeBiLevel,
                this::sizeOfBiLevelArray
            );
        }
    }

    /**
     * Gets array of all "biLevel" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect[] getBiLevelArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect[0]);
    }

    /**
     * Gets ith "biLevel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect getBiLevelArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "biLevel" element
     */
    @Override
    public int sizeOfBiLevelArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "biLevel" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBiLevelArray(org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect[] biLevelArray) {
        check_orphaned();
        arraySetterHelper(biLevelArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "biLevel" element
     */
    @Override
    public void setBiLevelArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect biLevel) {
        generatedSetterHelperImpl(biLevel, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "biLevel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect insertNewBiLevel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "biLevel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect addNewBiLevel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "biLevel" element
     */
    @Override
    public void removeBiLevel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "blur" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect> getBlurList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBlurArray,
                this::setBlurArray,
                this::insertNewBlur,
                this::removeBlur,
                this::sizeOfBlurArray
            );
        }
    }

    /**
     * Gets array of all "blur" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect[] getBlurArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect[0]);
    }

    /**
     * Gets ith "blur" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect getBlurArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "blur" element
     */
    @Override
    public int sizeOfBlurArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "blur" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBlurArray(org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect[] blurArray) {
        check_orphaned();
        arraySetterHelper(blurArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "blur" element
     */
    @Override
    public void setBlurArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect blur) {
        generatedSetterHelperImpl(blur, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "blur" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect insertNewBlur(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "blur" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect addNewBlur() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "blur" element
     */
    @Override
    public void removeBlur(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "clrChange" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect> getClrChangeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getClrChangeArray,
                this::setClrChangeArray,
                this::insertNewClrChange,
                this::removeClrChange,
                this::sizeOfClrChangeArray
            );
        }
    }

    /**
     * Gets array of all "clrChange" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect[] getClrChangeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect[0]);
    }

    /**
     * Gets ith "clrChange" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect getClrChangeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "clrChange" element
     */
    @Override
    public int sizeOfClrChangeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "clrChange" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setClrChangeArray(org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect[] clrChangeArray) {
        check_orphaned();
        arraySetterHelper(clrChangeArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "clrChange" element
     */
    @Override
    public void setClrChangeArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect clrChange) {
        generatedSetterHelperImpl(clrChange, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "clrChange" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect insertNewClrChange(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "clrChange" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect addNewClrChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "clrChange" element
     */
    @Override
    public void removeClrChange(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "clrRepl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect> getClrReplList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getClrReplArray,
                this::setClrReplArray,
                this::insertNewClrRepl,
                this::removeClrRepl,
                this::sizeOfClrReplArray
            );
        }
    }

    /**
     * Gets array of all "clrRepl" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect[] getClrReplArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect[0]);
    }

    /**
     * Gets ith "clrRepl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect getClrReplArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "clrRepl" element
     */
    @Override
    public int sizeOfClrReplArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "clrRepl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setClrReplArray(org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect[] clrReplArray) {
        check_orphaned();
        arraySetterHelper(clrReplArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "clrRepl" element
     */
    @Override
    public void setClrReplArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect clrRepl) {
        generatedSetterHelperImpl(clrRepl, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "clrRepl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect insertNewClrRepl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "clrRepl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect addNewClrRepl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorReplaceEffect)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "clrRepl" element
     */
    @Override
    public void removeClrRepl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "duotone" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect> getDuotoneList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDuotoneArray,
                this::setDuotoneArray,
                this::insertNewDuotone,
                this::removeDuotone,
                this::sizeOfDuotoneArray
            );
        }
    }

    /**
     * Gets array of all "duotone" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect[] getDuotoneArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect[0]);
    }

    /**
     * Gets ith "duotone" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect getDuotoneArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "duotone" element
     */
    @Override
    public int sizeOfDuotoneArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "duotone" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDuotoneArray(org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect[] duotoneArray) {
        check_orphaned();
        arraySetterHelper(duotoneArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "duotone" element
     */
    @Override
    public void setDuotoneArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect duotone) {
        generatedSetterHelperImpl(duotone, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "duotone" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect insertNewDuotone(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "duotone" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect addNewDuotone() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDuotoneEffect)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "duotone" element
     */
    @Override
    public void removeDuotone(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "fillOverlay" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect> getFillOverlayList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFillOverlayArray,
                this::setFillOverlayArray,
                this::insertNewFillOverlay,
                this::removeFillOverlay,
                this::sizeOfFillOverlayArray
            );
        }
    }

    /**
     * Gets array of all "fillOverlay" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect[] getFillOverlayArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect[0]);
    }

    /**
     * Gets ith "fillOverlay" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect getFillOverlayArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "fillOverlay" element
     */
    @Override
    public int sizeOfFillOverlayArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "fillOverlay" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFillOverlayArray(org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect[] fillOverlayArray) {
        check_orphaned();
        arraySetterHelper(fillOverlayArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "fillOverlay" element
     */
    @Override
    public void setFillOverlayArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect fillOverlay) {
        generatedSetterHelperImpl(fillOverlay, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fillOverlay" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect insertNewFillOverlay(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "fillOverlay" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect addNewFillOverlay() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "fillOverlay" element
     */
    @Override
    public void removeFillOverlay(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }

    /**
     * Gets a List of "grayscl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect> getGraysclList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGraysclArray,
                this::setGraysclArray,
                this::insertNewGrayscl,
                this::removeGrayscl,
                this::sizeOfGraysclArray
            );
        }
    }

    /**
     * Gets array of all "grayscl" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect[] getGraysclArray() {
        return getXmlObjectArray(PROPERTY_QNAME[13], new org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect[0]);
    }

    /**
     * Gets ith "grayscl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect getGraysclArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "grayscl" element
     */
    @Override
    public int sizeOfGraysclArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "grayscl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGraysclArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect[] graysclArray) {
        check_orphaned();
        arraySetterHelper(graysclArray, PROPERTY_QNAME[13]);
    }

    /**
     * Sets ith "grayscl" element
     */
    @Override
    public void setGraysclArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect grayscl) {
        generatedSetterHelperImpl(grayscl, PROPERTY_QNAME[13], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "grayscl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect insertNewGrayscl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "grayscl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect addNewGrayscl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGrayscaleEffect)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Removes the ith "grayscl" element
     */
    @Override
    public void removeGrayscl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], i);
        }
    }

    /**
     * Gets a List of "hsl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect> getHslList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getHslArray,
                this::setHslArray,
                this::insertNewHsl,
                this::removeHsl,
                this::sizeOfHslArray
            );
        }
    }

    /**
     * Gets array of all "hsl" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect[] getHslArray() {
        return getXmlObjectArray(PROPERTY_QNAME[14], new org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect[0]);
    }

    /**
     * Gets ith "hsl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect getHslArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "hsl" element
     */
    @Override
    public int sizeOfHslArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "hsl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setHslArray(org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect[] hslArray) {
        check_orphaned();
        arraySetterHelper(hslArray, PROPERTY_QNAME[14]);
    }

    /**
     * Sets ith "hsl" element
     */
    @Override
    public void setHslArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect hsl) {
        generatedSetterHelperImpl(hsl, PROPERTY_QNAME[14], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "hsl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect insertNewHsl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "hsl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect addNewHsl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHSLEffect)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Removes the ith "hsl" element
     */
    @Override
    public void removeHsl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], i);
        }
    }

    /**
     * Gets a List of "lum" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect> getLumList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect[] getLumArray() {
        return getXmlObjectArray(PROPERTY_QNAME[15], new org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect[0]);
    }

    /**
     * Gets ith "lum" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect getLumArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect)get_store().find_element_user(PROPERTY_QNAME[15], i);
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
            return get_store().count_elements(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets array of all "lum" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLumArray(org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect[] lumArray) {
        check_orphaned();
        arraySetterHelper(lumArray, PROPERTY_QNAME[15]);
    }

    /**
     * Sets ith "lum" element
     */
    @Override
    public void setLumArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect lum) {
        generatedSetterHelperImpl(lum, PROPERTY_QNAME[15], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lum" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect insertNewLum(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect)get_store().insert_element_user(PROPERTY_QNAME[15], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lum" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect addNewLum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect)get_store().add_element_user(PROPERTY_QNAME[15]);
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
            get_store().remove_element(PROPERTY_QNAME[15], i);
        }
    }

    /**
     * Gets a List of "tint" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect> getTintList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect[] getTintArray() {
        return getXmlObjectArray(PROPERTY_QNAME[16], new org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect[0]);
    }

    /**
     * Gets ith "tint" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect getTintArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect)get_store().find_element_user(PROPERTY_QNAME[16], i);
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
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "tint" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTintArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect[] tintArray) {
        check_orphaned();
        arraySetterHelper(tintArray, PROPERTY_QNAME[16]);
    }

    /**
     * Sets ith "tint" element
     */
    @Override
    public void setTintArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect tint) {
        generatedSetterHelperImpl(tint, PROPERTY_QNAME[16], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tint" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect insertNewTint(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tint" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect addNewTint() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTintEffect)get_store().add_element_user(PROPERTY_QNAME[16]);
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
            get_store().remove_element(PROPERTY_QNAME[16], i);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "embed" attribute
     */
    @Override
    public java.lang.String getEmbed() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "embed" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetEmbed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return target;
        }
    }

    /**
     * True if has "embed" attribute
     */
    @Override
    public boolean isSetEmbed() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "embed" attribute
     */
    @Override
    public void setEmbed(java.lang.String embed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setStringValue(embed);
        }
    }

    /**
     * Sets (as xml) the "embed" attribute
     */
    @Override
    public void xsetEmbed(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId embed) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(embed);
        }
    }

    /**
     * Unsets the "embed" attribute
     */
    @Override
    public void unsetEmbed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "link" attribute
     */
    @Override
    public java.lang.String getLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "link" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return target;
        }
    }

    /**
     * True if has "link" attribute
     */
    @Override
    public boolean isSetLink() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "link" attribute
     */
    @Override
    public void setLink(java.lang.String link) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setStringValue(link);
        }
    }

    /**
     * Sets (as xml) the "link" attribute
     */
    @Override
    public void xsetLink(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId link) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(link);
        }
    }

    /**
     * Unsets the "link" attribute
     */
    @Override
    public void unsetLink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "cstate" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression.Enum getCstate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "cstate" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression xgetCstate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return target;
        }
    }

    /**
     * True if has "cstate" attribute
     */
    @Override
    public boolean isSetCstate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "cstate" attribute
     */
    @Override
    public void setCstate(org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression.Enum cstate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setEnumValue(cstate);
        }
    }

    /**
     * Sets (as xml) the "cstate" attribute
     */
    @Override
    public void xsetCstate(org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression cstate) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlipCompression)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(cstate);
        }
    }

    /**
     * Unsets the "cstate" attribute
     */
    @Override
    public void unsetCstate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }
}
