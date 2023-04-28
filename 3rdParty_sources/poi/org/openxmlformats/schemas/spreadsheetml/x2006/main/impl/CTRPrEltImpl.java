/*
 * XML Type:  CT_RPrElt
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRPrElt
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RPrElt(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTRPrEltImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRPrElt {
    private static final long serialVersionUID = 1L;

    public CTRPrEltImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rFont"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "charset"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "family"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "b"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "i"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "strike"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "outline"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "shadow"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "condense"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extend"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "color"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sz"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "u"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "vertAlign"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "scheme"),
    };


    /**
     * Gets a List of "rFont" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName> getRFontList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRFontArray,
                this::setRFontArray,
                this::insertNewRFont,
                this::removeRFont,
                this::sizeOfRFontArray
            );
        }
    }

    /**
     * Gets array of all "rFont" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName[] getRFontArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName[0]);
    }

    /**
     * Gets ith "rFont" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName getRFontArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rFont" element
     */
    @Override
    public int sizeOfRFontArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "rFont" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRFontArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName[] rFontArray) {
        check_orphaned();
        arraySetterHelper(rFontArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "rFont" element
     */
    @Override
    public void setRFontArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName rFont) {
        generatedSetterHelperImpl(rFont, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rFont" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName insertNewRFont(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rFont" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName addNewRFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontName)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "rFont" element
     */
    @Override
    public void removeRFont(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "charset" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty> getCharsetList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCharsetArray,
                this::setCharsetArray,
                this::insertNewCharset,
                this::removeCharset,
                this::sizeOfCharsetArray
            );
        }
    }

    /**
     * Gets array of all "charset" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty[] getCharsetArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty[0]);
    }

    /**
     * Gets ith "charset" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty getCharsetArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "charset" element
     */
    @Override
    public int sizeOfCharsetArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "charset" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCharsetArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty[] charsetArray) {
        check_orphaned();
        arraySetterHelper(charsetArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "charset" element
     */
    @Override
    public void setCharsetArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty charset) {
        generatedSetterHelperImpl(charset, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "charset" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty insertNewCharset(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "charset" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty addNewCharset() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "charset" element
     */
    @Override
    public void removeCharset(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "family" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty> getFamilyList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFamilyArray,
                this::setFamilyArray,
                this::insertNewFamily,
                this::removeFamily,
                this::sizeOfFamilyArray
            );
        }
    }

    /**
     * Gets array of all "family" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty[] getFamilyArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty[0]);
    }

    /**
     * Gets ith "family" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty getFamilyArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "family" element
     */
    @Override
    public int sizeOfFamilyArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "family" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFamilyArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty[] familyArray) {
        check_orphaned();
        arraySetterHelper(familyArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "family" element
     */
    @Override
    public void setFamilyArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty family) {
        generatedSetterHelperImpl(family, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "family" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty insertNewFamily(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "family" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty addNewFamily() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIntProperty)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "family" element
     */
    @Override
    public void removeFamily(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "b" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty> getBList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBArray,
                this::setBArray,
                this::insertNewB,
                this::removeB,
                this::sizeOfBArray
            );
        }
    }

    /**
     * Gets array of all "b" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] getBArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[0]);
    }

    /**
     * Gets ith "b" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty getBArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "b" element
     */
    @Override
    public int sizeOfBArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "b" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] bArray) {
        check_orphaned();
        arraySetterHelper(bArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "b" element
     */
    @Override
    public void setBArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty b) {
        generatedSetterHelperImpl(b, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "b" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty insertNewB(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "b" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty addNewB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "b" element
     */
    @Override
    public void removeB(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "i" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty> getIList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getIArray,
                this::setIArray,
                this::insertNewI,
                this::removeI,
                this::sizeOfIArray
            );
        }
    }

    /**
     * Gets array of all "i" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] getIArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[0]);
    }

    /**
     * Gets ith "i" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty getIArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "i" element
     */
    @Override
    public int sizeOfIArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "i" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setIArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] iValueArray) {
        check_orphaned();
        arraySetterHelper(iValueArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "i" element
     */
    @Override
    public void setIArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty iValue) {
        generatedSetterHelperImpl(iValue, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "i" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty insertNewI(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "i" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty addNewI() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "i" element
     */
    @Override
    public void removeI(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "strike" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty> getStrikeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getStrikeArray,
                this::setStrikeArray,
                this::insertNewStrike,
                this::removeStrike,
                this::sizeOfStrikeArray
            );
        }
    }

    /**
     * Gets array of all "strike" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] getStrikeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[0]);
    }

    /**
     * Gets ith "strike" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty getStrikeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "strike" element
     */
    @Override
    public int sizeOfStrikeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "strike" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setStrikeArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] strikeArray) {
        check_orphaned();
        arraySetterHelper(strikeArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "strike" element
     */
    @Override
    public void setStrikeArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty strike) {
        generatedSetterHelperImpl(strike, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "strike" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty insertNewStrike(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "strike" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty addNewStrike() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "strike" element
     */
    @Override
    public void removeStrike(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "outline" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty> getOutlineList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOutlineArray,
                this::setOutlineArray,
                this::insertNewOutline,
                this::removeOutline,
                this::sizeOfOutlineArray
            );
        }
    }

    /**
     * Gets array of all "outline" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] getOutlineArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[0]);
    }

    /**
     * Gets ith "outline" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty getOutlineArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "outline" element
     */
    @Override
    public int sizeOfOutlineArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "outline" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOutlineArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] outlineArray) {
        check_orphaned();
        arraySetterHelper(outlineArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "outline" element
     */
    @Override
    public void setOutlineArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty outline) {
        generatedSetterHelperImpl(outline, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "outline" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty insertNewOutline(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "outline" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty addNewOutline() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "outline" element
     */
    @Override
    public void removeOutline(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "shadow" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty> getShadowList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getShadowArray,
                this::setShadowArray,
                this::insertNewShadow,
                this::removeShadow,
                this::sizeOfShadowArray
            );
        }
    }

    /**
     * Gets array of all "shadow" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] getShadowArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[0]);
    }

    /**
     * Gets ith "shadow" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty getShadowArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "shadow" element
     */
    @Override
    public int sizeOfShadowArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "shadow" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setShadowArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] shadowArray) {
        check_orphaned();
        arraySetterHelper(shadowArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "shadow" element
     */
    @Override
    public void setShadowArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty shadow) {
        generatedSetterHelperImpl(shadow, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "shadow" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty insertNewShadow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "shadow" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty addNewShadow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "shadow" element
     */
    @Override
    public void removeShadow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "condense" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty> getCondenseList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCondenseArray,
                this::setCondenseArray,
                this::insertNewCondense,
                this::removeCondense,
                this::sizeOfCondenseArray
            );
        }
    }

    /**
     * Gets array of all "condense" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] getCondenseArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[0]);
    }

    /**
     * Gets ith "condense" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty getCondenseArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "condense" element
     */
    @Override
    public int sizeOfCondenseArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "condense" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCondenseArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] condenseArray) {
        check_orphaned();
        arraySetterHelper(condenseArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "condense" element
     */
    @Override
    public void setCondenseArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty condense) {
        generatedSetterHelperImpl(condense, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "condense" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty insertNewCondense(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "condense" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty addNewCondense() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "condense" element
     */
    @Override
    public void removeCondense(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "extend" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty> getExtendList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getExtendArray,
                this::setExtendArray,
                this::insertNewExtend,
                this::removeExtend,
                this::sizeOfExtendArray
            );
        }
    }

    /**
     * Gets array of all "extend" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] getExtendArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[0]);
    }

    /**
     * Gets ith "extend" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty getExtendArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "extend" element
     */
    @Override
    public int sizeOfExtendArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "extend" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setExtendArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty[] extendArray) {
        check_orphaned();
        arraySetterHelper(extendArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "extend" element
     */
    @Override
    public void setExtendArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty extend) {
        generatedSetterHelperImpl(extend, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "extend" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty insertNewExtend(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "extend" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty addNewExtend() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBooleanProperty)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "extend" element
     */
    @Override
    public void removeExtend(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "color" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor> getColorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getColorArray,
                this::setColorArray,
                this::insertNewColor,
                this::removeColor,
                this::sizeOfColorArray
            );
        }
    }

    /**
     * Gets array of all "color" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor[] getColorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor[0]);
    }

    /**
     * Gets ith "color" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor getColorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "color" element
     */
    @Override
    public int sizeOfColorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "color" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setColorArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor[] colorArray) {
        check_orphaned();
        arraySetterHelper(colorArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "color" element
     */
    @Override
    public void setColorArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor color) {
        generatedSetterHelperImpl(color, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "color" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor insertNewColor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "color" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor addNewColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "color" element
     */
    @Override
    public void removeColor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "sz" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize> getSzList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSzArray,
                this::setSzArray,
                this::insertNewSz,
                this::removeSz,
                this::sizeOfSzArray
            );
        }
    }

    /**
     * Gets array of all "sz" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize[] getSzArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize[0]);
    }

    /**
     * Gets ith "sz" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize getSzArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sz" element
     */
    @Override
    public int sizeOfSzArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "sz" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSzArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize[] szArray) {
        check_orphaned();
        arraySetterHelper(szArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "sz" element
     */
    @Override
    public void setSzArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize sz) {
        generatedSetterHelperImpl(sz, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sz" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize insertNewSz(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sz" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize addNewSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontSize)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "sz" element
     */
    @Override
    public void removeSz(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "u" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty> getUList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getUArray,
                this::setUArray,
                this::insertNewU,
                this::removeU,
                this::sizeOfUArray
            );
        }
    }

    /**
     * Gets array of all "u" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty[] getUArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty[0]);
    }

    /**
     * Gets ith "u" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty getUArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "u" element
     */
    @Override
    public int sizeOfUArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "u" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setUArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty[] uArray) {
        check_orphaned();
        arraySetterHelper(uArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "u" element
     */
    @Override
    public void setUArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty u) {
        generatedSetterHelperImpl(u, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "u" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty insertNewU(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "u" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty addNewU() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUnderlineProperty)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "u" element
     */
    @Override
    public void removeU(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }

    /**
     * Gets a List of "vertAlign" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty> getVertAlignList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getVertAlignArray,
                this::setVertAlignArray,
                this::insertNewVertAlign,
                this::removeVertAlign,
                this::sizeOfVertAlignArray
            );
        }
    }

    /**
     * Gets array of all "vertAlign" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty[] getVertAlignArray() {
        return getXmlObjectArray(PROPERTY_QNAME[13], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty[0]);
    }

    /**
     * Gets ith "vertAlign" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty getVertAlignArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "vertAlign" element
     */
    @Override
    public int sizeOfVertAlignArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "vertAlign" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setVertAlignArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty[] vertAlignArray) {
        check_orphaned();
        arraySetterHelper(vertAlignArray, PROPERTY_QNAME[13]);
    }

    /**
     * Sets ith "vertAlign" element
     */
    @Override
    public void setVertAlignArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty vertAlign) {
        generatedSetterHelperImpl(vertAlign, PROPERTY_QNAME[13], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "vertAlign" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty insertNewVertAlign(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "vertAlign" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty addNewVertAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVerticalAlignFontProperty)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Removes the ith "vertAlign" element
     */
    @Override
    public void removeVertAlign(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], i);
        }
    }

    /**
     * Gets a List of "scheme" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme> getSchemeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSchemeArray,
                this::setSchemeArray,
                this::insertNewScheme,
                this::removeScheme,
                this::sizeOfSchemeArray
            );
        }
    }

    /**
     * Gets array of all "scheme" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme[] getSchemeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[14], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme[0]);
    }

    /**
     * Gets ith "scheme" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme getSchemeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "scheme" element
     */
    @Override
    public int sizeOfSchemeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "scheme" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSchemeArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme[] schemeArray) {
        check_orphaned();
        arraySetterHelper(schemeArray, PROPERTY_QNAME[14]);
    }

    /**
     * Sets ith "scheme" element
     */
    @Override
    public void setSchemeArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme scheme) {
        generatedSetterHelperImpl(scheme, PROPERTY_QNAME[14], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "scheme" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme insertNewScheme(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "scheme" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme addNewScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFontScheme)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Removes the ith "scheme" element
     */
    @Override
    public void removeScheme(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], i);
        }
    }
}
