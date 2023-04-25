/*
 * XML Type:  CT_PivotAreaReferences
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreaReferences
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PivotAreaReferences(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPivotAreaReferences extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreaReferences> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpivotareareferencesaef6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "reference" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreaReference> getReferenceList();

    /**
     * Gets array of all "reference" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreaReference[] getReferenceArray();

    /**
     * Gets ith "reference" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreaReference getReferenceArray(int i);

    /**
     * Returns number of "reference" element
     */
    int sizeOfReferenceArray();

    /**
     * Sets array of all "reference" element
     */
    void setReferenceArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreaReference[] referenceArray);

    /**
     * Sets ith "reference" element
     */
    void setReferenceArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreaReference reference);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "reference" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreaReference insertNewReference(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "reference" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreaReference addNewReference();

    /**
     * Removes the ith "reference" element
     */
    void removeReference(int i);

    /**
     * Gets the "count" attribute
     */
    long getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCount();

    /**
     * True if has "count" attribute
     */
    boolean isSetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(long count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count);

    /**
     * Unsets the "count" attribute
     */
    void unsetCount();
}
