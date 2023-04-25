/*
 * XML Type:  ST_TextScaleDecimal
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextScaleDecimal
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TextScaleDecimal(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextScaleDecimal.
 */
public interface STTextScaleDecimal extends org.apache.xmlbeans.XmlInteger {
    int getIntValue();
    void setIntValue(int i);
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextScaleDecimal> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttextscaledecimaldee4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();

}
