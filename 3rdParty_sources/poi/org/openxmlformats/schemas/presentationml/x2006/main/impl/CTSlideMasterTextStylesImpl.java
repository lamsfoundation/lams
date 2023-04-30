/*
 * XML Type:  CT_SlideMasterTextStyles
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SlideMasterTextStyles(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSlideMasterTextStylesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles {
    private static final long serialVersionUID = 1L;

    public CTSlideMasterTextStylesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "titleStyle"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bodyStyle"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "otherStyle"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
    };


    /**
     * Gets the "titleStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle getTitleStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "titleStyle" element
     */
    @Override
    public boolean isSetTitleStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "titleStyle" element
     */
    @Override
    public void setTitleStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle titleStyle) {
        generatedSetterHelperImpl(titleStyle, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "titleStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle addNewTitleStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "titleStyle" element
     */
    @Override
    public void unsetTitleStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "bodyStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle getBodyStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bodyStyle" element
     */
    @Override
    public boolean isSetBodyStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "bodyStyle" element
     */
    @Override
    public void setBodyStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle bodyStyle) {
        generatedSetterHelperImpl(bodyStyle, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bodyStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle addNewBodyStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "bodyStyle" element
     */
    @Override
    public void unsetBodyStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "otherStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle getOtherStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "otherStyle" element
     */
    @Override
    public boolean isSetOtherStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "otherStyle" element
     */
    @Override
    public void setOtherStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle otherStyle) {
        generatedSetterHelperImpl(otherStyle, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "otherStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle addNewOtherStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "otherStyle" element
     */
    @Override
    public void unsetOtherStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
