/*
 * XML Type:  CT_BuildList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BuildList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBuildList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbuildlistede3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "bldP" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph> getBldPList();

    /**
     * Gets array of all "bldP" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph[] getBldPArray();

    /**
     * Gets ith "bldP" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph getBldPArray(int i);

    /**
     * Returns number of "bldP" element
     */
    int sizeOfBldPArray();

    /**
     * Sets array of all "bldP" element
     */
    void setBldPArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph[] bldPArray);

    /**
     * Sets ith "bldP" element
     */
    void setBldPArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph bldP);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bldP" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph insertNewBldP(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "bldP" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildParagraph addNewBldP();

    /**
     * Removes the ith "bldP" element
     */
    void removeBldP(int i);

    /**
     * Gets a List of "bldDgm" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram> getBldDgmList();

    /**
     * Gets array of all "bldDgm" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram[] getBldDgmArray();

    /**
     * Gets ith "bldDgm" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram getBldDgmArray(int i);

    /**
     * Returns number of "bldDgm" element
     */
    int sizeOfBldDgmArray();

    /**
     * Sets array of all "bldDgm" element
     */
    void setBldDgmArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram[] bldDgmArray);

    /**
     * Sets ith "bldDgm" element
     */
    void setBldDgmArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram bldDgm);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bldDgm" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram insertNewBldDgm(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "bldDgm" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram addNewBldDgm();

    /**
     * Removes the ith "bldDgm" element
     */
    void removeBldDgm(int i);

    /**
     * Gets a List of "bldOleChart" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart> getBldOleChartList();

    /**
     * Gets array of all "bldOleChart" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart[] getBldOleChartArray();

    /**
     * Gets ith "bldOleChart" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart getBldOleChartArray(int i);

    /**
     * Returns number of "bldOleChart" element
     */
    int sizeOfBldOleChartArray();

    /**
     * Sets array of all "bldOleChart" element
     */
    void setBldOleChartArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart[] bldOleChartArray);

    /**
     * Sets ith "bldOleChart" element
     */
    void setBldOleChartArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart bldOleChart);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bldOleChart" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart insertNewBldOleChart(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "bldOleChart" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart addNewBldOleChart();

    /**
     * Removes the ith "bldOleChart" element
     */
    void removeBldOleChart(int i);

    /**
     * Gets a List of "bldGraphic" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild> getBldGraphicList();

    /**
     * Gets array of all "bldGraphic" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild[] getBldGraphicArray();

    /**
     * Gets ith "bldGraphic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild getBldGraphicArray(int i);

    /**
     * Returns number of "bldGraphic" element
     */
    int sizeOfBldGraphicArray();

    /**
     * Sets array of all "bldGraphic" element
     */
    void setBldGraphicArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild[] bldGraphicArray);

    /**
     * Sets ith "bldGraphic" element
     */
    void setBldGraphicArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild bldGraphic);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bldGraphic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild insertNewBldGraphic(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "bldGraphic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild addNewBldGraphic();

    /**
     * Removes the ith "bldGraphic" element
     */
    void removeBldGraphic(int i);
}
