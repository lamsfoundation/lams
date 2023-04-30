/*
 * XML Type:  CT_TLAnimateScaleBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLAnimateScaleBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLAnimateScaleBehavior extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlanimatescalebehavior91b2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cBhvr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData getCBhvr();

    /**
     * Sets the "cBhvr" element
     */
    void setCBhvr(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData cBhvr);

    /**
     * Appends and returns a new empty "cBhvr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData addNewCBhvr();

    /**
     * Gets the "by" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getBy();

    /**
     * True if has "by" element
     */
    boolean isSetBy();

    /**
     * Sets the "by" element
     */
    void setBy(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint by);

    /**
     * Appends and returns a new empty "by" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewBy();

    /**
     * Unsets the "by" element
     */
    void unsetBy();

    /**
     * Gets the "from" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getFrom();

    /**
     * True if has "from" element
     */
    boolean isSetFrom();

    /**
     * Sets the "from" element
     */
    void setFrom(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint from);

    /**
     * Appends and returns a new empty "from" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewFrom();

    /**
     * Unsets the "from" element
     */
    void unsetFrom();

    /**
     * Gets the "to" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getTo();

    /**
     * True if has "to" element
     */
    boolean isSetTo();

    /**
     * Sets the "to" element
     */
    void setTo(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint to);

    /**
     * Appends and returns a new empty "to" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewTo();

    /**
     * Unsets the "to" element
     */
    void unsetTo();

    /**
     * Gets the "zoomContents" attribute
     */
    boolean getZoomContents();

    /**
     * Gets (as xml) the "zoomContents" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetZoomContents();

    /**
     * True if has "zoomContents" attribute
     */
    boolean isSetZoomContents();

    /**
     * Sets the "zoomContents" attribute
     */
    void setZoomContents(boolean zoomContents);

    /**
     * Sets (as xml) the "zoomContents" attribute
     */
    void xsetZoomContents(org.apache.xmlbeans.XmlBoolean zoomContents);

    /**
     * Unsets the "zoomContents" attribute
     */
    void unsetZoomContents();
}
