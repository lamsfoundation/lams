/*
 * XML Type:  CT_RevisionDefinedName
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RevisionDefinedName(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTRevisionDefinedNameImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionDefinedName {
    private static final long serialVersionUID = 1L;

    public CTRevisionDefinedNameImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "formula"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "oldFormula"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
        new QName("", "rId"),
        new QName("", "ua"),
        new QName("", "ra"),
        new QName("", "localSheetId"),
        new QName("", "customView"),
        new QName("", "name"),
        new QName("", "function"),
        new QName("", "oldFunction"),
        new QName("", "functionGroupId"),
        new QName("", "oldFunctionGroupId"),
        new QName("", "shortcutKey"),
        new QName("", "oldShortcutKey"),
        new QName("", "hidden"),
        new QName("", "oldHidden"),
        new QName("", "customMenu"),
        new QName("", "oldCustomMenu"),
        new QName("", "description"),
        new QName("", "oldDescription"),
        new QName("", "help"),
        new QName("", "oldHelp"),
        new QName("", "statusBar"),
        new QName("", "oldStatusBar"),
        new QName("", "comment"),
        new QName("", "oldComment"),
    };


    /**
     * Gets the "formula" element
     */
    @Override
    public java.lang.String getFormula() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "formula" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula xgetFormula() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "formula" element
     */
    @Override
    public boolean isSetFormula() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "formula" element
     */
    @Override
    public void setFormula(java.lang.String formula) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(formula);
        }
    }

    /**
     * Sets (as xml) the "formula" element
     */
    @Override
    public void xsetFormula(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula formula) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(formula);
        }
    }

    /**
     * Unsets the "formula" element
     */
    @Override
    public void unsetFormula() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "oldFormula" element
     */
    @Override
    public java.lang.String getOldFormula() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "oldFormula" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula xgetOldFormula() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * True if has "oldFormula" element
     */
    @Override
    public boolean isSetOldFormula() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "oldFormula" element
     */
    @Override
    public void setOldFormula(java.lang.String oldFormula) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(oldFormula);
        }
    }

    /**
     * Sets (as xml) the "oldFormula" element
     */
    @Override
    public void xsetOldFormula(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula oldFormula) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(oldFormula);
        }
    }

    /**
     * Unsets the "oldFormula" element
     */
    @Override
    public void unsetOldFormula() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
     * Gets the "localSheetId" attribute
     */
    @Override
    public long getLocalSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "localSheetId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetLocalSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "localSheetId" attribute
     */
    @Override
    public boolean isSetLocalSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "localSheetId" attribute
     */
    @Override
    public void setLocalSheetId(long localSheetId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setLongValue(localSheetId);
        }
    }

    /**
     * Sets (as xml) the "localSheetId" attribute
     */
    @Override
    public void xsetLocalSheetId(org.apache.xmlbeans.XmlUnsignedInt localSheetId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(localSheetId);
        }
    }

    /**
     * Unsets the "localSheetId" attribute
     */
    @Override
    public void unsetLocalSheetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "customView" attribute
     */
    @Override
    public boolean getCustomView() {
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
     * Gets (as xml) the "customView" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCustomView() {
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
     * True if has "customView" attribute
     */
    @Override
    public boolean isSetCustomView() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "customView" attribute
     */
    @Override
    public void setCustomView(boolean customView) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(customView);
        }
    }

    /**
     * Sets (as xml) the "customView" attribute
     */
    @Override
    public void xsetCustomView(org.apache.xmlbeans.XmlBoolean customView) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(customView);
        }
    }

    /**
     * Unsets the "customView" attribute
     */
    @Override
    public void unsetCustomView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(name);
        }
    }

    /**
     * Gets the "function" attribute
     */
    @Override
    public boolean getFunction() {
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
     * Gets (as xml) the "function" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFunction() {
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
     * True if has "function" attribute
     */
    @Override
    public boolean isSetFunction() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "function" attribute
     */
    @Override
    public void setFunction(boolean function) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(function);
        }
    }

    /**
     * Sets (as xml) the "function" attribute
     */
    @Override
    public void xsetFunction(org.apache.xmlbeans.XmlBoolean function) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(function);
        }
    }

    /**
     * Unsets the "function" attribute
     */
    @Override
    public void unsetFunction() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "oldFunction" attribute
     */
    @Override
    public boolean getOldFunction() {
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
     * Gets (as xml) the "oldFunction" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetOldFunction() {
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
     * True if has "oldFunction" attribute
     */
    @Override
    public boolean isSetOldFunction() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "oldFunction" attribute
     */
    @Override
    public void setOldFunction(boolean oldFunction) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setBooleanValue(oldFunction);
        }
    }

    /**
     * Sets (as xml) the "oldFunction" attribute
     */
    @Override
    public void xsetOldFunction(org.apache.xmlbeans.XmlBoolean oldFunction) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(oldFunction);
        }
    }

    /**
     * Unsets the "oldFunction" attribute
     */
    @Override
    public void unsetOldFunction() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "functionGroupId" attribute
     */
    @Override
    public short getFunctionGroupId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "functionGroupId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedByte xgetFunctionGroupId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "functionGroupId" attribute
     */
    @Override
    public boolean isSetFunctionGroupId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "functionGroupId" attribute
     */
    @Override
    public void setFunctionGroupId(short functionGroupId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setShortValue(functionGroupId);
        }
    }

    /**
     * Sets (as xml) the "functionGroupId" attribute
     */
    @Override
    public void xsetFunctionGroupId(org.apache.xmlbeans.XmlUnsignedByte functionGroupId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(functionGroupId);
        }
    }

    /**
     * Unsets the "functionGroupId" attribute
     */
    @Override
    public void unsetFunctionGroupId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "oldFunctionGroupId" attribute
     */
    @Override
    public short getOldFunctionGroupId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "oldFunctionGroupId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedByte xgetOldFunctionGroupId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "oldFunctionGroupId" attribute
     */
    @Override
    public boolean isSetOldFunctionGroupId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "oldFunctionGroupId" attribute
     */
    @Override
    public void setOldFunctionGroupId(short oldFunctionGroupId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setShortValue(oldFunctionGroupId);
        }
    }

    /**
     * Sets (as xml) the "oldFunctionGroupId" attribute
     */
    @Override
    public void xsetOldFunctionGroupId(org.apache.xmlbeans.XmlUnsignedByte oldFunctionGroupId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(oldFunctionGroupId);
        }
    }

    /**
     * Unsets the "oldFunctionGroupId" attribute
     */
    @Override
    public void unsetOldFunctionGroupId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "shortcutKey" attribute
     */
    @Override
    public short getShortcutKey() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "shortcutKey" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedByte xgetShortcutKey() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * True if has "shortcutKey" attribute
     */
    @Override
    public boolean isSetShortcutKey() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "shortcutKey" attribute
     */
    @Override
    public void setShortcutKey(short shortcutKey) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setShortValue(shortcutKey);
        }
    }

    /**
     * Sets (as xml) the "shortcutKey" attribute
     */
    @Override
    public void xsetShortcutKey(org.apache.xmlbeans.XmlUnsignedByte shortcutKey) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(shortcutKey);
        }
    }

    /**
     * Unsets the "shortcutKey" attribute
     */
    @Override
    public void unsetShortcutKey() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "oldShortcutKey" attribute
     */
    @Override
    public short getOldShortcutKey() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "oldShortcutKey" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedByte xgetOldShortcutKey() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * True if has "oldShortcutKey" attribute
     */
    @Override
    public boolean isSetOldShortcutKey() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "oldShortcutKey" attribute
     */
    @Override
    public void setOldShortcutKey(short oldShortcutKey) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setShortValue(oldShortcutKey);
        }
    }

    /**
     * Sets (as xml) the "oldShortcutKey" attribute
     */
    @Override
    public void xsetOldShortcutKey(org.apache.xmlbeans.XmlUnsignedByte oldShortcutKey) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(oldShortcutKey);
        }
    }

    /**
     * Unsets the "oldShortcutKey" attribute
     */
    @Override
    public void unsetOldShortcutKey() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "hidden" attribute
     */
    @Override
    public boolean getHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "hidden" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return target;
        }
    }

    /**
     * True if has "hidden" attribute
     */
    @Override
    public boolean isSetHidden() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "hidden" attribute
     */
    @Override
    public void setHidden(boolean hidden) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setBooleanValue(hidden);
        }
    }

    /**
     * Sets (as xml) the "hidden" attribute
     */
    @Override
    public void xsetHidden(org.apache.xmlbeans.XmlBoolean hidden) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(hidden);
        }
    }

    /**
     * Unsets the "hidden" attribute
     */
    @Override
    public void unsetHidden() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "oldHidden" attribute
     */
    @Override
    public boolean getOldHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "oldHidden" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetOldHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return target;
        }
    }

    /**
     * True if has "oldHidden" attribute
     */
    @Override
    public boolean isSetOldHidden() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "oldHidden" attribute
     */
    @Override
    public void setOldHidden(boolean oldHidden) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setBooleanValue(oldHidden);
        }
    }

    /**
     * Sets (as xml) the "oldHidden" attribute
     */
    @Override
    public void xsetOldHidden(org.apache.xmlbeans.XmlBoolean oldHidden) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(oldHidden);
        }
    }

    /**
     * Unsets the "oldHidden" attribute
     */
    @Override
    public void unsetOldHidden() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "customMenu" attribute
     */
    @Override
    public java.lang.String getCustomMenu() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "customMenu" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetCustomMenu() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * True if has "customMenu" attribute
     */
    @Override
    public boolean isSetCustomMenu() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "customMenu" attribute
     */
    @Override
    public void setCustomMenu(java.lang.String customMenu) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setStringValue(customMenu);
        }
    }

    /**
     * Sets (as xml) the "customMenu" attribute
     */
    @Override
    public void xsetCustomMenu(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring customMenu) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(customMenu);
        }
    }

    /**
     * Unsets the "customMenu" attribute
     */
    @Override
    public void unsetCustomMenu() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "oldCustomMenu" attribute
     */
    @Override
    public java.lang.String getOldCustomMenu() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "oldCustomMenu" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetOldCustomMenu() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * True if has "oldCustomMenu" attribute
     */
    @Override
    public boolean isSetOldCustomMenu() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "oldCustomMenu" attribute
     */
    @Override
    public void setOldCustomMenu(java.lang.String oldCustomMenu) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setStringValue(oldCustomMenu);
        }
    }

    /**
     * Sets (as xml) the "oldCustomMenu" attribute
     */
    @Override
    public void xsetOldCustomMenu(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring oldCustomMenu) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(oldCustomMenu);
        }
    }

    /**
     * Unsets the "oldCustomMenu" attribute
     */
    @Override
    public void unsetOldCustomMenu() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "description" attribute
     */
    @Override
    public java.lang.String getDescription() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "description" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * True if has "description" attribute
     */
    @Override
    public boolean isSetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "description" attribute
     */
    @Override
    public void setDescription(java.lang.String description) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setStringValue(description);
        }
    }

    /**
     * Sets (as xml) the "description" attribute
     */
    @Override
    public void xsetDescription(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring description) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(description);
        }
    }

    /**
     * Unsets the "description" attribute
     */
    @Override
    public void unsetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "oldDescription" attribute
     */
    @Override
    public java.lang.String getOldDescription() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "oldDescription" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetOldDescription() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * True if has "oldDescription" attribute
     */
    @Override
    public boolean isSetOldDescription() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "oldDescription" attribute
     */
    @Override
    public void setOldDescription(java.lang.String oldDescription) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setStringValue(oldDescription);
        }
    }

    /**
     * Sets (as xml) the "oldDescription" attribute
     */
    @Override
    public void xsetOldDescription(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring oldDescription) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(oldDescription);
        }
    }

    /**
     * Unsets the "oldDescription" attribute
     */
    @Override
    public void unsetOldDescription() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "help" attribute
     */
    @Override
    public java.lang.String getHelp() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "help" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetHelp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * True if has "help" attribute
     */
    @Override
    public boolean isSetHelp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "help" attribute
     */
    @Override
    public void setHelp(java.lang.String help) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setStringValue(help);
        }
    }

    /**
     * Sets (as xml) the "help" attribute
     */
    @Override
    public void xsetHelp(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring help) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(help);
        }
    }

    /**
     * Unsets the "help" attribute
     */
    @Override
    public void unsetHelp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "oldHelp" attribute
     */
    @Override
    public java.lang.String getOldHelp() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "oldHelp" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetOldHelp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * True if has "oldHelp" attribute
     */
    @Override
    public boolean isSetOldHelp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "oldHelp" attribute
     */
    @Override
    public void setOldHelp(java.lang.String oldHelp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setStringValue(oldHelp);
        }
    }

    /**
     * Sets (as xml) the "oldHelp" attribute
     */
    @Override
    public void xsetOldHelp(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring oldHelp) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(oldHelp);
        }
    }

    /**
     * Unsets the "oldHelp" attribute
     */
    @Override
    public void unsetOldHelp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "statusBar" attribute
     */
    @Override
    public java.lang.String getStatusBar() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "statusBar" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetStatusBar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * True if has "statusBar" attribute
     */
    @Override
    public boolean isSetStatusBar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "statusBar" attribute
     */
    @Override
    public void setStatusBar(java.lang.String statusBar) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setStringValue(statusBar);
        }
    }

    /**
     * Sets (as xml) the "statusBar" attribute
     */
    @Override
    public void xsetStatusBar(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring statusBar) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(statusBar);
        }
    }

    /**
     * Unsets the "statusBar" attribute
     */
    @Override
    public void unsetStatusBar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "oldStatusBar" attribute
     */
    @Override
    public java.lang.String getOldStatusBar() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "oldStatusBar" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetOldStatusBar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * True if has "oldStatusBar" attribute
     */
    @Override
    public boolean isSetOldStatusBar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "oldStatusBar" attribute
     */
    @Override
    public void setOldStatusBar(java.lang.String oldStatusBar) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setStringValue(oldStatusBar);
        }
    }

    /**
     * Sets (as xml) the "oldStatusBar" attribute
     */
    @Override
    public void xsetOldStatusBar(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring oldStatusBar) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(oldStatusBar);
        }
    }

    /**
     * Unsets the "oldStatusBar" attribute
     */
    @Override
    public void unsetOldStatusBar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "comment" attribute
     */
    @Override
    public java.lang.String getComment() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "comment" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetComment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * True if has "comment" attribute
     */
    @Override
    public boolean isSetComment() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "comment" attribute
     */
    @Override
    public void setComment(java.lang.String comment) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setStringValue(comment);
        }
    }

    /**
     * Sets (as xml) the "comment" attribute
     */
    @Override
    public void xsetComment(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring comment) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(comment);
        }
    }

    /**
     * Unsets the "comment" attribute
     */
    @Override
    public void unsetComment() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "oldComment" attribute
     */
    @Override
    public java.lang.String getOldComment() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "oldComment" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetOldComment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * True if has "oldComment" attribute
     */
    @Override
    public boolean isSetOldComment() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "oldComment" attribute
     */
    @Override
    public void setOldComment(java.lang.String oldComment) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setStringValue(oldComment);
        }
    }

    /**
     * Sets (as xml) the "oldComment" attribute
     */
    @Override
    public void xsetOldComment(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring oldComment) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(oldComment);
        }
    }

    /**
     * Unsets the "oldComment" attribute
     */
    @Override
    public void unsetOldComment() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }
}
