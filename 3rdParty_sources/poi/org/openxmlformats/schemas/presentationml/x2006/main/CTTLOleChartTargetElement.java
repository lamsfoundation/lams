/*
 * XML Type:  CT_TLOleChartTargetElement
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleChartTargetElement
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLOleChartTargetElement(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLOleChartTargetElement extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleChartTargetElement> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlolecharttargetelement04f2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLChartSubelementType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLChartSubelementType xgetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.presentationml.x2006.main.STTLChartSubelementType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.presentationml.x2006.main.STTLChartSubelementType type);

    /**
     * Gets the "lvl" attribute
     */
    long getLvl();

    /**
     * Gets (as xml) the "lvl" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetLvl();

    /**
     * True if has "lvl" attribute
     */
    boolean isSetLvl();

    /**
     * Sets the "lvl" attribute
     */
    void setLvl(long lvl);

    /**
     * Sets (as xml) the "lvl" attribute
     */
    void xsetLvl(org.apache.xmlbeans.XmlUnsignedInt lvl);

    /**
     * Unsets the "lvl" attribute
     */
    void unsetLvl();
}
