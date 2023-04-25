/*
 * XML Type:  CT_IgnoredErrors
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_IgnoredErrors(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTIgnoredErrors extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctignorederrorsbebctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "ignoredError" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError> getIgnoredErrorList();

    /**
     * Gets array of all "ignoredError" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError[] getIgnoredErrorArray();

    /**
     * Gets ith "ignoredError" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError getIgnoredErrorArray(int i);

    /**
     * Returns number of "ignoredError" element
     */
    int sizeOfIgnoredErrorArray();

    /**
     * Sets array of all "ignoredError" element
     */
    void setIgnoredErrorArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError[] ignoredErrorArray);

    /**
     * Sets ith "ignoredError" element
     */
    void setIgnoredErrorArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError ignoredError);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ignoredError" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError insertNewIgnoredError(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ignoredError" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError addNewIgnoredError();

    /**
     * Removes the ith "ignoredError" element
     */
    void removeIgnoredError(int i);

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
