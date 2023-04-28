/*
 * XML Type:  CT_TimeNodeList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TimeNodeList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTimeNodeListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList {
    private static final long serialVersionUID = 1L;

    public CTTimeNodeListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "par"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "seq"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "excl"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "anim"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "animClr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "animEffect"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "animMotion"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "animRot"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "animScale"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cmd"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "set"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "audio"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "video"),
    };


    /**
     * Gets a List of "par" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel> getParList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getParArray,
                this::setParArray,
                this::insertNewPar,
                this::removePar,
                this::sizeOfParArray
            );
        }
    }

    /**
     * Gets array of all "par" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel[] getParArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel[0]);
    }

    /**
     * Gets ith "par" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel getParArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "par" element
     */
    @Override
    public int sizeOfParArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "par" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setParArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel[] parArray) {
        check_orphaned();
        arraySetterHelper(parArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "par" element
     */
    @Override
    public void setParArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel par) {
        generatedSetterHelperImpl(par, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "par" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel insertNewPar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "par" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel addNewPar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeParallel)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "par" element
     */
    @Override
    public void removePar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "seq" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence> getSeqList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSeqArray,
                this::setSeqArray,
                this::insertNewSeq,
                this::removeSeq,
                this::sizeOfSeqArray
            );
        }
    }

    /**
     * Gets array of all "seq" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence[] getSeqArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence[0]);
    }

    /**
     * Gets ith "seq" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence getSeqArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "seq" element
     */
    @Override
    public int sizeOfSeqArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "seq" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSeqArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence[] seqArray) {
        check_orphaned();
        arraySetterHelper(seqArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "seq" element
     */
    @Override
    public void setSeqArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence seq) {
        generatedSetterHelperImpl(seq, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "seq" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence insertNewSeq(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "seq" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence addNewSeq() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeSequence)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "seq" element
     */
    @Override
    public void removeSeq(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "excl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive> getExclList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getExclArray,
                this::setExclArray,
                this::insertNewExcl,
                this::removeExcl,
                this::sizeOfExclArray
            );
        }
    }

    /**
     * Gets array of all "excl" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive[] getExclArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive[0]);
    }

    /**
     * Gets ith "excl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive getExclArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "excl" element
     */
    @Override
    public int sizeOfExclArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "excl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setExclArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive[] exclArray) {
        check_orphaned();
        arraySetterHelper(exclArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "excl" element
     */
    @Override
    public void setExclArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive excl) {
        generatedSetterHelperImpl(excl, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "excl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive insertNewExcl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "excl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive addNewExcl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeNodeExclusive)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "excl" element
     */
    @Override
    public void removeExcl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "anim" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior> getAnimList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAnimArray,
                this::setAnimArray,
                this::insertNewAnim,
                this::removeAnim,
                this::sizeOfAnimArray
            );
        }
    }

    /**
     * Gets array of all "anim" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior[] getAnimArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior[0]);
    }

    /**
     * Gets ith "anim" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior getAnimArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "anim" element
     */
    @Override
    public int sizeOfAnimArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "anim" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAnimArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior[] animArray) {
        check_orphaned();
        arraySetterHelper(animArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "anim" element
     */
    @Override
    public void setAnimArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior anim) {
        generatedSetterHelperImpl(anim, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "anim" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior insertNewAnim(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "anim" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior addNewAnim() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateBehavior)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "anim" element
     */
    @Override
    public void removeAnim(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "animClr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior> getAnimClrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAnimClrArray,
                this::setAnimClrArray,
                this::insertNewAnimClr,
                this::removeAnimClr,
                this::sizeOfAnimClrArray
            );
        }
    }

    /**
     * Gets array of all "animClr" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior[] getAnimClrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior[0]);
    }

    /**
     * Gets ith "animClr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior getAnimClrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "animClr" element
     */
    @Override
    public int sizeOfAnimClrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "animClr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAnimClrArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior[] animClrArray) {
        check_orphaned();
        arraySetterHelper(animClrArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "animClr" element
     */
    @Override
    public void setAnimClrArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior animClr) {
        generatedSetterHelperImpl(animClr, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "animClr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior insertNewAnimClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "animClr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior addNewAnimClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateColorBehavior)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "animClr" element
     */
    @Override
    public void removeAnimClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "animEffect" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior> getAnimEffectList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAnimEffectArray,
                this::setAnimEffectArray,
                this::insertNewAnimEffect,
                this::removeAnimEffect,
                this::sizeOfAnimEffectArray
            );
        }
    }

    /**
     * Gets array of all "animEffect" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior[] getAnimEffectArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior[0]);
    }

    /**
     * Gets ith "animEffect" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior getAnimEffectArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "animEffect" element
     */
    @Override
    public int sizeOfAnimEffectArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "animEffect" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAnimEffectArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior[] animEffectArray) {
        check_orphaned();
        arraySetterHelper(animEffectArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "animEffect" element
     */
    @Override
    public void setAnimEffectArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior animEffect) {
        generatedSetterHelperImpl(animEffect, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "animEffect" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior insertNewAnimEffect(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "animEffect" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior addNewAnimEffect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "animEffect" element
     */
    @Override
    public void removeAnimEffect(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "animMotion" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior> getAnimMotionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAnimMotionArray,
                this::setAnimMotionArray,
                this::insertNewAnimMotion,
                this::removeAnimMotion,
                this::sizeOfAnimMotionArray
            );
        }
    }

    /**
     * Gets array of all "animMotion" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior[] getAnimMotionArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior[0]);
    }

    /**
     * Gets ith "animMotion" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior getAnimMotionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "animMotion" element
     */
    @Override
    public int sizeOfAnimMotionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "animMotion" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAnimMotionArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior[] animMotionArray) {
        check_orphaned();
        arraySetterHelper(animMotionArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "animMotion" element
     */
    @Override
    public void setAnimMotionArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior animMotion) {
        generatedSetterHelperImpl(animMotion, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "animMotion" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior insertNewAnimMotion(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "animMotion" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior addNewAnimMotion() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "animMotion" element
     */
    @Override
    public void removeAnimMotion(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "animRot" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior> getAnimRotList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAnimRotArray,
                this::setAnimRotArray,
                this::insertNewAnimRot,
                this::removeAnimRot,
                this::sizeOfAnimRotArray
            );
        }
    }

    /**
     * Gets array of all "animRot" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior[] getAnimRotArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior[0]);
    }

    /**
     * Gets ith "animRot" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior getAnimRotArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "animRot" element
     */
    @Override
    public int sizeOfAnimRotArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "animRot" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAnimRotArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior[] animRotArray) {
        check_orphaned();
        arraySetterHelper(animRotArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "animRot" element
     */
    @Override
    public void setAnimRotArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior animRot) {
        generatedSetterHelperImpl(animRot, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "animRot" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior insertNewAnimRot(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "animRot" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior addNewAnimRot() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "animRot" element
     */
    @Override
    public void removeAnimRot(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "animScale" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior> getAnimScaleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAnimScaleArray,
                this::setAnimScaleArray,
                this::insertNewAnimScale,
                this::removeAnimScale,
                this::sizeOfAnimScaleArray
            );
        }
    }

    /**
     * Gets array of all "animScale" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior[] getAnimScaleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior[0]);
    }

    /**
     * Gets ith "animScale" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior getAnimScaleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "animScale" element
     */
    @Override
    public int sizeOfAnimScaleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "animScale" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAnimScaleArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior[] animScaleArray) {
        check_orphaned();
        arraySetterHelper(animScaleArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "animScale" element
     */
    @Override
    public void setAnimScaleArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior animScale) {
        generatedSetterHelperImpl(animScale, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "animScale" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior insertNewAnimScale(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "animScale" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior addNewAnimScale() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "animScale" element
     */
    @Override
    public void removeAnimScale(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "cmd" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior> getCmdList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCmdArray,
                this::setCmdArray,
                this::insertNewCmd,
                this::removeCmd,
                this::sizeOfCmdArray
            );
        }
    }

    /**
     * Gets array of all "cmd" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior[] getCmdArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior[0]);
    }

    /**
     * Gets ith "cmd" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior getCmdArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cmd" element
     */
    @Override
    public int sizeOfCmdArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "cmd" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCmdArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior[] cmdArray) {
        check_orphaned();
        arraySetterHelper(cmdArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "cmd" element
     */
    @Override
    public void setCmdArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior cmd) {
        generatedSetterHelperImpl(cmd, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cmd" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior insertNewCmd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cmd" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior addNewCmd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "cmd" element
     */
    @Override
    public void removeCmd(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "set" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior> getSetList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSetArray,
                this::setSetArray,
                this::insertNewSet,
                this::removeSet,
                this::sizeOfSetArray
            );
        }
    }

    /**
     * Gets array of all "set" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior[] getSetArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior[0]);
    }

    /**
     * Gets ith "set" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior getSetArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "set" element
     */
    @Override
    public int sizeOfSetArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "set" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSetArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior[] setArray) {
        check_orphaned();
        arraySetterHelper(setArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "set" element
     */
    @Override
    public void setSetArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior set) {
        generatedSetterHelperImpl(set, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "set" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior insertNewSet(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "set" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior addNewSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "set" element
     */
    @Override
    public void removeSet(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "audio" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio> getAudioList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAudioArray,
                this::setAudioArray,
                this::insertNewAudio,
                this::removeAudio,
                this::sizeOfAudioArray
            );
        }
    }

    /**
     * Gets array of all "audio" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio[] getAudioArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio[0]);
    }

    /**
     * Gets ith "audio" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio getAudioArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "audio" element
     */
    @Override
    public int sizeOfAudioArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "audio" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAudioArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio[] audioArray) {
        check_orphaned();
        arraySetterHelper(audioArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "audio" element
     */
    @Override
    public void setAudioArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio audio) {
        generatedSetterHelperImpl(audio, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "audio" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio insertNewAudio(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "audio" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio addNewAudio() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "audio" element
     */
    @Override
    public void removeAudio(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "video" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo> getVideoList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getVideoArray,
                this::setVideoArray,
                this::insertNewVideo,
                this::removeVideo,
                this::sizeOfVideoArray
            );
        }
    }

    /**
     * Gets array of all "video" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo[] getVideoArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo[0]);
    }

    /**
     * Gets ith "video" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo getVideoArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "video" element
     */
    @Override
    public int sizeOfVideoArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "video" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setVideoArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo[] videoArray) {
        check_orphaned();
        arraySetterHelper(videoArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "video" element
     */
    @Override
    public void setVideoArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo video) {
        generatedSetterHelperImpl(video, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "video" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo insertNewVideo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "video" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo addNewVideo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "video" element
     */
    @Override
    public void removeVideo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }
}
