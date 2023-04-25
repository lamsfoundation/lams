/*
 * XML Type:  CT_CellMergeTrackChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCellMergeTrackChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CellMergeTrackChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTCellMergeTrackChangeImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrackChangeImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCellMergeTrackChange {
    private static final long serialVersionUID = 1L;

    public CTCellMergeTrackChangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vMerge"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vMergeOrig"),
    };


    /**
     * Gets the "vMerge" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum getVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "vMerge" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge xgetVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "vMerge" attribute
     */
    @Override
    public boolean isSetVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "vMerge" attribute
     */
    @Override
    public void setVMerge(org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum vMerge) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(vMerge);
        }
    }

    /**
     * Sets (as xml) the "vMerge" attribute
     */
    @Override
    public void xsetVMerge(org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge vMerge) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(vMerge);
        }
    }

    /**
     * Unsets the "vMerge" attribute
     */
    @Override
    public void unsetVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "vMergeOrig" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum getVMergeOrig() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "vMergeOrig" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge xgetVMergeOrig() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "vMergeOrig" attribute
     */
    @Override
    public boolean isSetVMergeOrig() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "vMergeOrig" attribute
     */
    @Override
    public void setVMergeOrig(org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge.Enum vMergeOrig) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(vMergeOrig);
        }
    }

    /**
     * Sets (as xml) the "vMergeOrig" attribute
     */
    @Override
    public void xsetVMergeOrig(org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge vMergeOrig) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STAnnotationVMerge)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(vMergeOrig);
        }
    }

    /**
     * Unsets the "vMergeOrig" attribute
     */
    @Override
    public void unsetVMergeOrig() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
