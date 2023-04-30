/*
 * XML Type:  CT_Caption
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Caption(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTCaptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption {
    private static final long serialVersionUID = 1L;

    public CTCaptionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "name"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pos"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "chapNum"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "heading"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noLabel"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numFmt"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sep"),
    };


    /**
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString name) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(name);
        }
    }

    /**
     * Gets the "pos" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos.Enum getPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "pos" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos xgetPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "pos" attribute
     */
    @Override
    public boolean isSetPos() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "pos" attribute
     */
    @Override
    public void setPos(org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos.Enum pos) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(pos);
        }
    }

    /**
     * Sets (as xml) the "pos" attribute
     */
    @Override
    public void xsetPos(org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos pos) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STCaptionPos)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(pos);
        }
    }

    /**
     * Unsets the "pos" attribute
     */
    @Override
    public void unsetPos() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "chapNum" attribute
     */
    @Override
    public java.lang.Object getChapNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "chapNum" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetChapNum() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "chapNum" attribute
     */
    @Override
    public boolean isSetChapNum() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "chapNum" attribute
     */
    @Override
    public void setChapNum(java.lang.Object chapNum) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(chapNum);
        }
    }

    /**
     * Sets (as xml) the "chapNum" attribute
     */
    @Override
    public void xsetChapNum(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff chapNum) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(chapNum);
        }
    }

    /**
     * Unsets the "chapNum" attribute
     */
    @Override
    public void unsetChapNum() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "heading" attribute
     */
    @Override
    public java.math.BigInteger getHeading() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "heading" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetHeading() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "heading" attribute
     */
    @Override
    public boolean isSetHeading() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "heading" attribute
     */
    @Override
    public void setHeading(java.math.BigInteger heading) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBigIntegerValue(heading);
        }
    }

    /**
     * Sets (as xml) the "heading" attribute
     */
    @Override
    public void xsetHeading(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber heading) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(heading);
        }
    }

    /**
     * Unsets the "heading" attribute
     */
    @Override
    public void unsetHeading() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "noLabel" attribute
     */
    @Override
    public java.lang.Object getNoLabel() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "noLabel" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetNoLabel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "noLabel" attribute
     */
    @Override
    public boolean isSetNoLabel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "noLabel" attribute
     */
    @Override
    public void setNoLabel(java.lang.Object noLabel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setObjectValue(noLabel);
        }
    }

    /**
     * Sets (as xml) the "noLabel" attribute
     */
    @Override
    public void xsetNoLabel(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff noLabel) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(noLabel);
        }
    }

    /**
     * Unsets the "noLabel" attribute
     */
    @Override
    public void unsetNoLabel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "numFmt" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum getNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "numFmt" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat xgetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "numFmt" attribute
     */
    @Override
    public boolean isSetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "numFmt" attribute
     */
    @Override
    public void setNumFmt(org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum numFmt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(numFmt);
        }
    }

    /**
     * Sets (as xml) the "numFmt" attribute
     */
    @Override
    public void xsetNumFmt(org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat numFmt) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(numFmt);
        }
    }

    /**
     * Unsets the "numFmt" attribute
     */
    @Override
    public void unsetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "sep" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.Enum getSep() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "sep" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep xgetSep() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "sep" attribute
     */
    @Override
    public boolean isSetSep() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "sep" attribute
     */
    @Override
    public void setSep(org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.Enum sep) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(sep);
        }
    }

    /**
     * Sets (as xml) the "sep" attribute
     */
    @Override
    public void xsetSep(org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep sep) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(sep);
        }
    }

    /**
     * Unsets the "sep" attribute
     */
    @Override
    public void unsetSep() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}
