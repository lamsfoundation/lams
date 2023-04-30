/*
 * XML Type:  CT_FileRecoveryPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FileRecoveryPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFileRecoveryPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfilerecoveryprf05ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "autoRecover" attribute
     */
    boolean getAutoRecover();

    /**
     * Gets (as xml) the "autoRecover" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoRecover();

    /**
     * True if has "autoRecover" attribute
     */
    boolean isSetAutoRecover();

    /**
     * Sets the "autoRecover" attribute
     */
    void setAutoRecover(boolean autoRecover);

    /**
     * Sets (as xml) the "autoRecover" attribute
     */
    void xsetAutoRecover(org.apache.xmlbeans.XmlBoolean autoRecover);

    /**
     * Unsets the "autoRecover" attribute
     */
    void unsetAutoRecover();

    /**
     * Gets the "crashSave" attribute
     */
    boolean getCrashSave();

    /**
     * Gets (as xml) the "crashSave" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetCrashSave();

    /**
     * True if has "crashSave" attribute
     */
    boolean isSetCrashSave();

    /**
     * Sets the "crashSave" attribute
     */
    void setCrashSave(boolean crashSave);

    /**
     * Sets (as xml) the "crashSave" attribute
     */
    void xsetCrashSave(org.apache.xmlbeans.XmlBoolean crashSave);

    /**
     * Unsets the "crashSave" attribute
     */
    void unsetCrashSave();

    /**
     * Gets the "dataExtractLoad" attribute
     */
    boolean getDataExtractLoad();

    /**
     * Gets (as xml) the "dataExtractLoad" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDataExtractLoad();

    /**
     * True if has "dataExtractLoad" attribute
     */
    boolean isSetDataExtractLoad();

    /**
     * Sets the "dataExtractLoad" attribute
     */
    void setDataExtractLoad(boolean dataExtractLoad);

    /**
     * Sets (as xml) the "dataExtractLoad" attribute
     */
    void xsetDataExtractLoad(org.apache.xmlbeans.XmlBoolean dataExtractLoad);

    /**
     * Unsets the "dataExtractLoad" attribute
     */
    void unsetDataExtractLoad();

    /**
     * Gets the "repairLoad" attribute
     */
    boolean getRepairLoad();

    /**
     * Gets (as xml) the "repairLoad" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetRepairLoad();

    /**
     * True if has "repairLoad" attribute
     */
    boolean isSetRepairLoad();

    /**
     * Sets the "repairLoad" attribute
     */
    void setRepairLoad(boolean repairLoad);

    /**
     * Sets (as xml) the "repairLoad" attribute
     */
    void xsetRepairLoad(org.apache.xmlbeans.XmlBoolean repairLoad);

    /**
     * Unsets the "repairLoad" attribute
     */
    void unsetRepairLoad();
}
