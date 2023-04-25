/*
 * XML Type:  CT_PTab
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PTab(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPTab extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctptaba283type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "alignment" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabAlignment.Enum getAlignment();

    /**
     * Gets (as xml) the "alignment" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabAlignment xgetAlignment();

    /**
     * Sets the "alignment" attribute
     */
    void setAlignment(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabAlignment.Enum alignment);

    /**
     * Sets (as xml) the "alignment" attribute
     */
    void xsetAlignment(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabAlignment alignment);

    /**
     * Gets the "relativeTo" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabRelativeTo.Enum getRelativeTo();

    /**
     * Gets (as xml) the "relativeTo" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabRelativeTo xgetRelativeTo();

    /**
     * Sets the "relativeTo" attribute
     */
    void setRelativeTo(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabRelativeTo.Enum relativeTo);

    /**
     * Sets (as xml) the "relativeTo" attribute
     */
    void xsetRelativeTo(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabRelativeTo relativeTo);

    /**
     * Gets the "leader" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabLeader.Enum getLeader();

    /**
     * Gets (as xml) the "leader" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabLeader xgetLeader();

    /**
     * Sets the "leader" attribute
     */
    void setLeader(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabLeader.Enum leader);

    /**
     * Sets (as xml) the "leader" attribute
     */
    void xsetLeader(org.openxmlformats.schemas.wordprocessingml.x2006.main.STPTabLeader leader);
}
