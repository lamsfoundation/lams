/*
 * XML Type:  CT_TableRow
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TableRow(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTableRow extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTableRow> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttablerow4ac7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "tc" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell> getTcList();

    /**
     * Gets array of all "tc" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell[] getTcArray();

    /**
     * Gets ith "tc" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell getTcArray(int i);

    /**
     * Returns number of "tc" element
     */
    int sizeOfTcArray();

    /**
     * Sets array of all "tc" element
     */
    void setTcArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell[] tcArray);

    /**
     * Sets ith "tc" element
     */
    void setTcArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell tc);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tc" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell insertNewTc(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "tc" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCell addNewTc();

    /**
     * Removes the ith "tc" element
     */
    void removeTc(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "h" attribute
     */
    java.lang.Object getH();

    /**
     * Gets (as xml) the "h" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetH();

    /**
     * Sets the "h" attribute
     */
    void setH(java.lang.Object h);

    /**
     * Sets (as xml) the "h" attribute
     */
    void xsetH(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate h);
}
