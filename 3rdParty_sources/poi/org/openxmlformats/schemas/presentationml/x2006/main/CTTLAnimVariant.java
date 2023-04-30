/*
 * XML Type:  CT_TLAnimVariant
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLAnimVariant(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLAnimVariant extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlanimvariantc9d3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "boolVal" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal getBoolVal();

    /**
     * True if has "boolVal" element
     */
    boolean isSetBoolVal();

    /**
     * Sets the "boolVal" element
     */
    void setBoolVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal boolVal);

    /**
     * Appends and returns a new empty "boolVal" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantBooleanVal addNewBoolVal();

    /**
     * Unsets the "boolVal" element
     */
    void unsetBoolVal();

    /**
     * Gets the "intVal" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal getIntVal();

    /**
     * True if has "intVal" element
     */
    boolean isSetIntVal();

    /**
     * Sets the "intVal" element
     */
    void setIntVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal intVal);

    /**
     * Appends and returns a new empty "intVal" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantIntegerVal addNewIntVal();

    /**
     * Unsets the "intVal" element
     */
    void unsetIntVal();

    /**
     * Gets the "fltVal" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal getFltVal();

    /**
     * True if has "fltVal" element
     */
    boolean isSetFltVal();

    /**
     * Sets the "fltVal" element
     */
    void setFltVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal fltVal);

    /**
     * Appends and returns a new empty "fltVal" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantFloatVal addNewFltVal();

    /**
     * Unsets the "fltVal" element
     */
    void unsetFltVal();

    /**
     * Gets the "strVal" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal getStrVal();

    /**
     * True if has "strVal" element
     */
    boolean isSetStrVal();

    /**
     * Sets the "strVal" element
     */
    void setStrVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal strVal);

    /**
     * Appends and returns a new empty "strVal" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariantStringVal addNewStrVal();

    /**
     * Unsets the "strVal" element
     */
    void unsetStrVal();

    /**
     * Gets the "clrVal" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor getClrVal();

    /**
     * True if has "clrVal" element
     */
    boolean isSetClrVal();

    /**
     * Sets the "clrVal" element
     */
    void setClrVal(org.openxmlformats.schemas.drawingml.x2006.main.CTColor clrVal);

    /**
     * Appends and returns a new empty "clrVal" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewClrVal();

    /**
     * Unsets the "clrVal" element
     */
    void unsetClrVal();
}
