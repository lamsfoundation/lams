/*
 * An XML document type.
 * Localname: videoFile
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.VideoFileDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one videoFile(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public class VideoFileDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.VideoFileDocument {
    private static final long serialVersionUID = 1L;

    public VideoFileDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "videoFile"),
    };


    /**
     * Gets the "videoFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile getVideoFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "videoFile" element
     */
    @Override
    public void setVideoFile(org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile videoFile) {
        generatedSetterHelperImpl(videoFile, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "videoFile" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile addNewVideoFile() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
