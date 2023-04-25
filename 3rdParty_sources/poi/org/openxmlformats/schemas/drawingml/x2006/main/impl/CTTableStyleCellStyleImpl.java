/*
 * XML Type:  CT_TableStyleCellStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TableStyleCellStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTableStyleCellStyleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle {
    private static final long serialVersionUID = 1L;

    public CTTableStyleCellStyleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tcBdr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fillRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cell3D"),
    };


    /**
     * Gets the "tcBdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle getTcBdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tcBdr" element
     */
    @Override
    public boolean isSetTcBdr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tcBdr" element
     */
    @Override
    public void setTcBdr(org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle tcBdr) {
        generatedSetterHelperImpl(tcBdr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcBdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle addNewTcBdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tcBdr" element
     */
    @Override
    public void unsetTcBdr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "fill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties getFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fill" element
     */
    @Override
    public boolean isSetFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "fill" element
     */
    @Override
    public void setFill(org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties fill) {
        generatedSetterHelperImpl(fill, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties addNewFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "fill" element
     */
    @Override
    public void unsetFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "fillRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getFillRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fillRef" element
     */
    @Override
    public boolean isSetFillRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "fillRef" element
     */
    @Override
    public void setFillRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference fillRef) {
        generatedSetterHelperImpl(fillRef, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fillRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewFillRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "fillRef" element
     */
    @Override
    public void unsetFillRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "cell3D" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D getCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cell3D" element
     */
    @Override
    public boolean isSetCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "cell3D" element
     */
    @Override
    public void setCell3D(org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D cell3D) {
        generatedSetterHelperImpl(cell3D, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cell3D" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D addNewCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "cell3D" element
     */
    @Override
    public void unsetCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
