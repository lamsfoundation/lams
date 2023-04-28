/*
 * XML Type:  CT_TLSetBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLSetBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLSetBehavior extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlsetbehaviord633type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cBhvr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData getCBhvr();

    /**
     * Sets the "cBhvr" element
     */
    void setCBhvr(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData cBhvr);

    /**
     * Appends and returns a new empty "cBhvr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData addNewCBhvr();

    /**
     * Gets the "to" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant getTo();

    /**
     * True if has "to" element
     */
    boolean isSetTo();

    /**
     * Sets the "to" element
     */
    void setTo(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant to);

    /**
     * Appends and returns a new empty "to" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant addNewTo();

    /**
     * Unsets the "to" element
     */
    void unsetTo();
}
