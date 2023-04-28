/*
 * XML Type:  CT_CustomGeometry2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTCustomGeometry2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CustomGeometry2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCustomGeometry2D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTCustomGeometry2D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcustomgeometry2dca70type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "avLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList getAvLst();

    /**
     * True if has "avLst" element
     */
    boolean isSetAvLst();

    /**
     * Sets the "avLst" element
     */
    void setAvLst(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList avLst);

    /**
     * Appends and returns a new empty "avLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList addNewAvLst();

    /**
     * Unsets the "avLst" element
     */
    void unsetAvLst();

    /**
     * Gets the "gdLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList getGdLst();

    /**
     * True if has "gdLst" element
     */
    boolean isSetGdLst();

    /**
     * Sets the "gdLst" element
     */
    void setGdLst(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList gdLst);

    /**
     * Appends and returns a new empty "gdLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList addNewGdLst();

    /**
     * Unsets the "gdLst" element
     */
    void unsetGdLst();

    /**
     * Gets the "ahLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList getAhLst();

    /**
     * True if has "ahLst" element
     */
    boolean isSetAhLst();

    /**
     * Sets the "ahLst" element
     */
    void setAhLst(org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList ahLst);

    /**
     * Appends and returns a new empty "ahLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList addNewAhLst();

    /**
     * Unsets the "ahLst" element
     */
    void unsetAhLst();

    /**
     * Gets the "cxnLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList getCxnLst();

    /**
     * True if has "cxnLst" element
     */
    boolean isSetCxnLst();

    /**
     * Sets the "cxnLst" element
     */
    void setCxnLst(org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList cxnLst);

    /**
     * Appends and returns a new empty "cxnLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList addNewCxnLst();

    /**
     * Unsets the "cxnLst" element
     */
    void unsetCxnLst();

    /**
     * Gets the "rect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect getRect();

    /**
     * True if has "rect" element
     */
    boolean isSetRect();

    /**
     * Sets the "rect" element
     */
    void setRect(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect rect);

    /**
     * Appends and returns a new empty "rect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect addNewRect();

    /**
     * Unsets the "rect" element
     */
    void unsetRect();

    /**
     * Gets the "pathLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList getPathLst();

    /**
     * Sets the "pathLst" element
     */
    void setPathLst(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList pathLst);

    /**
     * Appends and returns a new empty "pathLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList addNewPathLst();
}
