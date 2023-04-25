/*
 * XML Type:  CT_TransitionSoundAction
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TransitionSoundAction(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTransitionSoundActionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction {
    private static final long serialVersionUID = 1L;

    public CTTransitionSoundActionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "stSnd"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "endSnd"),
    };


    /**
     * Gets the "stSnd" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction getStSnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "stSnd" element
     */
    @Override
    public boolean isSetStSnd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "stSnd" element
     */
    @Override
    public void setStSnd(org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction stSnd) {
        generatedSetterHelperImpl(stSnd, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "stSnd" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction addNewStSnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "stSnd" element
     */
    @Override
    public void unsetStSnd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "endSnd" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getEndSnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "endSnd" element
     */
    @Override
    public boolean isSetEndSnd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "endSnd" element
     */
    @Override
    public void setEndSnd(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty endSnd) {
        generatedSetterHelperImpl(endSnd, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "endSnd" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewEndSnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "endSnd" element
     */
    @Override
    public void unsetEndSnd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
