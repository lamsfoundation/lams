/*
 * XML Type:  CT_OMathPara
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_OMathPara(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public class CTOMathParaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara {
    private static final long serialVersionUID = 1L;

    public CTOMathParaImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "oMathParaPr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "oMath"),
    };


    /**
     * Gets the "oMathParaPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr getOMathParaPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "oMathParaPr" element
     */
    @Override
    public boolean isSetOMathParaPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "oMathParaPr" element
     */
    @Override
    public void setOMathParaPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr oMathParaPr) {
        generatedSetterHelperImpl(oMathParaPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "oMathParaPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr addNewOMathParaPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "oMathParaPr" element
     */
    @Override
    public void unsetOMathParaPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets a List of "oMath" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath> getOMathList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOMathArray,
                this::setOMathArray,
                this::insertNewOMath,
                this::removeOMath,
                this::sizeOfOMathArray
            );
        }
    }

    /**
     * Gets array of all "oMath" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[] getOMathArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[0]);
    }

    /**
     * Gets ith "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath getOMathArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "oMath" element
     */
    @Override
    public int sizeOfOMathArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "oMath" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOMathArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[] oMathArray) {
        check_orphaned();
        arraySetterHelper(oMathArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "oMath" element
     */
    @Override
    public void setOMathArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath oMath) {
        generatedSetterHelperImpl(oMath, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath insertNewOMath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "oMath" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath addNewOMath() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "oMath" element
     */
    @Override
    public void removeOMath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }
}
