/*
 * XML Type:  ST_ShapeType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STShapeType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ShapeType(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STShapeType.
 */
public interface STShapeType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STShapeType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stshapetype069ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum LINE = Enum.forString("line");
    Enum LINE_INV = Enum.forString("lineInv");
    Enum TRIANGLE = Enum.forString("triangle");
    Enum RT_TRIANGLE = Enum.forString("rtTriangle");
    Enum RECT = Enum.forString("rect");
    Enum DIAMOND = Enum.forString("diamond");
    Enum PARALLELOGRAM = Enum.forString("parallelogram");
    Enum TRAPEZOID = Enum.forString("trapezoid");
    Enum NON_ISOSCELES_TRAPEZOID = Enum.forString("nonIsoscelesTrapezoid");
    Enum PENTAGON = Enum.forString("pentagon");
    Enum HEXAGON = Enum.forString("hexagon");
    Enum HEPTAGON = Enum.forString("heptagon");
    Enum OCTAGON = Enum.forString("octagon");
    Enum DECAGON = Enum.forString("decagon");
    Enum DODECAGON = Enum.forString("dodecagon");
    Enum STAR_4 = Enum.forString("star4");
    Enum STAR_5 = Enum.forString("star5");
    Enum STAR_6 = Enum.forString("star6");
    Enum STAR_7 = Enum.forString("star7");
    Enum STAR_8 = Enum.forString("star8");
    Enum STAR_10 = Enum.forString("star10");
    Enum STAR_12 = Enum.forString("star12");
    Enum STAR_16 = Enum.forString("star16");
    Enum STAR_24 = Enum.forString("star24");
    Enum STAR_32 = Enum.forString("star32");
    Enum ROUND_RECT = Enum.forString("roundRect");
    Enum ROUND_1_RECT = Enum.forString("round1Rect");
    Enum ROUND_2_SAME_RECT = Enum.forString("round2SameRect");
    Enum ROUND_2_DIAG_RECT = Enum.forString("round2DiagRect");
    Enum SNIP_ROUND_RECT = Enum.forString("snipRoundRect");
    Enum SNIP_1_RECT = Enum.forString("snip1Rect");
    Enum SNIP_2_SAME_RECT = Enum.forString("snip2SameRect");
    Enum SNIP_2_DIAG_RECT = Enum.forString("snip2DiagRect");
    Enum PLAQUE = Enum.forString("plaque");
    Enum ELLIPSE = Enum.forString("ellipse");
    Enum TEARDROP = Enum.forString("teardrop");
    Enum HOME_PLATE = Enum.forString("homePlate");
    Enum CHEVRON = Enum.forString("chevron");
    Enum PIE_WEDGE = Enum.forString("pieWedge");
    Enum PIE = Enum.forString("pie");
    Enum BLOCK_ARC = Enum.forString("blockArc");
    Enum DONUT = Enum.forString("donut");
    Enum NO_SMOKING = Enum.forString("noSmoking");
    Enum RIGHT_ARROW = Enum.forString("rightArrow");
    Enum LEFT_ARROW = Enum.forString("leftArrow");
    Enum UP_ARROW = Enum.forString("upArrow");
    Enum DOWN_ARROW = Enum.forString("downArrow");
    Enum STRIPED_RIGHT_ARROW = Enum.forString("stripedRightArrow");
    Enum NOTCHED_RIGHT_ARROW = Enum.forString("notchedRightArrow");
    Enum BENT_UP_ARROW = Enum.forString("bentUpArrow");
    Enum LEFT_RIGHT_ARROW = Enum.forString("leftRightArrow");
    Enum UP_DOWN_ARROW = Enum.forString("upDownArrow");
    Enum LEFT_UP_ARROW = Enum.forString("leftUpArrow");
    Enum LEFT_RIGHT_UP_ARROW = Enum.forString("leftRightUpArrow");
    Enum QUAD_ARROW = Enum.forString("quadArrow");
    Enum LEFT_ARROW_CALLOUT = Enum.forString("leftArrowCallout");
    Enum RIGHT_ARROW_CALLOUT = Enum.forString("rightArrowCallout");
    Enum UP_ARROW_CALLOUT = Enum.forString("upArrowCallout");
    Enum DOWN_ARROW_CALLOUT = Enum.forString("downArrowCallout");
    Enum LEFT_RIGHT_ARROW_CALLOUT = Enum.forString("leftRightArrowCallout");
    Enum UP_DOWN_ARROW_CALLOUT = Enum.forString("upDownArrowCallout");
    Enum QUAD_ARROW_CALLOUT = Enum.forString("quadArrowCallout");
    Enum BENT_ARROW = Enum.forString("bentArrow");
    Enum UTURN_ARROW = Enum.forString("uturnArrow");
    Enum CIRCULAR_ARROW = Enum.forString("circularArrow");
    Enum LEFT_CIRCULAR_ARROW = Enum.forString("leftCircularArrow");
    Enum LEFT_RIGHT_CIRCULAR_ARROW = Enum.forString("leftRightCircularArrow");
    Enum CURVED_RIGHT_ARROW = Enum.forString("curvedRightArrow");
    Enum CURVED_LEFT_ARROW = Enum.forString("curvedLeftArrow");
    Enum CURVED_UP_ARROW = Enum.forString("curvedUpArrow");
    Enum CURVED_DOWN_ARROW = Enum.forString("curvedDownArrow");
    Enum SWOOSH_ARROW = Enum.forString("swooshArrow");
    Enum CUBE = Enum.forString("cube");
    Enum CAN = Enum.forString("can");
    Enum LIGHTNING_BOLT = Enum.forString("lightningBolt");
    Enum HEART = Enum.forString("heart");
    Enum SUN = Enum.forString("sun");
    Enum MOON = Enum.forString("moon");
    Enum SMILEY_FACE = Enum.forString("smileyFace");
    Enum IRREGULAR_SEAL_1 = Enum.forString("irregularSeal1");
    Enum IRREGULAR_SEAL_2 = Enum.forString("irregularSeal2");
    Enum FOLDED_CORNER = Enum.forString("foldedCorner");
    Enum BEVEL = Enum.forString("bevel");
    Enum FRAME = Enum.forString("frame");
    Enum HALF_FRAME = Enum.forString("halfFrame");
    Enum CORNER = Enum.forString("corner");
    Enum DIAG_STRIPE = Enum.forString("diagStripe");
    Enum CHORD = Enum.forString("chord");
    Enum ARC = Enum.forString("arc");
    Enum LEFT_BRACKET = Enum.forString("leftBracket");
    Enum RIGHT_BRACKET = Enum.forString("rightBracket");
    Enum LEFT_BRACE = Enum.forString("leftBrace");
    Enum RIGHT_BRACE = Enum.forString("rightBrace");
    Enum BRACKET_PAIR = Enum.forString("bracketPair");
    Enum BRACE_PAIR = Enum.forString("bracePair");
    Enum STRAIGHT_CONNECTOR_1 = Enum.forString("straightConnector1");
    Enum BENT_CONNECTOR_2 = Enum.forString("bentConnector2");
    Enum BENT_CONNECTOR_3 = Enum.forString("bentConnector3");
    Enum BENT_CONNECTOR_4 = Enum.forString("bentConnector4");
    Enum BENT_CONNECTOR_5 = Enum.forString("bentConnector5");
    Enum CURVED_CONNECTOR_2 = Enum.forString("curvedConnector2");
    Enum CURVED_CONNECTOR_3 = Enum.forString("curvedConnector3");
    Enum CURVED_CONNECTOR_4 = Enum.forString("curvedConnector4");
    Enum CURVED_CONNECTOR_5 = Enum.forString("curvedConnector5");
    Enum CALLOUT_1 = Enum.forString("callout1");
    Enum CALLOUT_2 = Enum.forString("callout2");
    Enum CALLOUT_3 = Enum.forString("callout3");
    Enum ACCENT_CALLOUT_1 = Enum.forString("accentCallout1");
    Enum ACCENT_CALLOUT_2 = Enum.forString("accentCallout2");
    Enum ACCENT_CALLOUT_3 = Enum.forString("accentCallout3");
    Enum BORDER_CALLOUT_1 = Enum.forString("borderCallout1");
    Enum BORDER_CALLOUT_2 = Enum.forString("borderCallout2");
    Enum BORDER_CALLOUT_3 = Enum.forString("borderCallout3");
    Enum ACCENT_BORDER_CALLOUT_1 = Enum.forString("accentBorderCallout1");
    Enum ACCENT_BORDER_CALLOUT_2 = Enum.forString("accentBorderCallout2");
    Enum ACCENT_BORDER_CALLOUT_3 = Enum.forString("accentBorderCallout3");
    Enum WEDGE_RECT_CALLOUT = Enum.forString("wedgeRectCallout");
    Enum WEDGE_ROUND_RECT_CALLOUT = Enum.forString("wedgeRoundRectCallout");
    Enum WEDGE_ELLIPSE_CALLOUT = Enum.forString("wedgeEllipseCallout");
    Enum CLOUD_CALLOUT = Enum.forString("cloudCallout");
    Enum CLOUD = Enum.forString("cloud");
    Enum RIBBON = Enum.forString("ribbon");
    Enum RIBBON_2 = Enum.forString("ribbon2");
    Enum ELLIPSE_RIBBON = Enum.forString("ellipseRibbon");
    Enum ELLIPSE_RIBBON_2 = Enum.forString("ellipseRibbon2");
    Enum LEFT_RIGHT_RIBBON = Enum.forString("leftRightRibbon");
    Enum VERTICAL_SCROLL = Enum.forString("verticalScroll");
    Enum HORIZONTAL_SCROLL = Enum.forString("horizontalScroll");
    Enum WAVE = Enum.forString("wave");
    Enum DOUBLE_WAVE = Enum.forString("doubleWave");
    Enum PLUS = Enum.forString("plus");
    Enum FLOW_CHART_PROCESS = Enum.forString("flowChartProcess");
    Enum FLOW_CHART_DECISION = Enum.forString("flowChartDecision");
    Enum FLOW_CHART_INPUT_OUTPUT = Enum.forString("flowChartInputOutput");
    Enum FLOW_CHART_PREDEFINED_PROCESS = Enum.forString("flowChartPredefinedProcess");
    Enum FLOW_CHART_INTERNAL_STORAGE = Enum.forString("flowChartInternalStorage");
    Enum FLOW_CHART_DOCUMENT = Enum.forString("flowChartDocument");
    Enum FLOW_CHART_MULTIDOCUMENT = Enum.forString("flowChartMultidocument");
    Enum FLOW_CHART_TERMINATOR = Enum.forString("flowChartTerminator");
    Enum FLOW_CHART_PREPARATION = Enum.forString("flowChartPreparation");
    Enum FLOW_CHART_MANUAL_INPUT = Enum.forString("flowChartManualInput");
    Enum FLOW_CHART_MANUAL_OPERATION = Enum.forString("flowChartManualOperation");
    Enum FLOW_CHART_CONNECTOR = Enum.forString("flowChartConnector");
    Enum FLOW_CHART_PUNCHED_CARD = Enum.forString("flowChartPunchedCard");
    Enum FLOW_CHART_PUNCHED_TAPE = Enum.forString("flowChartPunchedTape");
    Enum FLOW_CHART_SUMMING_JUNCTION = Enum.forString("flowChartSummingJunction");
    Enum FLOW_CHART_OR = Enum.forString("flowChartOr");
    Enum FLOW_CHART_COLLATE = Enum.forString("flowChartCollate");
    Enum FLOW_CHART_SORT = Enum.forString("flowChartSort");
    Enum FLOW_CHART_EXTRACT = Enum.forString("flowChartExtract");
    Enum FLOW_CHART_MERGE = Enum.forString("flowChartMerge");
    Enum FLOW_CHART_OFFLINE_STORAGE = Enum.forString("flowChartOfflineStorage");
    Enum FLOW_CHART_ONLINE_STORAGE = Enum.forString("flowChartOnlineStorage");
    Enum FLOW_CHART_MAGNETIC_TAPE = Enum.forString("flowChartMagneticTape");
    Enum FLOW_CHART_MAGNETIC_DISK = Enum.forString("flowChartMagneticDisk");
    Enum FLOW_CHART_MAGNETIC_DRUM = Enum.forString("flowChartMagneticDrum");
    Enum FLOW_CHART_DISPLAY = Enum.forString("flowChartDisplay");
    Enum FLOW_CHART_DELAY = Enum.forString("flowChartDelay");
    Enum FLOW_CHART_ALTERNATE_PROCESS = Enum.forString("flowChartAlternateProcess");
    Enum FLOW_CHART_OFFPAGE_CONNECTOR = Enum.forString("flowChartOffpageConnector");
    Enum ACTION_BUTTON_BLANK = Enum.forString("actionButtonBlank");
    Enum ACTION_BUTTON_HOME = Enum.forString("actionButtonHome");
    Enum ACTION_BUTTON_HELP = Enum.forString("actionButtonHelp");
    Enum ACTION_BUTTON_INFORMATION = Enum.forString("actionButtonInformation");
    Enum ACTION_BUTTON_FORWARD_NEXT = Enum.forString("actionButtonForwardNext");
    Enum ACTION_BUTTON_BACK_PREVIOUS = Enum.forString("actionButtonBackPrevious");
    Enum ACTION_BUTTON_END = Enum.forString("actionButtonEnd");
    Enum ACTION_BUTTON_BEGINNING = Enum.forString("actionButtonBeginning");
    Enum ACTION_BUTTON_RETURN = Enum.forString("actionButtonReturn");
    Enum ACTION_BUTTON_DOCUMENT = Enum.forString("actionButtonDocument");
    Enum ACTION_BUTTON_SOUND = Enum.forString("actionButtonSound");
    Enum ACTION_BUTTON_MOVIE = Enum.forString("actionButtonMovie");
    Enum GEAR_6 = Enum.forString("gear6");
    Enum GEAR_9 = Enum.forString("gear9");
    Enum FUNNEL = Enum.forString("funnel");
    Enum MATH_PLUS = Enum.forString("mathPlus");
    Enum MATH_MINUS = Enum.forString("mathMinus");
    Enum MATH_MULTIPLY = Enum.forString("mathMultiply");
    Enum MATH_DIVIDE = Enum.forString("mathDivide");
    Enum MATH_EQUAL = Enum.forString("mathEqual");
    Enum MATH_NOT_EQUAL = Enum.forString("mathNotEqual");
    Enum CORNER_TABS = Enum.forString("cornerTabs");
    Enum SQUARE_TABS = Enum.forString("squareTabs");
    Enum PLAQUE_TABS = Enum.forString("plaqueTabs");
    Enum CHART_X = Enum.forString("chartX");
    Enum CHART_STAR = Enum.forString("chartStar");
    Enum CHART_PLUS = Enum.forString("chartPlus");

    int INT_LINE = Enum.INT_LINE;
    int INT_LINE_INV = Enum.INT_LINE_INV;
    int INT_TRIANGLE = Enum.INT_TRIANGLE;
    int INT_RT_TRIANGLE = Enum.INT_RT_TRIANGLE;
    int INT_RECT = Enum.INT_RECT;
    int INT_DIAMOND = Enum.INT_DIAMOND;
    int INT_PARALLELOGRAM = Enum.INT_PARALLELOGRAM;
    int INT_TRAPEZOID = Enum.INT_TRAPEZOID;
    int INT_NON_ISOSCELES_TRAPEZOID = Enum.INT_NON_ISOSCELES_TRAPEZOID;
    int INT_PENTAGON = Enum.INT_PENTAGON;
    int INT_HEXAGON = Enum.INT_HEXAGON;
    int INT_HEPTAGON = Enum.INT_HEPTAGON;
    int INT_OCTAGON = Enum.INT_OCTAGON;
    int INT_DECAGON = Enum.INT_DECAGON;
    int INT_DODECAGON = Enum.INT_DODECAGON;
    int INT_STAR_4 = Enum.INT_STAR_4;
    int INT_STAR_5 = Enum.INT_STAR_5;
    int INT_STAR_6 = Enum.INT_STAR_6;
    int INT_STAR_7 = Enum.INT_STAR_7;
    int INT_STAR_8 = Enum.INT_STAR_8;
    int INT_STAR_10 = Enum.INT_STAR_10;
    int INT_STAR_12 = Enum.INT_STAR_12;
    int INT_STAR_16 = Enum.INT_STAR_16;
    int INT_STAR_24 = Enum.INT_STAR_24;
    int INT_STAR_32 = Enum.INT_STAR_32;
    int INT_ROUND_RECT = Enum.INT_ROUND_RECT;
    int INT_ROUND_1_RECT = Enum.INT_ROUND_1_RECT;
    int INT_ROUND_2_SAME_RECT = Enum.INT_ROUND_2_SAME_RECT;
    int INT_ROUND_2_DIAG_RECT = Enum.INT_ROUND_2_DIAG_RECT;
    int INT_SNIP_ROUND_RECT = Enum.INT_SNIP_ROUND_RECT;
    int INT_SNIP_1_RECT = Enum.INT_SNIP_1_RECT;
    int INT_SNIP_2_SAME_RECT = Enum.INT_SNIP_2_SAME_RECT;
    int INT_SNIP_2_DIAG_RECT = Enum.INT_SNIP_2_DIAG_RECT;
    int INT_PLAQUE = Enum.INT_PLAQUE;
    int INT_ELLIPSE = Enum.INT_ELLIPSE;
    int INT_TEARDROP = Enum.INT_TEARDROP;
    int INT_HOME_PLATE = Enum.INT_HOME_PLATE;
    int INT_CHEVRON = Enum.INT_CHEVRON;
    int INT_PIE_WEDGE = Enum.INT_PIE_WEDGE;
    int INT_PIE = Enum.INT_PIE;
    int INT_BLOCK_ARC = Enum.INT_BLOCK_ARC;
    int INT_DONUT = Enum.INT_DONUT;
    int INT_NO_SMOKING = Enum.INT_NO_SMOKING;
    int INT_RIGHT_ARROW = Enum.INT_RIGHT_ARROW;
    int INT_LEFT_ARROW = Enum.INT_LEFT_ARROW;
    int INT_UP_ARROW = Enum.INT_UP_ARROW;
    int INT_DOWN_ARROW = Enum.INT_DOWN_ARROW;
    int INT_STRIPED_RIGHT_ARROW = Enum.INT_STRIPED_RIGHT_ARROW;
    int INT_NOTCHED_RIGHT_ARROW = Enum.INT_NOTCHED_RIGHT_ARROW;
    int INT_BENT_UP_ARROW = Enum.INT_BENT_UP_ARROW;
    int INT_LEFT_RIGHT_ARROW = Enum.INT_LEFT_RIGHT_ARROW;
    int INT_UP_DOWN_ARROW = Enum.INT_UP_DOWN_ARROW;
    int INT_LEFT_UP_ARROW = Enum.INT_LEFT_UP_ARROW;
    int INT_LEFT_RIGHT_UP_ARROW = Enum.INT_LEFT_RIGHT_UP_ARROW;
    int INT_QUAD_ARROW = Enum.INT_QUAD_ARROW;
    int INT_LEFT_ARROW_CALLOUT = Enum.INT_LEFT_ARROW_CALLOUT;
    int INT_RIGHT_ARROW_CALLOUT = Enum.INT_RIGHT_ARROW_CALLOUT;
    int INT_UP_ARROW_CALLOUT = Enum.INT_UP_ARROW_CALLOUT;
    int INT_DOWN_ARROW_CALLOUT = Enum.INT_DOWN_ARROW_CALLOUT;
    int INT_LEFT_RIGHT_ARROW_CALLOUT = Enum.INT_LEFT_RIGHT_ARROW_CALLOUT;
    int INT_UP_DOWN_ARROW_CALLOUT = Enum.INT_UP_DOWN_ARROW_CALLOUT;
    int INT_QUAD_ARROW_CALLOUT = Enum.INT_QUAD_ARROW_CALLOUT;
    int INT_BENT_ARROW = Enum.INT_BENT_ARROW;
    int INT_UTURN_ARROW = Enum.INT_UTURN_ARROW;
    int INT_CIRCULAR_ARROW = Enum.INT_CIRCULAR_ARROW;
    int INT_LEFT_CIRCULAR_ARROW = Enum.INT_LEFT_CIRCULAR_ARROW;
    int INT_LEFT_RIGHT_CIRCULAR_ARROW = Enum.INT_LEFT_RIGHT_CIRCULAR_ARROW;
    int INT_CURVED_RIGHT_ARROW = Enum.INT_CURVED_RIGHT_ARROW;
    int INT_CURVED_LEFT_ARROW = Enum.INT_CURVED_LEFT_ARROW;
    int INT_CURVED_UP_ARROW = Enum.INT_CURVED_UP_ARROW;
    int INT_CURVED_DOWN_ARROW = Enum.INT_CURVED_DOWN_ARROW;
    int INT_SWOOSH_ARROW = Enum.INT_SWOOSH_ARROW;
    int INT_CUBE = Enum.INT_CUBE;
    int INT_CAN = Enum.INT_CAN;
    int INT_LIGHTNING_BOLT = Enum.INT_LIGHTNING_BOLT;
    int INT_HEART = Enum.INT_HEART;
    int INT_SUN = Enum.INT_SUN;
    int INT_MOON = Enum.INT_MOON;
    int INT_SMILEY_FACE = Enum.INT_SMILEY_FACE;
    int INT_IRREGULAR_SEAL_1 = Enum.INT_IRREGULAR_SEAL_1;
    int INT_IRREGULAR_SEAL_2 = Enum.INT_IRREGULAR_SEAL_2;
    int INT_FOLDED_CORNER = Enum.INT_FOLDED_CORNER;
    int INT_BEVEL = Enum.INT_BEVEL;
    int INT_FRAME = Enum.INT_FRAME;
    int INT_HALF_FRAME = Enum.INT_HALF_FRAME;
    int INT_CORNER = Enum.INT_CORNER;
    int INT_DIAG_STRIPE = Enum.INT_DIAG_STRIPE;
    int INT_CHORD = Enum.INT_CHORD;
    int INT_ARC = Enum.INT_ARC;
    int INT_LEFT_BRACKET = Enum.INT_LEFT_BRACKET;
    int INT_RIGHT_BRACKET = Enum.INT_RIGHT_BRACKET;
    int INT_LEFT_BRACE = Enum.INT_LEFT_BRACE;
    int INT_RIGHT_BRACE = Enum.INT_RIGHT_BRACE;
    int INT_BRACKET_PAIR = Enum.INT_BRACKET_PAIR;
    int INT_BRACE_PAIR = Enum.INT_BRACE_PAIR;
    int INT_STRAIGHT_CONNECTOR_1 = Enum.INT_STRAIGHT_CONNECTOR_1;
    int INT_BENT_CONNECTOR_2 = Enum.INT_BENT_CONNECTOR_2;
    int INT_BENT_CONNECTOR_3 = Enum.INT_BENT_CONNECTOR_3;
    int INT_BENT_CONNECTOR_4 = Enum.INT_BENT_CONNECTOR_4;
    int INT_BENT_CONNECTOR_5 = Enum.INT_BENT_CONNECTOR_5;
    int INT_CURVED_CONNECTOR_2 = Enum.INT_CURVED_CONNECTOR_2;
    int INT_CURVED_CONNECTOR_3 = Enum.INT_CURVED_CONNECTOR_3;
    int INT_CURVED_CONNECTOR_4 = Enum.INT_CURVED_CONNECTOR_4;
    int INT_CURVED_CONNECTOR_5 = Enum.INT_CURVED_CONNECTOR_5;
    int INT_CALLOUT_1 = Enum.INT_CALLOUT_1;
    int INT_CALLOUT_2 = Enum.INT_CALLOUT_2;
    int INT_CALLOUT_3 = Enum.INT_CALLOUT_3;
    int INT_ACCENT_CALLOUT_1 = Enum.INT_ACCENT_CALLOUT_1;
    int INT_ACCENT_CALLOUT_2 = Enum.INT_ACCENT_CALLOUT_2;
    int INT_ACCENT_CALLOUT_3 = Enum.INT_ACCENT_CALLOUT_3;
    int INT_BORDER_CALLOUT_1 = Enum.INT_BORDER_CALLOUT_1;
    int INT_BORDER_CALLOUT_2 = Enum.INT_BORDER_CALLOUT_2;
    int INT_BORDER_CALLOUT_3 = Enum.INT_BORDER_CALLOUT_3;
    int INT_ACCENT_BORDER_CALLOUT_1 = Enum.INT_ACCENT_BORDER_CALLOUT_1;
    int INT_ACCENT_BORDER_CALLOUT_2 = Enum.INT_ACCENT_BORDER_CALLOUT_2;
    int INT_ACCENT_BORDER_CALLOUT_3 = Enum.INT_ACCENT_BORDER_CALLOUT_3;
    int INT_WEDGE_RECT_CALLOUT = Enum.INT_WEDGE_RECT_CALLOUT;
    int INT_WEDGE_ROUND_RECT_CALLOUT = Enum.INT_WEDGE_ROUND_RECT_CALLOUT;
    int INT_WEDGE_ELLIPSE_CALLOUT = Enum.INT_WEDGE_ELLIPSE_CALLOUT;
    int INT_CLOUD_CALLOUT = Enum.INT_CLOUD_CALLOUT;
    int INT_CLOUD = Enum.INT_CLOUD;
    int INT_RIBBON = Enum.INT_RIBBON;
    int INT_RIBBON_2 = Enum.INT_RIBBON_2;
    int INT_ELLIPSE_RIBBON = Enum.INT_ELLIPSE_RIBBON;
    int INT_ELLIPSE_RIBBON_2 = Enum.INT_ELLIPSE_RIBBON_2;
    int INT_LEFT_RIGHT_RIBBON = Enum.INT_LEFT_RIGHT_RIBBON;
    int INT_VERTICAL_SCROLL = Enum.INT_VERTICAL_SCROLL;
    int INT_HORIZONTAL_SCROLL = Enum.INT_HORIZONTAL_SCROLL;
    int INT_WAVE = Enum.INT_WAVE;
    int INT_DOUBLE_WAVE = Enum.INT_DOUBLE_WAVE;
    int INT_PLUS = Enum.INT_PLUS;
    int INT_FLOW_CHART_PROCESS = Enum.INT_FLOW_CHART_PROCESS;
    int INT_FLOW_CHART_DECISION = Enum.INT_FLOW_CHART_DECISION;
    int INT_FLOW_CHART_INPUT_OUTPUT = Enum.INT_FLOW_CHART_INPUT_OUTPUT;
    int INT_FLOW_CHART_PREDEFINED_PROCESS = Enum.INT_FLOW_CHART_PREDEFINED_PROCESS;
    int INT_FLOW_CHART_INTERNAL_STORAGE = Enum.INT_FLOW_CHART_INTERNAL_STORAGE;
    int INT_FLOW_CHART_DOCUMENT = Enum.INT_FLOW_CHART_DOCUMENT;
    int INT_FLOW_CHART_MULTIDOCUMENT = Enum.INT_FLOW_CHART_MULTIDOCUMENT;
    int INT_FLOW_CHART_TERMINATOR = Enum.INT_FLOW_CHART_TERMINATOR;
    int INT_FLOW_CHART_PREPARATION = Enum.INT_FLOW_CHART_PREPARATION;
    int INT_FLOW_CHART_MANUAL_INPUT = Enum.INT_FLOW_CHART_MANUAL_INPUT;
    int INT_FLOW_CHART_MANUAL_OPERATION = Enum.INT_FLOW_CHART_MANUAL_OPERATION;
    int INT_FLOW_CHART_CONNECTOR = Enum.INT_FLOW_CHART_CONNECTOR;
    int INT_FLOW_CHART_PUNCHED_CARD = Enum.INT_FLOW_CHART_PUNCHED_CARD;
    int INT_FLOW_CHART_PUNCHED_TAPE = Enum.INT_FLOW_CHART_PUNCHED_TAPE;
    int INT_FLOW_CHART_SUMMING_JUNCTION = Enum.INT_FLOW_CHART_SUMMING_JUNCTION;
    int INT_FLOW_CHART_OR = Enum.INT_FLOW_CHART_OR;
    int INT_FLOW_CHART_COLLATE = Enum.INT_FLOW_CHART_COLLATE;
    int INT_FLOW_CHART_SORT = Enum.INT_FLOW_CHART_SORT;
    int INT_FLOW_CHART_EXTRACT = Enum.INT_FLOW_CHART_EXTRACT;
    int INT_FLOW_CHART_MERGE = Enum.INT_FLOW_CHART_MERGE;
    int INT_FLOW_CHART_OFFLINE_STORAGE = Enum.INT_FLOW_CHART_OFFLINE_STORAGE;
    int INT_FLOW_CHART_ONLINE_STORAGE = Enum.INT_FLOW_CHART_ONLINE_STORAGE;
    int INT_FLOW_CHART_MAGNETIC_TAPE = Enum.INT_FLOW_CHART_MAGNETIC_TAPE;
    int INT_FLOW_CHART_MAGNETIC_DISK = Enum.INT_FLOW_CHART_MAGNETIC_DISK;
    int INT_FLOW_CHART_MAGNETIC_DRUM = Enum.INT_FLOW_CHART_MAGNETIC_DRUM;
    int INT_FLOW_CHART_DISPLAY = Enum.INT_FLOW_CHART_DISPLAY;
    int INT_FLOW_CHART_DELAY = Enum.INT_FLOW_CHART_DELAY;
    int INT_FLOW_CHART_ALTERNATE_PROCESS = Enum.INT_FLOW_CHART_ALTERNATE_PROCESS;
    int INT_FLOW_CHART_OFFPAGE_CONNECTOR = Enum.INT_FLOW_CHART_OFFPAGE_CONNECTOR;
    int INT_ACTION_BUTTON_BLANK = Enum.INT_ACTION_BUTTON_BLANK;
    int INT_ACTION_BUTTON_HOME = Enum.INT_ACTION_BUTTON_HOME;
    int INT_ACTION_BUTTON_HELP = Enum.INT_ACTION_BUTTON_HELP;
    int INT_ACTION_BUTTON_INFORMATION = Enum.INT_ACTION_BUTTON_INFORMATION;
    int INT_ACTION_BUTTON_FORWARD_NEXT = Enum.INT_ACTION_BUTTON_FORWARD_NEXT;
    int INT_ACTION_BUTTON_BACK_PREVIOUS = Enum.INT_ACTION_BUTTON_BACK_PREVIOUS;
    int INT_ACTION_BUTTON_END = Enum.INT_ACTION_BUTTON_END;
    int INT_ACTION_BUTTON_BEGINNING = Enum.INT_ACTION_BUTTON_BEGINNING;
    int INT_ACTION_BUTTON_RETURN = Enum.INT_ACTION_BUTTON_RETURN;
    int INT_ACTION_BUTTON_DOCUMENT = Enum.INT_ACTION_BUTTON_DOCUMENT;
    int INT_ACTION_BUTTON_SOUND = Enum.INT_ACTION_BUTTON_SOUND;
    int INT_ACTION_BUTTON_MOVIE = Enum.INT_ACTION_BUTTON_MOVIE;
    int INT_GEAR_6 = Enum.INT_GEAR_6;
    int INT_GEAR_9 = Enum.INT_GEAR_9;
    int INT_FUNNEL = Enum.INT_FUNNEL;
    int INT_MATH_PLUS = Enum.INT_MATH_PLUS;
    int INT_MATH_MINUS = Enum.INT_MATH_MINUS;
    int INT_MATH_MULTIPLY = Enum.INT_MATH_MULTIPLY;
    int INT_MATH_DIVIDE = Enum.INT_MATH_DIVIDE;
    int INT_MATH_EQUAL = Enum.INT_MATH_EQUAL;
    int INT_MATH_NOT_EQUAL = Enum.INT_MATH_NOT_EQUAL;
    int INT_CORNER_TABS = Enum.INT_CORNER_TABS;
    int INT_SQUARE_TABS = Enum.INT_SQUARE_TABS;
    int INT_PLAQUE_TABS = Enum.INT_PLAQUE_TABS;
    int INT_CHART_X = Enum.INT_CHART_X;
    int INT_CHART_STAR = Enum.INT_CHART_STAR;
    int INT_CHART_PLUS = Enum.INT_CHART_PLUS;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STShapeType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_LINE
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

        static final int INT_LINE = 1;
        static final int INT_LINE_INV = 2;
        static final int INT_TRIANGLE = 3;
        static final int INT_RT_TRIANGLE = 4;
        static final int INT_RECT = 5;
        static final int INT_DIAMOND = 6;
        static final int INT_PARALLELOGRAM = 7;
        static final int INT_TRAPEZOID = 8;
        static final int INT_NON_ISOSCELES_TRAPEZOID = 9;
        static final int INT_PENTAGON = 10;
        static final int INT_HEXAGON = 11;
        static final int INT_HEPTAGON = 12;
        static final int INT_OCTAGON = 13;
        static final int INT_DECAGON = 14;
        static final int INT_DODECAGON = 15;
        static final int INT_STAR_4 = 16;
        static final int INT_STAR_5 = 17;
        static final int INT_STAR_6 = 18;
        static final int INT_STAR_7 = 19;
        static final int INT_STAR_8 = 20;
        static final int INT_STAR_10 = 21;
        static final int INT_STAR_12 = 22;
        static final int INT_STAR_16 = 23;
        static final int INT_STAR_24 = 24;
        static final int INT_STAR_32 = 25;
        static final int INT_ROUND_RECT = 26;
        static final int INT_ROUND_1_RECT = 27;
        static final int INT_ROUND_2_SAME_RECT = 28;
        static final int INT_ROUND_2_DIAG_RECT = 29;
        static final int INT_SNIP_ROUND_RECT = 30;
        static final int INT_SNIP_1_RECT = 31;
        static final int INT_SNIP_2_SAME_RECT = 32;
        static final int INT_SNIP_2_DIAG_RECT = 33;
        static final int INT_PLAQUE = 34;
        static final int INT_ELLIPSE = 35;
        static final int INT_TEARDROP = 36;
        static final int INT_HOME_PLATE = 37;
        static final int INT_CHEVRON = 38;
        static final int INT_PIE_WEDGE = 39;
        static final int INT_PIE = 40;
        static final int INT_BLOCK_ARC = 41;
        static final int INT_DONUT = 42;
        static final int INT_NO_SMOKING = 43;
        static final int INT_RIGHT_ARROW = 44;
        static final int INT_LEFT_ARROW = 45;
        static final int INT_UP_ARROW = 46;
        static final int INT_DOWN_ARROW = 47;
        static final int INT_STRIPED_RIGHT_ARROW = 48;
        static final int INT_NOTCHED_RIGHT_ARROW = 49;
        static final int INT_BENT_UP_ARROW = 50;
        static final int INT_LEFT_RIGHT_ARROW = 51;
        static final int INT_UP_DOWN_ARROW = 52;
        static final int INT_LEFT_UP_ARROW = 53;
        static final int INT_LEFT_RIGHT_UP_ARROW = 54;
        static final int INT_QUAD_ARROW = 55;
        static final int INT_LEFT_ARROW_CALLOUT = 56;
        static final int INT_RIGHT_ARROW_CALLOUT = 57;
        static final int INT_UP_ARROW_CALLOUT = 58;
        static final int INT_DOWN_ARROW_CALLOUT = 59;
        static final int INT_LEFT_RIGHT_ARROW_CALLOUT = 60;
        static final int INT_UP_DOWN_ARROW_CALLOUT = 61;
        static final int INT_QUAD_ARROW_CALLOUT = 62;
        static final int INT_BENT_ARROW = 63;
        static final int INT_UTURN_ARROW = 64;
        static final int INT_CIRCULAR_ARROW = 65;
        static final int INT_LEFT_CIRCULAR_ARROW = 66;
        static final int INT_LEFT_RIGHT_CIRCULAR_ARROW = 67;
        static final int INT_CURVED_RIGHT_ARROW = 68;
        static final int INT_CURVED_LEFT_ARROW = 69;
        static final int INT_CURVED_UP_ARROW = 70;
        static final int INT_CURVED_DOWN_ARROW = 71;
        static final int INT_SWOOSH_ARROW = 72;
        static final int INT_CUBE = 73;
        static final int INT_CAN = 74;
        static final int INT_LIGHTNING_BOLT = 75;
        static final int INT_HEART = 76;
        static final int INT_SUN = 77;
        static final int INT_MOON = 78;
        static final int INT_SMILEY_FACE = 79;
        static final int INT_IRREGULAR_SEAL_1 = 80;
        static final int INT_IRREGULAR_SEAL_2 = 81;
        static final int INT_FOLDED_CORNER = 82;
        static final int INT_BEVEL = 83;
        static final int INT_FRAME = 84;
        static final int INT_HALF_FRAME = 85;
        static final int INT_CORNER = 86;
        static final int INT_DIAG_STRIPE = 87;
        static final int INT_CHORD = 88;
        static final int INT_ARC = 89;
        static final int INT_LEFT_BRACKET = 90;
        static final int INT_RIGHT_BRACKET = 91;
        static final int INT_LEFT_BRACE = 92;
        static final int INT_RIGHT_BRACE = 93;
        static final int INT_BRACKET_PAIR = 94;
        static final int INT_BRACE_PAIR = 95;
        static final int INT_STRAIGHT_CONNECTOR_1 = 96;
        static final int INT_BENT_CONNECTOR_2 = 97;
        static final int INT_BENT_CONNECTOR_3 = 98;
        static final int INT_BENT_CONNECTOR_4 = 99;
        static final int INT_BENT_CONNECTOR_5 = 100;
        static final int INT_CURVED_CONNECTOR_2 = 101;
        static final int INT_CURVED_CONNECTOR_3 = 102;
        static final int INT_CURVED_CONNECTOR_4 = 103;
        static final int INT_CURVED_CONNECTOR_5 = 104;
        static final int INT_CALLOUT_1 = 105;
        static final int INT_CALLOUT_2 = 106;
        static final int INT_CALLOUT_3 = 107;
        static final int INT_ACCENT_CALLOUT_1 = 108;
        static final int INT_ACCENT_CALLOUT_2 = 109;
        static final int INT_ACCENT_CALLOUT_3 = 110;
        static final int INT_BORDER_CALLOUT_1 = 111;
        static final int INT_BORDER_CALLOUT_2 = 112;
        static final int INT_BORDER_CALLOUT_3 = 113;
        static final int INT_ACCENT_BORDER_CALLOUT_1 = 114;
        static final int INT_ACCENT_BORDER_CALLOUT_2 = 115;
        static final int INT_ACCENT_BORDER_CALLOUT_3 = 116;
        static final int INT_WEDGE_RECT_CALLOUT = 117;
        static final int INT_WEDGE_ROUND_RECT_CALLOUT = 118;
        static final int INT_WEDGE_ELLIPSE_CALLOUT = 119;
        static final int INT_CLOUD_CALLOUT = 120;
        static final int INT_CLOUD = 121;
        static final int INT_RIBBON = 122;
        static final int INT_RIBBON_2 = 123;
        static final int INT_ELLIPSE_RIBBON = 124;
        static final int INT_ELLIPSE_RIBBON_2 = 125;
        static final int INT_LEFT_RIGHT_RIBBON = 126;
        static final int INT_VERTICAL_SCROLL = 127;
        static final int INT_HORIZONTAL_SCROLL = 128;
        static final int INT_WAVE = 129;
        static final int INT_DOUBLE_WAVE = 130;
        static final int INT_PLUS = 131;
        static final int INT_FLOW_CHART_PROCESS = 132;
        static final int INT_FLOW_CHART_DECISION = 133;
        static final int INT_FLOW_CHART_INPUT_OUTPUT = 134;
        static final int INT_FLOW_CHART_PREDEFINED_PROCESS = 135;
        static final int INT_FLOW_CHART_INTERNAL_STORAGE = 136;
        static final int INT_FLOW_CHART_DOCUMENT = 137;
        static final int INT_FLOW_CHART_MULTIDOCUMENT = 138;
        static final int INT_FLOW_CHART_TERMINATOR = 139;
        static final int INT_FLOW_CHART_PREPARATION = 140;
        static final int INT_FLOW_CHART_MANUAL_INPUT = 141;
        static final int INT_FLOW_CHART_MANUAL_OPERATION = 142;
        static final int INT_FLOW_CHART_CONNECTOR = 143;
        static final int INT_FLOW_CHART_PUNCHED_CARD = 144;
        static final int INT_FLOW_CHART_PUNCHED_TAPE = 145;
        static final int INT_FLOW_CHART_SUMMING_JUNCTION = 146;
        static final int INT_FLOW_CHART_OR = 147;
        static final int INT_FLOW_CHART_COLLATE = 148;
        static final int INT_FLOW_CHART_SORT = 149;
        static final int INT_FLOW_CHART_EXTRACT = 150;
        static final int INT_FLOW_CHART_MERGE = 151;
        static final int INT_FLOW_CHART_OFFLINE_STORAGE = 152;
        static final int INT_FLOW_CHART_ONLINE_STORAGE = 153;
        static final int INT_FLOW_CHART_MAGNETIC_TAPE = 154;
        static final int INT_FLOW_CHART_MAGNETIC_DISK = 155;
        static final int INT_FLOW_CHART_MAGNETIC_DRUM = 156;
        static final int INT_FLOW_CHART_DISPLAY = 157;
        static final int INT_FLOW_CHART_DELAY = 158;
        static final int INT_FLOW_CHART_ALTERNATE_PROCESS = 159;
        static final int INT_FLOW_CHART_OFFPAGE_CONNECTOR = 160;
        static final int INT_ACTION_BUTTON_BLANK = 161;
        static final int INT_ACTION_BUTTON_HOME = 162;
        static final int INT_ACTION_BUTTON_HELP = 163;
        static final int INT_ACTION_BUTTON_INFORMATION = 164;
        static final int INT_ACTION_BUTTON_FORWARD_NEXT = 165;
        static final int INT_ACTION_BUTTON_BACK_PREVIOUS = 166;
        static final int INT_ACTION_BUTTON_END = 167;
        static final int INT_ACTION_BUTTON_BEGINNING = 168;
        static final int INT_ACTION_BUTTON_RETURN = 169;
        static final int INT_ACTION_BUTTON_DOCUMENT = 170;
        static final int INT_ACTION_BUTTON_SOUND = 171;
        static final int INT_ACTION_BUTTON_MOVIE = 172;
        static final int INT_GEAR_6 = 173;
        static final int INT_GEAR_9 = 174;
        static final int INT_FUNNEL = 175;
        static final int INT_MATH_PLUS = 176;
        static final int INT_MATH_MINUS = 177;
        static final int INT_MATH_MULTIPLY = 178;
        static final int INT_MATH_DIVIDE = 179;
        static final int INT_MATH_EQUAL = 180;
        static final int INT_MATH_NOT_EQUAL = 181;
        static final int INT_CORNER_TABS = 182;
        static final int INT_SQUARE_TABS = 183;
        static final int INT_PLAQUE_TABS = 184;
        static final int INT_CHART_X = 185;
        static final int INT_CHART_STAR = 186;
        static final int INT_CHART_PLUS = 187;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("line", INT_LINE),
            new Enum("lineInv", INT_LINE_INV),
            new Enum("triangle", INT_TRIANGLE),
            new Enum("rtTriangle", INT_RT_TRIANGLE),
            new Enum("rect", INT_RECT),
            new Enum("diamond", INT_DIAMOND),
            new Enum("parallelogram", INT_PARALLELOGRAM),
            new Enum("trapezoid", INT_TRAPEZOID),
            new Enum("nonIsoscelesTrapezoid", INT_NON_ISOSCELES_TRAPEZOID),
            new Enum("pentagon", INT_PENTAGON),
            new Enum("hexagon", INT_HEXAGON),
            new Enum("heptagon", INT_HEPTAGON),
            new Enum("octagon", INT_OCTAGON),
            new Enum("decagon", INT_DECAGON),
            new Enum("dodecagon", INT_DODECAGON),
            new Enum("star4", INT_STAR_4),
            new Enum("star5", INT_STAR_5),
            new Enum("star6", INT_STAR_6),
            new Enum("star7", INT_STAR_7),
            new Enum("star8", INT_STAR_8),
            new Enum("star10", INT_STAR_10),
            new Enum("star12", INT_STAR_12),
            new Enum("star16", INT_STAR_16),
            new Enum("star24", INT_STAR_24),
            new Enum("star32", INT_STAR_32),
            new Enum("roundRect", INT_ROUND_RECT),
            new Enum("round1Rect", INT_ROUND_1_RECT),
            new Enum("round2SameRect", INT_ROUND_2_SAME_RECT),
            new Enum("round2DiagRect", INT_ROUND_2_DIAG_RECT),
            new Enum("snipRoundRect", INT_SNIP_ROUND_RECT),
            new Enum("snip1Rect", INT_SNIP_1_RECT),
            new Enum("snip2SameRect", INT_SNIP_2_SAME_RECT),
            new Enum("snip2DiagRect", INT_SNIP_2_DIAG_RECT),
            new Enum("plaque", INT_PLAQUE),
            new Enum("ellipse", INT_ELLIPSE),
            new Enum("teardrop", INT_TEARDROP),
            new Enum("homePlate", INT_HOME_PLATE),
            new Enum("chevron", INT_CHEVRON),
            new Enum("pieWedge", INT_PIE_WEDGE),
            new Enum("pie", INT_PIE),
            new Enum("blockArc", INT_BLOCK_ARC),
            new Enum("donut", INT_DONUT),
            new Enum("noSmoking", INT_NO_SMOKING),
            new Enum("rightArrow", INT_RIGHT_ARROW),
            new Enum("leftArrow", INT_LEFT_ARROW),
            new Enum("upArrow", INT_UP_ARROW),
            new Enum("downArrow", INT_DOWN_ARROW),
            new Enum("stripedRightArrow", INT_STRIPED_RIGHT_ARROW),
            new Enum("notchedRightArrow", INT_NOTCHED_RIGHT_ARROW),
            new Enum("bentUpArrow", INT_BENT_UP_ARROW),
            new Enum("leftRightArrow", INT_LEFT_RIGHT_ARROW),
            new Enum("upDownArrow", INT_UP_DOWN_ARROW),
            new Enum("leftUpArrow", INT_LEFT_UP_ARROW),
            new Enum("leftRightUpArrow", INT_LEFT_RIGHT_UP_ARROW),
            new Enum("quadArrow", INT_QUAD_ARROW),
            new Enum("leftArrowCallout", INT_LEFT_ARROW_CALLOUT),
            new Enum("rightArrowCallout", INT_RIGHT_ARROW_CALLOUT),
            new Enum("upArrowCallout", INT_UP_ARROW_CALLOUT),
            new Enum("downArrowCallout", INT_DOWN_ARROW_CALLOUT),
            new Enum("leftRightArrowCallout", INT_LEFT_RIGHT_ARROW_CALLOUT),
            new Enum("upDownArrowCallout", INT_UP_DOWN_ARROW_CALLOUT),
            new Enum("quadArrowCallout", INT_QUAD_ARROW_CALLOUT),
            new Enum("bentArrow", INT_BENT_ARROW),
            new Enum("uturnArrow", INT_UTURN_ARROW),
            new Enum("circularArrow", INT_CIRCULAR_ARROW),
            new Enum("leftCircularArrow", INT_LEFT_CIRCULAR_ARROW),
            new Enum("leftRightCircularArrow", INT_LEFT_RIGHT_CIRCULAR_ARROW),
            new Enum("curvedRightArrow", INT_CURVED_RIGHT_ARROW),
            new Enum("curvedLeftArrow", INT_CURVED_LEFT_ARROW),
            new Enum("curvedUpArrow", INT_CURVED_UP_ARROW),
            new Enum("curvedDownArrow", INT_CURVED_DOWN_ARROW),
            new Enum("swooshArrow", INT_SWOOSH_ARROW),
            new Enum("cube", INT_CUBE),
            new Enum("can", INT_CAN),
            new Enum("lightningBolt", INT_LIGHTNING_BOLT),
            new Enum("heart", INT_HEART),
            new Enum("sun", INT_SUN),
            new Enum("moon", INT_MOON),
            new Enum("smileyFace", INT_SMILEY_FACE),
            new Enum("irregularSeal1", INT_IRREGULAR_SEAL_1),
            new Enum("irregularSeal2", INT_IRREGULAR_SEAL_2),
            new Enum("foldedCorner", INT_FOLDED_CORNER),
            new Enum("bevel", INT_BEVEL),
            new Enum("frame", INT_FRAME),
            new Enum("halfFrame", INT_HALF_FRAME),
            new Enum("corner", INT_CORNER),
            new Enum("diagStripe", INT_DIAG_STRIPE),
            new Enum("chord", INT_CHORD),
            new Enum("arc", INT_ARC),
            new Enum("leftBracket", INT_LEFT_BRACKET),
            new Enum("rightBracket", INT_RIGHT_BRACKET),
            new Enum("leftBrace", INT_LEFT_BRACE),
            new Enum("rightBrace", INT_RIGHT_BRACE),
            new Enum("bracketPair", INT_BRACKET_PAIR),
            new Enum("bracePair", INT_BRACE_PAIR),
            new Enum("straightConnector1", INT_STRAIGHT_CONNECTOR_1),
            new Enum("bentConnector2", INT_BENT_CONNECTOR_2),
            new Enum("bentConnector3", INT_BENT_CONNECTOR_3),
            new Enum("bentConnector4", INT_BENT_CONNECTOR_4),
            new Enum("bentConnector5", INT_BENT_CONNECTOR_5),
            new Enum("curvedConnector2", INT_CURVED_CONNECTOR_2),
            new Enum("curvedConnector3", INT_CURVED_CONNECTOR_3),
            new Enum("curvedConnector4", INT_CURVED_CONNECTOR_4),
            new Enum("curvedConnector5", INT_CURVED_CONNECTOR_5),
            new Enum("callout1", INT_CALLOUT_1),
            new Enum("callout2", INT_CALLOUT_2),
            new Enum("callout3", INT_CALLOUT_3),
            new Enum("accentCallout1", INT_ACCENT_CALLOUT_1),
            new Enum("accentCallout2", INT_ACCENT_CALLOUT_2),
            new Enum("accentCallout3", INT_ACCENT_CALLOUT_3),
            new Enum("borderCallout1", INT_BORDER_CALLOUT_1),
            new Enum("borderCallout2", INT_BORDER_CALLOUT_2),
            new Enum("borderCallout3", INT_BORDER_CALLOUT_3),
            new Enum("accentBorderCallout1", INT_ACCENT_BORDER_CALLOUT_1),
            new Enum("accentBorderCallout2", INT_ACCENT_BORDER_CALLOUT_2),
            new Enum("accentBorderCallout3", INT_ACCENT_BORDER_CALLOUT_3),
            new Enum("wedgeRectCallout", INT_WEDGE_RECT_CALLOUT),
            new Enum("wedgeRoundRectCallout", INT_WEDGE_ROUND_RECT_CALLOUT),
            new Enum("wedgeEllipseCallout", INT_WEDGE_ELLIPSE_CALLOUT),
            new Enum("cloudCallout", INT_CLOUD_CALLOUT),
            new Enum("cloud", INT_CLOUD),
            new Enum("ribbon", INT_RIBBON),
            new Enum("ribbon2", INT_RIBBON_2),
            new Enum("ellipseRibbon", INT_ELLIPSE_RIBBON),
            new Enum("ellipseRibbon2", INT_ELLIPSE_RIBBON_2),
            new Enum("leftRightRibbon", INT_LEFT_RIGHT_RIBBON),
            new Enum("verticalScroll", INT_VERTICAL_SCROLL),
            new Enum("horizontalScroll", INT_HORIZONTAL_SCROLL),
            new Enum("wave", INT_WAVE),
            new Enum("doubleWave", INT_DOUBLE_WAVE),
            new Enum("plus", INT_PLUS),
            new Enum("flowChartProcess", INT_FLOW_CHART_PROCESS),
            new Enum("flowChartDecision", INT_FLOW_CHART_DECISION),
            new Enum("flowChartInputOutput", INT_FLOW_CHART_INPUT_OUTPUT),
            new Enum("flowChartPredefinedProcess", INT_FLOW_CHART_PREDEFINED_PROCESS),
            new Enum("flowChartInternalStorage", INT_FLOW_CHART_INTERNAL_STORAGE),
            new Enum("flowChartDocument", INT_FLOW_CHART_DOCUMENT),
            new Enum("flowChartMultidocument", INT_FLOW_CHART_MULTIDOCUMENT),
            new Enum("flowChartTerminator", INT_FLOW_CHART_TERMINATOR),
            new Enum("flowChartPreparation", INT_FLOW_CHART_PREPARATION),
            new Enum("flowChartManualInput", INT_FLOW_CHART_MANUAL_INPUT),
            new Enum("flowChartManualOperation", INT_FLOW_CHART_MANUAL_OPERATION),
            new Enum("flowChartConnector", INT_FLOW_CHART_CONNECTOR),
            new Enum("flowChartPunchedCard", INT_FLOW_CHART_PUNCHED_CARD),
            new Enum("flowChartPunchedTape", INT_FLOW_CHART_PUNCHED_TAPE),
            new Enum("flowChartSummingJunction", INT_FLOW_CHART_SUMMING_JUNCTION),
            new Enum("flowChartOr", INT_FLOW_CHART_OR),
            new Enum("flowChartCollate", INT_FLOW_CHART_COLLATE),
            new Enum("flowChartSort", INT_FLOW_CHART_SORT),
            new Enum("flowChartExtract", INT_FLOW_CHART_EXTRACT),
            new Enum("flowChartMerge", INT_FLOW_CHART_MERGE),
            new Enum("flowChartOfflineStorage", INT_FLOW_CHART_OFFLINE_STORAGE),
            new Enum("flowChartOnlineStorage", INT_FLOW_CHART_ONLINE_STORAGE),
            new Enum("flowChartMagneticTape", INT_FLOW_CHART_MAGNETIC_TAPE),
            new Enum("flowChartMagneticDisk", INT_FLOW_CHART_MAGNETIC_DISK),
            new Enum("flowChartMagneticDrum", INT_FLOW_CHART_MAGNETIC_DRUM),
            new Enum("flowChartDisplay", INT_FLOW_CHART_DISPLAY),
            new Enum("flowChartDelay", INT_FLOW_CHART_DELAY),
            new Enum("flowChartAlternateProcess", INT_FLOW_CHART_ALTERNATE_PROCESS),
            new Enum("flowChartOffpageConnector", INT_FLOW_CHART_OFFPAGE_CONNECTOR),
            new Enum("actionButtonBlank", INT_ACTION_BUTTON_BLANK),
            new Enum("actionButtonHome", INT_ACTION_BUTTON_HOME),
            new Enum("actionButtonHelp", INT_ACTION_BUTTON_HELP),
            new Enum("actionButtonInformation", INT_ACTION_BUTTON_INFORMATION),
            new Enum("actionButtonForwardNext", INT_ACTION_BUTTON_FORWARD_NEXT),
            new Enum("actionButtonBackPrevious", INT_ACTION_BUTTON_BACK_PREVIOUS),
            new Enum("actionButtonEnd", INT_ACTION_BUTTON_END),
            new Enum("actionButtonBeginning", INT_ACTION_BUTTON_BEGINNING),
            new Enum("actionButtonReturn", INT_ACTION_BUTTON_RETURN),
            new Enum("actionButtonDocument", INT_ACTION_BUTTON_DOCUMENT),
            new Enum("actionButtonSound", INT_ACTION_BUTTON_SOUND),
            new Enum("actionButtonMovie", INT_ACTION_BUTTON_MOVIE),
            new Enum("gear6", INT_GEAR_6),
            new Enum("gear9", INT_GEAR_9),
            new Enum("funnel", INT_FUNNEL),
            new Enum("mathPlus", INT_MATH_PLUS),
            new Enum("mathMinus", INT_MATH_MINUS),
            new Enum("mathMultiply", INT_MATH_MULTIPLY),
            new Enum("mathDivide", INT_MATH_DIVIDE),
            new Enum("mathEqual", INT_MATH_EQUAL),
            new Enum("mathNotEqual", INT_MATH_NOT_EQUAL),
            new Enum("cornerTabs", INT_CORNER_TABS),
            new Enum("squareTabs", INT_SQUARE_TABS),
            new Enum("plaqueTabs", INT_PLAQUE_TABS),
            new Enum("chartX", INT_CHART_X),
            new Enum("chartStar", INT_CHART_STAR),
            new Enum("chartPlus", INT_CHART_PLUS),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
