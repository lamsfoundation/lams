/*
 * XML Type:  CT_FileVersion
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileVersion
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FileVersion(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFileVersion extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileVersion> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfileversion559btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "appName" attribute
     */
    java.lang.String getAppName();

    /**
     * Gets (as xml) the "appName" attribute
     */
    org.apache.xmlbeans.XmlString xgetAppName();

    /**
     * True if has "appName" attribute
     */
    boolean isSetAppName();

    /**
     * Sets the "appName" attribute
     */
    void setAppName(java.lang.String appName);

    /**
     * Sets (as xml) the "appName" attribute
     */
    void xsetAppName(org.apache.xmlbeans.XmlString appName);

    /**
     * Unsets the "appName" attribute
     */
    void unsetAppName();

    /**
     * Gets the "lastEdited" attribute
     */
    java.lang.String getLastEdited();

    /**
     * Gets (as xml) the "lastEdited" attribute
     */
    org.apache.xmlbeans.XmlString xgetLastEdited();

    /**
     * True if has "lastEdited" attribute
     */
    boolean isSetLastEdited();

    /**
     * Sets the "lastEdited" attribute
     */
    void setLastEdited(java.lang.String lastEdited);

    /**
     * Sets (as xml) the "lastEdited" attribute
     */
    void xsetLastEdited(org.apache.xmlbeans.XmlString lastEdited);

    /**
     * Unsets the "lastEdited" attribute
     */
    void unsetLastEdited();

    /**
     * Gets the "lowestEdited" attribute
     */
    java.lang.String getLowestEdited();

    /**
     * Gets (as xml) the "lowestEdited" attribute
     */
    org.apache.xmlbeans.XmlString xgetLowestEdited();

    /**
     * True if has "lowestEdited" attribute
     */
    boolean isSetLowestEdited();

    /**
     * Sets the "lowestEdited" attribute
     */
    void setLowestEdited(java.lang.String lowestEdited);

    /**
     * Sets (as xml) the "lowestEdited" attribute
     */
    void xsetLowestEdited(org.apache.xmlbeans.XmlString lowestEdited);

    /**
     * Unsets the "lowestEdited" attribute
     */
    void unsetLowestEdited();

    /**
     * Gets the "rupBuild" attribute
     */
    java.lang.String getRupBuild();

    /**
     * Gets (as xml) the "rupBuild" attribute
     */
    org.apache.xmlbeans.XmlString xgetRupBuild();

    /**
     * True if has "rupBuild" attribute
     */
    boolean isSetRupBuild();

    /**
     * Sets the "rupBuild" attribute
     */
    void setRupBuild(java.lang.String rupBuild);

    /**
     * Sets (as xml) the "rupBuild" attribute
     */
    void xsetRupBuild(org.apache.xmlbeans.XmlString rupBuild);

    /**
     * Unsets the "rupBuild" attribute
     */
    void unsetRupBuild();

    /**
     * Gets the "codeName" attribute
     */
    java.lang.String getCodeName();

    /**
     * Gets (as xml) the "codeName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetCodeName();

    /**
     * True if has "codeName" attribute
     */
    boolean isSetCodeName();

    /**
     * Sets the "codeName" attribute
     */
    void setCodeName(java.lang.String codeName);

    /**
     * Sets (as xml) the "codeName" attribute
     */
    void xsetCodeName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid codeName);

    /**
     * Unsets the "codeName" attribute
     */
    void unsetCodeName();
}
