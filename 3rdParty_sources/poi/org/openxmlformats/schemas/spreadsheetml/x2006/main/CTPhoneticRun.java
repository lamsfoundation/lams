/*
 * XML Type:  CT_PhoneticRun
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PhoneticRun(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPhoneticRun extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticRun> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctphoneticrun2b2atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "t" element
     */
    java.lang.String getT();

    /**
     * Gets (as xml) the "t" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetT();

    /**
     * Sets the "t" element
     */
    void setT(java.lang.String t);

    /**
     * Sets (as xml) the "t" element
     */
    void xsetT(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring t);

    /**
     * Gets the "sb" attribute
     */
    long getSb();

    /**
     * Gets (as xml) the "sb" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetSb();

    /**
     * Sets the "sb" attribute
     */
    void setSb(long sb);

    /**
     * Sets (as xml) the "sb" attribute
     */
    void xsetSb(org.apache.xmlbeans.XmlUnsignedInt sb);

    /**
     * Gets the "eb" attribute
     */
    long getEb();

    /**
     * Gets (as xml) the "eb" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetEb();

    /**
     * Sets the "eb" attribute
     */
    void setEb(long eb);

    /**
     * Sets (as xml) the "eb" attribute
     */
    void xsetEb(org.apache.xmlbeans.XmlUnsignedInt eb);
}
