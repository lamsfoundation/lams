/*
 * XML Type:  CT_Frameset
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Frameset(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFramesetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset {
    private static final long serialVersionUID = 1L;

    public CTFramesetImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sz"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "framesetSplitbar"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "frameLayout"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "title"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "frameset"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "frame"),
    };


    /**
     * Gets the "sz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sz" element
     */
    @Override
    public boolean isSetSz() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "sz" element
     */
    @Override
    public void setSz(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString sz) {
        generatedSetterHelperImpl(sz, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sz" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "sz" element
     */
    @Override
    public void unsetSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "framesetSplitbar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar getFramesetSplitbar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "framesetSplitbar" element
     */
    @Override
    public boolean isSetFramesetSplitbar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "framesetSplitbar" element
     */
    @Override
    public void setFramesetSplitbar(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar framesetSplitbar) {
        generatedSetterHelperImpl(framesetSplitbar, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "framesetSplitbar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar addNewFramesetSplitbar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "framesetSplitbar" element
     */
    @Override
    public void unsetFramesetSplitbar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "frameLayout" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout getFrameLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "frameLayout" element
     */
    @Override
    public boolean isSetFrameLayout() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "frameLayout" element
     */
    @Override
    public void setFrameLayout(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout frameLayout) {
        generatedSetterHelperImpl(frameLayout, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "frameLayout" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout addNewFrameLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "frameLayout" element
     */
    @Override
    public void unsetFrameLayout() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "title" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "title" element
     */
    @Override
    public boolean isSetTitle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "title" element
     */
    @Override
    public void setTitle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString title) {
        generatedSetterHelperImpl(title, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "title" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "title" element
     */
    @Override
    public void unsetTitle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets a List of "frameset" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset> getFramesetList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFramesetArray,
                this::setFramesetArray,
                this::insertNewFrameset,
                this::removeFrameset,
                this::sizeOfFramesetArray
            );
        }
    }

    /**
     * Gets array of all "frameset" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset[] getFramesetArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset[0]);
    }

    /**
     * Gets ith "frameset" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset getFramesetArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "frameset" element
     */
    @Override
    public int sizeOfFramesetArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "frameset" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFramesetArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset[] framesetArray) {
        check_orphaned();
        arraySetterHelper(framesetArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "frameset" element
     */
    @Override
    public void setFramesetArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset frameset) {
        generatedSetterHelperImpl(frameset, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "frameset" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset insertNewFrameset(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "frameset" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset addNewFrameset() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "frameset" element
     */
    @Override
    public void removeFrameset(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "frame" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame> getFrameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFrameArray,
                this::setFrameArray,
                this::insertNewFrame,
                this::removeFrame,
                this::sizeOfFrameArray
            );
        }
    }

    /**
     * Gets array of all "frame" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame[] getFrameArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame[0]);
    }

    /**
     * Gets ith "frame" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame getFrameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "frame" element
     */
    @Override
    public int sizeOfFrameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "frame" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFrameArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame[] frameArray) {
        check_orphaned();
        arraySetterHelper(frameArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "frame" element
     */
    @Override
    public void setFrameArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame frame) {
        generatedSetterHelperImpl(frame, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "frame" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame insertNewFrame(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "frame" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame addNewFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "frame" element
     */
    @Override
    public void removeFrame(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }
}
