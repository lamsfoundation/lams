/*
 * XML Type:  CT_AnimationChartElement
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AnimationChartElement(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAnimationChartElement extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctanimationchartelemente953type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "seriesIdx" attribute
     */
    int getSeriesIdx();

    /**
     * Gets (as xml) the "seriesIdx" attribute
     */
    org.apache.xmlbeans.XmlInt xgetSeriesIdx();

    /**
     * True if has "seriesIdx" attribute
     */
    boolean isSetSeriesIdx();

    /**
     * Sets the "seriesIdx" attribute
     */
    void setSeriesIdx(int seriesIdx);

    /**
     * Sets (as xml) the "seriesIdx" attribute
     */
    void xsetSeriesIdx(org.apache.xmlbeans.XmlInt seriesIdx);

    /**
     * Unsets the "seriesIdx" attribute
     */
    void unsetSeriesIdx();

    /**
     * Gets the "categoryIdx" attribute
     */
    int getCategoryIdx();

    /**
     * Gets (as xml) the "categoryIdx" attribute
     */
    org.apache.xmlbeans.XmlInt xgetCategoryIdx();

    /**
     * True if has "categoryIdx" attribute
     */
    boolean isSetCategoryIdx();

    /**
     * Sets the "categoryIdx" attribute
     */
    void setCategoryIdx(int categoryIdx);

    /**
     * Sets (as xml) the "categoryIdx" attribute
     */
    void xsetCategoryIdx(org.apache.xmlbeans.XmlInt categoryIdx);

    /**
     * Unsets the "categoryIdx" attribute
     */
    void unsetCategoryIdx();

    /**
     * Gets the "bldStep" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep.Enum getBldStep();

    /**
     * Gets (as xml) the "bldStep" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep xgetBldStep();

    /**
     * Sets the "bldStep" attribute
     */
    void setBldStep(org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep.Enum bldStep);

    /**
     * Sets (as xml) the "bldStep" attribute
     */
    void xsetBldStep(org.openxmlformats.schemas.drawingml.x2006.main.STChartBuildStep bldStep);
}
