/*
 * XML Type:  CT_TrackChangeRange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeRange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TrackChangeRange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTrackChangeRange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeRange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttrackchangerange0b06type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "displacedByCustomXml" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDisplacedByCustomXml.Enum getDisplacedByCustomXml();

    /**
     * Gets (as xml) the "displacedByCustomXml" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDisplacedByCustomXml xgetDisplacedByCustomXml();

    /**
     * True if has "displacedByCustomXml" attribute
     */
    boolean isSetDisplacedByCustomXml();

    /**
     * Sets the "displacedByCustomXml" attribute
     */
    void setDisplacedByCustomXml(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDisplacedByCustomXml.Enum displacedByCustomXml);

    /**
     * Sets (as xml) the "displacedByCustomXml" attribute
     */
    void xsetDisplacedByCustomXml(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDisplacedByCustomXml displacedByCustomXml);

    /**
     * Unsets the "displacedByCustomXml" attribute
     */
    void unsetDisplacedByCustomXml();
}
