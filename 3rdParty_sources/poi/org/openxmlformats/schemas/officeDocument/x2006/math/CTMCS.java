/*
 * XML Type:  CT_MCS
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MCS(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public interface CTMCS extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmcs4b1ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "mc" elements
     */
    java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTMC> getMcList();

    /**
     * Gets array of all "mc" elements
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTMC[] getMcArray();

    /**
     * Gets ith "mc" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTMC getMcArray(int i);

    /**
     * Returns number of "mc" element
     */
    int sizeOfMcArray();

    /**
     * Sets array of all "mc" element
     */
    void setMcArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTMC[] mcArray);

    /**
     * Sets ith "mc" element
     */
    void setMcArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTMC mc);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "mc" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTMC insertNewMc(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "mc" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTMC addNewMc();

    /**
     * Removes the ith "mc" element
     */
    void removeMc(int i);
}
