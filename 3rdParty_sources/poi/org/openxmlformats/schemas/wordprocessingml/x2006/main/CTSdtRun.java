/*
 * XML Type:  CT_SdtRun
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SdtRun(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSdtRun extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsdtrun5c60type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sdtPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr getSdtPr();

    /**
     * True if has "sdtPr" element
     */
    boolean isSetSdtPr();

    /**
     * Sets the "sdtPr" element
     */
    void setSdtPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr sdtPr);

    /**
     * Appends and returns a new empty "sdtPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr addNewSdtPr();

    /**
     * Unsets the "sdtPr" element
     */
    void unsetSdtPr();

    /**
     * Gets the "sdtEndPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr getSdtEndPr();

    /**
     * True if has "sdtEndPr" element
     */
    boolean isSetSdtEndPr();

    /**
     * Sets the "sdtEndPr" element
     */
    void setSdtEndPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr sdtEndPr);

    /**
     * Appends and returns a new empty "sdtEndPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr addNewSdtEndPr();

    /**
     * Unsets the "sdtEndPr" element
     */
    void unsetSdtEndPr();

    /**
     * Gets the "sdtContent" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentRun getSdtContent();

    /**
     * True if has "sdtContent" element
     */
    boolean isSetSdtContent();

    /**
     * Sets the "sdtContent" element
     */
    void setSdtContent(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentRun sdtContent);

    /**
     * Appends and returns a new empty "sdtContent" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentRun addNewSdtContent();

    /**
     * Unsets the "sdtContent" element
     */
    void unsetSdtContent();
}
