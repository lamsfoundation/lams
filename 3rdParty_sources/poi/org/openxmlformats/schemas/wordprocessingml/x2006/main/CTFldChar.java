/*
 * XML Type:  CT_FldChar
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FldChar(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFldChar extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfldchare83etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fldData" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText getFldData();

    /**
     * True if has "fldData" element
     */
    boolean isSetFldData();

    /**
     * Sets the "fldData" element
     */
    void setFldData(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText fldData);

    /**
     * Appends and returns a new empty "fldData" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText addNewFldData();

    /**
     * Unsets the "fldData" element
     */
    void unsetFldData();

    /**
     * Gets the "ffData" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData getFfData();

    /**
     * True if has "ffData" element
     */
    boolean isSetFfData();

    /**
     * Sets the "ffData" element
     */
    void setFfData(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData ffData);

    /**
     * Appends and returns a new empty "ffData" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFData addNewFfData();

    /**
     * Unsets the "ffData" element
     */
    void unsetFfData();

    /**
     * Gets the "numberingChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering getNumberingChange();

    /**
     * True if has "numberingChange" element
     */
    boolean isSetNumberingChange();

    /**
     * Sets the "numberingChange" element
     */
    void setNumberingChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering numberingChange);

    /**
     * Appends and returns a new empty "numberingChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering addNewNumberingChange();

    /**
     * Unsets the "numberingChange" element
     */
    void unsetNumberingChange();

    /**
     * Gets the "fldCharType" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType.Enum getFldCharType();

    /**
     * Gets (as xml) the "fldCharType" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType xgetFldCharType();

    /**
     * Sets the "fldCharType" attribute
     */
    void setFldCharType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType.Enum fldCharType);

    /**
     * Sets (as xml) the "fldCharType" attribute
     */
    void xsetFldCharType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType fldCharType);

    /**
     * Gets the "fldLock" attribute
     */
    java.lang.Object getFldLock();

    /**
     * Gets (as xml) the "fldLock" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetFldLock();

    /**
     * True if has "fldLock" attribute
     */
    boolean isSetFldLock();

    /**
     * Sets the "fldLock" attribute
     */
    void setFldLock(java.lang.Object fldLock);

    /**
     * Sets (as xml) the "fldLock" attribute
     */
    void xsetFldLock(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff fldLock);

    /**
     * Unsets the "fldLock" attribute
     */
    void unsetFldLock();

    /**
     * Gets the "dirty" attribute
     */
    java.lang.Object getDirty();

    /**
     * Gets (as xml) the "dirty" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDirty();

    /**
     * True if has "dirty" attribute
     */
    boolean isSetDirty();

    /**
     * Sets the "dirty" attribute
     */
    void setDirty(java.lang.Object dirty);

    /**
     * Sets (as xml) the "dirty" attribute
     */
    void xsetDirty(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff dirty);

    /**
     * Unsets the "dirty" attribute
     */
    void unsetDirty();
}
