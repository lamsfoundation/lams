/*
 * XML Type:  CT_DPt
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DPt(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTDPt extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTDPt> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdpt255etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "idx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getIdx();

    /**
     * Sets the "idx" element
     */
    void setIdx(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt idx);

    /**
     * Appends and returns a new empty "idx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewIdx();

    /**
     * Gets the "invertIfNegative" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getInvertIfNegative();

    /**
     * True if has "invertIfNegative" element
     */
    boolean isSetInvertIfNegative();

    /**
     * Sets the "invertIfNegative" element
     */
    void setInvertIfNegative(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean invertIfNegative);

    /**
     * Appends and returns a new empty "invertIfNegative" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewInvertIfNegative();

    /**
     * Unsets the "invertIfNegative" element
     */
    void unsetInvertIfNegative();

    /**
     * Gets the "marker" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker getMarker();

    /**
     * True if has "marker" element
     */
    boolean isSetMarker();

    /**
     * Sets the "marker" element
     */
    void setMarker(org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker marker);

    /**
     * Appends and returns a new empty "marker" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTMarker addNewMarker();

    /**
     * Unsets the "marker" element
     */
    void unsetMarker();

    /**
     * Gets the "bubble3D" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getBubble3D();

    /**
     * True if has "bubble3D" element
     */
    boolean isSetBubble3D();

    /**
     * Sets the "bubble3D" element
     */
    void setBubble3D(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean bubble3D);

    /**
     * Appends and returns a new empty "bubble3D" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewBubble3D();

    /**
     * Unsets the "bubble3D" element
     */
    void unsetBubble3D();

    /**
     * Gets the "explosion" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getExplosion();

    /**
     * True if has "explosion" element
     */
    boolean isSetExplosion();

    /**
     * Sets the "explosion" element
     */
    void setExplosion(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt explosion);

    /**
     * Appends and returns a new empty "explosion" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewExplosion();

    /**
     * Unsets the "explosion" element
     */
    void unsetExplosion();

    /**
     * Gets the "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties getSpPr();

    /**
     * True if has "spPr" element
     */
    boolean isSetSpPr();

    /**
     * Sets the "spPr" element
     */
    void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr);

    /**
     * Appends and returns a new empty "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr();

    /**
     * Unsets the "spPr" element
     */
    void unsetSpPr();

    /**
     * Gets the "pictureOptions" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions getPictureOptions();

    /**
     * True if has "pictureOptions" element
     */
    boolean isSetPictureOptions();

    /**
     * Sets the "pictureOptions" element
     */
    void setPictureOptions(org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions pictureOptions);

    /**
     * Appends and returns a new empty "pictureOptions" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTPictureOptions addNewPictureOptions();

    /**
     * Unsets the "pictureOptions" element
     */
    void unsetPictureOptions();

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
