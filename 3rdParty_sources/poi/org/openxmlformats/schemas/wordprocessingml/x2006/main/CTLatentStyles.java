/*
 * XML Type:  CT_LatentStyles
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LatentStyles(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLatentStyles extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLatentStyles> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlatentstyles2e3atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "lsdException" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException> getLsdExceptionList();

    /**
     * Gets array of all "lsdException" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException[] getLsdExceptionArray();

    /**
     * Gets ith "lsdException" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException getLsdExceptionArray(int i);

    /**
     * Returns number of "lsdException" element
     */
    int sizeOfLsdExceptionArray();

    /**
     * Sets array of all "lsdException" element
     */
    void setLsdExceptionArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException[] lsdExceptionArray);

    /**
     * Sets ith "lsdException" element
     */
    void setLsdExceptionArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException lsdException);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lsdException" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException insertNewLsdException(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "lsdException" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException addNewLsdException();

    /**
     * Removes the ith "lsdException" element
     */
    void removeLsdException(int i);

    /**
     * Gets the "defLockedState" attribute
     */
    java.lang.Object getDefLockedState();

    /**
     * Gets (as xml) the "defLockedState" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDefLockedState();

    /**
     * True if has "defLockedState" attribute
     */
    boolean isSetDefLockedState();

    /**
     * Sets the "defLockedState" attribute
     */
    void setDefLockedState(java.lang.Object defLockedState);

    /**
     * Sets (as xml) the "defLockedState" attribute
     */
    void xsetDefLockedState(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff defLockedState);

    /**
     * Unsets the "defLockedState" attribute
     */
    void unsetDefLockedState();

    /**
     * Gets the "defUIPriority" attribute
     */
    java.math.BigInteger getDefUIPriority();

    /**
     * Gets (as xml) the "defUIPriority" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetDefUIPriority();

    /**
     * True if has "defUIPriority" attribute
     */
    boolean isSetDefUIPriority();

    /**
     * Sets the "defUIPriority" attribute
     */
    void setDefUIPriority(java.math.BigInteger defUIPriority);

    /**
     * Sets (as xml) the "defUIPriority" attribute
     */
    void xsetDefUIPriority(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber defUIPriority);

    /**
     * Unsets the "defUIPriority" attribute
     */
    void unsetDefUIPriority();

    /**
     * Gets the "defSemiHidden" attribute
     */
    java.lang.Object getDefSemiHidden();

    /**
     * Gets (as xml) the "defSemiHidden" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDefSemiHidden();

    /**
     * True if has "defSemiHidden" attribute
     */
    boolean isSetDefSemiHidden();

    /**
     * Sets the "defSemiHidden" attribute
     */
    void setDefSemiHidden(java.lang.Object defSemiHidden);

    /**
     * Sets (as xml) the "defSemiHidden" attribute
     */
    void xsetDefSemiHidden(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff defSemiHidden);

    /**
     * Unsets the "defSemiHidden" attribute
     */
    void unsetDefSemiHidden();

    /**
     * Gets the "defUnhideWhenUsed" attribute
     */
    java.lang.Object getDefUnhideWhenUsed();

    /**
     * Gets (as xml) the "defUnhideWhenUsed" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDefUnhideWhenUsed();

    /**
     * True if has "defUnhideWhenUsed" attribute
     */
    boolean isSetDefUnhideWhenUsed();

    /**
     * Sets the "defUnhideWhenUsed" attribute
     */
    void setDefUnhideWhenUsed(java.lang.Object defUnhideWhenUsed);

    /**
     * Sets (as xml) the "defUnhideWhenUsed" attribute
     */
    void xsetDefUnhideWhenUsed(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff defUnhideWhenUsed);

    /**
     * Unsets the "defUnhideWhenUsed" attribute
     */
    void unsetDefUnhideWhenUsed();

    /**
     * Gets the "defQFormat" attribute
     */
    java.lang.Object getDefQFormat();

    /**
     * Gets (as xml) the "defQFormat" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDefQFormat();

    /**
     * True if has "defQFormat" attribute
     */
    boolean isSetDefQFormat();

    /**
     * Sets the "defQFormat" attribute
     */
    void setDefQFormat(java.lang.Object defQFormat);

    /**
     * Sets (as xml) the "defQFormat" attribute
     */
    void xsetDefQFormat(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff defQFormat);

    /**
     * Unsets the "defQFormat" attribute
     */
    void unsetDefQFormat();

    /**
     * Gets the "count" attribute
     */
    java.math.BigInteger getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetCount();

    /**
     * True if has "count" attribute
     */
    boolean isSetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(java.math.BigInteger count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber count);

    /**
     * Unsets the "count" attribute
     */
    void unsetCount();
}
