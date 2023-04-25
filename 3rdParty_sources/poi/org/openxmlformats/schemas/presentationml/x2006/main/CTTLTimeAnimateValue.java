/*
 * XML Type:  CT_TLTimeAnimateValue
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLTimeAnimateValue(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLTimeAnimateValue extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttltimeanimatevalue07d0type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant getVal();

    /**
     * True if has "val" element
     */
    boolean isSetVal();

    /**
     * Sets the "val" element
     */
    void setVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant val);

    /**
     * Appends and returns a new empty "val" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant addNewVal();

    /**
     * Unsets the "val" element
     */
    void unsetVal();

    /**
     * Gets the "tm" attribute
     */
    java.lang.Object getTm();

    /**
     * Gets (as xml) the "tm" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime xgetTm();

    /**
     * True if has "tm" attribute
     */
    boolean isSetTm();

    /**
     * Sets the "tm" attribute
     */
    void setTm(java.lang.Object tm);

    /**
     * Sets (as xml) the "tm" attribute
     */
    void xsetTm(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime tm);

    /**
     * Unsets the "tm" attribute
     */
    void unsetTm();

    /**
     * Gets the "fmla" attribute
     */
    java.lang.String getFmla();

    /**
     * Gets (as xml) the "fmla" attribute
     */
    org.apache.xmlbeans.XmlString xgetFmla();

    /**
     * True if has "fmla" attribute
     */
    boolean isSetFmla();

    /**
     * Sets the "fmla" attribute
     */
    void setFmla(java.lang.String fmla);

    /**
     * Sets (as xml) the "fmla" attribute
     */
    void xsetFmla(org.apache.xmlbeans.XmlString fmla);

    /**
     * Unsets the "fmla" attribute
     */
    void unsetFmla();
}
