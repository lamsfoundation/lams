/*
 * XML Type:  ST_TextRotation
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TextRotation(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation$Member
 *     org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation$Member2
 */
public interface STTextRotation extends org.apache.xmlbeans.XmlAnySimpleType {
    java.lang.Object getObjectValue();
    void setObjectValue(java.lang.Object val);
    org.apache.xmlbeans.SchemaType instanceType();
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttextrotationec64type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * An anonymous inner XML type.
     *
     * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation$Member.
     */
    public interface Member extends org.apache.xmlbeans.XmlNonNegativeInteger {
        int getIntValue();
        void setIntValue(int i);
        ElementFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation.Member> Factory = new ElementFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "anon9d89type");
        org.apache.xmlbeans.SchemaType type = Factory.getType();

    }

    /**
     * An anonymous inner XML type.
     *
     * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation$Member2.
     */
    public interface Member2 extends org.apache.xmlbeans.XmlNonNegativeInteger {
        ElementFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STTextRotation.Member2> Factory = new ElementFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "anon1c0atype");
        org.apache.xmlbeans.SchemaType type = Factory.getType();

    }
}
