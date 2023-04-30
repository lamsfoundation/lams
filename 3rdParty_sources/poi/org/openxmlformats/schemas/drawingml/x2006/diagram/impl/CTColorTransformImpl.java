/*
 * XML Type:  CT_ColorTransform
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ColorTransform(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTColorTransformImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform {
    private static final long serialVersionUID = 1L;

    public CTColorTransformImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "title"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "desc"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "catLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "styleLbl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "extLst"),
        new QName("", "uniqueId"),
        new QName("", "minVer"),
    };


    /**
     * Gets a List of "title" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName> getTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTitleArray,
                this::setTitleArray,
                this::insertNewTitle,
                this::removeTitle,
                this::sizeOfTitleArray
            );
        }
    }

    /**
     * Gets array of all "title" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName[] getTitleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName[0]);
    }

    /**
     * Gets ith "title" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName getTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "title" element
     */
    @Override
    public int sizeOfTitleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "title" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTitleArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName[] titleArray) {
        check_orphaned();
        arraySetterHelper(titleArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "title" element
     */
    @Override
    public void setTitleArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName title) {
        generatedSetterHelperImpl(title, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "title" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName insertNewTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "title" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName addNewTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTName)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "title" element
     */
    @Override
    public void removeTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "desc" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription> getDescList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDescArray,
                this::setDescArray,
                this::insertNewDesc,
                this::removeDesc,
                this::sizeOfDescArray
            );
        }
    }

    /**
     * Gets array of all "desc" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription[] getDescArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription[0]);
    }

    /**
     * Gets ith "desc" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription getDescArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "desc" element
     */
    @Override
    public int sizeOfDescArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "desc" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDescArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription[] descArray) {
        check_orphaned();
        arraySetterHelper(descArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "desc" element
     */
    @Override
    public void setDescArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription desc) {
        generatedSetterHelperImpl(desc, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "desc" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription insertNewDesc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "desc" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription addNewDesc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTDescription)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "desc" element
     */
    @Override
    public void removeDesc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets the "catLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategories getCatLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategories target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategories)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "catLst" element
     */
    @Override
    public boolean isSetCatLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "catLst" element
     */
    @Override
    public void setCatLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategories catLst) {
        generatedSetterHelperImpl(catLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "catLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategories addNewCatLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategories target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategories)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "catLst" element
     */
    @Override
    public void unsetCatLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets a List of "styleLbl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel> getStyleLblList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getStyleLblArray,
                this::setStyleLblArray,
                this::insertNewStyleLbl,
                this::removeStyleLbl,
                this::sizeOfStyleLblArray
            );
        }
    }

    /**
     * Gets array of all "styleLbl" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel[] getStyleLblArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel[0]);
    }

    /**
     * Gets ith "styleLbl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel getStyleLblArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "styleLbl" element
     */
    @Override
    public int sizeOfStyleLblArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "styleLbl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setStyleLblArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel[] styleLblArray) {
        check_orphaned();
        arraySetterHelper(styleLblArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "styleLbl" element
     */
    @Override
    public void setStyleLblArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel styleLbl) {
        generatedSetterHelperImpl(styleLbl, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "styleLbl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel insertNewStyleLbl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "styleLbl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel addNewStyleLbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "styleLbl" element
     */
    @Override
    public void removeStyleLbl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "uniqueId" attribute
     */
    @Override
    public java.lang.String getUniqueId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "uniqueId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetUniqueId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "uniqueId" attribute
     */
    @Override
    public boolean isSetUniqueId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "uniqueId" attribute
     */
    @Override
    public void setUniqueId(java.lang.String uniqueId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setStringValue(uniqueId);
        }
    }

    /**
     * Sets (as xml) the "uniqueId" attribute
     */
    @Override
    public void xsetUniqueId(org.apache.xmlbeans.XmlString uniqueId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(uniqueId);
        }
    }

    /**
     * Unsets the "uniqueId" attribute
     */
    @Override
    public void unsetUniqueId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "minVer" attribute
     */
    @Override
    public java.lang.String getMinVer() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "minVer" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetMinVer() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "minVer" attribute
     */
    @Override
    public boolean isSetMinVer() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "minVer" attribute
     */
    @Override
    public void setMinVer(java.lang.String minVer) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setStringValue(minVer);
        }
    }

    /**
     * Sets (as xml) the "minVer" attribute
     */
    @Override
    public void xsetMinVer(org.apache.xmlbeans.XmlString minVer) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(minVer);
        }
    }

    /**
     * Unsets the "minVer" attribute
     */
    @Override
    public void unsetMinVer() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}
