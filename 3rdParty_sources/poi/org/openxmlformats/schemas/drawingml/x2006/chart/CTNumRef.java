/*
 * XML Type:  CT_NumRef
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumRef(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTNumRef extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTNumRef> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumref062ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "f" element
     */
    java.lang.String getF();

    /**
     * Gets (as xml) the "f" element
     */
    org.apache.xmlbeans.XmlString xgetF();

    /**
     * Sets the "f" element
     */
    void setF(java.lang.String f);

    /**
     * Sets (as xml) the "f" element
     */
    void xsetF(org.apache.xmlbeans.XmlString f);

    /**
     * Gets the "numCache" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData getNumCache();

    /**
     * True if has "numCache" element
     */
    boolean isSetNumCache();

    /**
     * Sets the "numCache" element
     */
    void setNumCache(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData numCache);

    /**
     * Appends and returns a new empty "numCache" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumData addNewNumCache();

    /**
     * Unsets the "numCache" element
     */
    void unsetNumCache();

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
