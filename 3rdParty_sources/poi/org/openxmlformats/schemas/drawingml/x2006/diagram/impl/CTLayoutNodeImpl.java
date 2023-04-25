/*
 * XML Type:  CT_LayoutNode
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_LayoutNode(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTLayoutNodeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode {
    private static final long serialVersionUID = 1L;

    public CTLayoutNodeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "alg"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "shape"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "presOf"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "constrLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "ruleLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "varLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "forEach"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "layoutNode"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "choose"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "extLst"),
        new QName("", "name"),
        new QName("", "styleLbl"),
        new QName("", "chOrder"),
        new QName("", "moveWith"),
    };


    /**
     * Gets a List of "alg" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm> getAlgList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAlgArray,
                this::setAlgArray,
                this::insertNewAlg,
                this::removeAlg,
                this::sizeOfAlgArray
            );
        }
    }

    /**
     * Gets array of all "alg" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm[] getAlgArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm[0]);
    }

    /**
     * Gets ith "alg" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm getAlgArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "alg" element
     */
    @Override
    public int sizeOfAlgArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "alg" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAlgArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm[] algArray) {
        check_orphaned();
        arraySetterHelper(algArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "alg" element
     */
    @Override
    public void setAlgArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm alg) {
        generatedSetterHelperImpl(alg, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "alg" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm insertNewAlg(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "alg" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm addNewAlg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "alg" element
     */
    @Override
    public void removeAlg(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "shape" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape> getShapeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getShapeArray,
                this::setShapeArray,
                this::insertNewShape,
                this::removeShape,
                this::sizeOfShapeArray
            );
        }
    }

    /**
     * Gets array of all "shape" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape[] getShapeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape[0]);
    }

    /**
     * Gets ith "shape" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape getShapeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "shape" element
     */
    @Override
    public int sizeOfShapeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "shape" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setShapeArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape[] shapeArray) {
        check_orphaned();
        arraySetterHelper(shapeArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "shape" element
     */
    @Override
    public void setShapeArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape shape) {
        generatedSetterHelperImpl(shape, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "shape" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape insertNewShape(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "shape" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape addNewShape() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTShape)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "shape" element
     */
    @Override
    public void removeShape(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "presOf" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf> getPresOfList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPresOfArray,
                this::setPresOfArray,
                this::insertNewPresOf,
                this::removePresOf,
                this::sizeOfPresOfArray
            );
        }
    }

    /**
     * Gets array of all "presOf" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf[] getPresOfArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf[0]);
    }

    /**
     * Gets ith "presOf" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf getPresOfArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "presOf" element
     */
    @Override
    public int sizeOfPresOfArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "presOf" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPresOfArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf[] presOfArray) {
        check_orphaned();
        arraySetterHelper(presOfArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "presOf" element
     */
    @Override
    public void setPresOfArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf presOf) {
        generatedSetterHelperImpl(presOf, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "presOf" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf insertNewPresOf(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "presOf" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf addNewPresOf() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "presOf" element
     */
    @Override
    public void removePresOf(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "constrLst" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints> getConstrLstList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getConstrLstArray,
                this::setConstrLstArray,
                this::insertNewConstrLst,
                this::removeConstrLst,
                this::sizeOfConstrLstArray
            );
        }
    }

    /**
     * Gets array of all "constrLst" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints[] getConstrLstArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints[0]);
    }

    /**
     * Gets ith "constrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints getConstrLstArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "constrLst" element
     */
    @Override
    public int sizeOfConstrLstArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "constrLst" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setConstrLstArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints[] constrLstArray) {
        check_orphaned();
        arraySetterHelper(constrLstArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "constrLst" element
     */
    @Override
    public void setConstrLstArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints constrLst) {
        generatedSetterHelperImpl(constrLst, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "constrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints insertNewConstrLst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "constrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints addNewConstrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "constrLst" element
     */
    @Override
    public void removeConstrLst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "ruleLst" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules> getRuleLstList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRuleLstArray,
                this::setRuleLstArray,
                this::insertNewRuleLst,
                this::removeRuleLst,
                this::sizeOfRuleLstArray
            );
        }
    }

    /**
     * Gets array of all "ruleLst" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules[] getRuleLstArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules[0]);
    }

    /**
     * Gets ith "ruleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules getRuleLstArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ruleLst" element
     */
    @Override
    public int sizeOfRuleLstArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "ruleLst" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRuleLstArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules[] ruleLstArray) {
        check_orphaned();
        arraySetterHelper(ruleLstArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "ruleLst" element
     */
    @Override
    public void setRuleLstArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules ruleLst) {
        generatedSetterHelperImpl(ruleLst, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ruleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules insertNewRuleLst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ruleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules addNewRuleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "ruleLst" element
     */
    @Override
    public void removeRuleLst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "varLst" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet> getVarLstList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getVarLstArray,
                this::setVarLstArray,
                this::insertNewVarLst,
                this::removeVarLst,
                this::sizeOfVarLstArray
            );
        }
    }

    /**
     * Gets array of all "varLst" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet[] getVarLstArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet[0]);
    }

    /**
     * Gets ith "varLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet getVarLstArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "varLst" element
     */
    @Override
    public int sizeOfVarLstArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "varLst" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setVarLstArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet[] varLstArray) {
        check_orphaned();
        arraySetterHelper(varLstArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "varLst" element
     */
    @Override
    public void setVarLstArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet varLst) {
        generatedSetterHelperImpl(varLst, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "varLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet insertNewVarLst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "varLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet addNewVarLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "varLst" element
     */
    @Override
    public void removeVarLst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "forEach" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach> getForEachList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getForEachArray,
                this::setForEachArray,
                this::insertNewForEach,
                this::removeForEach,
                this::sizeOfForEachArray
            );
        }
    }

    /**
     * Gets array of all "forEach" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach[] getForEachArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach[0]);
    }

    /**
     * Gets ith "forEach" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach getForEachArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "forEach" element
     */
    @Override
    public int sizeOfForEachArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "forEach" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setForEachArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach[] forEachArray) {
        check_orphaned();
        arraySetterHelper(forEachArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "forEach" element
     */
    @Override
    public void setForEachArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach forEach) {
        generatedSetterHelperImpl(forEach, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "forEach" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach insertNewForEach(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "forEach" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach addNewForEach() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTForEach)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "forEach" element
     */
    @Override
    public void removeForEach(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "layoutNode" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode> getLayoutNodeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLayoutNodeArray,
                this::setLayoutNodeArray,
                this::insertNewLayoutNode,
                this::removeLayoutNode,
                this::sizeOfLayoutNodeArray
            );
        }
    }

    /**
     * Gets array of all "layoutNode" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode[] getLayoutNodeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode[0]);
    }

    /**
     * Gets ith "layoutNode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode getLayoutNodeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "layoutNode" element
     */
    @Override
    public int sizeOfLayoutNodeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "layoutNode" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLayoutNodeArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode[] layoutNodeArray) {
        check_orphaned();
        arraySetterHelper(layoutNodeArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "layoutNode" element
     */
    @Override
    public void setLayoutNodeArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode layoutNode) {
        generatedSetterHelperImpl(layoutNode, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "layoutNode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode insertNewLayoutNode(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "layoutNode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode addNewLayoutNode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "layoutNode" element
     */
    @Override
    public void removeLayoutNode(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "choose" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose> getChooseList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getChooseArray,
                this::setChooseArray,
                this::insertNewChoose,
                this::removeChoose,
                this::sizeOfChooseArray
            );
        }
    }

    /**
     * Gets array of all "choose" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose[] getChooseArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose[0]);
    }

    /**
     * Gets ith "choose" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose getChooseArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "choose" element
     */
    @Override
    public int sizeOfChooseArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "choose" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setChooseArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose[] chooseArray) {
        check_orphaned();
        arraySetterHelper(chooseArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "choose" element
     */
    @Override
    public void setChooseArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose choose) {
        generatedSetterHelperImpl(choose, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "choose" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose insertNewChoose(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "choose" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose addNewChoose() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTChoose)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "choose" element
     */
    @Override
    public void removeChoose(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "extLst" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList> getExtLstList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getExtLstArray,
                this::setExtLstArray,
                this::insertNewExtLst,
                this::removeExtLst,
                this::sizeOfExtLstArray
            );
        }
    }

    /**
     * Gets array of all "extLst" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList[] getExtLstArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList[0]);
    }

    /**
     * Gets ith "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLstArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "extLst" element
     */
    @Override
    public int sizeOfExtLstArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "extLst" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setExtLstArray(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList[] extLstArray) {
        check_orphaned();
        arraySetterHelper(extLstArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "extLst" element
     */
    @Override
    public void setExtLstArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList insertNewExtLst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "extLst" element
     */
    @Override
    public void removeExtLst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return target;
        }
    }

    /**
     * True if has "name" attribute
     */
    @Override
    public boolean isSetName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.apache.xmlbeans.XmlString name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(name);
        }
    }

    /**
     * Unsets the "name" attribute
     */
    @Override
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "styleLbl" attribute
     */
    @Override
    public java.lang.String getStyleLbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "styleLbl" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetStyleLbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return target;
        }
    }

    /**
     * True if has "styleLbl" attribute
     */
    @Override
    public boolean isSetStyleLbl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "styleLbl" attribute
     */
    @Override
    public void setStyleLbl(java.lang.String styleLbl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setStringValue(styleLbl);
        }
    }

    /**
     * Sets (as xml) the "styleLbl" attribute
     */
    @Override
    public void xsetStyleLbl(org.apache.xmlbeans.XmlString styleLbl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(styleLbl);
        }
    }

    /**
     * Unsets the "styleLbl" attribute
     */
    @Override
    public void unsetStyleLbl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "chOrder" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType.Enum getChOrder() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[12]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "chOrder" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType xgetChOrder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType)get_default_attribute_value(PROPERTY_QNAME[12]);
            }
            return target;
        }
    }

    /**
     * True if has "chOrder" attribute
     */
    @Override
    public boolean isSetChOrder() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "chOrder" attribute
     */
    @Override
    public void setChOrder(org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType.Enum chOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setEnumValue(chOrder);
        }
    }

    /**
     * Sets (as xml) the "chOrder" attribute
     */
    @Override
    public void xsetChOrder(org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType chOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STChildOrderType)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(chOrder);
        }
    }

    /**
     * Unsets the "chOrder" attribute
     */
    @Override
    public void unsetChOrder() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "moveWith" attribute
     */
    @Override
    public java.lang.String getMoveWith() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "moveWith" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetMoveWith() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return target;
        }
    }

    /**
     * True if has "moveWith" attribute
     */
    @Override
    public boolean isSetMoveWith() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "moveWith" attribute
     */
    @Override
    public void setMoveWith(java.lang.String moveWith) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setStringValue(moveWith);
        }
    }

    /**
     * Sets (as xml) the "moveWith" attribute
     */
    @Override
    public void xsetMoveWith(org.apache.xmlbeans.XmlString moveWith) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(moveWith);
        }
    }

    /**
     * Unsets the "moveWith" attribute
     */
    @Override
    public void unsetMoveWith() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }
}
