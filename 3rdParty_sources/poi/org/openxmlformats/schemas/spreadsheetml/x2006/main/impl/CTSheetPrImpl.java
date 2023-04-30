/*
 * XML Type:  CT_SheetPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SheetPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTSheetPrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr {
    private static final long serialVersionUID = 1L;

    public CTSheetPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "tabColor"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "outlinePr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pageSetUpPr"),
        new QName("", "syncHorizontal"),
        new QName("", "syncVertical"),
        new QName("", "syncRef"),
        new QName("", "transitionEvaluation"),
        new QName("", "transitionEntry"),
        new QName("", "published"),
        new QName("", "codeName"),
        new QName("", "filterMode"),
        new QName("", "enableFormatConditionsCalculation"),
    };


    /**
     * Gets the "tabColor" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor getTabColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tabColor" element
     */
    @Override
    public boolean isSetTabColor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tabColor" element
     */
    @Override
    public void setTabColor(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor tabColor) {
        generatedSetterHelperImpl(tabColor, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tabColor" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor addNewTabColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tabColor" element
     */
    @Override
    public void unsetTabColor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "outlinePr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr getOutlinePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "outlinePr" element
     */
    @Override
    public boolean isSetOutlinePr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "outlinePr" element
     */
    @Override
    public void setOutlinePr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr outlinePr) {
        generatedSetterHelperImpl(outlinePr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "outlinePr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr addNewOutlinePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOutlinePr)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "outlinePr" element
     */
    @Override
    public void unsetOutlinePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "pageSetUpPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr getPageSetUpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pageSetUpPr" element
     */
    @Override
    public boolean isSetPageSetUpPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "pageSetUpPr" element
     */
    @Override
    public void setPageSetUpPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr pageSetUpPr) {
        generatedSetterHelperImpl(pageSetUpPr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageSetUpPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr addNewPageSetUpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetUpPr)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "pageSetUpPr" element
     */
    @Override
    public void unsetPageSetUpPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "syncHorizontal" attribute
     */
    @Override
    public boolean getSyncHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "syncHorizontal" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSyncHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "syncHorizontal" attribute
     */
    @Override
    public boolean isSetSyncHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "syncHorizontal" attribute
     */
    @Override
    public void setSyncHorizontal(boolean syncHorizontal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBooleanValue(syncHorizontal);
        }
    }

    /**
     * Sets (as xml) the "syncHorizontal" attribute
     */
    @Override
    public void xsetSyncHorizontal(org.apache.xmlbeans.XmlBoolean syncHorizontal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(syncHorizontal);
        }
    }

    /**
     * Unsets the "syncHorizontal" attribute
     */
    @Override
    public void unsetSyncHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "syncVertical" attribute
     */
    @Override
    public boolean getSyncVertical() {
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
     * Gets (as xml) the "syncVertical" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSyncVertical() {
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
     * True if has "syncVertical" attribute
     */
    @Override
    public boolean isSetSyncVertical() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "syncVertical" attribute
     */
    @Override
    public void setSyncVertical(boolean syncVertical) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(syncVertical);
        }
    }

    /**
     * Sets (as xml) the "syncVertical" attribute
     */
    @Override
    public void xsetSyncVertical(org.apache.xmlbeans.XmlBoolean syncVertical) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(syncVertical);
        }
    }

    /**
     * Unsets the "syncVertical" attribute
     */
    @Override
    public void unsetSyncVertical() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "syncRef" attribute
     */
    @Override
    public java.lang.String getSyncRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "syncRef" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef xgetSyncRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "syncRef" attribute
     */
    @Override
    public boolean isSetSyncRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "syncRef" attribute
     */
    @Override
    public void setSyncRef(java.lang.String syncRef) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setStringValue(syncRef);
        }
    }

    /**
     * Sets (as xml) the "syncRef" attribute
     */
    @Override
    public void xsetSyncRef(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef syncRef) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(syncRef);
        }
    }

    /**
     * Unsets the "syncRef" attribute
     */
    @Override
    public void unsetSyncRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "transitionEvaluation" attribute
     */
    @Override
    public boolean getTransitionEvaluation() {
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
     * Gets (as xml) the "transitionEvaluation" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetTransitionEvaluation() {
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
     * True if has "transitionEvaluation" attribute
     */
    @Override
    public boolean isSetTransitionEvaluation() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "transitionEvaluation" attribute
     */
    @Override
    public void setTransitionEvaluation(boolean transitionEvaluation) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBooleanValue(transitionEvaluation);
        }
    }

    /**
     * Sets (as xml) the "transitionEvaluation" attribute
     */
    @Override
    public void xsetTransitionEvaluation(org.apache.xmlbeans.XmlBoolean transitionEvaluation) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(transitionEvaluation);
        }
    }

    /**
     * Unsets the "transitionEvaluation" attribute
     */
    @Override
    public void unsetTransitionEvaluation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "transitionEntry" attribute
     */
    @Override
    public boolean getTransitionEntry() {
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
     * Gets (as xml) the "transitionEntry" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetTransitionEntry() {
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
     * True if has "transitionEntry" attribute
     */
    @Override
    public boolean isSetTransitionEntry() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "transitionEntry" attribute
     */
    @Override
    public void setTransitionEntry(boolean transitionEntry) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(transitionEntry);
        }
    }

    /**
     * Sets (as xml) the "transitionEntry" attribute
     */
    @Override
    public void xsetTransitionEntry(org.apache.xmlbeans.XmlBoolean transitionEntry) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(transitionEntry);
        }
    }

    /**
     * Unsets the "transitionEntry" attribute
     */
    @Override
    public void unsetTransitionEntry() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "published" attribute
     */
    @Override
    public boolean getPublished() {
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
     * Gets (as xml) the "published" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPublished() {
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
     * True if has "published" attribute
     */
    @Override
    public boolean isSetPublished() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "published" attribute
     */
    @Override
    public void setPublished(boolean published) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setBooleanValue(published);
        }
    }

    /**
     * Sets (as xml) the "published" attribute
     */
    @Override
    public void xsetPublished(org.apache.xmlbeans.XmlBoolean published) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(published);
        }
    }

    /**
     * Unsets the "published" attribute
     */
    @Override
    public void unsetPublished() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "codeName" attribute
     */
    @Override
    public java.lang.String getCodeName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "codeName" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetCodeName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "codeName" attribute
     */
    @Override
    public boolean isSetCodeName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "codeName" attribute
     */
    @Override
    public void setCodeName(java.lang.String codeName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setStringValue(codeName);
        }
    }

    /**
     * Sets (as xml) the "codeName" attribute
     */
    @Override
    public void xsetCodeName(org.apache.xmlbeans.XmlString codeName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(codeName);
        }
    }

    /**
     * Unsets the "codeName" attribute
     */
    @Override
    public void unsetCodeName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "filterMode" attribute
     */
    @Override
    public boolean getFilterMode() {
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
     * Gets (as xml) the "filterMode" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFilterMode() {
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
     * True if has "filterMode" attribute
     */
    @Override
    public boolean isSetFilterMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "filterMode" attribute
     */
    @Override
    public void setFilterMode(boolean filterMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setBooleanValue(filterMode);
        }
    }

    /**
     * Sets (as xml) the "filterMode" attribute
     */
    @Override
    public void xsetFilterMode(org.apache.xmlbeans.XmlBoolean filterMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(filterMode);
        }
    }

    /**
     * Unsets the "filterMode" attribute
     */
    @Override
    public void unsetFilterMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "enableFormatConditionsCalculation" attribute
     */
    @Override
    public boolean getEnableFormatConditionsCalculation() {
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
     * Gets (as xml) the "enableFormatConditionsCalculation" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetEnableFormatConditionsCalculation() {
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
     * True if has "enableFormatConditionsCalculation" attribute
     */
    @Override
    public boolean isSetEnableFormatConditionsCalculation() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "enableFormatConditionsCalculation" attribute
     */
    @Override
    public void setEnableFormatConditionsCalculation(boolean enableFormatConditionsCalculation) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setBooleanValue(enableFormatConditionsCalculation);
        }
    }

    /**
     * Sets (as xml) the "enableFormatConditionsCalculation" attribute
     */
    @Override
    public void xsetEnableFormatConditionsCalculation(org.apache.xmlbeans.XmlBoolean enableFormatConditionsCalculation) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(enableFormatConditionsCalculation);
        }
    }

    /**
     * Unsets the "enableFormatConditionsCalculation" attribute
     */
    @Override
    public void unsetEnableFormatConditionsCalculation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }
}
