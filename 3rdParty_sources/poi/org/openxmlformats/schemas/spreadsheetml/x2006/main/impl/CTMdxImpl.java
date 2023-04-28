/*
 * XML Type:  CT_Mdx
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdx
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Mdx(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTMdxImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdx {
    private static final long serialVersionUID = 1L;

    public CTMdxImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "t"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "ms"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "p"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "k"),
        new QName("", "n"),
        new QName("", "f"),
    };


    /**
     * Gets the "t" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxTuple getT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxTuple target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxTuple)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "t" element
     */
    @Override
    public boolean isSetT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "t" element
     */
    @Override
    public void setT(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxTuple t) {
        generatedSetterHelperImpl(t, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "t" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxTuple addNewT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxTuple target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxTuple)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "t" element
     */
    @Override
    public void unsetT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "ms" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxSet getMs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxSet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxSet)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ms" element
     */
    @Override
    public boolean isSetMs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "ms" element
     */
    @Override
    public void setMs(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxSet ms) {
        generatedSetterHelperImpl(ms, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ms" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxSet addNewMs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxSet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxSet)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "ms" element
     */
    @Override
    public void unsetMs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "p" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMemeberProp getP() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMemeberProp target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMemeberProp)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "p" element
     */
    @Override
    public boolean isSetP() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "p" element
     */
    @Override
    public void setP(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMemeberProp p) {
        generatedSetterHelperImpl(p, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "p" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMemeberProp addNewP() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMemeberProp target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMemeberProp)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "p" element
     */
    @Override
    public void unsetP() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "k" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI getK() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "k" element
     */
    @Override
    public boolean isSetK() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "k" element
     */
    @Override
    public void setK(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI k) {
        generatedSetterHelperImpl(k, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "k" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI addNewK() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "k" element
     */
    @Override
    public void unsetK() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "n" attribute
     */
    @Override
    public long getN() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "n" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetN() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Sets the "n" attribute
     */
    @Override
    public void setN(long n) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setLongValue(n);
        }
    }

    /**
     * Sets (as xml) the "n" attribute
     */
    @Override
    public void xsetN(org.apache.xmlbeans.XmlUnsignedInt n) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(n);
        }
    }

    /**
     * Gets the "f" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType.Enum getF() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "f" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType xgetF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Sets the "f" attribute
     */
    @Override
    public void setF(org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType.Enum f) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(f);
        }
    }

    /**
     * Sets (as xml) the "f" attribute
     */
    @Override
    public void xsetF(org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType f) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxFunctionType)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(f);
        }
    }
}
