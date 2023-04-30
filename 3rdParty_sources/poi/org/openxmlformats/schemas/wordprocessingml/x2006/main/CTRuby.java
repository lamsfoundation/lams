/*
 * XML Type:  CT_Ruby
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Ruby(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRuby extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRuby> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctruby9dcetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rubyPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr getRubyPr();

    /**
     * Sets the "rubyPr" element
     */
    void setRubyPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr rubyPr);

    /**
     * Appends and returns a new empty "rubyPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr addNewRubyPr();

    /**
     * Gets the "rt" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent getRt();

    /**
     * Sets the "rt" element
     */
    void setRt(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent rt);

    /**
     * Appends and returns a new empty "rt" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent addNewRt();

    /**
     * Gets the "rubyBase" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent getRubyBase();

    /**
     * Sets the "rubyBase" element
     */
    void setRubyBase(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent rubyBase);

    /**
     * Appends and returns a new empty "rubyBase" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyContent addNewRubyBase();
}
