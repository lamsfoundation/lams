/*
 * XML Type:  CT_EdnProps
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_EdnProps(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTEdnPropsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps {
    private static final long serialVersionUID = 1L;

    public CTEdnPropsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pos"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numFmt"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numStart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numRestart"),
    };


    /**
     * Gets the "pos" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos getPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pos" element
     */
    @Override
    public boolean isSetPos() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "pos" element
     */
    @Override
    public void setPos(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos pos) {
        generatedSetterHelperImpl(pos, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pos" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos addNewPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "pos" element
     */
    @Override
    public void unsetPos() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "numFmt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt getNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numFmt" element
     */
    @Override
    public boolean isSetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "numFmt" element
     */
    @Override
    public void setNumFmt(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt numFmt) {
        generatedSetterHelperImpl(numFmt, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numFmt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt addNewNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "numFmt" element
     */
    @Override
    public void unsetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "numStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getNumStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numStart" element
     */
    @Override
    public boolean isSetNumStart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "numStart" element
     */
    @Override
    public void setNumStart(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber numStart) {
        generatedSetterHelperImpl(numStart, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numStart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewNumStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "numStart" element
     */
    @Override
    public void unsetNumStart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "numRestart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart getNumRestart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numRestart" element
     */
    @Override
    public boolean isSetNumRestart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "numRestart" element
     */
    @Override
    public void setNumRestart(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart numRestart) {
        generatedSetterHelperImpl(numRestart, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numRestart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart addNewNumRestart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "numRestart" element
     */
    @Override
    public void unsetNumRestart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
