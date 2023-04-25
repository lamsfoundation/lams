/*
 * XML Type:  CT_SectPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SectPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTSectPrChangeImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrackChangeImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrChange {
    private static final long serialVersionUID = 1L;

    public CTSectPrChangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sectPr"),
    };


    /**
     * Gets the "sectPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase getSectPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sectPr" element
     */
    @Override
    public boolean isSetSectPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "sectPr" element
     */
    @Override
    public void setSectPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase sectPr) {
        generatedSetterHelperImpl(sectPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sectPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase addNewSectPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPrBase)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "sectPr" element
     */
    @Override
    public void unsetSectPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}
