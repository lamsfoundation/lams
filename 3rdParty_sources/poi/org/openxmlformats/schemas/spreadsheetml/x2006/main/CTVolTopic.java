/*
 * XML Type:  CT_VolTopic
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_VolTopic(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTVolTopic extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopic> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctvoltopic23e5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "v" element
     */
    java.lang.String getV();

    /**
     * Gets (as xml) the "v" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetV();

    /**
     * Sets the "v" element
     */
    void setV(java.lang.String v);

    /**
     * Sets (as xml) the "v" element
     */
    void xsetV(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring v);

    /**
     * Gets a List of "stp" elements
     */
    java.util.List<java.lang.String> getStpList();

    /**
     * Gets array of all "stp" elements
     */
    java.lang.String[] getStpArray();

    /**
     * Gets ith "stp" element
     */
    java.lang.String getStpArray(int i);

    /**
     * Gets (as xml) a List of "stp" elements
     */
    java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring> xgetStpList();

    /**
     * Gets (as xml) array of all "stp" elements
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[] xgetStpArray();

    /**
     * Gets (as xml) ith "stp" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetStpArray(int i);

    /**
     * Returns number of "stp" element
     */
    int sizeOfStpArray();

    /**
     * Sets array of all "stp" element
     */
    void setStpArray(java.lang.String[] stpArray);

    /**
     * Sets ith "stp" element
     */
    void setStpArray(int i, java.lang.String stp);

    /**
     * Sets (as xml) array of all "stp" element
     */
    void xsetStpArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[] stpArray);

    /**
     * Sets (as xml) ith "stp" element
     */
    void xsetStpArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring stp);

    /**
     * Inserts the value as the ith "stp" element
     */
    void insertStp(int i, java.lang.String stp);

    /**
     * Appends the value as the last "stp" element
     */
    void addStp(java.lang.String stp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "stp" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring insertNewStp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "stp" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring addNewStp();

    /**
     * Removes the ith "stp" element
     */
    void removeStp(int i);

    /**
     * Gets a List of "tr" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef> getTrList();

    /**
     * Gets array of all "tr" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef[] getTrArray();

    /**
     * Gets ith "tr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef getTrArray(int i);

    /**
     * Returns number of "tr" element
     */
    int sizeOfTrArray();

    /**
     * Sets array of all "tr" element
     */
    void setTrArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef[] trArray);

    /**
     * Sets ith "tr" element
     */
    void setTrArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef tr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef insertNewTr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "tr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTVolTopicRef addNewTr();

    /**
     * Removes the ith "tr" element
     */
    void removeTr(int i);

    /**
     * Gets the "t" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType.Enum getT();

    /**
     * Gets (as xml) the "t" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType xgetT();

    /**
     * True if has "t" attribute
     */
    boolean isSetT();

    /**
     * Sets the "t" attribute
     */
    void setT(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType.Enum t);

    /**
     * Sets (as xml) the "t" attribute
     */
    void xsetT(org.openxmlformats.schemas.spreadsheetml.x2006.main.STVolValueType t);

    /**
     * Unsets the "t" attribute
     */
    void unsetT();
}
