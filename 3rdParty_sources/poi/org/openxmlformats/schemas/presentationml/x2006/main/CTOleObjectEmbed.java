/*
 * XML Type:  CT_OleObjectEmbed
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTOleObjectEmbed
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OleObjectEmbed(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOleObjectEmbed extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTOleObjectEmbed> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoleobjectembedcc93type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "followColorScheme" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STOleObjectFollowColorScheme.Enum getFollowColorScheme();

    /**
     * Gets (as xml) the "followColorScheme" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STOleObjectFollowColorScheme xgetFollowColorScheme();

    /**
     * True if has "followColorScheme" attribute
     */
    boolean isSetFollowColorScheme();

    /**
     * Sets the "followColorScheme" attribute
     */
    void setFollowColorScheme(org.openxmlformats.schemas.presentationml.x2006.main.STOleObjectFollowColorScheme.Enum followColorScheme);

    /**
     * Sets (as xml) the "followColorScheme" attribute
     */
    void xsetFollowColorScheme(org.openxmlformats.schemas.presentationml.x2006.main.STOleObjectFollowColorScheme followColorScheme);

    /**
     * Unsets the "followColorScheme" attribute
     */
    void unsetFollowColorScheme();
}
