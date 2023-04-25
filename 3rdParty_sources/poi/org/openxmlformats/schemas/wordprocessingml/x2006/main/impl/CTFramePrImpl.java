/*
 * XML Type:  CT_FramePr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FramePr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFramePrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr {
    private static final long serialVersionUID = 1L;

    public CTFramePrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dropCap"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lines"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "w"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "h"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vSpace"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hSpace"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "wrap"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hAnchor"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vAnchor"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "x"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "xAlign"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "y"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "yAlign"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hRule"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "anchorLock"),
    };


    /**
     * Gets the "dropCap" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap.Enum getDropCap() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "dropCap" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap xgetDropCap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "dropCap" attribute
     */
    @Override
    public boolean isSetDropCap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "dropCap" attribute
     */
    @Override
    public void setDropCap(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap.Enum dropCap) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(dropCap);
        }
    }

    /**
     * Sets (as xml) the "dropCap" attribute
     */
    @Override
    public void xsetDropCap(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap dropCap) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(dropCap);
        }
    }

    /**
     * Unsets the "dropCap" attribute
     */
    @Override
    public void unsetDropCap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "lines" attribute
     */
    @Override
    public java.math.BigInteger getLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "lines" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "lines" attribute
     */
    @Override
    public boolean isSetLines() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "lines" attribute
     */
    @Override
    public void setLines(java.math.BigInteger lines) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setBigIntegerValue(lines);
        }
    }

    /**
     * Sets (as xml) the "lines" attribute
     */
    @Override
    public void xsetLines(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber lines) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(lines);
        }
    }

    /**
     * Unsets the "lines" attribute
     */
    @Override
    public void unsetLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "w" attribute
     */
    @Override
    public java.lang.Object getW() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "w" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "w" attribute
     */
    @Override
    public boolean isSetW() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "w" attribute
     */
    @Override
    public void setW(java.lang.Object w) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(w);
        }
    }

    /**
     * Sets (as xml) the "w" attribute
     */
    @Override
    public void xsetW(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure w) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(w);
        }
    }

    /**
     * Unsets the "w" attribute
     */
    @Override
    public void unsetW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "h" attribute
     */
    @Override
    public java.lang.Object getH() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "h" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetH() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "h" attribute
     */
    @Override
    public boolean isSetH() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "h" attribute
     */
    @Override
    public void setH(java.lang.Object h) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(h);
        }
    }

    /**
     * Sets (as xml) the "h" attribute
     */
    @Override
    public void xsetH(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure h) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(h);
        }
    }

    /**
     * Unsets the "h" attribute
     */
    @Override
    public void unsetH() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "vSpace" attribute
     */
    @Override
    public java.lang.Object getVSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "vSpace" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetVSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "vSpace" attribute
     */
    @Override
    public boolean isSetVSpace() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "vSpace" attribute
     */
    @Override
    public void setVSpace(java.lang.Object vSpace) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setObjectValue(vSpace);
        }
    }

    /**
     * Sets (as xml) the "vSpace" attribute
     */
    @Override
    public void xsetVSpace(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure vSpace) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(vSpace);
        }
    }

    /**
     * Unsets the "vSpace" attribute
     */
    @Override
    public void unsetVSpace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "hSpace" attribute
     */
    @Override
    public java.lang.Object getHSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "hSpace" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetHSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "hSpace" attribute
     */
    @Override
    public boolean isSetHSpace() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "hSpace" attribute
     */
    @Override
    public void setHSpace(java.lang.Object hSpace) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setObjectValue(hSpace);
        }
    }

    /**
     * Sets (as xml) the "hSpace" attribute
     */
    @Override
    public void xsetHSpace(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure hSpace) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(hSpace);
        }
    }

    /**
     * Unsets the "hSpace" attribute
     */
    @Override
    public void unsetHSpace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "wrap" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap.Enum getWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "wrap" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap xgetWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "wrap" attribute
     */
    @Override
    public boolean isSetWrap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "wrap" attribute
     */
    @Override
    public void setWrap(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap.Enum wrap) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(wrap);
        }
    }

    /**
     * Sets (as xml) the "wrap" attribute
     */
    @Override
    public void xsetWrap(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap wrap) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(wrap);
        }
    }

    /**
     * Unsets the "wrap" attribute
     */
    @Override
    public void unsetWrap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "hAnchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor.Enum getHAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "hAnchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor xgetHAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "hAnchor" attribute
     */
    @Override
    public boolean isSetHAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "hAnchor" attribute
     */
    @Override
    public void setHAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor.Enum hAnchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setEnumValue(hAnchor);
        }
    }

    /**
     * Sets (as xml) the "hAnchor" attribute
     */
    @Override
    public void xsetHAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor hAnchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(hAnchor);
        }
    }

    /**
     * Unsets the "hAnchor" attribute
     */
    @Override
    public void unsetHAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "vAnchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor.Enum getVAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "vAnchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor xgetVAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "vAnchor" attribute
     */
    @Override
    public boolean isSetVAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "vAnchor" attribute
     */
    @Override
    public void setVAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor.Enum vAnchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setEnumValue(vAnchor);
        }
    }

    /**
     * Sets (as xml) the "vAnchor" attribute
     */
    @Override
    public void xsetVAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor vAnchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(vAnchor);
        }
    }

    /**
     * Unsets the "vAnchor" attribute
     */
    @Override
    public void unsetVAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "x" attribute
     */
    @Override
    public java.lang.Object getX() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "x" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "x" attribute
     */
    @Override
    public boolean isSetX() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "x" attribute
     */
    @Override
    public void setX(java.lang.Object x) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setObjectValue(x);
        }
    }

    /**
     * Sets (as xml) the "x" attribute
     */
    @Override
    public void xsetX(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure x) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(x);
        }
    }

    /**
     * Unsets the "x" attribute
     */
    @Override
    public void unsetX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "xAlign" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign.Enum getXAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "xAlign" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign xgetXAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "xAlign" attribute
     */
    @Override
    public boolean isSetXAlign() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "xAlign" attribute
     */
    @Override
    public void setXAlign(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign.Enum xAlign) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setEnumValue(xAlign);
        }
    }

    /**
     * Sets (as xml) the "xAlign" attribute
     */
    @Override
    public void xsetXAlign(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign xAlign) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(xAlign);
        }
    }

    /**
     * Unsets the "xAlign" attribute
     */
    @Override
    public void unsetXAlign() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "y" attribute
     */
    @Override
    public java.lang.Object getY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "y" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "y" attribute
     */
    @Override
    public boolean isSetY() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "y" attribute
     */
    @Override
    public void setY(java.lang.Object y) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setObjectValue(y);
        }
    }

    /**
     * Sets (as xml) the "y" attribute
     */
    @Override
    public void xsetY(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure y) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(y);
        }
    }

    /**
     * Unsets the "y" attribute
     */
    @Override
    public void unsetY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "yAlign" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign.Enum getYAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "yAlign" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign xgetYAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "yAlign" attribute
     */
    @Override
    public boolean isSetYAlign() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "yAlign" attribute
     */
    @Override
    public void setYAlign(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign.Enum yAlign) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setEnumValue(yAlign);
        }
    }

    /**
     * Sets (as xml) the "yAlign" attribute
     */
    @Override
    public void xsetYAlign(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign yAlign) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(yAlign);
        }
    }

    /**
     * Unsets the "yAlign" attribute
     */
    @Override
    public void unsetYAlign() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "hRule" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule.Enum getHRule() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "hRule" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule xgetHRule() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * True if has "hRule" attribute
     */
    @Override
    public boolean isSetHRule() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "hRule" attribute
     */
    @Override
    public void setHRule(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule.Enum hRule) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setEnumValue(hRule);
        }
    }

    /**
     * Sets (as xml) the "hRule" attribute
     */
    @Override
    public void xsetHRule(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule hRule) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(hRule);
        }
    }

    /**
     * Unsets the "hRule" attribute
     */
    @Override
    public void unsetHRule() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "anchorLock" attribute
     */
    @Override
    public java.lang.Object getAnchorLock() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "anchorLock" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetAnchorLock() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * True if has "anchorLock" attribute
     */
    @Override
    public boolean isSetAnchorLock() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "anchorLock" attribute
     */
    @Override
    public void setAnchorLock(java.lang.Object anchorLock) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setObjectValue(anchorLock);
        }
    }

    /**
     * Sets (as xml) the "anchorLock" attribute
     */
    @Override
    public void xsetAnchorLock(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff anchorLock) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(anchorLock);
        }
    }

    /**
     * Unsets the "anchorLock" attribute
     */
    @Override
    public void unsetAnchorLock() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }
}
