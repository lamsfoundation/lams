/*
 * XML Type:  CT_MdxKPI
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MdxKPI(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMdxKPI extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmdxkpiec6ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "n" attribute
     */
    long getN();

    /**
     * Gets (as xml) the "n" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetN();

    /**
     * Sets the "n" attribute
     */
    void setN(long n);

    /**
     * Sets (as xml) the "n" attribute
     */
    void xsetN(org.apache.xmlbeans.XmlUnsignedInt n);

    /**
     * Gets the "np" attribute
     */
    long getNp();

    /**
     * Gets (as xml) the "np" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetNp();

    /**
     * Sets the "np" attribute
     */
    void setNp(long np);

    /**
     * Sets (as xml) the "np" attribute
     */
    void xsetNp(org.apache.xmlbeans.XmlUnsignedInt np);

    /**
     * Gets the "p" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty.Enum getP();

    /**
     * Gets (as xml) the "p" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty xgetP();

    /**
     * Sets the "p" attribute
     */
    void setP(org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty.Enum p);

    /**
     * Sets (as xml) the "p" attribute
     */
    void xsetP(org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty p);
}
