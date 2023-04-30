/*
 * XML Type:  CT_BuildList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_BuildList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTBuildListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList {
    private static final long serialVersionUID = 1L;

    public CTBuildListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bldP"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bldDgm"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bldOleChart"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bldGraphic"),
    };


    /**
     * Gets a List of "bldP" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph> getBldPList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBldPArray,
                this::setBldPArray,
                this::insertNewBldP,
                this::removeBldP,
                this::sizeOfBldPArray
            );
        }
    }

    /**
     * Gets array of all "bldP" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph[] getBldPArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph[0]);
    }

    /**
     * Gets ith "bldP" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph getBldPArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bldP" element
     */
    @Override
    public int sizeOfBldPArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "bldP" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBldPArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph[] bldPArray) {
        check_orphaned();
        arraySetterHelper(bldPArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "bldP" element
     */
    @Override
    public void setBldPArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph bldP) {
        generatedSetterHelperImpl(bldP, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bldP" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph insertNewBldP(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bldP" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph addNewBldP() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "bldP" element
     */
    @Override
    public void removeBldP(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "bldDgm" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram> getBldDgmList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBldDgmArray,
                this::setBldDgmArray,
                this::insertNewBldDgm,
                this::removeBldDgm,
                this::sizeOfBldDgmArray
            );
        }
    }

    /**
     * Gets array of all "bldDgm" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram[] getBldDgmArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram[0]);
    }

    /**
     * Gets ith "bldDgm" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram getBldDgmArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bldDgm" element
     */
    @Override
    public int sizeOfBldDgmArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "bldDgm" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBldDgmArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram[] bldDgmArray) {
        check_orphaned();
        arraySetterHelper(bldDgmArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "bldDgm" element
     */
    @Override
    public void setBldDgmArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram bldDgm) {
        generatedSetterHelperImpl(bldDgm, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bldDgm" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram insertNewBldDgm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bldDgm" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram addNewBldDgm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "bldDgm" element
     */
    @Override
    public void removeBldDgm(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "bldOleChart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart> getBldOleChartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBldOleChartArray,
                this::setBldOleChartArray,
                this::insertNewBldOleChart,
                this::removeBldOleChart,
                this::sizeOfBldOleChartArray
            );
        }
    }

    /**
     * Gets array of all "bldOleChart" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart[] getBldOleChartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart[0]);
    }

    /**
     * Gets ith "bldOleChart" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart getBldOleChartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bldOleChart" element
     */
    @Override
    public int sizeOfBldOleChartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "bldOleChart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBldOleChartArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart[] bldOleChartArray) {
        check_orphaned();
        arraySetterHelper(bldOleChartArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "bldOleChart" element
     */
    @Override
    public void setBldOleChartArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart bldOleChart) {
        generatedSetterHelperImpl(bldOleChart, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bldOleChart" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart insertNewBldOleChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bldOleChart" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart addNewBldOleChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "bldOleChart" element
     */
    @Override
    public void removeBldOleChart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "bldGraphic" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild> getBldGraphicList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBldGraphicArray,
                this::setBldGraphicArray,
                this::insertNewBldGraphic,
                this::removeBldGraphic,
                this::sizeOfBldGraphicArray
            );
        }
    }

    /**
     * Gets array of all "bldGraphic" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild[] getBldGraphicArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild[0]);
    }

    /**
     * Gets ith "bldGraphic" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild getBldGraphicArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "bldGraphic" element
     */
    @Override
    public int sizeOfBldGraphicArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "bldGraphic" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBldGraphicArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild[] bldGraphicArray) {
        check_orphaned();
        arraySetterHelper(bldGraphicArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "bldGraphic" element
     */
    @Override
    public void setBldGraphicArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild bldGraphic) {
        generatedSetterHelperImpl(bldGraphic, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bldGraphic" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild insertNewBldGraphic(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "bldGraphic" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild addNewBldGraphic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "bldGraphic" element
     */
    @Override
    public void removeBldGraphic(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }
}
