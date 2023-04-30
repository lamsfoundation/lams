/*
 * XML Type:  ST_View
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STView
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_View(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STView.
 */
public interface STView extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STView> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stviewc2d3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NONE = Enum.forString("none");
    Enum PRINT = Enum.forString("print");
    Enum OUTLINE = Enum.forString("outline");
    Enum MASTER_PAGES = Enum.forString("masterPages");
    Enum NORMAL = Enum.forString("normal");
    Enum WEB = Enum.forString("web");

    int INT_NONE = Enum.INT_NONE;
    int INT_PRINT = Enum.INT_PRINT;
    int INT_OUTLINE = Enum.INT_OUTLINE;
    int INT_MASTER_PAGES = Enum.INT_MASTER_PAGES;
    int INT_NORMAL = Enum.INT_NORMAL;
    int INT_WEB = Enum.INT_WEB;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STView.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NONE
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

        static final int INT_NONE = 1;
        static final int INT_PRINT = 2;
        static final int INT_OUTLINE = 3;
        static final int INT_MASTER_PAGES = 4;
        static final int INT_NORMAL = 5;
        static final int INT_WEB = 6;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("none", INT_NONE),
            new Enum("print", INT_PRINT),
            new Enum("outline", INT_OUTLINE),
            new Enum("masterPages", INT_MASTER_PAGES),
            new Enum("normal", INT_NORMAL),
            new Enum("web", INT_WEB),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
