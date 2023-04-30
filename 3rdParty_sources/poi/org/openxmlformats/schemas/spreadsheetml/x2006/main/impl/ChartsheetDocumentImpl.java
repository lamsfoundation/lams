/*
 * An XML document type.
 * Localname: chartsheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.ChartsheetDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one chartsheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class ChartsheetDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.ChartsheetDocument {
    private static final long serialVersionUID = 1L;

    public ChartsheetDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "chartsheet"),
    };


    /**
     * Gets the "chartsheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet getChartsheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "chartsheet" element
     */
    @Override
    public void setChartsheet(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet chartsheet) {
        generatedSetterHelperImpl(chartsheet, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "chartsheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet addNewChartsheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
