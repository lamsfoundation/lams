/*
 * XML Type:  CT_SectPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SectPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSectPrChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsectprchangee2d3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sectPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase getSectPr();

    /**
     * True if has "sectPr" element
     */
    boolean isSetSectPr();

    /**
     * Sets the "sectPr" element
     */
    void setSectPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase sectPr);

    /**
     * Appends and returns a new empty "sectPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase addNewSectPr();

    /**
     * Unsets the "sectPr" element
     */
    void unsetSectPr();
}
