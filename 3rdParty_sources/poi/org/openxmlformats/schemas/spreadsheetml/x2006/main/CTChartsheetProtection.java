/*
 * XML Type:  CT_ChartsheetProtection
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetProtection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ChartsheetProtection(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTChartsheetProtection extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetProtection> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctchartsheetprotection3cc3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "password" attribute
     */
    byte[] getPassword();

    /**
     * Gets (as xml) the "password" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex xgetPassword();

    /**
     * True if has "password" attribute
     */
    boolean isSetPassword();

    /**
     * Sets the "password" attribute
     */
    void setPassword(byte[] password);

    /**
     * Sets (as xml) the "password" attribute
     */
    void xsetPassword(org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex password);

    /**
     * Unsets the "password" attribute
     */
    void unsetPassword();

    /**
     * Gets the "algorithmName" attribute
     */
    java.lang.String getAlgorithmName();

    /**
     * Gets (as xml) the "algorithmName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAlgorithmName();

    /**
     * True if has "algorithmName" attribute
     */
    boolean isSetAlgorithmName();

    /**
     * Sets the "algorithmName" attribute
     */
    void setAlgorithmName(java.lang.String algorithmName);

    /**
     * Sets (as xml) the "algorithmName" attribute
     */
    void xsetAlgorithmName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring algorithmName);

    /**
     * Unsets the "algorithmName" attribute
     */
    void unsetAlgorithmName();

    /**
     * Gets the "hashValue" attribute
     */
    byte[] getHashValue();

    /**
     * Gets (as xml) the "hashValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetHashValue();

    /**
     * True if has "hashValue" attribute
     */
    boolean isSetHashValue();

    /**
     * Sets the "hashValue" attribute
     */
    void setHashValue(byte[] hashValue);

    /**
     * Sets (as xml) the "hashValue" attribute
     */
    void xsetHashValue(org.apache.xmlbeans.XmlBase64Binary hashValue);

    /**
     * Unsets the "hashValue" attribute
     */
    void unsetHashValue();

    /**
     * Gets the "saltValue" attribute
     */
    byte[] getSaltValue();

    /**
     * Gets (as xml) the "saltValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetSaltValue();

    /**
     * True if has "saltValue" attribute
     */
    boolean isSetSaltValue();

    /**
     * Sets the "saltValue" attribute
     */
    void setSaltValue(byte[] saltValue);

    /**
     * Sets (as xml) the "saltValue" attribute
     */
    void xsetSaltValue(org.apache.xmlbeans.XmlBase64Binary saltValue);

    /**
     * Unsets the "saltValue" attribute
     */
    void unsetSaltValue();

    /**
     * Gets the "spinCount" attribute
     */
    long getSpinCount();

    /**
     * Gets (as xml) the "spinCount" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetSpinCount();

    /**
     * True if has "spinCount" attribute
     */
    boolean isSetSpinCount();

    /**
     * Sets the "spinCount" attribute
     */
    void setSpinCount(long spinCount);

    /**
     * Sets (as xml) the "spinCount" attribute
     */
    void xsetSpinCount(org.apache.xmlbeans.XmlUnsignedInt spinCount);

    /**
     * Unsets the "spinCount" attribute
     */
    void unsetSpinCount();

    /**
     * Gets the "content" attribute
     */
    boolean getContent();

    /**
     * Gets (as xml) the "content" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetContent();

    /**
     * True if has "content" attribute
     */
    boolean isSetContent();

    /**
     * Sets the "content" attribute
     */
    void setContent(boolean content);

    /**
     * Sets (as xml) the "content" attribute
     */
    void xsetContent(org.apache.xmlbeans.XmlBoolean content);

    /**
     * Unsets the "content" attribute
     */
    void unsetContent();

    /**
     * Gets the "objects" attribute
     */
    boolean getObjects();

    /**
     * Gets (as xml) the "objects" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetObjects();

    /**
     * True if has "objects" attribute
     */
    boolean isSetObjects();

    /**
     * Sets the "objects" attribute
     */
    void setObjects(boolean objects);

    /**
     * Sets (as xml) the "objects" attribute
     */
    void xsetObjects(org.apache.xmlbeans.XmlBoolean objects);

    /**
     * Unsets the "objects" attribute
     */
    void unsetObjects();
}
