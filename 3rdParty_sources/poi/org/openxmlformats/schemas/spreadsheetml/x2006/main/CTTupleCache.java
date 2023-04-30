/*
 * XML Type:  CT_TupleCache
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TupleCache(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTupleCache extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttuplecache39a3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "entries" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries getEntries();

    /**
     * True if has "entries" element
     */
    boolean isSetEntries();

    /**
     * Sets the "entries" element
     */
    void setEntries(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries entries);

    /**
     * Appends and returns a new empty "entries" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries addNewEntries();

    /**
     * Unsets the "entries" element
     */
    void unsetEntries();

    /**
     * Gets the "sets" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets getSets();

    /**
     * True if has "sets" element
     */
    boolean isSetSets();

    /**
     * Sets the "sets" element
     */
    void setSets(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets sets);

    /**
     * Appends and returns a new empty "sets" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets addNewSets();

    /**
     * Unsets the "sets" element
     */
    void unsetSets();

    /**
     * Gets the "queryCache" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache getQueryCache();

    /**
     * True if has "queryCache" element
     */
    boolean isSetQueryCache();

    /**
     * Sets the "queryCache" element
     */
    void setQueryCache(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache queryCache);

    /**
     * Appends and returns a new empty "queryCache" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache addNewQueryCache();

    /**
     * Unsets the "queryCache" element
     */
    void unsetQueryCache();

    /**
     * Gets the "serverFormats" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats getServerFormats();

    /**
     * True if has "serverFormats" element
     */
    boolean isSetServerFormats();

    /**
     * Sets the "serverFormats" element
     */
    void setServerFormats(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats serverFormats);

    /**
     * Appends and returns a new empty "serverFormats" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats addNewServerFormats();

    /**
     * Unsets the "serverFormats" element
     */
    void unsetServerFormats();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
