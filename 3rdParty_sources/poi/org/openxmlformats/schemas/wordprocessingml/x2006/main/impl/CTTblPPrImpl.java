/*
 * XML Type:  CT_TblPPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TblPPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTblPPrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPPr {
    private static final long serialVersionUID = 1L;

    public CTTblPPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "leftFromText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rightFromText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "topFromText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bottomFromText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vertAnchor"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "horzAnchor"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblpXSpec"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblpX"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblpYSpec"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblpY"),
    };


    /**
     * Gets the "leftFromText" attribute
     */
    @Override
    public java.lang.Object getLeftFromText() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "leftFromText" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetLeftFromText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "leftFromText" attribute
     */
    @Override
    public boolean isSetLeftFromText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "leftFromText" attribute
     */
    @Override
    public void setLeftFromText(java.lang.Object leftFromText) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(leftFromText);
        }
    }

    /**
     * Sets (as xml) the "leftFromText" attribute
     */
    @Override
    public void xsetLeftFromText(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure leftFromText) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(leftFromText);
        }
    }

    /**
     * Unsets the "leftFromText" attribute
     */
    @Override
    public void unsetLeftFromText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "rightFromText" attribute
     */
    @Override
    public java.lang.Object getRightFromText() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "rightFromText" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetRightFromText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "rightFromText" attribute
     */
    @Override
    public boolean isSetRightFromText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "rightFromText" attribute
     */
    @Override
    public void setRightFromText(java.lang.Object rightFromText) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(rightFromText);
        }
    }

    /**
     * Sets (as xml) the "rightFromText" attribute
     */
    @Override
    public void xsetRightFromText(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure rightFromText) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(rightFromText);
        }
    }

    /**
     * Unsets the "rightFromText" attribute
     */
    @Override
    public void unsetRightFromText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "topFromText" attribute
     */
    @Override
    public java.lang.Object getTopFromText() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "topFromText" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetTopFromText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "topFromText" attribute
     */
    @Override
    public boolean isSetTopFromText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "topFromText" attribute
     */
    @Override
    public void setTopFromText(java.lang.Object topFromText) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(topFromText);
        }
    }

    /**
     * Sets (as xml) the "topFromText" attribute
     */
    @Override
    public void xsetTopFromText(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure topFromText) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(topFromText);
        }
    }

    /**
     * Unsets the "topFromText" attribute
     */
    @Override
    public void unsetTopFromText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "bottomFromText" attribute
     */
    @Override
    public java.lang.Object getBottomFromText() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "bottomFromText" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetBottomFromText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "bottomFromText" attribute
     */
    @Override
    public boolean isSetBottomFromText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "bottomFromText" attribute
     */
    @Override
    public void setBottomFromText(java.lang.Object bottomFromText) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(bottomFromText);
        }
    }

    /**
     * Sets (as xml) the "bottomFromText" attribute
     */
    @Override
    public void xsetBottomFromText(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure bottomFromText) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(bottomFromText);
        }
    }

    /**
     * Unsets the "bottomFromText" attribute
     */
    @Override
    public void unsetBottomFromText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "vertAnchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor.Enum getVertAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "vertAnchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor xgetVertAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "vertAnchor" attribute
     */
    @Override
    public boolean isSetVertAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "vertAnchor" attribute
     */
    @Override
    public void setVertAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor.Enum vertAnchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(vertAnchor);
        }
    }

    /**
     * Sets (as xml) the "vertAnchor" attribute
     */
    @Override
    public void xsetVertAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor vertAnchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(vertAnchor);
        }
    }

    /**
     * Unsets the "vertAnchor" attribute
     */
    @Override
    public void unsetVertAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "horzAnchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor.Enum getHorzAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "horzAnchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor xgetHorzAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "horzAnchor" attribute
     */
    @Override
    public boolean isSetHorzAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "horzAnchor" attribute
     */
    @Override
    public void setHorzAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor.Enum horzAnchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(horzAnchor);
        }
    }

    /**
     * Sets (as xml) the "horzAnchor" attribute
     */
    @Override
    public void xsetHorzAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor horzAnchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(horzAnchor);
        }
    }

    /**
     * Unsets the "horzAnchor" attribute
     */
    @Override
    public void unsetHorzAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "tblpXSpec" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign.Enum getTblpXSpec() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "tblpXSpec" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign xgetTblpXSpec() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "tblpXSpec" attribute
     */
    @Override
    public boolean isSetTblpXSpec() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "tblpXSpec" attribute
     */
    @Override
    public void setTblpXSpec(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign.Enum tblpXSpec) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(tblpXSpec);
        }
    }

    /**
     * Sets (as xml) the "tblpXSpec" attribute
     */
    @Override
    public void xsetTblpXSpec(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign tblpXSpec) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(tblpXSpec);
        }
    }

    /**
     * Unsets the "tblpXSpec" attribute
     */
    @Override
    public void unsetTblpXSpec() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "tblpX" attribute
     */
    @Override
    public java.lang.Object getTblpX() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "tblpX" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetTblpX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "tblpX" attribute
     */
    @Override
    public boolean isSetTblpX() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "tblpX" attribute
     */
    @Override
    public void setTblpX(java.lang.Object tblpX) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setObjectValue(tblpX);
        }
    }

    /**
     * Sets (as xml) the "tblpX" attribute
     */
    @Override
    public void xsetTblpX(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure tblpX) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(tblpX);
        }
    }

    /**
     * Unsets the "tblpX" attribute
     */
    @Override
    public void unsetTblpX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "tblpYSpec" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign.Enum getTblpYSpec() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "tblpYSpec" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign xgetTblpYSpec() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "tblpYSpec" attribute
     */
    @Override
    public boolean isSetTblpYSpec() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "tblpYSpec" attribute
     */
    @Override
    public void setTblpYSpec(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign.Enum tblpYSpec) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setEnumValue(tblpYSpec);
        }
    }

    /**
     * Sets (as xml) the "tblpYSpec" attribute
     */
    @Override
    public void xsetTblpYSpec(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign tblpYSpec) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(tblpYSpec);
        }
    }

    /**
     * Unsets the "tblpYSpec" attribute
     */
    @Override
    public void unsetTblpYSpec() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "tblpY" attribute
     */
    @Override
    public java.lang.Object getTblpY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "tblpY" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetTblpY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "tblpY" attribute
     */
    @Override
    public boolean isSetTblpY() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "tblpY" attribute
     */
    @Override
    public void setTblpY(java.lang.Object tblpY) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setObjectValue(tblpY);
        }
    }

    /**
     * Sets (as xml) the "tblpY" attribute
     */
    @Override
    public void xsetTblpY(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure tblpY) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(tblpY);
        }
    }

    /**
     * Unsets the "tblpY" attribute
     */
    @Override
    public void unsetTblpY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }
}
