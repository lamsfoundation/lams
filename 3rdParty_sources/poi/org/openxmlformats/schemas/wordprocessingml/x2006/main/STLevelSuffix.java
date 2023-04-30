/*
 * XML Type:  ST_LevelSuffix
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STLevelSuffix
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_LevelSuffix(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STLevelSuffix.
 */
public interface STLevelSuffix extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STLevelSuffix> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stlevelsuffixe64dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum TAB = Enum.forString("tab");
    Enum SPACE = Enum.forString("space");
    Enum NOTHING = Enum.forString("nothing");

    int INT_TAB = Enum.INT_TAB;
    int INT_SPACE = Enum.INT_SPACE;
    int INT_NOTHING = Enum.INT_NOTHING;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STLevelSuffix.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_TAB
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

        static final int INT_TAB = 1;
        static final int INT_SPACE = 2;
        static final int INT_NOTHING = 3;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("tab", INT_TAB),
            new Enum("space", INT_SPACE),
            new Enum("nothing", INT_NOTHING),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
