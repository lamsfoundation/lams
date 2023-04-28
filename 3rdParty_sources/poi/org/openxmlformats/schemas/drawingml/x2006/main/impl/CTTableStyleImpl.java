/*
 * XML Type:  CT_TableStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TableStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTableStyleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle {
    private static final long serialVersionUID = 1L;

    public CTTableStyleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tblBg"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "wholeTbl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "band1H"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "band2H"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "band1V"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "band2V"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lastCol"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "firstCol"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lastRow"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "seCell"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "swCell"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "firstRow"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "neCell"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "nwCell"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "styleId"),
        new QName("", "styleName"),
    };


    /**
     * Gets the "tblBg" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableBackgroundStyle getTblBg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableBackgroundStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableBackgroundStyle)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblBg" element
     */
    @Override
    public boolean isSetTblBg() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tblBg" element
     */
    @Override
    public void setTblBg(org.openxmlformats.schemas.drawingml.x2006.main.CTTableBackgroundStyle tblBg) {
        generatedSetterHelperImpl(tblBg, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblBg" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableBackgroundStyle addNewTblBg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableBackgroundStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableBackgroundStyle)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tblBg" element
     */
    @Override
    public void unsetTblBg() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "wholeTbl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getWholeTbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wholeTbl" element
     */
    @Override
    public boolean isSetWholeTbl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "wholeTbl" element
     */
    @Override
    public void setWholeTbl(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle wholeTbl) {
        generatedSetterHelperImpl(wholeTbl, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wholeTbl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewWholeTbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "wholeTbl" element
     */
    @Override
    public void unsetWholeTbl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "band1H" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getBand1H() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "band1H" element
     */
    @Override
    public boolean isSetBand1H() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "band1H" element
     */
    @Override
    public void setBand1H(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle band1H) {
        generatedSetterHelperImpl(band1H, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "band1H" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewBand1H() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "band1H" element
     */
    @Override
    public void unsetBand1H() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "band2H" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getBand2H() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "band2H" element
     */
    @Override
    public boolean isSetBand2H() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "band2H" element
     */
    @Override
    public void setBand2H(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle band2H) {
        generatedSetterHelperImpl(band2H, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "band2H" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewBand2H() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "band2H" element
     */
    @Override
    public void unsetBand2H() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "band1V" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getBand1V() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "band1V" element
     */
    @Override
    public boolean isSetBand1V() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "band1V" element
     */
    @Override
    public void setBand1V(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle band1V) {
        generatedSetterHelperImpl(band1V, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "band1V" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewBand1V() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "band1V" element
     */
    @Override
    public void unsetBand1V() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "band2V" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getBand2V() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "band2V" element
     */
    @Override
    public boolean isSetBand2V() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "band2V" element
     */
    @Override
    public void setBand2V(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle band2V) {
        generatedSetterHelperImpl(band2V, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "band2V" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewBand2V() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "band2V" element
     */
    @Override
    public void unsetBand2V() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "lastCol" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getLastCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lastCol" element
     */
    @Override
    public boolean isSetLastCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "lastCol" element
     */
    @Override
    public void setLastCol(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle lastCol) {
        generatedSetterHelperImpl(lastCol, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lastCol" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewLastCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "lastCol" element
     */
    @Override
    public void unsetLastCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "firstCol" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getFirstCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "firstCol" element
     */
    @Override
    public boolean isSetFirstCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "firstCol" element
     */
    @Override
    public void setFirstCol(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle firstCol) {
        generatedSetterHelperImpl(firstCol, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "firstCol" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewFirstCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "firstCol" element
     */
    @Override
    public void unsetFirstCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "lastRow" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getLastRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lastRow" element
     */
    @Override
    public boolean isSetLastRow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "lastRow" element
     */
    @Override
    public void setLastRow(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle lastRow) {
        generatedSetterHelperImpl(lastRow, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lastRow" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewLastRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "lastRow" element
     */
    @Override
    public void unsetLastRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "seCell" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getSeCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "seCell" element
     */
    @Override
    public boolean isSetSeCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "seCell" element
     */
    @Override
    public void setSeCell(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle seCell) {
        generatedSetterHelperImpl(seCell, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "seCell" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewSeCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "seCell" element
     */
    @Override
    public void unsetSeCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "swCell" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getSwCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "swCell" element
     */
    @Override
    public boolean isSetSwCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "swCell" element
     */
    @Override
    public void setSwCell(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle swCell) {
        generatedSetterHelperImpl(swCell, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "swCell" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewSwCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "swCell" element
     */
    @Override
    public void unsetSwCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "firstRow" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getFirstRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "firstRow" element
     */
    @Override
    public boolean isSetFirstRow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "firstRow" element
     */
    @Override
    public void setFirstRow(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle firstRow) {
        generatedSetterHelperImpl(firstRow, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "firstRow" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewFirstRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "firstRow" element
     */
    @Override
    public void unsetFirstRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "neCell" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getNeCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "neCell" element
     */
    @Override
    public boolean isSetNeCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "neCell" element
     */
    @Override
    public void setNeCell(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle neCell) {
        generatedSetterHelperImpl(neCell, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "neCell" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewNeCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "neCell" element
     */
    @Override
    public void unsetNeCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "nwCell" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle getNwCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "nwCell" element
     */
    @Override
    public boolean isSetNwCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "nwCell" element
     */
    @Override
    public void setNwCell(org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle nwCell) {
        generatedSetterHelperImpl(nwCell, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "nwCell" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle addNewNwCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "nwCell" element
     */
    @Override
    public void unsetNwCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[14], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[14]);
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
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "styleId" attribute
     */
    @Override
    public java.lang.String getStyleId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "styleId" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetStyleId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Sets the "styleId" attribute
     */
    @Override
    public void setStyleId(java.lang.String styleId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setStringValue(styleId);
        }
    }

    /**
     * Sets (as xml) the "styleId" attribute
     */
    @Override
    public void xsetStyleId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid styleId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(styleId);
        }
    }

    /**
     * Gets the "styleName" attribute
     */
    @Override
    public java.lang.String getStyleName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "styleName" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetStyleName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Sets the "styleName" attribute
     */
    @Override
    public void setStyleName(java.lang.String styleName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setStringValue(styleName);
        }
    }

    /**
     * Sets (as xml) the "styleName" attribute
     */
    @Override
    public void xsetStyleName(org.apache.xmlbeans.XmlString styleName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(styleName);
        }
    }
}
