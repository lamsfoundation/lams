/*
 * XML Type:  CT_PageSetup
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PageSetup(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTPageSetupImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup {
    private static final long serialVersionUID = 1L;

    public CTPageSetupImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "paperSize"),
        new QName("", "paperHeight"),
        new QName("", "paperWidth"),
        new QName("", "firstPageNumber"),
        new QName("", "orientation"),
        new QName("", "blackAndWhite"),
        new QName("", "draft"),
        new QName("", "useFirstPageNumber"),
        new QName("", "horizontalDpi"),
        new QName("", "verticalDpi"),
        new QName("", "copies"),
    };


    /**
     * Gets the "paperSize" attribute
     */
    @Override
    public long getPaperSize() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "paperSize" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetPaperSize() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "paperSize" attribute
     */
    @Override
    public boolean isSetPaperSize() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "paperSize" attribute
     */
    @Override
    public void setPaperSize(long paperSize) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setLongValue(paperSize);
        }
    }

    /**
     * Sets (as xml) the "paperSize" attribute
     */
    @Override
    public void xsetPaperSize(org.apache.xmlbeans.XmlUnsignedInt paperSize) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(paperSize);
        }
    }

    /**
     * Unsets the "paperSize" attribute
     */
    @Override
    public void unsetPaperSize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "paperHeight" attribute
     */
    @Override
    public java.lang.String getPaperHeight() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "paperHeight" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure xgetPaperHeight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "paperHeight" attribute
     */
    @Override
    public boolean isSetPaperHeight() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "paperHeight" attribute
     */
    @Override
    public void setPaperHeight(java.lang.String paperHeight) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(paperHeight);
        }
    }

    /**
     * Sets (as xml) the "paperHeight" attribute
     */
    @Override
    public void xsetPaperHeight(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure paperHeight) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(paperHeight);
        }
    }

    /**
     * Unsets the "paperHeight" attribute
     */
    @Override
    public void unsetPaperHeight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "paperWidth" attribute
     */
    @Override
    public java.lang.String getPaperWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "paperWidth" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure xgetPaperWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "paperWidth" attribute
     */
    @Override
    public boolean isSetPaperWidth() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "paperWidth" attribute
     */
    @Override
    public void setPaperWidth(java.lang.String paperWidth) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(paperWidth);
        }
    }

    /**
     * Sets (as xml) the "paperWidth" attribute
     */
    @Override
    public void xsetPaperWidth(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure paperWidth) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPositiveUniversalMeasure)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(paperWidth);
        }
    }

    /**
     * Unsets the "paperWidth" attribute
     */
    @Override
    public void unsetPaperWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "firstPageNumber" attribute
     */
    @Override
    public long getFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "firstPageNumber" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "firstPageNumber" attribute
     */
    @Override
    public boolean isSetFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "firstPageNumber" attribute
     */
    @Override
    public void setFirstPageNumber(long firstPageNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setLongValue(firstPageNumber);
        }
    }

    /**
     * Sets (as xml) the "firstPageNumber" attribute
     */
    @Override
    public void xsetFirstPageNumber(org.apache.xmlbeans.XmlUnsignedInt firstPageNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(firstPageNumber);
        }
    }

    /**
     * Unsets the "firstPageNumber" attribute
     */
    @Override
    public void unsetFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "orientation" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation.Enum getOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "orientation" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation xgetOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "orientation" attribute
     */
    @Override
    public boolean isSetOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "orientation" attribute
     */
    @Override
    public void setOrientation(org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation.Enum orientation) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(orientation);
        }
    }

    /**
     * Sets (as xml) the "orientation" attribute
     */
    @Override
    public void xsetOrientation(org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation orientation) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.chart.STPageSetupOrientation)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(orientation);
        }
    }

    /**
     * Unsets the "orientation" attribute
     */
    @Override
    public void unsetOrientation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "blackAndWhite" attribute
     */
    @Override
    public boolean getBlackAndWhite() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "blackAndWhite" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetBlackAndWhite() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "blackAndWhite" attribute
     */
    @Override
    public boolean isSetBlackAndWhite() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "blackAndWhite" attribute
     */
    @Override
    public void setBlackAndWhite(boolean blackAndWhite) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setBooleanValue(blackAndWhite);
        }
    }

    /**
     * Sets (as xml) the "blackAndWhite" attribute
     */
    @Override
    public void xsetBlackAndWhite(org.apache.xmlbeans.XmlBoolean blackAndWhite) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(blackAndWhite);
        }
    }

    /**
     * Unsets the "blackAndWhite" attribute
     */
    @Override
    public void unsetBlackAndWhite() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "draft" attribute
     */
    @Override
    public boolean getDraft() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "draft" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDraft() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "draft" attribute
     */
    @Override
    public boolean isSetDraft() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "draft" attribute
     */
    @Override
    public void setDraft(boolean draft) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBooleanValue(draft);
        }
    }

    /**
     * Sets (as xml) the "draft" attribute
     */
    @Override
    public void xsetDraft(org.apache.xmlbeans.XmlBoolean draft) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(draft);
        }
    }

    /**
     * Unsets the "draft" attribute
     */
    @Override
    public void unsetDraft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "useFirstPageNumber" attribute
     */
    @Override
    public boolean getUseFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "useFirstPageNumber" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetUseFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return target;
        }
    }

    /**
     * True if has "useFirstPageNumber" attribute
     */
    @Override
    public boolean isSetUseFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "useFirstPageNumber" attribute
     */
    @Override
    public void setUseFirstPageNumber(boolean useFirstPageNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(useFirstPageNumber);
        }
    }

    /**
     * Sets (as xml) the "useFirstPageNumber" attribute
     */
    @Override
    public void xsetUseFirstPageNumber(org.apache.xmlbeans.XmlBoolean useFirstPageNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(useFirstPageNumber);
        }
    }

    /**
     * Unsets the "useFirstPageNumber" attribute
     */
    @Override
    public void unsetUseFirstPageNumber() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "horizontalDpi" attribute
     */
    @Override
    public int getHorizontalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "horizontalDpi" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetHorizontalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return target;
        }
    }

    /**
     * True if has "horizontalDpi" attribute
     */
    @Override
    public boolean isSetHorizontalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "horizontalDpi" attribute
     */
    @Override
    public void setHorizontalDpi(int horizontalDpi) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setIntValue(horizontalDpi);
        }
    }

    /**
     * Sets (as xml) the "horizontalDpi" attribute
     */
    @Override
    public void xsetHorizontalDpi(org.apache.xmlbeans.XmlInt horizontalDpi) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(horizontalDpi);
        }
    }

    /**
     * Unsets the "horizontalDpi" attribute
     */
    @Override
    public void unsetHorizontalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "verticalDpi" attribute
     */
    @Override
    public int getVerticalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "verticalDpi" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetVerticalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return target;
        }
    }

    /**
     * True if has "verticalDpi" attribute
     */
    @Override
    public boolean isSetVerticalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "verticalDpi" attribute
     */
    @Override
    public void setVerticalDpi(int verticalDpi) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setIntValue(verticalDpi);
        }
    }

    /**
     * Sets (as xml) the "verticalDpi" attribute
     */
    @Override
    public void xsetVerticalDpi(org.apache.xmlbeans.XmlInt verticalDpi) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(verticalDpi);
        }
    }

    /**
     * Unsets the "verticalDpi" attribute
     */
    @Override
    public void unsetVerticalDpi() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "copies" attribute
     */
    @Override
    public long getCopies() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "copies" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCopies() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return target;
        }
    }

    /**
     * True if has "copies" attribute
     */
    @Override
    public boolean isSetCopies() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "copies" attribute
     */
    @Override
    public void setCopies(long copies) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setLongValue(copies);
        }
    }

    /**
     * Sets (as xml) the "copies" attribute
     */
    @Override
    public void xsetCopies(org.apache.xmlbeans.XmlUnsignedInt copies) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(copies);
        }
    }

    /**
     * Unsets the "copies" attribute
     */
    @Override
    public void unsetCopies() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }
}
