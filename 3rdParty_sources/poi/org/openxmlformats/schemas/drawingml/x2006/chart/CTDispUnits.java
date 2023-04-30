/*
 * XML Type:  CT_DispUnits
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnits
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DispUnits(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTDispUnits extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnits> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdispunitsaa99type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "custUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getCustUnit();

    /**
     * True if has "custUnit" element
     */
    boolean isSetCustUnit();

    /**
     * Sets the "custUnit" element
     */
    void setCustUnit(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble custUnit);

    /**
     * Appends and returns a new empty "custUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewCustUnit();

    /**
     * Unsets the "custUnit" element
     */
    void unsetCustUnit();

    /**
     * Gets the "builtInUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit getBuiltInUnit();

    /**
     * True if has "builtInUnit" element
     */
    boolean isSetBuiltInUnit();

    /**
     * Sets the "builtInUnit" element
     */
    void setBuiltInUnit(org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit builtInUnit);

    /**
     * Appends and returns a new empty "builtInUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBuiltInUnit addNewBuiltInUnit();

    /**
     * Unsets the "builtInUnit" element
     */
    void unsetBuiltInUnit();

    /**
     * Gets the "dispUnitsLbl" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl getDispUnitsLbl();

    /**
     * True if has "dispUnitsLbl" element
     */
    boolean isSetDispUnitsLbl();

    /**
     * Sets the "dispUnitsLbl" element
     */
    void setDispUnitsLbl(org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl dispUnitsLbl);

    /**
     * Appends and returns a new empty "dispUnitsLbl" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnitsLbl addNewDispUnitsLbl();

    /**
     * Unsets the "dispUnitsLbl" element
     */
    void unsetDispUnitsLbl();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
