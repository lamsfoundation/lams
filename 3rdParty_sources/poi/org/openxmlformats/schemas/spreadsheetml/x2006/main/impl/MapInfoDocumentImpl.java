/*
 * An XML document type.
 * Localname: MapInfo
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one MapInfo(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class MapInfoDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument {
    private static final long serialVersionUID = 1L;

    public MapInfoDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "MapInfo"),
    };


    /**
     * Gets the "MapInfo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo getMapInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "MapInfo" element
     */
    @Override
    public void setMapInfo(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo mapInfo) {
        generatedSetterHelperImpl(mapInfo, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "MapInfo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo addNewMapInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
