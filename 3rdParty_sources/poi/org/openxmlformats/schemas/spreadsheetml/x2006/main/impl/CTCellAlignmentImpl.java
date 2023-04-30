/*
 * XML Type:  CT_CellAlignment
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CellAlignment(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCellAlignmentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment {
    private static final long serialVersionUID = 1L;

    public CTCellAlignmentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "horizontal"),
        new QName("", "vertical"),
        new QName("", "textRotation"),
        new QName("", "wrapText"),
        new QName("", "indent"),
        new QName("", "relativeIndent"),
        new QName("", "justifyLastLine"),
        new QName("", "shrinkToFit"),
        new QName("", "readingOrder"),
    };


    /**
     * Gets the "horizontal" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment.Enum getHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "horizontal" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment xgetHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "horizontal" attribute
     */
    @Override
    public boolean isSetHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "horizontal" attribute
     */
    @Override
    public void setHorizontal(org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment.Enum horizontal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(horizontal);
        }
    }

    /**
     * Sets (as xml) the "horizontal" attribute
     */
    @Override
    public void xsetHorizontal(org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment horizontal) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(horizontal);
        }
    }

    /**
     * Unsets the "horizontal" attribute
     */
    @Override
    public void unsetHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "vertical" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment.Enum getVertical() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "vertical" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment xgetVertical() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "vertical" attribute
     */
    @Override
    public boolean isSetVertical() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "vertical" attribute
     */
    @Override
    public void setVertical(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment.Enum vertical) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(vertical);
        }
    }

    /**
     * Sets (as xml) the "vertical" attribute
     */
    @Override
    public void xsetVertical(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment vertical) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STVerticalAlignment)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(vertical);
        }
    }

    /**
     * Unsets the "vertical" attribute
     */
    @Override
    public void unsetVertical() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "textRotation" attribute
     */
    @Override
    public java.math.BigInteger getTextRotation() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "textRotation" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation xgetTextRotation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "textRotation" attribute
     */
    @Override
    public boolean isSetTextRotation() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "textRotation" attribute
     */
    @Override
    public void setTextRotation(java.math.BigInteger textRotation) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBigIntegerValue(textRotation);
        }
    }

    /**
     * Sets (as xml) the "textRotation" attribute
     */
    @Override
    public void xsetTextRotation(org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation textRotation) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(textRotation);
        }
    }

    /**
     * Unsets the "textRotation" attribute
     */
    @Override
    public void unsetTextRotation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "wrapText" attribute
     */
    @Override
    public boolean getWrapText() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "wrapText" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetWrapText() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "wrapText" attribute
     */
    @Override
    public boolean isSetWrapText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "wrapText" attribute
     */
    @Override
    public void setWrapText(boolean wrapText) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBooleanValue(wrapText);
        }
    }

    /**
     * Sets (as xml) the "wrapText" attribute
     */
    @Override
    public void xsetWrapText(org.apache.xmlbeans.XmlBoolean wrapText) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(wrapText);
        }
    }

    /**
     * Unsets the "wrapText" attribute
     */
    @Override
    public void unsetWrapText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "indent" attribute
     */
    @Override
    public long getIndent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "indent" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "indent" attribute
     */
    @Override
    public boolean isSetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "indent" attribute
     */
    @Override
    public void setIndent(long indent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setLongValue(indent);
        }
    }

    /**
     * Sets (as xml) the "indent" attribute
     */
    @Override
    public void xsetIndent(org.apache.xmlbeans.XmlUnsignedInt indent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(indent);
        }
    }

    /**
     * Unsets the "indent" attribute
     */
    @Override
    public void unsetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "relativeIndent" attribute
     */
    @Override
    public int getRelativeIndent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "relativeIndent" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetRelativeIndent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "relativeIndent" attribute
     */
    @Override
    public boolean isSetRelativeIndent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "relativeIndent" attribute
     */
    @Override
    public void setRelativeIndent(int relativeIndent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setIntValue(relativeIndent);
        }
    }

    /**
     * Sets (as xml) the "relativeIndent" attribute
     */
    @Override
    public void xsetRelativeIndent(org.apache.xmlbeans.XmlInt relativeIndent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(relativeIndent);
        }
    }

    /**
     * Unsets the "relativeIndent" attribute
     */
    @Override
    public void unsetRelativeIndent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "justifyLastLine" attribute
     */
    @Override
    public boolean getJustifyLastLine() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "justifyLastLine" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetJustifyLastLine() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "justifyLastLine" attribute
     */
    @Override
    public boolean isSetJustifyLastLine() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "justifyLastLine" attribute
     */
    @Override
    public void setJustifyLastLine(boolean justifyLastLine) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBooleanValue(justifyLastLine);
        }
    }

    /**
     * Sets (as xml) the "justifyLastLine" attribute
     */
    @Override
    public void xsetJustifyLastLine(org.apache.xmlbeans.XmlBoolean justifyLastLine) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(justifyLastLine);
        }
    }

    /**
     * Unsets the "justifyLastLine" attribute
     */
    @Override
    public void unsetJustifyLastLine() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "shrinkToFit" attribute
     */
    @Override
    public boolean getShrinkToFit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "shrinkToFit" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShrinkToFit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "shrinkToFit" attribute
     */
    @Override
    public boolean isSetShrinkToFit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "shrinkToFit" attribute
     */
    @Override
    public void setShrinkToFit(boolean shrinkToFit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(shrinkToFit);
        }
    }

    /**
     * Sets (as xml) the "shrinkToFit" attribute
     */
    @Override
    public void xsetShrinkToFit(org.apache.xmlbeans.XmlBoolean shrinkToFit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(shrinkToFit);
        }
    }

    /**
     * Unsets the "shrinkToFit" attribute
     */
    @Override
    public void unsetShrinkToFit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "readingOrder" attribute
     */
    @Override
    public long getReadingOrder() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "readingOrder" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetReadingOrder() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "readingOrder" attribute
     */
    @Override
    public boolean isSetReadingOrder() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "readingOrder" attribute
     */
    @Override
    public void setReadingOrder(long readingOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setLongValue(readingOrder);
        }
    }

    /**
     * Sets (as xml) the "readingOrder" attribute
     */
    @Override
    public void xsetReadingOrder(org.apache.xmlbeans.XmlUnsignedInt readingOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(readingOrder);
        }
    }

    /**
     * Unsets the "readingOrder" attribute
     */
    @Override
    public void unsetReadingOrder() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }
}
