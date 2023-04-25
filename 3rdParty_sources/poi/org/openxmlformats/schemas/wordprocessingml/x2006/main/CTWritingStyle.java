/*
 * XML Type:  CT_WritingStyle
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WritingStyle(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTWritingStyle extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwritingstyled853type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "lang" attribute
     */
    java.lang.String getLang();

    /**
     * Gets (as xml) the "lang" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang xgetLang();

    /**
     * Sets the "lang" attribute
     */
    void setLang(java.lang.String lang);

    /**
     * Sets (as xml) the "lang" attribute
     */
    void xsetLang(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang lang);

    /**
     * Gets the "vendorID" attribute
     */
    java.lang.String getVendorID();

    /**
     * Gets (as xml) the "vendorID" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetVendorID();

    /**
     * Sets the "vendorID" attribute
     */
    void setVendorID(java.lang.String vendorID);

    /**
     * Sets (as xml) the "vendorID" attribute
     */
    void xsetVendorID(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString vendorID);

    /**
     * Gets the "dllVersion" attribute
     */
    java.lang.String getDllVersion();

    /**
     * Gets (as xml) the "dllVersion" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetDllVersion();

    /**
     * Sets the "dllVersion" attribute
     */
    void setDllVersion(java.lang.String dllVersion);

    /**
     * Sets (as xml) the "dllVersion" attribute
     */
    void xsetDllVersion(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString dllVersion);

    /**
     * Gets the "nlCheck" attribute
     */
    java.lang.Object getNlCheck();

    /**
     * Gets (as xml) the "nlCheck" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetNlCheck();

    /**
     * True if has "nlCheck" attribute
     */
    boolean isSetNlCheck();

    /**
     * Sets the "nlCheck" attribute
     */
    void setNlCheck(java.lang.Object nlCheck);

    /**
     * Sets (as xml) the "nlCheck" attribute
     */
    void xsetNlCheck(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff nlCheck);

    /**
     * Unsets the "nlCheck" attribute
     */
    void unsetNlCheck();

    /**
     * Gets the "checkStyle" attribute
     */
    java.lang.Object getCheckStyle();

    /**
     * Gets (as xml) the "checkStyle" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetCheckStyle();

    /**
     * Sets the "checkStyle" attribute
     */
    void setCheckStyle(java.lang.Object checkStyle);

    /**
     * Sets (as xml) the "checkStyle" attribute
     */
    void xsetCheckStyle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff checkStyle);

    /**
     * Gets the "appName" attribute
     */
    java.lang.String getAppName();

    /**
     * Gets (as xml) the "appName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetAppName();

    /**
     * Sets the "appName" attribute
     */
    void setAppName(java.lang.String appName);

    /**
     * Sets (as xml) the "appName" attribute
     */
    void xsetAppName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString appName);
}
