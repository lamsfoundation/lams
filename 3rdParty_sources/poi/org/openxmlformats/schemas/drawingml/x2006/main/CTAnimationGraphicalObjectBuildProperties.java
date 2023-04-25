/*
 * XML Type:  CT_AnimationGraphicalObjectBuildProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AnimationGraphicalObjectBuildProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAnimationGraphicalObjectBuildProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctanimationgraphicalobjectbuildpropertiesd944type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bldDgm" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties getBldDgm();

    /**
     * True if has "bldDgm" element
     */
    boolean isSetBldDgm();

    /**
     * Sets the "bldDgm" element
     */
    void setBldDgm(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties bldDgm);

    /**
     * Appends and returns a new empty "bldDgm" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties addNewBldDgm();

    /**
     * Unsets the "bldDgm" element
     */
    void unsetBldDgm();

    /**
     * Gets the "bldChart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties getBldChart();

    /**
     * True if has "bldChart" element
     */
    boolean isSetBldChart();

    /**
     * Sets the "bldChart" element
     */
    void setBldChart(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties bldChart);

    /**
     * Appends and returns a new empty "bldChart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties addNewBldChart();

    /**
     * Unsets the "bldChart" element
     */
    void unsetBldChart();
}
