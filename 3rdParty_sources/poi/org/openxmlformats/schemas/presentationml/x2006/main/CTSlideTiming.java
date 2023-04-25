/*
 * XML Type:  CT_SlideTiming
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SlideTiming(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlideTiming extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslidetiming4214type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tnLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList getTnLst();

    /**
     * True if has "tnLst" element
     */
    boolean isSetTnLst();

    /**
     * Sets the "tnLst" element
     */
    void setTnLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList tnLst);

    /**
     * Appends and returns a new empty "tnLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList addNewTnLst();

    /**
     * Unsets the "tnLst" element
     */
    void unsetTnLst();

    /**
     * Gets the "bldLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList getBldLst();

    /**
     * True if has "bldLst" element
     */
    boolean isSetBldLst();

    /**
     * Sets the "bldLst" element
     */
    void setBldLst(org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList bldLst);

    /**
     * Appends and returns a new empty "bldLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList addNewBldLst();

    /**
     * Unsets the "bldLst" element
     */
    void unsetBldLst();

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
}
