/*
 * XML Type:  ST_ViewType
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.STViewType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ViewType(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.presentationml.x2006.main.STViewType.
 */
public interface STViewType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.presentationml.x2006.main.STViewType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stviewtype8eb6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum SLD_VIEW = Enum.forString("sldView");
    Enum SLD_MASTER_VIEW = Enum.forString("sldMasterView");
    Enum NOTES_VIEW = Enum.forString("notesView");
    Enum HANDOUT_VIEW = Enum.forString("handoutView");
    Enum NOTES_MASTER_VIEW = Enum.forString("notesMasterView");
    Enum OUTLINE_VIEW = Enum.forString("outlineView");
    Enum SLD_SORTER_VIEW = Enum.forString("sldSorterView");
    Enum SLD_THUMBNAIL_VIEW = Enum.forString("sldThumbnailView");

    int INT_SLD_VIEW = Enum.INT_SLD_VIEW;
    int INT_SLD_MASTER_VIEW = Enum.INT_SLD_MASTER_VIEW;
    int INT_NOTES_VIEW = Enum.INT_NOTES_VIEW;
    int INT_HANDOUT_VIEW = Enum.INT_HANDOUT_VIEW;
    int INT_NOTES_MASTER_VIEW = Enum.INT_NOTES_MASTER_VIEW;
    int INT_OUTLINE_VIEW = Enum.INT_OUTLINE_VIEW;
    int INT_SLD_SORTER_VIEW = Enum.INT_SLD_SORTER_VIEW;
    int INT_SLD_THUMBNAIL_VIEW = Enum.INT_SLD_THUMBNAIL_VIEW;

    /**
     * Enumeration value class for org.openxmlformats.schemas.presentationml.x2006.main.STViewType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_SLD_VIEW
     * Enum.forString(s); // returns the enum value for a string
     * Enum.forInt(i); // returns the enum value for an int
     * </pre>
     * Enumeration objects are immutable singleton objects that
     * can be compared using == object equality. They have no
     * public constructor. See the constants defined within this
     * class for all the valid values.
     */
    final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase {
        /**
         * Returns the enum value for a string, or null if none.
         */
        public static Enum forString(java.lang.String s) {
            return (Enum)table.forString(s);
        }

        /**
         * Returns the enum value corresponding to an int, or null if none.
         */
        public static Enum forInt(int i) {
            return (Enum)table.forInt(i);
        }

        private Enum(java.lang.String s, int i) {
            super(s, i);
        }

        static final int INT_SLD_VIEW = 1;
        static final int INT_SLD_MASTER_VIEW = 2;
        static final int INT_NOTES_VIEW = 3;
        static final int INT_HANDOUT_VIEW = 4;
        static final int INT_NOTES_MASTER_VIEW = 5;
        static final int INT_OUTLINE_VIEW = 6;
        static final int INT_SLD_SORTER_VIEW = 7;
        static final int INT_SLD_THUMBNAIL_VIEW = 8;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("sldView", INT_SLD_VIEW),
            new Enum("sldMasterView", INT_SLD_MASTER_VIEW),
            new Enum("notesView", INT_NOTES_VIEW),
            new Enum("handoutView", INT_HANDOUT_VIEW),
            new Enum("notesMasterView", INT_NOTES_MASTER_VIEW),
            new Enum("outlineView", INT_OUTLINE_VIEW),
            new Enum("sldSorterView", INT_SLD_SORTER_VIEW),
            new Enum("sldThumbnailView", INT_SLD_THUMBNAIL_VIEW),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
