/*
 * XML Type:  CT_Path2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Path2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPath2D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpath2d73d2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "close" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose> getCloseList();

    /**
     * Gets array of all "close" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose[] getCloseArray();

    /**
     * Gets ith "close" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose getCloseArray(int i);

    /**
     * Returns number of "close" element
     */
    int sizeOfCloseArray();

    /**
     * Sets array of all "close" element
     */
    void setCloseArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose[] closeArray);

    /**
     * Sets ith "close" element
     */
    void setCloseArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose close);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "close" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose insertNewClose(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "close" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose addNewClose();

    /**
     * Removes the ith "close" element
     */
    void removeClose(int i);

    /**
     * Gets a List of "moveTo" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo> getMoveToList();

    /**
     * Gets array of all "moveTo" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo[] getMoveToArray();

    /**
     * Gets ith "moveTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo getMoveToArray(int i);

    /**
     * Returns number of "moveTo" element
     */
    int sizeOfMoveToArray();

    /**
     * Sets array of all "moveTo" element
     */
    void setMoveToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo[] moveToArray);

    /**
     * Sets ith "moveTo" element
     */
    void setMoveToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo moveTo);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo insertNewMoveTo(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "moveTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo addNewMoveTo();

    /**
     * Removes the ith "moveTo" element
     */
    void removeMoveTo(int i);

    /**
     * Gets a List of "lnTo" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo> getLnToList();

    /**
     * Gets array of all "lnTo" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo[] getLnToArray();

    /**
     * Gets ith "lnTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo getLnToArray(int i);

    /**
     * Returns number of "lnTo" element
     */
    int sizeOfLnToArray();

    /**
     * Sets array of all "lnTo" element
     */
    void setLnToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo[] lnToArray);

    /**
     * Sets ith "lnTo" element
     */
    void setLnToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo lnTo);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lnTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo insertNewLnTo(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "lnTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo addNewLnTo();

    /**
     * Removes the ith "lnTo" element
     */
    void removeLnTo(int i);

    /**
     * Gets a List of "arcTo" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo> getArcToList();

    /**
     * Gets array of all "arcTo" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo[] getArcToArray();

    /**
     * Gets ith "arcTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo getArcToArray(int i);

    /**
     * Returns number of "arcTo" element
     */
    int sizeOfArcToArray();

    /**
     * Sets array of all "arcTo" element
     */
    void setArcToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo[] arcToArray);

    /**
     * Sets ith "arcTo" element
     */
    void setArcToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo arcTo);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "arcTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo insertNewArcTo(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "arcTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo addNewArcTo();

    /**
     * Removes the ith "arcTo" element
     */
    void removeArcTo(int i);

    /**
     * Gets a List of "quadBezTo" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo> getQuadBezToList();

    /**
     * Gets array of all "quadBezTo" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo[] getQuadBezToArray();

    /**
     * Gets ith "quadBezTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo getQuadBezToArray(int i);

    /**
     * Returns number of "quadBezTo" element
     */
    int sizeOfQuadBezToArray();

    /**
     * Sets array of all "quadBezTo" element
     */
    void setQuadBezToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo[] quadBezToArray);

    /**
     * Sets ith "quadBezTo" element
     */
    void setQuadBezToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo quadBezTo);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "quadBezTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo insertNewQuadBezTo(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "quadBezTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo addNewQuadBezTo();

    /**
     * Removes the ith "quadBezTo" element
     */
    void removeQuadBezTo(int i);

    /**
     * Gets a List of "cubicBezTo" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo> getCubicBezToList();

    /**
     * Gets array of all "cubicBezTo" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo[] getCubicBezToArray();

    /**
     * Gets ith "cubicBezTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo getCubicBezToArray(int i);

    /**
     * Returns number of "cubicBezTo" element
     */
    int sizeOfCubicBezToArray();

    /**
     * Sets array of all "cubicBezTo" element
     */
    void setCubicBezToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo[] cubicBezToArray);

    /**
     * Sets ith "cubicBezTo" element
     */
    void setCubicBezToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo cubicBezTo);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cubicBezTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo insertNewCubicBezTo(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cubicBezTo" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo addNewCubicBezTo();

    /**
     * Removes the ith "cubicBezTo" element
     */
    void removeCubicBezTo(int i);

    /**
     * Gets the "w" attribute
     */
    long getW();

    /**
     * Gets (as xml) the "w" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetW();

    /**
     * True if has "w" attribute
     */
    boolean isSetW();

    /**
     * Sets the "w" attribute
     */
    void setW(long w);

    /**
     * Sets (as xml) the "w" attribute
     */
    void xsetW(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate w);

    /**
     * Unsets the "w" attribute
     */
    void unsetW();

    /**
     * Gets the "h" attribute
     */
    long getH();

    /**
     * Gets (as xml) the "h" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetH();

    /**
     * True if has "h" attribute
     */
    boolean isSetH();

    /**
     * Sets the "h" attribute
     */
    void setH(long h);

    /**
     * Sets (as xml) the "h" attribute
     */
    void xsetH(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate h);

    /**
     * Unsets the "h" attribute
     */
    void unsetH();

    /**
     * Gets the "fill" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode.Enum getFill();

    /**
     * Gets (as xml) the "fill" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode xgetFill();

    /**
     * True if has "fill" attribute
     */
    boolean isSetFill();

    /**
     * Sets the "fill" attribute
     */
    void setFill(org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode.Enum fill);

    /**
     * Sets (as xml) the "fill" attribute
     */
    void xsetFill(org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode fill);

    /**
     * Unsets the "fill" attribute
     */
    void unsetFill();

    /**
     * Gets the "stroke" attribute
     */
    boolean getStroke();

    /**
     * Gets (as xml) the "stroke" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetStroke();

    /**
     * True if has "stroke" attribute
     */
    boolean isSetStroke();

    /**
     * Sets the "stroke" attribute
     */
    void setStroke(boolean stroke);

    /**
     * Sets (as xml) the "stroke" attribute
     */
    void xsetStroke(org.apache.xmlbeans.XmlBoolean stroke);

    /**
     * Unsets the "stroke" attribute
     */
    void unsetStroke();

    /**
     * Gets the "extrusionOk" attribute
     */
    boolean getExtrusionOk();

    /**
     * Gets (as xml) the "extrusionOk" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetExtrusionOk();

    /**
     * True if has "extrusionOk" attribute
     */
    boolean isSetExtrusionOk();

    /**
     * Sets the "extrusionOk" attribute
     */
    void setExtrusionOk(boolean extrusionOk);

    /**
     * Sets (as xml) the "extrusionOk" attribute
     */
    void xsetExtrusionOk(org.apache.xmlbeans.XmlBoolean extrusionOk);

    /**
     * Unsets the "extrusionOk" attribute
     */
    void unsetExtrusionOk();
}
