/*
 * XML Type:  CT_Path2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Path2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPath2DImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D {
    private static final long serialVersionUID = 1L;

    public CTPath2DImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "close"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "moveTo"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnTo"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "arcTo"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "quadBezTo"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cubicBezTo"),
        new QName("", "w"),
        new QName("", "h"),
        new QName("", "fill"),
        new QName("", "stroke"),
        new QName("", "extrusionOk"),
    };


    /**
     * Gets a List of "close" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose> getCloseList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCloseArray,
                this::setCloseArray,
                this::insertNewClose,
                this::removeClose,
                this::sizeOfCloseArray
            );
        }
    }

    /**
     * Gets array of all "close" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose[] getCloseArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose[0]);
    }

    /**
     * Gets ith "close" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose getCloseArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "close" element
     */
    @Override
    public int sizeOfCloseArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "close" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCloseArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose[] closeArray) {
        check_orphaned();
        arraySetterHelper(closeArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "close" element
     */
    @Override
    public void setCloseArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose close) {
        generatedSetterHelperImpl(close, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "close" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose insertNewClose(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "close" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose addNewClose() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DClose)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "close" element
     */
    @Override
    public void removeClose(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "moveTo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo> getMoveToList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMoveToArray,
                this::setMoveToArray,
                this::insertNewMoveTo,
                this::removeMoveTo,
                this::sizeOfMoveToArray
            );
        }
    }

    /**
     * Gets array of all "moveTo" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo[] getMoveToArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo[0]);
    }

    /**
     * Gets ith "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo getMoveToArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "moveTo" element
     */
    @Override
    public int sizeOfMoveToArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "moveTo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMoveToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo[] moveToArray) {
        check_orphaned();
        arraySetterHelper(moveToArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "moveTo" element
     */
    @Override
    public void setMoveToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo moveTo) {
        generatedSetterHelperImpl(moveTo, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo insertNewMoveTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "moveTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo addNewMoveTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "moveTo" element
     */
    @Override
    public void removeMoveTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "lnTo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo> getLnToList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLnToArray,
                this::setLnToArray,
                this::insertNewLnTo,
                this::removeLnTo,
                this::sizeOfLnToArray
            );
        }
    }

    /**
     * Gets array of all "lnTo" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo[] getLnToArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo[0]);
    }

    /**
     * Gets ith "lnTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo getLnToArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lnTo" element
     */
    @Override
    public int sizeOfLnToArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "lnTo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLnToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo[] lnToArray) {
        check_orphaned();
        arraySetterHelper(lnToArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "lnTo" element
     */
    @Override
    public void setLnToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo lnTo) {
        generatedSetterHelperImpl(lnTo, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lnTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo insertNewLnTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lnTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo addNewLnTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "lnTo" element
     */
    @Override
    public void removeLnTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "arcTo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo> getArcToList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getArcToArray,
                this::setArcToArray,
                this::insertNewArcTo,
                this::removeArcTo,
                this::sizeOfArcToArray
            );
        }
    }

    /**
     * Gets array of all "arcTo" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo[] getArcToArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo[0]);
    }

    /**
     * Gets ith "arcTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo getArcToArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "arcTo" element
     */
    @Override
    public int sizeOfArcToArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "arcTo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setArcToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo[] arcToArray) {
        check_orphaned();
        arraySetterHelper(arcToArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "arcTo" element
     */
    @Override
    public void setArcToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo arcTo) {
        generatedSetterHelperImpl(arcTo, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "arcTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo insertNewArcTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "arcTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo addNewArcTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "arcTo" element
     */
    @Override
    public void removeArcTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "quadBezTo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo> getQuadBezToList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getQuadBezToArray,
                this::setQuadBezToArray,
                this::insertNewQuadBezTo,
                this::removeQuadBezTo,
                this::sizeOfQuadBezToArray
            );
        }
    }

    /**
     * Gets array of all "quadBezTo" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo[] getQuadBezToArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo[0]);
    }

    /**
     * Gets ith "quadBezTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo getQuadBezToArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "quadBezTo" element
     */
    @Override
    public int sizeOfQuadBezToArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "quadBezTo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setQuadBezToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo[] quadBezToArray) {
        check_orphaned();
        arraySetterHelper(quadBezToArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "quadBezTo" element
     */
    @Override
    public void setQuadBezToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo quadBezTo) {
        generatedSetterHelperImpl(quadBezTo, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "quadBezTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo insertNewQuadBezTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "quadBezTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo addNewQuadBezTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DQuadBezierTo)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "quadBezTo" element
     */
    @Override
    public void removeQuadBezTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "cubicBezTo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo> getCubicBezToList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCubicBezToArray,
                this::setCubicBezToArray,
                this::insertNewCubicBezTo,
                this::removeCubicBezTo,
                this::sizeOfCubicBezToArray
            );
        }
    }

    /**
     * Gets array of all "cubicBezTo" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo[] getCubicBezToArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo[0]);
    }

    /**
     * Gets ith "cubicBezTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo getCubicBezToArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cubicBezTo" element
     */
    @Override
    public int sizeOfCubicBezToArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "cubicBezTo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCubicBezToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo[] cubicBezToArray) {
        check_orphaned();
        arraySetterHelper(cubicBezToArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "cubicBezTo" element
     */
    @Override
    public void setCubicBezToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo cubicBezTo) {
        generatedSetterHelperImpl(cubicBezTo, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cubicBezTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo insertNewCubicBezTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cubicBezTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo addNewCubicBezTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DCubicBezierTo)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "cubicBezTo" element
     */
    @Override
    public void removeCubicBezTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets the "w" attribute
     */
    @Override
    public long getW() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "w" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "w" attribute
     */
    @Override
    public boolean isSetW() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "w" attribute
     */
    @Override
    public void setW(long w) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setLongValue(w);
        }
    }

    /**
     * Sets (as xml) the "w" attribute
     */
    @Override
    public void xsetW(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate w) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(w);
        }
    }

    /**
     * Unsets the "w" attribute
     */
    @Override
    public void unsetW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "h" attribute
     */
    @Override
    public long getH() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "h" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetH() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return target;
        }
    }

    /**
     * True if has "h" attribute
     */
    @Override
    public boolean isSetH() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "h" attribute
     */
    @Override
    public void setH(long h) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setLongValue(h);
        }
    }

    /**
     * Sets (as xml) the "h" attribute
     */
    @Override
    public void xsetH(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate h) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(h);
        }
    }

    /**
     * Unsets the "h" attribute
     */
    @Override
    public void unsetH() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "fill" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode.Enum getFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "fill" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode xgetFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return target;
        }
    }

    /**
     * True if has "fill" attribute
     */
    @Override
    public boolean isSetFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "fill" attribute
     */
    @Override
    public void setFill(org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode.Enum fill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setEnumValue(fill);
        }
    }

    /**
     * Sets (as xml) the "fill" attribute
     */
    @Override
    public void xsetFill(org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode fill) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPathFillMode)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(fill);
        }
    }

    /**
     * Unsets the "fill" attribute
     */
    @Override
    public void unsetFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "stroke" attribute
     */
    @Override
    public boolean getStroke() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "stroke" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetStroke() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return target;
        }
    }

    /**
     * True if has "stroke" attribute
     */
    @Override
    public boolean isSetStroke() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "stroke" attribute
     */
    @Override
    public void setStroke(boolean stroke) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(stroke);
        }
    }

    /**
     * Sets (as xml) the "stroke" attribute
     */
    @Override
    public void xsetStroke(org.apache.xmlbeans.XmlBoolean stroke) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(stroke);
        }
    }

    /**
     * Unsets the "stroke" attribute
     */
    @Override
    public void unsetStroke() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "extrusionOk" attribute
     */
    @Override
    public boolean getExtrusionOk() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "extrusionOk" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetExtrusionOk() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return target;
        }
    }

    /**
     * True if has "extrusionOk" attribute
     */
    @Override
    public boolean isSetExtrusionOk() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "extrusionOk" attribute
     */
    @Override
    public void setExtrusionOk(boolean extrusionOk) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setBooleanValue(extrusionOk);
        }
    }

    /**
     * Sets (as xml) the "extrusionOk" attribute
     */
    @Override
    public void xsetExtrusionOk(org.apache.xmlbeans.XmlBoolean extrusionOk) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(extrusionOk);
        }
    }

    /**
     * Unsets the "extrusionOk" attribute
     */
    @Override
    public void unsetExtrusionOk() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }
}
