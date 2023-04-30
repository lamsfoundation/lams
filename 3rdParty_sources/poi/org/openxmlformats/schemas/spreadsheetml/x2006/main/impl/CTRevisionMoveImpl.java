/*
 * XML Type:  CT_RevisionMove
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RevisionMove(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTRevisionMoveImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionMove {
    private static final long serialVersionUID = 1L;

    public CTRevisionMoveImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "undo"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rcc"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rfmt"),
        new QName("", "rId"),
        new QName("", "ua"),
        new QName("", "ra"),
        new QName("", "sheetId"),
        new QName("", "source"),
        new QName("", "destination"),
        new QName("", "sourceSheetId"),
    };


    /**
     * Gets a List of "undo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo> getUndoList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getUndoArray,
                this::setUndoArray,
                this::insertNewUndo,
                this::removeUndo,
                this::sizeOfUndoArray
            );
        }
    }

    /**
     * Gets array of all "undo" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo[] getUndoArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo[0]);
    }

    /**
     * Gets ith "undo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo getUndoArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "undo" element
     */
    @Override
    public int sizeOfUndoArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "undo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setUndoArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo[] undoArray) {
        check_orphaned();
        arraySetterHelper(undoArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "undo" element
     */
    @Override
    public void setUndoArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo undo) {
        generatedSetterHelperImpl(undo, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "undo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo insertNewUndo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "undo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo addNewUndo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUndoInfo)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "undo" element
     */
    @Override
    public void removeUndo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "rcc" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange> getRccList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRccArray,
                this::setRccArray,
                this::insertNewRcc,
                this::removeRcc,
                this::sizeOfRccArray
            );
        }
    }

    /**
     * Gets array of all "rcc" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange[] getRccArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange[0]);
    }

    /**
     * Gets ith "rcc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange getRccArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rcc" element
     */
    @Override
    public int sizeOfRccArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "rcc" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRccArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange[] rccArray) {
        check_orphaned();
        arraySetterHelper(rccArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "rcc" element
     */
    @Override
    public void setRccArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange rcc) {
        generatedSetterHelperImpl(rcc, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rcc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange insertNewRcc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rcc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange addNewRcc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCellChange)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "rcc" element
     */
    @Override
    public void removeRcc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "rfmt" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting> getRfmtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRfmtArray,
                this::setRfmtArray,
                this::insertNewRfmt,
                this::removeRfmt,
                this::sizeOfRfmtArray
            );
        }
    }

    /**
     * Gets array of all "rfmt" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting[] getRfmtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting[0]);
    }

    /**
     * Gets ith "rfmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting getRfmtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rfmt" element
     */
    @Override
    public int sizeOfRfmtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "rfmt" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRfmtArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting[] rfmtArray) {
        check_orphaned();
        arraySetterHelper(rfmtArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "rfmt" element
     */
    @Override
    public void setRfmtArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting rfmt) {
        generatedSetterHelperImpl(rfmt, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rfmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting insertNewRfmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rfmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting addNewRfmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionFormatting)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "rfmt" element
     */
    @Override
    public void removeRfmt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets the "rId" attribute
     */
    @Override
    public long getRId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "rId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetRId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "rId" attribute
     */
    @Override
    public void setRId(long rId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setLongValue(rId);
        }
    }

    /**
     * Sets (as xml) the "rId" attribute
     */
    @Override
    public void xsetRId(org.apache.xmlbeans.XmlUnsignedInt rId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(rId);
        }
    }

    /**
     * Gets the "ua" attribute
     */
    @Override
    public boolean getUa() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "ua" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetUa() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "ua" attribute
     */
    @Override
    public boolean isSetUa() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "ua" attribute
     */
    @Override
    public void setUa(boolean ua) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(ua);
        }
    }

    /**
     * Sets (as xml) the "ua" attribute
     */
    @Override
    public void xsetUa(org.apache.xmlbeans.XmlBoolean ua) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(ua);
        }
    }

    /**
     * Unsets the "ua" attribute
     */
    @Override
    public void unsetUa() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "ra" attribute
     */
    @Override
    public boolean getRa() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "ra" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRa() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "ra" attribute
     */
    @Override
    public boolean isSetRa() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "ra" attribute
     */
    @Override
    public void setRa(boolean ra) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setBooleanValue(ra);
        }
    }

    /**
     * Sets (as xml) the "ra" attribute
     */
    @Override
    public void xsetRa(org.apache.xmlbeans.XmlBoolean ra) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(ra);
        }
    }

    /**
     * Unsets the "ra" attribute
     */
    @Override
    public void unsetRa() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "sheetId" attribute
     */
    @Override
    public long getSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "sheetId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Sets the "sheetId" attribute
     */
    @Override
    public void setSheetId(long sheetId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setLongValue(sheetId);
        }
    }

    /**
     * Sets (as xml) the "sheetId" attribute
     */
    @Override
    public void xsetSheetId(org.apache.xmlbeans.XmlUnsignedInt sheetId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(sheetId);
        }
    }

    /**
     * Gets the "source" attribute
     */
    @Override
    public java.lang.String getSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "source" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef xgetSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Sets the "source" attribute
     */
    @Override
    public void setSource(java.lang.String source) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setStringValue(source);
        }
    }

    /**
     * Sets (as xml) the "source" attribute
     */
    @Override
    public void xsetSource(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef source) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(source);
        }
    }

    /**
     * Gets the "destination" attribute
     */
    @Override
    public java.lang.String getDestination() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "destination" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef xgetDestination() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Sets the "destination" attribute
     */
    @Override
    public void setDestination(java.lang.String destination) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setStringValue(destination);
        }
    }

    /**
     * Sets (as xml) the "destination" attribute
     */
    @Override
    public void xsetDestination(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef destination) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(destination);
        }
    }

    /**
     * Gets the "sourceSheetId" attribute
     */
    @Override
    public long getSourceSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "sourceSheetId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetSourceSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return target;
        }
    }

    /**
     * True if has "sourceSheetId" attribute
     */
    @Override
    public boolean isSetSourceSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "sourceSheetId" attribute
     */
    @Override
    public void setSourceSheetId(long sourceSheetId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setLongValue(sourceSheetId);
        }
    }

    /**
     * Sets (as xml) the "sourceSheetId" attribute
     */
    @Override
    public void xsetSourceSheetId(org.apache.xmlbeans.XmlUnsignedInt sourceSheetId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(sourceSheetId);
        }
    }

    /**
     * Unsets the "sourceSheetId" attribute
     */
    @Override
    public void unsetSourceSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }
}
