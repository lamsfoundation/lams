/*
 * XML Type:  CT_DiagramDefinition
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DiagramDefinition(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTDiagramDefinitionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition {
    private static final long serialVersionUID = 1L;

    public CTDiagramDefinitionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "title"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "desc"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "catLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "sampData"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "styleData"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "clrData"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "layoutNode"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "extLst"),
        new QName("", "uniqueId"),
        new QName("", "minVer"),
        new QName("", "defStyle"),
    };


    /**
     * Gets a List of "title" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTName> getTitleList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTName[] getTitleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTName[0]);
    }

    /**
     * Gets ith "title" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTName getTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTName)get_store().find_element_user(PROPERTY_QNAME[0], i);
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
    public void setTitleArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTName[] titleArray) {
        check_orphaned();
        arraySetterHelper(titleArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "title" element
     */
    @Override
    public void setTitleArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTName title) {
        generatedSetterHelperImpl(title, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "title" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTName insertNewTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTName)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "title" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTName addNewTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTName)get_store().add_element_user(PROPERTY_QNAME[0]);
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
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription> getDescList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription[] getDescArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription[0]);
    }

    /**
     * Gets ith "desc" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription getDescArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription)get_store().find_element_user(PROPERTY_QNAME[1], i);
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
    public void setDescArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription[] descArray) {
        check_orphaned();
        arraySetterHelper(descArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "desc" element
     */
    @Override
    public void setDescArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription desc) {
        generatedSetterHelperImpl(desc, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "desc" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription insertNewDesc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "desc" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription addNewDesc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription)get_store().add_element_user(PROPERTY_QNAME[1]);
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
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories getCatLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
    public void setCatLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories catLst) {
        generatedSetterHelperImpl(catLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "catLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories addNewCatLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories)get_store().add_element_user(PROPERTY_QNAME[2]);
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
     * Gets the "sampData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData getSampData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sampData" element
     */
    @Override
    public boolean isSetSampData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "sampData" element
     */
    @Override
    public void setSampData(org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData sampData) {
        generatedSetterHelperImpl(sampData, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sampData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData addNewSampData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "sampData" element
     */
    @Override
    public void unsetSampData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "styleData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData getStyleData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "styleData" element
     */
    @Override
    public boolean isSetStyleData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "styleData" element
     */
    @Override
    public void setStyleData(org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData styleData) {
        generatedSetterHelperImpl(styleData, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "styleData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData addNewStyleData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "styleData" element
     */
    @Override
    public void unsetStyleData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "clrData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData getClrData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "clrData" element
     */
    @Override
    public boolean isSetClrData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "clrData" element
     */
    @Override
    public void setClrData(org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData clrData) {
        generatedSetterHelperImpl(clrData, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData addNewClrData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "clrData" element
     */
    @Override
    public void unsetClrData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "layoutNode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode getLayoutNode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "layoutNode" element
     */
    @Override
    public void setLayoutNode(org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode layoutNode) {
        generatedSetterHelperImpl(layoutNode, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "layoutNode" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode addNewLayoutNode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[7], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_default_attribute_value(PROPERTY_QNAME[8]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[8]);
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
            get_store().remove_attribute(PROPERTY_QNAME[8]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[9]);
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
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "defStyle" attribute
     */
    @Override
    public java.lang.String getDefStyle() {
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
     * Gets (as xml) the "defStyle" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetDefStyle() {
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
     * True if has "defStyle" attribute
     */
    @Override
    public boolean isSetDefStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "defStyle" attribute
     */
    @Override
    public void setDefStyle(java.lang.String defStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setStringValue(defStyle);
        }
    }

    /**
     * Sets (as xml) the "defStyle" attribute
     */
    @Override
    public void xsetDefStyle(org.apache.xmlbeans.XmlString defStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(defStyle);
        }
    }

    /**
     * Unsets the "defStyle" attribute
     */
    @Override
    public void unsetDefStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }
}
