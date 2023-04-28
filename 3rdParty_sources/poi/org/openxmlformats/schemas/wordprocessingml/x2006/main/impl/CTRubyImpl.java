/*
 * XML Type:  CT_Ruby
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Ruby(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTRubyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby {
    private static final long serialVersionUID = 1L;

    public CTRubyImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rubyPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rt"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rubyBase"),
    };


    /**
     * Gets the "rubyPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr getRubyPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "rubyPr" element
     */
    @Override
    public void setRubyPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr rubyPr) {
        generatedSetterHelperImpl(rubyPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rubyPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr addNewRubyPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "rt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent getRt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "rt" element
     */
    @Override
    public void setRt(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent rt) {
        generatedSetterHelperImpl(rt, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent addNewRt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "rubyBase" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent getRubyBase() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "rubyBase" element
     */
    @Override
    public void setRubyBase(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent rubyBase) {
        generatedSetterHelperImpl(rubyBase, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rubyBase" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent addNewRubyBase() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }
}
