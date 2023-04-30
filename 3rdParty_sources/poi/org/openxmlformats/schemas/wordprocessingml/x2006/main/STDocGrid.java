/*
 * XML Type:  ST_DocGrid
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocGrid
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_DocGrid(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocGrid.
 */
public interface STDocGrid extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocGrid> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stdocgrid1cc4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum DEFAULT = Enum.forString("default");
    Enum LINES = Enum.forString("lines");
    Enum LINES_AND_CHARS = Enum.forString("linesAndChars");
    Enum SNAP_TO_CHARS = Enum.forString("snapToChars");

    int INT_DEFAULT = Enum.INT_DEFAULT;
    int INT_LINES = Enum.INT_LINES;
    int INT_LINES_AND_CHARS = Enum.INT_LINES_AND_CHARS;
    int INT_SNAP_TO_CHARS = Enum.INT_SNAP_TO_CHARS;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocGrid.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_DEFAULT
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

        static final int INT_DEFAULT = 1;
        static final int INT_LINES = 2;
        static final int INT_LINES_AND_CHARS = 3;
        static final int INT_SNAP_TO_CHARS = 4;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("default", INT_DEFAULT),
            new Enum("lines", INT_LINES),
            new Enum("linesAndChars", INT_LINES_AND_CHARS),
            new Enum("snapToChars", INT_SNAP_TO_CHARS),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
