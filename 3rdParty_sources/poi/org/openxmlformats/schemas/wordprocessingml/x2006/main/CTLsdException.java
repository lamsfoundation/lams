/*
 * XML Type:  CT_LsdException
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LsdException(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLsdException extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLsdException> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlsdexceptiona296type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString name);

    /**
     * Gets the "locked" attribute
     */
    java.lang.Object getLocked();

    /**
     * Gets (as xml) the "locked" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetLocked();

    /**
     * True if has "locked" attribute
     */
    boolean isSetLocked();

    /**
     * Sets the "locked" attribute
     */
    void setLocked(java.lang.Object locked);

    /**
     * Sets (as xml) the "locked" attribute
     */
    void xsetLocked(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff locked);

    /**
     * Unsets the "locked" attribute
     */
    void unsetLocked();

    /**
     * Gets the "uiPriority" attribute
     */
    java.math.BigInteger getUiPriority();

    /**
     * Gets (as xml) the "uiPriority" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetUiPriority();

    /**
     * True if has "uiPriority" attribute
     */
    boolean isSetUiPriority();

    /**
     * Sets the "uiPriority" attribute
     */
    void setUiPriority(java.math.BigInteger uiPriority);

    /**
     * Sets (as xml) the "uiPriority" attribute
     */
    void xsetUiPriority(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber uiPriority);

    /**
     * Unsets the "uiPriority" attribute
     */
    void unsetUiPriority();

    /**
     * Gets the "semiHidden" attribute
     */
    java.lang.Object getSemiHidden();

    /**
     * Gets (as xml) the "semiHidden" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetSemiHidden();

    /**
     * True if has "semiHidden" attribute
     */
    boolean isSetSemiHidden();

    /**
     * Sets the "semiHidden" attribute
     */
    void setSemiHidden(java.lang.Object semiHidden);

    /**
     * Sets (as xml) the "semiHidden" attribute
     */
    void xsetSemiHidden(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff semiHidden);

    /**
     * Unsets the "semiHidden" attribute
     */
    void unsetSemiHidden();

    /**
     * Gets the "unhideWhenUsed" attribute
     */
    java.lang.Object getUnhideWhenUsed();

    /**
     * Gets (as xml) the "unhideWhenUsed" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetUnhideWhenUsed();

    /**
     * True if has "unhideWhenUsed" attribute
     */
    boolean isSetUnhideWhenUsed();

    /**
     * Sets the "unhideWhenUsed" attribute
     */
    void setUnhideWhenUsed(java.lang.Object unhideWhenUsed);

    /**
     * Sets (as xml) the "unhideWhenUsed" attribute
     */
    void xsetUnhideWhenUsed(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff unhideWhenUsed);

    /**
     * Unsets the "unhideWhenUsed" attribute
     */
    void unsetUnhideWhenUsed();

    /**
     * Gets the "qFormat" attribute
     */
    java.lang.Object getQFormat();

    /**
     * Gets (as xml) the "qFormat" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetQFormat();

    /**
     * True if has "qFormat" attribute
     */
    boolean isSetQFormat();

    /**
     * Sets the "qFormat" attribute
     */
    void setQFormat(java.lang.Object qFormat);

    /**
     * Sets (as xml) the "qFormat" attribute
     */
    void xsetQFormat(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff qFormat);

    /**
     * Unsets the "qFormat" attribute
     */
    void unsetQFormat();
}
