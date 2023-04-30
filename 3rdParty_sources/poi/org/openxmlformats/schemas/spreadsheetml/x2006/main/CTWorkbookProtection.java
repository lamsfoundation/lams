/*
 * XML Type:  CT_WorkbookProtection
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WorkbookProtection(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTWorkbookProtection extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctworkbookprotection56bctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "workbookPassword" attribute
     */
    byte[] getWorkbookPassword();

    /**
     * Gets (as xml) the "workbookPassword" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex xgetWorkbookPassword();

    /**
     * True if has "workbookPassword" attribute
     */
    boolean isSetWorkbookPassword();

    /**
     * Sets the "workbookPassword" attribute
     */
    void setWorkbookPassword(byte[] workbookPassword);

    /**
     * Sets (as xml) the "workbookPassword" attribute
     */
    void xsetWorkbookPassword(org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex workbookPassword);

    /**
     * Unsets the "workbookPassword" attribute
     */
    void unsetWorkbookPassword();

    /**
     * Gets the "workbookPasswordCharacterSet" attribute
     */
    java.lang.String getWorkbookPasswordCharacterSet();

    /**
     * Gets (as xml) the "workbookPasswordCharacterSet" attribute
     */
    org.apache.xmlbeans.XmlString xgetWorkbookPasswordCharacterSet();

    /**
     * True if has "workbookPasswordCharacterSet" attribute
     */
    boolean isSetWorkbookPasswordCharacterSet();

    /**
     * Sets the "workbookPasswordCharacterSet" attribute
     */
    void setWorkbookPasswordCharacterSet(java.lang.String workbookPasswordCharacterSet);

    /**
     * Sets (as xml) the "workbookPasswordCharacterSet" attribute
     */
    void xsetWorkbookPasswordCharacterSet(org.apache.xmlbeans.XmlString workbookPasswordCharacterSet);

    /**
     * Unsets the "workbookPasswordCharacterSet" attribute
     */
    void unsetWorkbookPasswordCharacterSet();

    /**
     * Gets the "revisionsPassword" attribute
     */
    byte[] getRevisionsPassword();

    /**
     * Gets (as xml) the "revisionsPassword" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex xgetRevisionsPassword();

    /**
     * True if has "revisionsPassword" attribute
     */
    boolean isSetRevisionsPassword();

    /**
     * Sets the "revisionsPassword" attribute
     */
    void setRevisionsPassword(byte[] revisionsPassword);

    /**
     * Sets (as xml) the "revisionsPassword" attribute
     */
    void xsetRevisionsPassword(org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex revisionsPassword);

    /**
     * Unsets the "revisionsPassword" attribute
     */
    void unsetRevisionsPassword();

    /**
     * Gets the "revisionsPasswordCharacterSet" attribute
     */
    java.lang.String getRevisionsPasswordCharacterSet();

    /**
     * Gets (as xml) the "revisionsPasswordCharacterSet" attribute
     */
    org.apache.xmlbeans.XmlString xgetRevisionsPasswordCharacterSet();

    /**
     * True if has "revisionsPasswordCharacterSet" attribute
     */
    boolean isSetRevisionsPasswordCharacterSet();

    /**
     * Sets the "revisionsPasswordCharacterSet" attribute
     */
    void setRevisionsPasswordCharacterSet(java.lang.String revisionsPasswordCharacterSet);

    /**
     * Sets (as xml) the "revisionsPasswordCharacterSet" attribute
     */
    void xsetRevisionsPasswordCharacterSet(org.apache.xmlbeans.XmlString revisionsPasswordCharacterSet);

    /**
     * Unsets the "revisionsPasswordCharacterSet" attribute
     */
    void unsetRevisionsPasswordCharacterSet();

    /**
     * Gets the "lockStructure" attribute
     */
    boolean getLockStructure();

    /**
     * Gets (as xml) the "lockStructure" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetLockStructure();

    /**
     * True if has "lockStructure" attribute
     */
    boolean isSetLockStructure();

    /**
     * Sets the "lockStructure" attribute
     */
    void setLockStructure(boolean lockStructure);

    /**
     * Sets (as xml) the "lockStructure" attribute
     */
    void xsetLockStructure(org.apache.xmlbeans.XmlBoolean lockStructure);

    /**
     * Unsets the "lockStructure" attribute
     */
    void unsetLockStructure();

    /**
     * Gets the "lockWindows" attribute
     */
    boolean getLockWindows();

    /**
     * Gets (as xml) the "lockWindows" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetLockWindows();

    /**
     * True if has "lockWindows" attribute
     */
    boolean isSetLockWindows();

    /**
     * Sets the "lockWindows" attribute
     */
    void setLockWindows(boolean lockWindows);

    /**
     * Sets (as xml) the "lockWindows" attribute
     */
    void xsetLockWindows(org.apache.xmlbeans.XmlBoolean lockWindows);

    /**
     * Unsets the "lockWindows" attribute
     */
    void unsetLockWindows();

    /**
     * Gets the "lockRevision" attribute
     */
    boolean getLockRevision();

    /**
     * Gets (as xml) the "lockRevision" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetLockRevision();

    /**
     * True if has "lockRevision" attribute
     */
    boolean isSetLockRevision();

    /**
     * Sets the "lockRevision" attribute
     */
    void setLockRevision(boolean lockRevision);

    /**
     * Sets (as xml) the "lockRevision" attribute
     */
    void xsetLockRevision(org.apache.xmlbeans.XmlBoolean lockRevision);

    /**
     * Unsets the "lockRevision" attribute
     */
    void unsetLockRevision();

    /**
     * Gets the "revisionsAlgorithmName" attribute
     */
    java.lang.String getRevisionsAlgorithmName();

    /**
     * Gets (as xml) the "revisionsAlgorithmName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetRevisionsAlgorithmName();

    /**
     * True if has "revisionsAlgorithmName" attribute
     */
    boolean isSetRevisionsAlgorithmName();

    /**
     * Sets the "revisionsAlgorithmName" attribute
     */
    void setRevisionsAlgorithmName(java.lang.String revisionsAlgorithmName);

    /**
     * Sets (as xml) the "revisionsAlgorithmName" attribute
     */
    void xsetRevisionsAlgorithmName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring revisionsAlgorithmName);

    /**
     * Unsets the "revisionsAlgorithmName" attribute
     */
    void unsetRevisionsAlgorithmName();

    /**
     * Gets the "revisionsHashValue" attribute
     */
    byte[] getRevisionsHashValue();

    /**
     * Gets (as xml) the "revisionsHashValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetRevisionsHashValue();

    /**
     * True if has "revisionsHashValue" attribute
     */
    boolean isSetRevisionsHashValue();

    /**
     * Sets the "revisionsHashValue" attribute
     */
    void setRevisionsHashValue(byte[] revisionsHashValue);

    /**
     * Sets (as xml) the "revisionsHashValue" attribute
     */
    void xsetRevisionsHashValue(org.apache.xmlbeans.XmlBase64Binary revisionsHashValue);

    /**
     * Unsets the "revisionsHashValue" attribute
     */
    void unsetRevisionsHashValue();

    /**
     * Gets the "revisionsSaltValue" attribute
     */
    byte[] getRevisionsSaltValue();

    /**
     * Gets (as xml) the "revisionsSaltValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetRevisionsSaltValue();

    /**
     * True if has "revisionsSaltValue" attribute
     */
    boolean isSetRevisionsSaltValue();

    /**
     * Sets the "revisionsSaltValue" attribute
     */
    void setRevisionsSaltValue(byte[] revisionsSaltValue);

    /**
     * Sets (as xml) the "revisionsSaltValue" attribute
     */
    void xsetRevisionsSaltValue(org.apache.xmlbeans.XmlBase64Binary revisionsSaltValue);

    /**
     * Unsets the "revisionsSaltValue" attribute
     */
    void unsetRevisionsSaltValue();

    /**
     * Gets the "revisionsSpinCount" attribute
     */
    long getRevisionsSpinCount();

    /**
     * Gets (as xml) the "revisionsSpinCount" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetRevisionsSpinCount();

    /**
     * True if has "revisionsSpinCount" attribute
     */
    boolean isSetRevisionsSpinCount();

    /**
     * Sets the "revisionsSpinCount" attribute
     */
    void setRevisionsSpinCount(long revisionsSpinCount);

    /**
     * Sets (as xml) the "revisionsSpinCount" attribute
     */
    void xsetRevisionsSpinCount(org.apache.xmlbeans.XmlUnsignedInt revisionsSpinCount);

    /**
     * Unsets the "revisionsSpinCount" attribute
     */
    void unsetRevisionsSpinCount();

    /**
     * Gets the "workbookAlgorithmName" attribute
     */
    java.lang.String getWorkbookAlgorithmName();

    /**
     * Gets (as xml) the "workbookAlgorithmName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetWorkbookAlgorithmName();

    /**
     * True if has "workbookAlgorithmName" attribute
     */
    boolean isSetWorkbookAlgorithmName();

    /**
     * Sets the "workbookAlgorithmName" attribute
     */
    void setWorkbookAlgorithmName(java.lang.String workbookAlgorithmName);

    /**
     * Sets (as xml) the "workbookAlgorithmName" attribute
     */
    void xsetWorkbookAlgorithmName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring workbookAlgorithmName);

    /**
     * Unsets the "workbookAlgorithmName" attribute
     */
    void unsetWorkbookAlgorithmName();

    /**
     * Gets the "workbookHashValue" attribute
     */
    byte[] getWorkbookHashValue();

    /**
     * Gets (as xml) the "workbookHashValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetWorkbookHashValue();

    /**
     * True if has "workbookHashValue" attribute
     */
    boolean isSetWorkbookHashValue();

    /**
     * Sets the "workbookHashValue" attribute
     */
    void setWorkbookHashValue(byte[] workbookHashValue);

    /**
     * Sets (as xml) the "workbookHashValue" attribute
     */
    void xsetWorkbookHashValue(org.apache.xmlbeans.XmlBase64Binary workbookHashValue);

    /**
     * Unsets the "workbookHashValue" attribute
     */
    void unsetWorkbookHashValue();

    /**
     * Gets the "workbookSaltValue" attribute
     */
    byte[] getWorkbookSaltValue();

    /**
     * Gets (as xml) the "workbookSaltValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetWorkbookSaltValue();

    /**
     * True if has "workbookSaltValue" attribute
     */
    boolean isSetWorkbookSaltValue();

    /**
     * Sets the "workbookSaltValue" attribute
     */
    void setWorkbookSaltValue(byte[] workbookSaltValue);

    /**
     * Sets (as xml) the "workbookSaltValue" attribute
     */
    void xsetWorkbookSaltValue(org.apache.xmlbeans.XmlBase64Binary workbookSaltValue);

    /**
     * Unsets the "workbookSaltValue" attribute
     */
    void unsetWorkbookSaltValue();

    /**
     * Gets the "workbookSpinCount" attribute
     */
    long getWorkbookSpinCount();

    /**
     * Gets (as xml) the "workbookSpinCount" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetWorkbookSpinCount();

    /**
     * True if has "workbookSpinCount" attribute
     */
    boolean isSetWorkbookSpinCount();

    /**
     * Sets the "workbookSpinCount" attribute
     */
    void setWorkbookSpinCount(long workbookSpinCount);

    /**
     * Sets (as xml) the "workbookSpinCount" attribute
     */
    void xsetWorkbookSpinCount(org.apache.xmlbeans.XmlUnsignedInt workbookSpinCount);

    /**
     * Unsets the "workbookSpinCount" attribute
     */
    void unsetWorkbookSpinCount();
}
