/*
 * XML Type:  CT_ViewProperties
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ViewProperties(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTViewPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties {
    private static final long serialVersionUID = 1L;

    public CTViewPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "normalViewPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "slideViewPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "outlineViewPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "notesTextViewPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sorterViewPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "notesViewPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "gridSpacing"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
        new QName("", "lastView"),
        new QName("", "showComments"),
    };


    /**
     * Gets the "normalViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewProperties getNormalViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "normalViewPr" element
     */
    @Override
    public boolean isSetNormalViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "normalViewPr" element
     */
    @Override
    public void setNormalViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewProperties normalViewPr) {
        generatedSetterHelperImpl(normalViewPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "normalViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewProperties addNewNormalViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "normalViewPr" element
     */
    @Override
    public void unsetNormalViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "slideViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideViewProperties getSlideViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideViewProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "slideViewPr" element
     */
    @Override
    public boolean isSetSlideViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "slideViewPr" element
     */
    @Override
    public void setSlideViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideViewProperties slideViewPr) {
        generatedSetterHelperImpl(slideViewPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "slideViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideViewProperties addNewSlideViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideViewProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "slideViewPr" element
     */
    @Override
    public void unsetSlideViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "outlineViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewProperties getOutlineViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "outlineViewPr" element
     */
    @Override
    public boolean isSetOutlineViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "outlineViewPr" element
     */
    @Override
    public void setOutlineViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewProperties outlineViewPr) {
        generatedSetterHelperImpl(outlineViewPr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "outlineViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewProperties addNewOutlineViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "outlineViewPr" element
     */
    @Override
    public void unsetOutlineViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "notesTextViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesTextViewProperties getNotesTextViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesTextViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesTextViewProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "notesTextViewPr" element
     */
    @Override
    public boolean isSetNotesTextViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "notesTextViewPr" element
     */
    @Override
    public void setNotesTextViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTNotesTextViewProperties notesTextViewPr) {
        generatedSetterHelperImpl(notesTextViewPr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "notesTextViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesTextViewProperties addNewNotesTextViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesTextViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesTextViewProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "notesTextViewPr" element
     */
    @Override
    public void unsetNotesTextViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "sorterViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSorterViewProperties getSorterViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSorterViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSorterViewProperties)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sorterViewPr" element
     */
    @Override
    public boolean isSetSorterViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "sorterViewPr" element
     */
    @Override
    public void setSorterViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSorterViewProperties sorterViewPr) {
        generatedSetterHelperImpl(sorterViewPr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sorterViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSorterViewProperties addNewSorterViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSorterViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSorterViewProperties)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "sorterViewPr" element
     */
    @Override
    public void unsetSorterViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "notesViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesViewProperties getNotesViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesViewProperties)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "notesViewPr" element
     */
    @Override
    public boolean isSetNotesViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "notesViewPr" element
     */
    @Override
    public void setNotesViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTNotesViewProperties notesViewPr) {
        generatedSetterHelperImpl(notesViewPr, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "notesViewPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTNotesViewProperties addNewNotesViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTNotesViewProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTNotesViewProperties)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "notesViewPr" element
     */
    @Override
    public void unsetNotesViewPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "gridSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D getGridSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "gridSpacing" element
     */
    @Override
    public boolean isSetGridSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "gridSpacing" element
     */
    @Override
    public void setGridSpacing(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D gridSpacing) {
        generatedSetterHelperImpl(gridSpacing, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gridSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D addNewGridSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "gridSpacing" element
     */
    @Override
    public void unsetGridSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
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
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[7], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "lastView" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STViewType.Enum getLastView() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STViewType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "lastView" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STViewType xgetLastView() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STViewType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STViewType)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STViewType)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return target;
        }
    }

    /**
     * True if has "lastView" attribute
     */
    @Override
    public boolean isSetLastView() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "lastView" attribute
     */
    @Override
    public void setLastView(org.openxmlformats.schemas.presentationml.x2006.main.STViewType.Enum lastView) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setEnumValue(lastView);
        }
    }

    /**
     * Sets (as xml) the "lastView" attribute
     */
    @Override
    public void xsetLastView(org.openxmlformats.schemas.presentationml.x2006.main.STViewType lastView) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STViewType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STViewType)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STViewType)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(lastView);
        }
    }

    /**
     * Unsets the "lastView" attribute
     */
    @Override
    public void unsetLastView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "showComments" attribute
     */
    @Override
    public boolean getShowComments() {
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
     * Gets (as xml) the "showComments" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowComments() {
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
     * True if has "showComments" attribute
     */
    @Override
    public boolean isSetShowComments() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "showComments" attribute
     */
    @Override
    public void setShowComments(boolean showComments) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(showComments);
        }
    }

    /**
     * Sets (as xml) the "showComments" attribute
     */
    @Override
    public void xsetShowComments(org.apache.xmlbeans.XmlBoolean showComments) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(showComments);
        }
    }

    /**
     * Unsets the "showComments" attribute
     */
    @Override
    public void unsetShowComments() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }
}
