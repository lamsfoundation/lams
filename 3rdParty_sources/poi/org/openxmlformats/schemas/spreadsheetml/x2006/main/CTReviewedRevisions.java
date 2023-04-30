/*
 * XML Type:  CT_ReviewedRevisions
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewedRevisions
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ReviewedRevisions(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTReviewedRevisions extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewedRevisions> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctreviewedrevisionsed20type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "reviewed" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewed> getReviewedList();

    /**
     * Gets array of all "reviewed" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewed[] getReviewedArray();

    /**
     * Gets ith "reviewed" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewed getReviewedArray(int i);

    /**
     * Returns number of "reviewed" element
     */
    int sizeOfReviewedArray();

    /**
     * Sets array of all "reviewed" element
     */
    void setReviewedArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewed[] reviewedArray);

    /**
     * Sets ith "reviewed" element
     */
    void setReviewedArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewed reviewed);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "reviewed" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewed insertNewReviewed(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "reviewed" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTReviewed addNewReviewed();

    /**
     * Removes the ith "reviewed" element
     */
    void removeReviewed(int i);

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
