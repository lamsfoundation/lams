/*
 * XML Type:  CT_TLAnimateRotationBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLAnimateRotationBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLAnimateRotationBehavior extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateRotationBehavior> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlanimaterotationbehavior5076type");
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
     * Gets the "by" attribute
     */
    int getBy();

    /**
     * Gets (as xml) the "by" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAngle xgetBy();

    /**
     * True if has "by" attribute
     */
    boolean isSetBy();

    /**
     * Sets the "by" attribute
     */
    void setBy(int by);

    /**
     * Sets (as xml) the "by" attribute
     */
    void xsetBy(org.openxmlformats.schemas.drawingml.x2006.main.STAngle by);

    /**
     * Unsets the "by" attribute
     */
    void unsetBy();

    /**
     * Gets the "from" attribute
     */
    int getFrom();

    /**
     * Gets (as xml) the "from" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAngle xgetFrom();

    /**
     * True if has "from" attribute
     */
    boolean isSetFrom();

    /**
     * Sets the "from" attribute
     */
    void setFrom(int from);

    /**
     * Sets (as xml) the "from" attribute
     */
    void xsetFrom(org.openxmlformats.schemas.drawingml.x2006.main.STAngle from);

    /**
     * Unsets the "from" attribute
     */
    void unsetFrom();

    /**
     * Gets the "to" attribute
     */
    int getTo();

    /**
     * Gets (as xml) the "to" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAngle xgetTo();

    /**
     * True if has "to" attribute
     */
    boolean isSetTo();

    /**
     * Sets the "to" attribute
     */
    void setTo(int to);

    /**
     * Sets (as xml) the "to" attribute
     */
    void xsetTo(org.openxmlformats.schemas.drawingml.x2006.main.STAngle to);

    /**
     * Unsets the "to" attribute
     */
    void unsetTo();
}
