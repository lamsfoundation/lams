/*
 * XML Type:  CT_AnimationElementChoice
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AnimationElementChoice(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAnimationElementChoice extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctanimationelementchoice9db4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dgm" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement getDgm();

    /**
     * True if has "dgm" element
     */
    boolean isSetDgm();

    /**
     * Sets the "dgm" element
     */
    void setDgm(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement dgm);

    /**
     * Appends and returns a new empty "dgm" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmElement addNewDgm();

    /**
     * Unsets the "dgm" element
     */
    void unsetDgm();

    /**
     * Gets the "chart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement getChart();

    /**
     * True if has "chart" element
     */
    boolean isSetChart();

    /**
     * Sets the "chart" element
     */
    void setChart(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement chart);

    /**
     * Appends and returns a new empty "chart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartElement addNewChart();

    /**
     * Unsets the "chart" element
     */
    void unsetChart();
}
