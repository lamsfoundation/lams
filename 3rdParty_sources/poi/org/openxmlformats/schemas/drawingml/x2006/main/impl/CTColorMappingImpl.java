/*
 * XML Type:  CT_ColorMapping
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ColorMapping(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTColorMappingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping {
    private static final long serialVersionUID = 1L;

    public CTColorMappingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "bg1"),
        new QName("", "tx1"),
        new QName("", "bg2"),
        new QName("", "tx2"),
        new QName("", "accent1"),
        new QName("", "accent2"),
        new QName("", "accent3"),
        new QName("", "accent4"),
        new QName("", "accent5"),
        new QName("", "accent6"),
        new QName("", "hlink"),
        new QName("", "folHlink"),
    };


    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "bg1" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getBg1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bg1" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetBg1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "bg1" attribute
     */
    @Override
    public void setBg1(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum bg1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(bg1);
        }
    }

    /**
     * Sets (as xml) the "bg1" attribute
     */
    @Override
    public void xsetBg1(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex bg1) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(bg1);
        }
    }

    /**
     * Gets the "tx1" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getTx1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "tx1" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetTx1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "tx1" attribute
     */
    @Override
    public void setTx1(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum tx1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(tx1);
        }
    }

    /**
     * Sets (as xml) the "tx1" attribute
     */
    @Override
    public void xsetTx1(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex tx1) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(tx1);
        }
    }

    /**
     * Gets the "bg2" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getBg2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bg2" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetBg2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "bg2" attribute
     */
    @Override
    public void setBg2(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum bg2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(bg2);
        }
    }

    /**
     * Sets (as xml) the "bg2" attribute
     */
    @Override
    public void xsetBg2(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex bg2) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(bg2);
        }
    }

    /**
     * Gets the "tx2" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getTx2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "tx2" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetTx2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Sets the "tx2" attribute
     */
    @Override
    public void setTx2(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum tx2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(tx2);
        }
    }

    /**
     * Sets (as xml) the "tx2" attribute
     */
    @Override
    public void xsetTx2(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex tx2) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(tx2);
        }
    }

    /**
     * Gets the "accent1" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getAccent1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent1" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetAccent1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Sets the "accent1" attribute
     */
    @Override
    public void setAccent1(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum accent1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(accent1);
        }
    }

    /**
     * Sets (as xml) the "accent1" attribute
     */
    @Override
    public void xsetAccent1(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex accent1) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(accent1);
        }
    }

    /**
     * Gets the "accent2" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getAccent2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent2" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetAccent2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Sets the "accent2" attribute
     */
    @Override
    public void setAccent2(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum accent2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(accent2);
        }
    }

    /**
     * Sets (as xml) the "accent2" attribute
     */
    @Override
    public void xsetAccent2(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex accent2) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(accent2);
        }
    }

    /**
     * Gets the "accent3" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getAccent3() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent3" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetAccent3() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Sets the "accent3" attribute
     */
    @Override
    public void setAccent3(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum accent3) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setEnumValue(accent3);
        }
    }

    /**
     * Sets (as xml) the "accent3" attribute
     */
    @Override
    public void xsetAccent3(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex accent3) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(accent3);
        }
    }

    /**
     * Gets the "accent4" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getAccent4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent4" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetAccent4() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Sets the "accent4" attribute
     */
    @Override
    public void setAccent4(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum accent4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setEnumValue(accent4);
        }
    }

    /**
     * Sets (as xml) the "accent4" attribute
     */
    @Override
    public void xsetAccent4(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex accent4) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(accent4);
        }
    }

    /**
     * Gets the "accent5" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getAccent5() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent5" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetAccent5() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Sets the "accent5" attribute
     */
    @Override
    public void setAccent5(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum accent5) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setEnumValue(accent5);
        }
    }

    /**
     * Sets (as xml) the "accent5" attribute
     */
    @Override
    public void xsetAccent5(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex accent5) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(accent5);
        }
    }

    /**
     * Gets the "accent6" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getAccent6() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accent6" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetAccent6() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Sets the "accent6" attribute
     */
    @Override
    public void setAccent6(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum accent6) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setEnumValue(accent6);
        }
    }

    /**
     * Sets (as xml) the "accent6" attribute
     */
    @Override
    public void xsetAccent6(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex accent6) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(accent6);
        }
    }

    /**
     * Gets the "hlink" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getHlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "hlink" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetHlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Sets the "hlink" attribute
     */
    @Override
    public void setHlink(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum hlink) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setEnumValue(hlink);
        }
    }

    /**
     * Sets (as xml) the "hlink" attribute
     */
    @Override
    public void xsetHlink(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex hlink) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(hlink);
        }
    }

    /**
     * Gets the "folHlink" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum getFolHlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "folHlink" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex xgetFolHlink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Sets the "folHlink" attribute
     */
    @Override
    public void setFolHlink(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.Enum folHlink) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setEnumValue(folHlink);
        }
    }

    /**
     * Sets (as xml) the "folHlink" attribute
     */
    @Override
    public void xsetFolHlink(org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex folHlink) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(folHlink);
        }
    }
}
