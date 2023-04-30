/*
 * XML Type:  ST_StyleSort
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleSort
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_StyleSort(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleSort.
 */
public interface STStyleSort extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleSort> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ststylesortb6d3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NAME = Enum.forString("name");
    Enum PRIORITY = Enum.forString("priority");
    Enum DEFAULT = Enum.forString("default");
    Enum FONT = Enum.forString("font");
    Enum BASED_ON = Enum.forString("basedOn");
    Enum TYPE = Enum.forString("type");
    Enum X_0000 = Enum.forString("0000");
    Enum X_0001 = Enum.forString("0001");
    Enum X_0002 = Enum.forString("0002");
    Enum X_0003 = Enum.forString("0003");
    Enum X_0004 = Enum.forString("0004");
    Enum X_0005 = Enum.forString("0005");

    int INT_NAME = Enum.INT_NAME;
    int INT_PRIORITY = Enum.INT_PRIORITY;
    int INT_DEFAULT = Enum.INT_DEFAULT;
    int INT_FONT = Enum.INT_FONT;
    int INT_BASED_ON = Enum.INT_BASED_ON;
    int INT_TYPE = Enum.INT_TYPE;
    int INT_X_0000 = Enum.INT_X_0000;
    int INT_X_0001 = Enum.INT_X_0001;
    int INT_X_0002 = Enum.INT_X_0002;
    int INT_X_0003 = Enum.INT_X_0003;
    int INT_X_0004 = Enum.INT_X_0004;
    int INT_X_0005 = Enum.INT_X_0005;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleSort.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NAME
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

        static final int INT_NAME = 1;
        static final int INT_PRIORITY = 2;
        static final int INT_DEFAULT = 3;
        static final int INT_FONT = 4;
        static final int INT_BASED_ON = 5;
        static final int INT_TYPE = 6;
        static final int INT_X_0000 = 7;
        static final int INT_X_0001 = 8;
        static final int INT_X_0002 = 9;
        static final int INT_X_0003 = 10;
        static final int INT_X_0004 = 11;
        static final int INT_X_0005 = 12;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("name", INT_NAME),
            new Enum("priority", INT_PRIORITY),
            new Enum("default", INT_DEFAULT),
            new Enum("font", INT_FONT),
            new Enum("basedOn", INT_BASED_ON),
            new Enum("type", INT_TYPE),
            new Enum("0000", INT_X_0000),
            new Enum("0001", INT_X_0001),
            new Enum("0002", INT_X_0002),
            new Enum("0003", INT_X_0003),
            new Enum("0004", INT_X_0004),
            new Enum("0005", INT_X_0005),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
