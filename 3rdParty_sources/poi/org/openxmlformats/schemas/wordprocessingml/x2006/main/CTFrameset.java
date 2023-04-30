/*
 * XML Type:  CT_Frameset
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Frameset(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFrameset extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctframeset1033type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sz" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getSz();

    /**
     * True if has "sz" element
     */
    boolean isSetSz();

    /**
     * Sets the "sz" element
     */
    void setSz(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString sz);

    /**
     * Appends and returns a new empty "sz" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewSz();

    /**
     * Unsets the "sz" element
     */
    void unsetSz();

    /**
     * Gets the "framesetSplitbar" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar getFramesetSplitbar();

    /**
     * True if has "framesetSplitbar" element
     */
    boolean isSetFramesetSplitbar();

    /**
     * Sets the "framesetSplitbar" element
     */
    void setFramesetSplitbar(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar framesetSplitbar);

    /**
     * Appends and returns a new empty "framesetSplitbar" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramesetSplitbar addNewFramesetSplitbar();

    /**
     * Unsets the "framesetSplitbar" element
     */
    void unsetFramesetSplitbar();

    /**
     * Gets the "frameLayout" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout getFrameLayout();

    /**
     * True if has "frameLayout" element
     */
    boolean isSetFrameLayout();

    /**
     * Sets the "frameLayout" element
     */
    void setFrameLayout(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout frameLayout);

    /**
     * Appends and returns a new empty "frameLayout" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameLayout addNewFrameLayout();

    /**
     * Unsets the "frameLayout" element
     */
    void unsetFrameLayout();

    /**
     * Gets the "title" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getTitle();

    /**
     * True if has "title" element
     */
    boolean isSetTitle();

    /**
     * Sets the "title" element
     */
    void setTitle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString title);

    /**
     * Appends and returns a new empty "title" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewTitle();

    /**
     * Unsets the "title" element
     */
    void unsetTitle();

    /**
     * Gets a List of "frameset" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset> getFramesetList();

    /**
     * Gets array of all "frameset" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset[] getFramesetArray();

    /**
     * Gets ith "frameset" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset getFramesetArray(int i);

    /**
     * Returns number of "frameset" element
     */
    int sizeOfFramesetArray();

    /**
     * Sets array of all "frameset" element
     */
    void setFramesetArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset[] framesetArray);

    /**
     * Sets ith "frameset" element
     */
    void setFramesetArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset frameset);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "frameset" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset insertNewFrameset(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "frameset" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrameset addNewFrameset();

    /**
     * Removes the ith "frameset" element
     */
    void removeFrameset(int i);

    /**
     * Gets a List of "frame" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame> getFrameList();

    /**
     * Gets array of all "frame" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame[] getFrameArray();

    /**
     * Gets ith "frame" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame getFrameArray(int i);

    /**
     * Returns number of "frame" element
     */
    int sizeOfFrameArray();

    /**
     * Sets array of all "frame" element
     */
    void setFrameArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame[] frameArray);

    /**
     * Sets ith "frame" element
     */
    void setFrameArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame frame);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "frame" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame insertNewFrame(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "frame" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFrame addNewFrame();

    /**
     * Removes the ith "frame" element
     */
    void removeFrame(int i);
}
