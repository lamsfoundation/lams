/*
 * XML Type:  CT_MeasureDimensionMap
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMap
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MeasureDimensionMap(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMeasureDimensionMap extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMap> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmeasuredimensionmap6b73type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "measureGroup" attribute
     */
    long getMeasureGroup();

    /**
     * Gets (as xml) the "measureGroup" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetMeasureGroup();

    /**
     * True if has "measureGroup" attribute
     */
    boolean isSetMeasureGroup();

    /**
     * Sets the "measureGroup" attribute
     */
    void setMeasureGroup(long measureGroup);

    /**
     * Sets (as xml) the "measureGroup" attribute
     */
    void xsetMeasureGroup(org.apache.xmlbeans.XmlUnsignedInt measureGroup);

    /**
     * Unsets the "measureGroup" attribute
     */
    void unsetMeasureGroup();

    /**
     * Gets the "dimension" attribute
     */
    long getDimension();

    /**
     * Gets (as xml) the "dimension" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetDimension();

    /**
     * True if has "dimension" attribute
     */
    boolean isSetDimension();

    /**
     * Sets the "dimension" attribute
     */
    void setDimension(long dimension);

    /**
     * Sets (as xml) the "dimension" attribute
     */
    void xsetDimension(org.apache.xmlbeans.XmlUnsignedInt dimension);

    /**
     * Unsets the "dimension" attribute
     */
    void unsetDimension();
}
