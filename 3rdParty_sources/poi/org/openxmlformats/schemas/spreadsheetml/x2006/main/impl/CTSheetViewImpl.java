/*
 * XML Type:  CT_SheetView
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SheetView(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTSheetViewImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView {
    private static final long serialVersionUID = 1L;

    public CTSheetViewImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pane"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "selection"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pivotSelection"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
        new QName("", "windowProtection"),
        new QName("", "showFormulas"),
        new QName("", "showGridLines"),
        new QName("", "showRowColHeaders"),
        new QName("", "showZeros"),
        new QName("", "rightToLeft"),
        new QName("", "tabSelected"),
        new QName("", "showRuler"),
        new QName("", "showOutlineSymbols"),
        new QName("", "defaultGridColor"),
        new QName("", "showWhiteSpace"),
        new QName("", "view"),
        new QName("", "topLeftCell"),
        new QName("", "colorId"),
        new QName("", "zoomScale"),
        new QName("", "zoomScaleNormal"),
        new QName("", "zoomScaleSheetLayoutView"),
        new QName("", "zoomScalePageLayoutView"),
        new QName("", "workbookViewId"),
    };


    /**
     * Gets the "pane" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane getPane() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pane" element
     */
    @Override
    public boolean isSetPane() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "pane" element
     */
    @Override
    public void setPane(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane pane) {
        generatedSetterHelperImpl(pane, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pane" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane addNewPane() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "pane" element
     */
    @Override
    public void unsetPane() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets a List of "selection" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection> getSelectionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSelectionArray,
                this::setSelectionArray,
                this::insertNewSelection,
                this::removeSelection,
                this::sizeOfSelectionArray
            );
        }
    }

    /**
     * Gets array of all "selection" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection[] getSelectionArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection[0]);
    }

    /**
     * Gets ith "selection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection getSelectionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "selection" element
     */
    @Override
    public int sizeOfSelectionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "selection" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSelectionArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection[] selectionArray) {
        check_orphaned();
        arraySetterHelper(selectionArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "selection" element
     */
    @Override
    public void setSelectionArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection selection) {
        generatedSetterHelperImpl(selection, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "selection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection insertNewSelection(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "selection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection addNewSelection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "selection" element
     */
    @Override
    public void removeSelection(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "pivotSelection" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection> getPivotSelectionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPivotSelectionArray,
                this::setPivotSelectionArray,
                this::insertNewPivotSelection,
                this::removePivotSelection,
                this::sizeOfPivotSelectionArray
            );
        }
    }

    /**
     * Gets array of all "pivotSelection" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection[] getPivotSelectionArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection[0]);
    }

    /**
     * Gets ith "pivotSelection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection getPivotSelectionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pivotSelection" element
     */
    @Override
    public int sizeOfPivotSelectionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "pivotSelection" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPivotSelectionArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection[] pivotSelectionArray) {
        check_orphaned();
        arraySetterHelper(pivotSelectionArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "pivotSelection" element
     */
    @Override
    public void setPivotSelectionArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection pivotSelection) {
        generatedSetterHelperImpl(pivotSelection, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pivotSelection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection insertNewPivotSelection(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pivotSelection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection addNewPivotSelection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotSelection)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "pivotSelection" element
     */
    @Override
    public void removePivotSelection(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "windowProtection" attribute
     */
    @Override
    public boolean getWindowProtection() {
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
     * Gets (as xml) the "windowProtection" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetWindowProtection() {
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
     * True if has "windowProtection" attribute
     */
    @Override
    public boolean isSetWindowProtection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "windowProtection" attribute
     */
    @Override
    public void setWindowProtection(boolean windowProtection) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(windowProtection);
        }
    }

    /**
     * Sets (as xml) the "windowProtection" attribute
     */
    @Override
    public void xsetWindowProtection(org.apache.xmlbeans.XmlBoolean windowProtection) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(windowProtection);
        }
    }

    /**
     * Unsets the "windowProtection" attribute
     */
    @Override
    public void unsetWindowProtection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "showFormulas" attribute
     */
    @Override
    public boolean getShowFormulas() {
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
     * Gets (as xml) the "showFormulas" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowFormulas() {
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
     * True if has "showFormulas" attribute
     */
    @Override
    public boolean isSetShowFormulas() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "showFormulas" attribute
     */
    @Override
    public void setShowFormulas(boolean showFormulas) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setBooleanValue(showFormulas);
        }
    }

    /**
     * Sets (as xml) the "showFormulas" attribute
     */
    @Override
    public void xsetShowFormulas(org.apache.xmlbeans.XmlBoolean showFormulas) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(showFormulas);
        }
    }

    /**
     * Unsets the "showFormulas" attribute
     */
    @Override
    public void unsetShowFormulas() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "showGridLines" attribute
     */
    @Override
    public boolean getShowGridLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showGridLines" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowGridLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "showGridLines" attribute
     */
    @Override
    public boolean isSetShowGridLines() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "showGridLines" attribute
     */
    @Override
    public void setShowGridLines(boolean showGridLines) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBooleanValue(showGridLines);
        }
    }

    /**
     * Sets (as xml) the "showGridLines" attribute
     */
    @Override
    public void xsetShowGridLines(org.apache.xmlbeans.XmlBoolean showGridLines) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(showGridLines);
        }
    }

    /**
     * Unsets the "showGridLines" attribute
     */
    @Override
    public void unsetShowGridLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "showRowColHeaders" attribute
     */
    @Override
    public boolean getShowRowColHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showRowColHeaders" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowRowColHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return target;
        }
    }

    /**
     * True if has "showRowColHeaders" attribute
     */
    @Override
    public boolean isSetShowRowColHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "showRowColHeaders" attribute
     */
    @Override
    public void setShowRowColHeaders(boolean showRowColHeaders) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(showRowColHeaders);
        }
    }

    /**
     * Sets (as xml) the "showRowColHeaders" attribute
     */
    @Override
    public void xsetShowRowColHeaders(org.apache.xmlbeans.XmlBoolean showRowColHeaders) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(showRowColHeaders);
        }
    }

    /**
     * Unsets the "showRowColHeaders" attribute
     */
    @Override
    public void unsetShowRowColHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "showZeros" attribute
     */
    @Override
    public boolean getShowZeros() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showZeros" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowZeros() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return target;
        }
    }

    /**
     * True if has "showZeros" attribute
     */
    @Override
    public boolean isSetShowZeros() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "showZeros" attribute
     */
    @Override
    public void setShowZeros(boolean showZeros) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setBooleanValue(showZeros);
        }
    }

    /**
     * Sets (as xml) the "showZeros" attribute
     */
    @Override
    public void xsetShowZeros(org.apache.xmlbeans.XmlBoolean showZeros) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(showZeros);
        }
    }

    /**
     * Unsets the "showZeros" attribute
     */
    @Override
    public void unsetShowZeros() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "rightToLeft" attribute
     */
    @Override
    public boolean getRightToLeft() {
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
     * Gets (as xml) the "rightToLeft" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRightToLeft() {
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
     * True if has "rightToLeft" attribute
     */
    @Override
    public boolean isSetRightToLeft() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "rightToLeft" attribute
     */
    @Override
    public void setRightToLeft(boolean rightToLeft) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(rightToLeft);
        }
    }

    /**
     * Sets (as xml) the "rightToLeft" attribute
     */
    @Override
    public void xsetRightToLeft(org.apache.xmlbeans.XmlBoolean rightToLeft) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(rightToLeft);
        }
    }

    /**
     * Unsets the "rightToLeft" attribute
     */
    @Override
    public void unsetRightToLeft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "tabSelected" attribute
     */
    @Override
    public boolean getTabSelected() {
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
     * Gets (as xml) the "tabSelected" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetTabSelected() {
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
     * True if has "tabSelected" attribute
     */
    @Override
    public boolean isSetTabSelected() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "tabSelected" attribute
     */
    @Override
    public void setTabSelected(boolean tabSelected) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setBooleanValue(tabSelected);
        }
    }

    /**
     * Sets (as xml) the "tabSelected" attribute
     */
    @Override
    public void xsetTabSelected(org.apache.xmlbeans.XmlBoolean tabSelected) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(tabSelected);
        }
    }

    /**
     * Unsets the "tabSelected" attribute
     */
    @Override
    public void unsetTabSelected() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "showRuler" attribute
     */
    @Override
    public boolean getShowRuler() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showRuler" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowRuler() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return target;
        }
    }

    /**
     * True if has "showRuler" attribute
     */
    @Override
    public boolean isSetShowRuler() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "showRuler" attribute
     */
    @Override
    public void setShowRuler(boolean showRuler) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setBooleanValue(showRuler);
        }
    }

    /**
     * Sets (as xml) the "showRuler" attribute
     */
    @Override
    public void xsetShowRuler(org.apache.xmlbeans.XmlBoolean showRuler) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(showRuler);
        }
    }

    /**
     * Unsets the "showRuler" attribute
     */
    @Override
    public void unsetShowRuler() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "showOutlineSymbols" attribute
     */
    @Override
    public boolean getShowOutlineSymbols() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[12]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showOutlineSymbols" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowOutlineSymbols() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[12]);
            }
            return target;
        }
    }

    /**
     * True if has "showOutlineSymbols" attribute
     */
    @Override
    public boolean isSetShowOutlineSymbols() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "showOutlineSymbols" attribute
     */
    @Override
    public void setShowOutlineSymbols(boolean showOutlineSymbols) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setBooleanValue(showOutlineSymbols);
        }
    }

    /**
     * Sets (as xml) the "showOutlineSymbols" attribute
     */
    @Override
    public void xsetShowOutlineSymbols(org.apache.xmlbeans.XmlBoolean showOutlineSymbols) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(showOutlineSymbols);
        }
    }

    /**
     * Unsets the "showOutlineSymbols" attribute
     */
    @Override
    public void unsetShowOutlineSymbols() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "defaultGridColor" attribute
     */
    @Override
    public boolean getDefaultGridColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "defaultGridColor" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDefaultGridColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return target;
        }
    }

    /**
     * True if has "defaultGridColor" attribute
     */
    @Override
    public boolean isSetDefaultGridColor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "defaultGridColor" attribute
     */
    @Override
    public void setDefaultGridColor(boolean defaultGridColor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setBooleanValue(defaultGridColor);
        }
    }

    /**
     * Sets (as xml) the "defaultGridColor" attribute
     */
    @Override
    public void xsetDefaultGridColor(org.apache.xmlbeans.XmlBoolean defaultGridColor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(defaultGridColor);
        }
    }

    /**
     * Unsets the "defaultGridColor" attribute
     */
    @Override
    public void unsetDefaultGridColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "showWhiteSpace" attribute
     */
    @Override
    public boolean getShowWhiteSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showWhiteSpace" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowWhiteSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return target;
        }
    }

    /**
     * True if has "showWhiteSpace" attribute
     */
    @Override
    public boolean isSetShowWhiteSpace() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "showWhiteSpace" attribute
     */
    @Override
    public void setShowWhiteSpace(boolean showWhiteSpace) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setBooleanValue(showWhiteSpace);
        }
    }

    /**
     * Sets (as xml) the "showWhiteSpace" attribute
     */
    @Override
    public void xsetShowWhiteSpace(org.apache.xmlbeans.XmlBoolean showWhiteSpace) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(showWhiteSpace);
        }
    }

    /**
     * Unsets the "showWhiteSpace" attribute
     */
    @Override
    public void unsetShowWhiteSpace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "view" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType.Enum getView() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "view" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType xgetView() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return target;
        }
    }

    /**
     * True if has "view" attribute
     */
    @Override
    public boolean isSetView() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "view" attribute
     */
    @Override
    public void setView(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType.Enum view) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setEnumValue(view);
        }
    }

    /**
     * Sets (as xml) the "view" attribute
     */
    @Override
    public void xsetView(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType view) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetViewType)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(view);
        }
    }

    /**
     * Unsets the "view" attribute
     */
    @Override
    public void unsetView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "topLeftCell" attribute
     */
    @Override
    public java.lang.String getTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "topLeftCell" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef xgetTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * True if has "topLeftCell" attribute
     */
    @Override
    public boolean isSetTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "topLeftCell" attribute
     */
    @Override
    public void setTopLeftCell(java.lang.String topLeftCell) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setStringValue(topLeftCell);
        }
    }

    /**
     * Sets (as xml) the "topLeftCell" attribute
     */
    @Override
    public void xsetTopLeftCell(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef topLeftCell) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(topLeftCell);
        }
    }

    /**
     * Unsets the "topLeftCell" attribute
     */
    @Override
    public void unsetTopLeftCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "colorId" attribute
     */
    @Override
    public long getColorId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "colorId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetColorId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return target;
        }
    }

    /**
     * True if has "colorId" attribute
     */
    @Override
    public boolean isSetColorId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "colorId" attribute
     */
    @Override
    public void setColorId(long colorId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setLongValue(colorId);
        }
    }

    /**
     * Sets (as xml) the "colorId" attribute
     */
    @Override
    public void xsetColorId(org.apache.xmlbeans.XmlUnsignedInt colorId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(colorId);
        }
    }

    /**
     * Unsets the "colorId" attribute
     */
    @Override
    public void unsetColorId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "zoomScale" attribute
     */
    @Override
    public long getZoomScale() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "zoomScale" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetZoomScale() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return target;
        }
    }

    /**
     * True if has "zoomScale" attribute
     */
    @Override
    public boolean isSetZoomScale() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "zoomScale" attribute
     */
    @Override
    public void setZoomScale(long zoomScale) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setLongValue(zoomScale);
        }
    }

    /**
     * Sets (as xml) the "zoomScale" attribute
     */
    @Override
    public void xsetZoomScale(org.apache.xmlbeans.XmlUnsignedInt zoomScale) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(zoomScale);
        }
    }

    /**
     * Unsets the "zoomScale" attribute
     */
    @Override
    public void unsetZoomScale() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "zoomScaleNormal" attribute
     */
    @Override
    public long getZoomScaleNormal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "zoomScaleNormal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetZoomScaleNormal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return target;
        }
    }

    /**
     * True if has "zoomScaleNormal" attribute
     */
    @Override
    public boolean isSetZoomScaleNormal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "zoomScaleNormal" attribute
     */
    @Override
    public void setZoomScaleNormal(long zoomScaleNormal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setLongValue(zoomScaleNormal);
        }
    }

    /**
     * Sets (as xml) the "zoomScaleNormal" attribute
     */
    @Override
    public void xsetZoomScaleNormal(org.apache.xmlbeans.XmlUnsignedInt zoomScaleNormal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(zoomScaleNormal);
        }
    }

    /**
     * Unsets the "zoomScaleNormal" attribute
     */
    @Override
    public void unsetZoomScaleNormal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "zoomScaleSheetLayoutView" attribute
     */
    @Override
    public long getZoomScaleSheetLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "zoomScaleSheetLayoutView" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetZoomScaleSheetLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return target;
        }
    }

    /**
     * True if has "zoomScaleSheetLayoutView" attribute
     */
    @Override
    public boolean isSetZoomScaleSheetLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "zoomScaleSheetLayoutView" attribute
     */
    @Override
    public void setZoomScaleSheetLayoutView(long zoomScaleSheetLayoutView) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setLongValue(zoomScaleSheetLayoutView);
        }
    }

    /**
     * Sets (as xml) the "zoomScaleSheetLayoutView" attribute
     */
    @Override
    public void xsetZoomScaleSheetLayoutView(org.apache.xmlbeans.XmlUnsignedInt zoomScaleSheetLayoutView) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(zoomScaleSheetLayoutView);
        }
    }

    /**
     * Unsets the "zoomScaleSheetLayoutView" attribute
     */
    @Override
    public void unsetZoomScaleSheetLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "zoomScalePageLayoutView" attribute
     */
    @Override
    public long getZoomScalePageLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[21]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "zoomScalePageLayoutView" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetZoomScalePageLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[21]);
            }
            return target;
        }
    }

    /**
     * True if has "zoomScalePageLayoutView" attribute
     */
    @Override
    public boolean isSetZoomScalePageLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "zoomScalePageLayoutView" attribute
     */
    @Override
    public void setZoomScalePageLayoutView(long zoomScalePageLayoutView) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setLongValue(zoomScalePageLayoutView);
        }
    }

    /**
     * Sets (as xml) the "zoomScalePageLayoutView" attribute
     */
    @Override
    public void xsetZoomScalePageLayoutView(org.apache.xmlbeans.XmlUnsignedInt zoomScalePageLayoutView) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(zoomScalePageLayoutView);
        }
    }

    /**
     * Unsets the "zoomScalePageLayoutView" attribute
     */
    @Override
    public void unsetZoomScalePageLayoutView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "workbookViewId" attribute
     */
    @Override
    public long getWorkbookViewId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "workbookViewId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetWorkbookViewId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Sets the "workbookViewId" attribute
     */
    @Override
    public void setWorkbookViewId(long workbookViewId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setLongValue(workbookViewId);
        }
    }

    /**
     * Sets (as xml) the "workbookViewId" attribute
     */
    @Override
    public void xsetWorkbookViewId(org.apache.xmlbeans.XmlUnsignedInt workbookViewId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(workbookViewId);
        }
    }
}
