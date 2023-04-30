/*
 * XML Type:  CT_IconFilter
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_IconFilter(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTIconFilter extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconFilter> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cticonfilterb79atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "iconSet" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum getIconSet();

    /**
     * Gets (as xml) the "iconSet" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType xgetIconSet();

    /**
     * Sets the "iconSet" attribute
     */
    void setIconSet(org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum iconSet);

    /**
     * Sets (as xml) the "iconSet" attribute
     */
    void xsetIconSet(org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType iconSet);

    /**
     * Gets the "iconId" attribute
     */
    long getIconId();

    /**
     * Gets (as xml) the "iconId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetIconId();

    /**
     * True if has "iconId" attribute
     */
    boolean isSetIconId();

    /**
     * Sets the "iconId" attribute
     */
    void setIconId(long iconId);

    /**
     * Sets (as xml) the "iconId" attribute
     */
    void xsetIconId(org.apache.xmlbeans.XmlUnsignedInt iconId);

    /**
     * Unsets the "iconId" attribute
     */
    void unsetIconId();
}
