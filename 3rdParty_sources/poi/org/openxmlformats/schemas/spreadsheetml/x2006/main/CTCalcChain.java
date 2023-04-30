/*
 * XML Type:  CT_CalcChain
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CalcChain(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCalcChain extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcalcchain5a0btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "c" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcCell> getCList();

    /**
     * Gets array of all "c" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcCell[] getCArray();

    /**
     * Gets ith "c" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcCell getCArray(int i);

    /**
     * Returns number of "c" element
     */
    int sizeOfCArray();

    /**
     * Sets array of all "c" element
     */
    void setCArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcCell[] cArray);

    /**
     * Sets ith "c" element
     */
    void setCArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcCell c);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "c" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcCell insertNewC(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "c" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcCell addNewC();

    /**
     * Removes the ith "c" element
     */
    void removeC(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
