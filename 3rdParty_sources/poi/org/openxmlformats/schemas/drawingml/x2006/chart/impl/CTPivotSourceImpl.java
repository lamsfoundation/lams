/*
 * XML Type:  CT_PivotSource
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PivotSource(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTPivotSourceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource {
    private static final long serialVersionUID = 1L;

    public CTPivotSourceImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "name"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "fmtId"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "name" element
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "name" element
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" element
     */
    @Override
    public void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(name);
        }
    }

    /**
     * Gets the "fmtId" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "fmtId" element
     */
    @Override
    public void setFmtId(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt fmtId) {
        generatedSetterHelperImpl(fmtId, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fmtId" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewFmtId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets a List of "extLst" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList> getExtLstList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList[] getExtLstArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList[0]);
    }

    /**
     * Gets ith "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLstArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[2], i);
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
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "extLst" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setExtLstArray(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList[] extLstArray) {
        check_orphaned();
        arraySetterHelper(extLstArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "extLst" element
     */
    @Override
    public void setExtLstArray(int i, org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList insertNewExtLst(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }
}
