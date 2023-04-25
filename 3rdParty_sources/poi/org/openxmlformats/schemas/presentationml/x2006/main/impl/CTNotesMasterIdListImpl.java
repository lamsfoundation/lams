/*
 * XML Type:  CT_NotesMasterIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_NotesMasterIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTNotesMasterIdListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdList {
    private static final long serialVersionUID = 1L;

    public CTNotesMasterIdListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "notesMasterId"),
    };


    /**
     * Gets the "notesMasterId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry getNotesMasterId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "notesMasterId" element
     */
    @Override
    public boolean isSetNotesMasterId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "notesMasterId" element
     */
    @Override
    public void setNotesMasterId(org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry notesMasterId) {
        generatedSetterHelperImpl(notesMasterId, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "notesMasterId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry addNewNotesMasterId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesMasterIdListEntry)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "notesMasterId" element
     */
    @Override
    public void unsetNotesMasterId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}
