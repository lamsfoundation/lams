/*
 * XML Type:  CT_PivotDimension
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotDimension
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PivotDimension(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPivotDimension extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotDimension> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpivotdimension6d4dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "measure" attribute
     */
    boolean getMeasure();

    /**
     * Gets (as xml) the "measure" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetMeasure();

    /**
     * True if has "measure" attribute
     */
    boolean isSetMeasure();

    /**
     * Sets the "measure" attribute
     */
    void setMeasure(boolean measure);

    /**
     * Sets (as xml) the "measure" attribute
     */
    void xsetMeasure(org.apache.xmlbeans.XmlBoolean measure);

    /**
     * Unsets the "measure" attribute
     */
    void unsetMeasure();

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name);

    /**
     * Gets the "uniqueName" attribute
     */
    java.lang.String getUniqueName();

    /**
     * Gets (as xml) the "uniqueName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetUniqueName();

    /**
     * Sets the "uniqueName" attribute
     */
    void setUniqueName(java.lang.String uniqueName);

    /**
     * Sets (as xml) the "uniqueName" attribute
     */
    void xsetUniqueName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring uniqueName);

    /**
     * Gets the "caption" attribute
     */
    java.lang.String getCaption();

    /**
     * Gets (as xml) the "caption" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetCaption();

    /**
     * Sets the "caption" attribute
     */
    void setCaption(java.lang.String caption);

    /**
     * Sets (as xml) the "caption" attribute
     */
    void xsetCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring caption);
}
