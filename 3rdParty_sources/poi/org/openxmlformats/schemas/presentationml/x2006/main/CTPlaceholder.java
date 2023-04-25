/*
 * XML Type:  CT_Placeholder
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Placeholder(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPlaceholder extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctplaceholder9efctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "orient" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum getOrient();

    /**
     * Gets (as xml) the "orient" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STDirection xgetOrient();

    /**
     * True if has "orient" attribute
     */
    boolean isSetOrient();

    /**
     * Sets the "orient" attribute
     */
    void setOrient(org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum orient);

    /**
     * Sets (as xml) the "orient" attribute
     */
    void xsetOrient(org.openxmlformats.schemas.presentationml.x2006.main.STDirection orient);

    /**
     * Unsets the "orient" attribute
     */
    void unsetOrient();

    /**
     * Gets the "sz" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize.Enum getSz();

    /**
     * Gets (as xml) the "sz" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize xgetSz();

    /**
     * True if has "sz" attribute
     */
    boolean isSetSz();

    /**
     * Sets the "sz" attribute
     */
    void setSz(org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize.Enum sz);

    /**
     * Sets (as xml) the "sz" attribute
     */
    void xsetSz(org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize sz);

    /**
     * Unsets the "sz" attribute
     */
    void unsetSz();

    /**
     * Gets the "idx" attribute
     */
    long getIdx();

    /**
     * Gets (as xml) the "idx" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetIdx();

    /**
     * True if has "idx" attribute
     */
    boolean isSetIdx();

    /**
     * Sets the "idx" attribute
     */
    void setIdx(long idx);

    /**
     * Sets (as xml) the "idx" attribute
     */
    void xsetIdx(org.apache.xmlbeans.XmlUnsignedInt idx);

    /**
     * Unsets the "idx" attribute
     */
    void unsetIdx();

    /**
     * Gets the "hasCustomPrompt" attribute
     */
    boolean getHasCustomPrompt();

    /**
     * Gets (as xml) the "hasCustomPrompt" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetHasCustomPrompt();

    /**
     * True if has "hasCustomPrompt" attribute
     */
    boolean isSetHasCustomPrompt();

    /**
     * Sets the "hasCustomPrompt" attribute
     */
    void setHasCustomPrompt(boolean hasCustomPrompt);

    /**
     * Sets (as xml) the "hasCustomPrompt" attribute
     */
    void xsetHasCustomPrompt(org.apache.xmlbeans.XmlBoolean hasCustomPrompt);

    /**
     * Unsets the "hasCustomPrompt" attribute
     */
    void unsetHasCustomPrompt();
}
