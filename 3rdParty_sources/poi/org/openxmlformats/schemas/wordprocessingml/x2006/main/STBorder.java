/*
 * XML Type:  ST_Border
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_Border(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.
 */
public interface STBorder extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stborderd7ectype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NIL = Enum.forString("nil");
    Enum NONE = Enum.forString("none");
    Enum SINGLE = Enum.forString("single");
    Enum THICK = Enum.forString("thick");
    Enum DOUBLE = Enum.forString("double");
    Enum DOTTED = Enum.forString("dotted");
    Enum DASHED = Enum.forString("dashed");
    Enum DOT_DASH = Enum.forString("dotDash");
    Enum DOT_DOT_DASH = Enum.forString("dotDotDash");
    Enum TRIPLE = Enum.forString("triple");
    Enum THIN_THICK_SMALL_GAP = Enum.forString("thinThickSmallGap");
    Enum THICK_THIN_SMALL_GAP = Enum.forString("thickThinSmallGap");
    Enum THIN_THICK_THIN_SMALL_GAP = Enum.forString("thinThickThinSmallGap");
    Enum THIN_THICK_MEDIUM_GAP = Enum.forString("thinThickMediumGap");
    Enum THICK_THIN_MEDIUM_GAP = Enum.forString("thickThinMediumGap");
    Enum THIN_THICK_THIN_MEDIUM_GAP = Enum.forString("thinThickThinMediumGap");
    Enum THIN_THICK_LARGE_GAP = Enum.forString("thinThickLargeGap");
    Enum THICK_THIN_LARGE_GAP = Enum.forString("thickThinLargeGap");
    Enum THIN_THICK_THIN_LARGE_GAP = Enum.forString("thinThickThinLargeGap");
    Enum WAVE = Enum.forString("wave");
    Enum DOUBLE_WAVE = Enum.forString("doubleWave");
    Enum DASH_SMALL_GAP = Enum.forString("dashSmallGap");
    Enum DASH_DOT_STROKED = Enum.forString("dashDotStroked");
    Enum THREE_D_EMBOSS = Enum.forString("threeDEmboss");
    Enum THREE_D_ENGRAVE = Enum.forString("threeDEngrave");
    Enum OUTSET = Enum.forString("outset");
    Enum INSET = Enum.forString("inset");
    Enum APPLES = Enum.forString("apples");
    Enum ARCHED_SCALLOPS = Enum.forString("archedScallops");
    Enum BABY_PACIFIER = Enum.forString("babyPacifier");
    Enum BABY_RATTLE = Enum.forString("babyRattle");
    Enum BALLOONS_3_COLORS = Enum.forString("balloons3Colors");
    Enum BALLOONS_HOT_AIR = Enum.forString("balloonsHotAir");
    Enum BASIC_BLACK_DASHES = Enum.forString("basicBlackDashes");
    Enum BASIC_BLACK_DOTS = Enum.forString("basicBlackDots");
    Enum BASIC_BLACK_SQUARES = Enum.forString("basicBlackSquares");
    Enum BASIC_THIN_LINES = Enum.forString("basicThinLines");
    Enum BASIC_WHITE_DASHES = Enum.forString("basicWhiteDashes");
    Enum BASIC_WHITE_DOTS = Enum.forString("basicWhiteDots");
    Enum BASIC_WHITE_SQUARES = Enum.forString("basicWhiteSquares");
    Enum BASIC_WIDE_INLINE = Enum.forString("basicWideInline");
    Enum BASIC_WIDE_MIDLINE = Enum.forString("basicWideMidline");
    Enum BASIC_WIDE_OUTLINE = Enum.forString("basicWideOutline");
    Enum BATS = Enum.forString("bats");
    Enum BIRDS = Enum.forString("birds");
    Enum BIRDS_FLIGHT = Enum.forString("birdsFlight");
    Enum CABINS = Enum.forString("cabins");
    Enum CAKE_SLICE = Enum.forString("cakeSlice");
    Enum CANDY_CORN = Enum.forString("candyCorn");
    Enum CELTIC_KNOTWORK = Enum.forString("celticKnotwork");
    Enum CERTIFICATE_BANNER = Enum.forString("certificateBanner");
    Enum CHAIN_LINK = Enum.forString("chainLink");
    Enum CHAMPAGNE_BOTTLE = Enum.forString("champagneBottle");
    Enum CHECKED_BAR_BLACK = Enum.forString("checkedBarBlack");
    Enum CHECKED_BAR_COLOR = Enum.forString("checkedBarColor");
    Enum CHECKERED = Enum.forString("checkered");
    Enum CHRISTMAS_TREE = Enum.forString("christmasTree");
    Enum CIRCLES_LINES = Enum.forString("circlesLines");
    Enum CIRCLES_RECTANGLES = Enum.forString("circlesRectangles");
    Enum CLASSICAL_WAVE = Enum.forString("classicalWave");
    Enum CLOCKS = Enum.forString("clocks");
    Enum COMPASS = Enum.forString("compass");
    Enum CONFETTI = Enum.forString("confetti");
    Enum CONFETTI_GRAYS = Enum.forString("confettiGrays");
    Enum CONFETTI_OUTLINE = Enum.forString("confettiOutline");
    Enum CONFETTI_STREAMERS = Enum.forString("confettiStreamers");
    Enum CONFETTI_WHITE = Enum.forString("confettiWhite");
    Enum CORNER_TRIANGLES = Enum.forString("cornerTriangles");
    Enum COUPON_CUTOUT_DASHES = Enum.forString("couponCutoutDashes");
    Enum COUPON_CUTOUT_DOTS = Enum.forString("couponCutoutDots");
    Enum CRAZY_MAZE = Enum.forString("crazyMaze");
    Enum CREATURES_BUTTERFLY = Enum.forString("creaturesButterfly");
    Enum CREATURES_FISH = Enum.forString("creaturesFish");
    Enum CREATURES_INSECTS = Enum.forString("creaturesInsects");
    Enum CREATURES_LADY_BUG = Enum.forString("creaturesLadyBug");
    Enum CROSS_STITCH = Enum.forString("crossStitch");
    Enum CUP = Enum.forString("cup");
    Enum DECO_ARCH = Enum.forString("decoArch");
    Enum DECO_ARCH_COLOR = Enum.forString("decoArchColor");
    Enum DECO_BLOCKS = Enum.forString("decoBlocks");
    Enum DIAMONDS_GRAY = Enum.forString("diamondsGray");
    Enum DOUBLE_D = Enum.forString("doubleD");
    Enum DOUBLE_DIAMONDS = Enum.forString("doubleDiamonds");
    Enum EARTH_1 = Enum.forString("earth1");
    Enum EARTH_2 = Enum.forString("earth2");
    Enum EARTH_3 = Enum.forString("earth3");
    Enum ECLIPSING_SQUARES_1 = Enum.forString("eclipsingSquares1");
    Enum ECLIPSING_SQUARES_2 = Enum.forString("eclipsingSquares2");
    Enum EGGS_BLACK = Enum.forString("eggsBlack");
    Enum FANS = Enum.forString("fans");
    Enum FILM = Enum.forString("film");
    Enum FIRECRACKERS = Enum.forString("firecrackers");
    Enum FLOWERS_BLOCK_PRINT = Enum.forString("flowersBlockPrint");
    Enum FLOWERS_DAISIES = Enum.forString("flowersDaisies");
    Enum FLOWERS_MODERN_1 = Enum.forString("flowersModern1");
    Enum FLOWERS_MODERN_2 = Enum.forString("flowersModern2");
    Enum FLOWERS_PANSY = Enum.forString("flowersPansy");
    Enum FLOWERS_RED_ROSE = Enum.forString("flowersRedRose");
    Enum FLOWERS_ROSES = Enum.forString("flowersRoses");
    Enum FLOWERS_TEACUP = Enum.forString("flowersTeacup");
    Enum FLOWERS_TINY = Enum.forString("flowersTiny");
    Enum GEMS = Enum.forString("gems");
    Enum GINGERBREAD_MAN = Enum.forString("gingerbreadMan");
    Enum GRADIENT = Enum.forString("gradient");
    Enum HANDMADE_1 = Enum.forString("handmade1");
    Enum HANDMADE_2 = Enum.forString("handmade2");
    Enum HEART_BALLOON = Enum.forString("heartBalloon");
    Enum HEART_GRAY = Enum.forString("heartGray");
    Enum HEARTS = Enum.forString("hearts");
    Enum HEEBIE_JEEBIES = Enum.forString("heebieJeebies");
    Enum HOLLY = Enum.forString("holly");
    Enum HOUSE_FUNKY = Enum.forString("houseFunky");
    Enum HYPNOTIC = Enum.forString("hypnotic");
    Enum ICE_CREAM_CONES = Enum.forString("iceCreamCones");
    Enum LIGHT_BULB = Enum.forString("lightBulb");
    Enum LIGHTNING_1 = Enum.forString("lightning1");
    Enum LIGHTNING_2 = Enum.forString("lightning2");
    Enum MAP_PINS = Enum.forString("mapPins");
    Enum MAPLE_LEAF = Enum.forString("mapleLeaf");
    Enum MAPLE_MUFFINS = Enum.forString("mapleMuffins");
    Enum MARQUEE = Enum.forString("marquee");
    Enum MARQUEE_TOOTHED = Enum.forString("marqueeToothed");
    Enum MOONS = Enum.forString("moons");
    Enum MOSAIC = Enum.forString("mosaic");
    Enum MUSIC_NOTES = Enum.forString("musicNotes");
    Enum NORTHWEST = Enum.forString("northwest");
    Enum OVALS = Enum.forString("ovals");
    Enum PACKAGES = Enum.forString("packages");
    Enum PALMS_BLACK = Enum.forString("palmsBlack");
    Enum PALMS_COLOR = Enum.forString("palmsColor");
    Enum PAPER_CLIPS = Enum.forString("paperClips");
    Enum PAPYRUS = Enum.forString("papyrus");
    Enum PARTY_FAVOR = Enum.forString("partyFavor");
    Enum PARTY_GLASS = Enum.forString("partyGlass");
    Enum PENCILS = Enum.forString("pencils");
    Enum PEOPLE = Enum.forString("people");
    Enum PEOPLE_WAVING = Enum.forString("peopleWaving");
    Enum PEOPLE_HATS = Enum.forString("peopleHats");
    Enum POINSETTIAS = Enum.forString("poinsettias");
    Enum POSTAGE_STAMP = Enum.forString("postageStamp");
    Enum PUMPKIN_1 = Enum.forString("pumpkin1");
    Enum PUSH_PIN_NOTE_2 = Enum.forString("pushPinNote2");
    Enum PUSH_PIN_NOTE_1 = Enum.forString("pushPinNote1");
    Enum PYRAMIDS = Enum.forString("pyramids");
    Enum PYRAMIDS_ABOVE = Enum.forString("pyramidsAbove");
    Enum QUADRANTS = Enum.forString("quadrants");
    Enum RINGS = Enum.forString("rings");
    Enum SAFARI = Enum.forString("safari");
    Enum SAWTOOTH = Enum.forString("sawtooth");
    Enum SAWTOOTH_GRAY = Enum.forString("sawtoothGray");
    Enum SCARED_CAT = Enum.forString("scaredCat");
    Enum SEATTLE = Enum.forString("seattle");
    Enum SHADOWED_SQUARES = Enum.forString("shadowedSquares");
    Enum SHARKS_TEETH = Enum.forString("sharksTeeth");
    Enum SHOREBIRD_TRACKS = Enum.forString("shorebirdTracks");
    Enum SKYROCKET = Enum.forString("skyrocket");
    Enum SNOWFLAKE_FANCY = Enum.forString("snowflakeFancy");
    Enum SNOWFLAKES = Enum.forString("snowflakes");
    Enum SOMBRERO = Enum.forString("sombrero");
    Enum SOUTHWEST = Enum.forString("southwest");
    Enum STARS = Enum.forString("stars");
    Enum STARS_TOP = Enum.forString("starsTop");
    Enum STARS_3_D = Enum.forString("stars3d");
    Enum STARS_BLACK = Enum.forString("starsBlack");
    Enum STARS_SHADOWED = Enum.forString("starsShadowed");
    Enum SUN = Enum.forString("sun");
    Enum SWIRLIGIG = Enum.forString("swirligig");
    Enum TORN_PAPER = Enum.forString("tornPaper");
    Enum TORN_PAPER_BLACK = Enum.forString("tornPaperBlack");
    Enum TREES = Enum.forString("trees");
    Enum TRIANGLE_PARTY = Enum.forString("triangleParty");
    Enum TRIANGLES = Enum.forString("triangles");
    Enum TRIANGLE_1 = Enum.forString("triangle1");
    Enum TRIANGLE_2 = Enum.forString("triangle2");
    Enum TRIANGLE_CIRCLE_1 = Enum.forString("triangleCircle1");
    Enum TRIANGLE_CIRCLE_2 = Enum.forString("triangleCircle2");
    Enum SHAPES_1 = Enum.forString("shapes1");
    Enum SHAPES_2 = Enum.forString("shapes2");
    Enum TWISTED_LINES_1 = Enum.forString("twistedLines1");
    Enum TWISTED_LINES_2 = Enum.forString("twistedLines2");
    Enum VINE = Enum.forString("vine");
    Enum WAVELINE = Enum.forString("waveline");
    Enum WEAVING_ANGLES = Enum.forString("weavingAngles");
    Enum WEAVING_BRAID = Enum.forString("weavingBraid");
    Enum WEAVING_RIBBON = Enum.forString("weavingRibbon");
    Enum WEAVING_STRIPS = Enum.forString("weavingStrips");
    Enum WHITE_FLOWERS = Enum.forString("whiteFlowers");
    Enum WOODWORK = Enum.forString("woodwork");
    Enum X_ILLUSIONS = Enum.forString("xIllusions");
    Enum ZANY_TRIANGLES = Enum.forString("zanyTriangles");
    Enum ZIG_ZAG = Enum.forString("zigZag");
    Enum ZIG_ZAG_STITCH = Enum.forString("zigZagStitch");
    Enum CUSTOM = Enum.forString("custom");

    int INT_NIL = Enum.INT_NIL;
    int INT_NONE = Enum.INT_NONE;
    int INT_SINGLE = Enum.INT_SINGLE;
    int INT_THICK = Enum.INT_THICK;
    int INT_DOUBLE = Enum.INT_DOUBLE;
    int INT_DOTTED = Enum.INT_DOTTED;
    int INT_DASHED = Enum.INT_DASHED;
    int INT_DOT_DASH = Enum.INT_DOT_DASH;
    int INT_DOT_DOT_DASH = Enum.INT_DOT_DOT_DASH;
    int INT_TRIPLE = Enum.INT_TRIPLE;
    int INT_THIN_THICK_SMALL_GAP = Enum.INT_THIN_THICK_SMALL_GAP;
    int INT_THICK_THIN_SMALL_GAP = Enum.INT_THICK_THIN_SMALL_GAP;
    int INT_THIN_THICK_THIN_SMALL_GAP = Enum.INT_THIN_THICK_THIN_SMALL_GAP;
    int INT_THIN_THICK_MEDIUM_GAP = Enum.INT_THIN_THICK_MEDIUM_GAP;
    int INT_THICK_THIN_MEDIUM_GAP = Enum.INT_THICK_THIN_MEDIUM_GAP;
    int INT_THIN_THICK_THIN_MEDIUM_GAP = Enum.INT_THIN_THICK_THIN_MEDIUM_GAP;
    int INT_THIN_THICK_LARGE_GAP = Enum.INT_THIN_THICK_LARGE_GAP;
    int INT_THICK_THIN_LARGE_GAP = Enum.INT_THICK_THIN_LARGE_GAP;
    int INT_THIN_THICK_THIN_LARGE_GAP = Enum.INT_THIN_THICK_THIN_LARGE_GAP;
    int INT_WAVE = Enum.INT_WAVE;
    int INT_DOUBLE_WAVE = Enum.INT_DOUBLE_WAVE;
    int INT_DASH_SMALL_GAP = Enum.INT_DASH_SMALL_GAP;
    int INT_DASH_DOT_STROKED = Enum.INT_DASH_DOT_STROKED;
    int INT_THREE_D_EMBOSS = Enum.INT_THREE_D_EMBOSS;
    int INT_THREE_D_ENGRAVE = Enum.INT_THREE_D_ENGRAVE;
    int INT_OUTSET = Enum.INT_OUTSET;
    int INT_INSET = Enum.INT_INSET;
    int INT_APPLES = Enum.INT_APPLES;
    int INT_ARCHED_SCALLOPS = Enum.INT_ARCHED_SCALLOPS;
    int INT_BABY_PACIFIER = Enum.INT_BABY_PACIFIER;
    int INT_BABY_RATTLE = Enum.INT_BABY_RATTLE;
    int INT_BALLOONS_3_COLORS = Enum.INT_BALLOONS_3_COLORS;
    int INT_BALLOONS_HOT_AIR = Enum.INT_BALLOONS_HOT_AIR;
    int INT_BASIC_BLACK_DASHES = Enum.INT_BASIC_BLACK_DASHES;
    int INT_BASIC_BLACK_DOTS = Enum.INT_BASIC_BLACK_DOTS;
    int INT_BASIC_BLACK_SQUARES = Enum.INT_BASIC_BLACK_SQUARES;
    int INT_BASIC_THIN_LINES = Enum.INT_BASIC_THIN_LINES;
    int INT_BASIC_WHITE_DASHES = Enum.INT_BASIC_WHITE_DASHES;
    int INT_BASIC_WHITE_DOTS = Enum.INT_BASIC_WHITE_DOTS;
    int INT_BASIC_WHITE_SQUARES = Enum.INT_BASIC_WHITE_SQUARES;
    int INT_BASIC_WIDE_INLINE = Enum.INT_BASIC_WIDE_INLINE;
    int INT_BASIC_WIDE_MIDLINE = Enum.INT_BASIC_WIDE_MIDLINE;
    int INT_BASIC_WIDE_OUTLINE = Enum.INT_BASIC_WIDE_OUTLINE;
    int INT_BATS = Enum.INT_BATS;
    int INT_BIRDS = Enum.INT_BIRDS;
    int INT_BIRDS_FLIGHT = Enum.INT_BIRDS_FLIGHT;
    int INT_CABINS = Enum.INT_CABINS;
    int INT_CAKE_SLICE = Enum.INT_CAKE_SLICE;
    int INT_CANDY_CORN = Enum.INT_CANDY_CORN;
    int INT_CELTIC_KNOTWORK = Enum.INT_CELTIC_KNOTWORK;
    int INT_CERTIFICATE_BANNER = Enum.INT_CERTIFICATE_BANNER;
    int INT_CHAIN_LINK = Enum.INT_CHAIN_LINK;
    int INT_CHAMPAGNE_BOTTLE = Enum.INT_CHAMPAGNE_BOTTLE;
    int INT_CHECKED_BAR_BLACK = Enum.INT_CHECKED_BAR_BLACK;
    int INT_CHECKED_BAR_COLOR = Enum.INT_CHECKED_BAR_COLOR;
    int INT_CHECKERED = Enum.INT_CHECKERED;
    int INT_CHRISTMAS_TREE = Enum.INT_CHRISTMAS_TREE;
    int INT_CIRCLES_LINES = Enum.INT_CIRCLES_LINES;
    int INT_CIRCLES_RECTANGLES = Enum.INT_CIRCLES_RECTANGLES;
    int INT_CLASSICAL_WAVE = Enum.INT_CLASSICAL_WAVE;
    int INT_CLOCKS = Enum.INT_CLOCKS;
    int INT_COMPASS = Enum.INT_COMPASS;
    int INT_CONFETTI = Enum.INT_CONFETTI;
    int INT_CONFETTI_GRAYS = Enum.INT_CONFETTI_GRAYS;
    int INT_CONFETTI_OUTLINE = Enum.INT_CONFETTI_OUTLINE;
    int INT_CONFETTI_STREAMERS = Enum.INT_CONFETTI_STREAMERS;
    int INT_CONFETTI_WHITE = Enum.INT_CONFETTI_WHITE;
    int INT_CORNER_TRIANGLES = Enum.INT_CORNER_TRIANGLES;
    int INT_COUPON_CUTOUT_DASHES = Enum.INT_COUPON_CUTOUT_DASHES;
    int INT_COUPON_CUTOUT_DOTS = Enum.INT_COUPON_CUTOUT_DOTS;
    int INT_CRAZY_MAZE = Enum.INT_CRAZY_MAZE;
    int INT_CREATURES_BUTTERFLY = Enum.INT_CREATURES_BUTTERFLY;
    int INT_CREATURES_FISH = Enum.INT_CREATURES_FISH;
    int INT_CREATURES_INSECTS = Enum.INT_CREATURES_INSECTS;
    int INT_CREATURES_LADY_BUG = Enum.INT_CREATURES_LADY_BUG;
    int INT_CROSS_STITCH = Enum.INT_CROSS_STITCH;
    int INT_CUP = Enum.INT_CUP;
    int INT_DECO_ARCH = Enum.INT_DECO_ARCH;
    int INT_DECO_ARCH_COLOR = Enum.INT_DECO_ARCH_COLOR;
    int INT_DECO_BLOCKS = Enum.INT_DECO_BLOCKS;
    int INT_DIAMONDS_GRAY = Enum.INT_DIAMONDS_GRAY;
    int INT_DOUBLE_D = Enum.INT_DOUBLE_D;
    int INT_DOUBLE_DIAMONDS = Enum.INT_DOUBLE_DIAMONDS;
    int INT_EARTH_1 = Enum.INT_EARTH_1;
    int INT_EARTH_2 = Enum.INT_EARTH_2;
    int INT_EARTH_3 = Enum.INT_EARTH_3;
    int INT_ECLIPSING_SQUARES_1 = Enum.INT_ECLIPSING_SQUARES_1;
    int INT_ECLIPSING_SQUARES_2 = Enum.INT_ECLIPSING_SQUARES_2;
    int INT_EGGS_BLACK = Enum.INT_EGGS_BLACK;
    int INT_FANS = Enum.INT_FANS;
    int INT_FILM = Enum.INT_FILM;
    int INT_FIRECRACKERS = Enum.INT_FIRECRACKERS;
    int INT_FLOWERS_BLOCK_PRINT = Enum.INT_FLOWERS_BLOCK_PRINT;
    int INT_FLOWERS_DAISIES = Enum.INT_FLOWERS_DAISIES;
    int INT_FLOWERS_MODERN_1 = Enum.INT_FLOWERS_MODERN_1;
    int INT_FLOWERS_MODERN_2 = Enum.INT_FLOWERS_MODERN_2;
    int INT_FLOWERS_PANSY = Enum.INT_FLOWERS_PANSY;
    int INT_FLOWERS_RED_ROSE = Enum.INT_FLOWERS_RED_ROSE;
    int INT_FLOWERS_ROSES = Enum.INT_FLOWERS_ROSES;
    int INT_FLOWERS_TEACUP = Enum.INT_FLOWERS_TEACUP;
    int INT_FLOWERS_TINY = Enum.INT_FLOWERS_TINY;
    int INT_GEMS = Enum.INT_GEMS;
    int INT_GINGERBREAD_MAN = Enum.INT_GINGERBREAD_MAN;
    int INT_GRADIENT = Enum.INT_GRADIENT;
    int INT_HANDMADE_1 = Enum.INT_HANDMADE_1;
    int INT_HANDMADE_2 = Enum.INT_HANDMADE_2;
    int INT_HEART_BALLOON = Enum.INT_HEART_BALLOON;
    int INT_HEART_GRAY = Enum.INT_HEART_GRAY;
    int INT_HEARTS = Enum.INT_HEARTS;
    int INT_HEEBIE_JEEBIES = Enum.INT_HEEBIE_JEEBIES;
    int INT_HOLLY = Enum.INT_HOLLY;
    int INT_HOUSE_FUNKY = Enum.INT_HOUSE_FUNKY;
    int INT_HYPNOTIC = Enum.INT_HYPNOTIC;
    int INT_ICE_CREAM_CONES = Enum.INT_ICE_CREAM_CONES;
    int INT_LIGHT_BULB = Enum.INT_LIGHT_BULB;
    int INT_LIGHTNING_1 = Enum.INT_LIGHTNING_1;
    int INT_LIGHTNING_2 = Enum.INT_LIGHTNING_2;
    int INT_MAP_PINS = Enum.INT_MAP_PINS;
    int INT_MAPLE_LEAF = Enum.INT_MAPLE_LEAF;
    int INT_MAPLE_MUFFINS = Enum.INT_MAPLE_MUFFINS;
    int INT_MARQUEE = Enum.INT_MARQUEE;
    int INT_MARQUEE_TOOTHED = Enum.INT_MARQUEE_TOOTHED;
    int INT_MOONS = Enum.INT_MOONS;
    int INT_MOSAIC = Enum.INT_MOSAIC;
    int INT_MUSIC_NOTES = Enum.INT_MUSIC_NOTES;
    int INT_NORTHWEST = Enum.INT_NORTHWEST;
    int INT_OVALS = Enum.INT_OVALS;
    int INT_PACKAGES = Enum.INT_PACKAGES;
    int INT_PALMS_BLACK = Enum.INT_PALMS_BLACK;
    int INT_PALMS_COLOR = Enum.INT_PALMS_COLOR;
    int INT_PAPER_CLIPS = Enum.INT_PAPER_CLIPS;
    int INT_PAPYRUS = Enum.INT_PAPYRUS;
    int INT_PARTY_FAVOR = Enum.INT_PARTY_FAVOR;
    int INT_PARTY_GLASS = Enum.INT_PARTY_GLASS;
    int INT_PENCILS = Enum.INT_PENCILS;
    int INT_PEOPLE = Enum.INT_PEOPLE;
    int INT_PEOPLE_WAVING = Enum.INT_PEOPLE_WAVING;
    int INT_PEOPLE_HATS = Enum.INT_PEOPLE_HATS;
    int INT_POINSETTIAS = Enum.INT_POINSETTIAS;
    int INT_POSTAGE_STAMP = Enum.INT_POSTAGE_STAMP;
    int INT_PUMPKIN_1 = Enum.INT_PUMPKIN_1;
    int INT_PUSH_PIN_NOTE_2 = Enum.INT_PUSH_PIN_NOTE_2;
    int INT_PUSH_PIN_NOTE_1 = Enum.INT_PUSH_PIN_NOTE_1;
    int INT_PYRAMIDS = Enum.INT_PYRAMIDS;
    int INT_PYRAMIDS_ABOVE = Enum.INT_PYRAMIDS_ABOVE;
    int INT_QUADRANTS = Enum.INT_QUADRANTS;
    int INT_RINGS = Enum.INT_RINGS;
    int INT_SAFARI = Enum.INT_SAFARI;
    int INT_SAWTOOTH = Enum.INT_SAWTOOTH;
    int INT_SAWTOOTH_GRAY = Enum.INT_SAWTOOTH_GRAY;
    int INT_SCARED_CAT = Enum.INT_SCARED_CAT;
    int INT_SEATTLE = Enum.INT_SEATTLE;
    int INT_SHADOWED_SQUARES = Enum.INT_SHADOWED_SQUARES;
    int INT_SHARKS_TEETH = Enum.INT_SHARKS_TEETH;
    int INT_SHOREBIRD_TRACKS = Enum.INT_SHOREBIRD_TRACKS;
    int INT_SKYROCKET = Enum.INT_SKYROCKET;
    int INT_SNOWFLAKE_FANCY = Enum.INT_SNOWFLAKE_FANCY;
    int INT_SNOWFLAKES = Enum.INT_SNOWFLAKES;
    int INT_SOMBRERO = Enum.INT_SOMBRERO;
    int INT_SOUTHWEST = Enum.INT_SOUTHWEST;
    int INT_STARS = Enum.INT_STARS;
    int INT_STARS_TOP = Enum.INT_STARS_TOP;
    int INT_STARS_3_D = Enum.INT_STARS_3_D;
    int INT_STARS_BLACK = Enum.INT_STARS_BLACK;
    int INT_STARS_SHADOWED = Enum.INT_STARS_SHADOWED;
    int INT_SUN = Enum.INT_SUN;
    int INT_SWIRLIGIG = Enum.INT_SWIRLIGIG;
    int INT_TORN_PAPER = Enum.INT_TORN_PAPER;
    int INT_TORN_PAPER_BLACK = Enum.INT_TORN_PAPER_BLACK;
    int INT_TREES = Enum.INT_TREES;
    int INT_TRIANGLE_PARTY = Enum.INT_TRIANGLE_PARTY;
    int INT_TRIANGLES = Enum.INT_TRIANGLES;
    int INT_TRIANGLE_1 = Enum.INT_TRIANGLE_1;
    int INT_TRIANGLE_2 = Enum.INT_TRIANGLE_2;
    int INT_TRIANGLE_CIRCLE_1 = Enum.INT_TRIANGLE_CIRCLE_1;
    int INT_TRIANGLE_CIRCLE_2 = Enum.INT_TRIANGLE_CIRCLE_2;
    int INT_SHAPES_1 = Enum.INT_SHAPES_1;
    int INT_SHAPES_2 = Enum.INT_SHAPES_2;
    int INT_TWISTED_LINES_1 = Enum.INT_TWISTED_LINES_1;
    int INT_TWISTED_LINES_2 = Enum.INT_TWISTED_LINES_2;
    int INT_VINE = Enum.INT_VINE;
    int INT_WAVELINE = Enum.INT_WAVELINE;
    int INT_WEAVING_ANGLES = Enum.INT_WEAVING_ANGLES;
    int INT_WEAVING_BRAID = Enum.INT_WEAVING_BRAID;
    int INT_WEAVING_RIBBON = Enum.INT_WEAVING_RIBBON;
    int INT_WEAVING_STRIPS = Enum.INT_WEAVING_STRIPS;
    int INT_WHITE_FLOWERS = Enum.INT_WHITE_FLOWERS;
    int INT_WOODWORK = Enum.INT_WOODWORK;
    int INT_X_ILLUSIONS = Enum.INT_X_ILLUSIONS;
    int INT_ZANY_TRIANGLES = Enum.INT_ZANY_TRIANGLES;
    int INT_ZIG_ZAG = Enum.INT_ZIG_ZAG;
    int INT_ZIG_ZAG_STITCH = Enum.INT_ZIG_ZAG_STITCH;
    int INT_CUSTOM = Enum.INT_CUSTOM;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NIL
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

        static final int INT_NIL = 1;
        static final int INT_NONE = 2;
        static final int INT_SINGLE = 3;
        static final int INT_THICK = 4;
        static final int INT_DOUBLE = 5;
        static final int INT_DOTTED = 6;
        static final int INT_DASHED = 7;
        static final int INT_DOT_DASH = 8;
        static final int INT_DOT_DOT_DASH = 9;
        static final int INT_TRIPLE = 10;
        static final int INT_THIN_THICK_SMALL_GAP = 11;
        static final int INT_THICK_THIN_SMALL_GAP = 12;
        static final int INT_THIN_THICK_THIN_SMALL_GAP = 13;
        static final int INT_THIN_THICK_MEDIUM_GAP = 14;
        static final int INT_THICK_THIN_MEDIUM_GAP = 15;
        static final int INT_THIN_THICK_THIN_MEDIUM_GAP = 16;
        static final int INT_THIN_THICK_LARGE_GAP = 17;
        static final int INT_THICK_THIN_LARGE_GAP = 18;
        static final int INT_THIN_THICK_THIN_LARGE_GAP = 19;
        static final int INT_WAVE = 20;
        static final int INT_DOUBLE_WAVE = 21;
        static final int INT_DASH_SMALL_GAP = 22;
        static final int INT_DASH_DOT_STROKED = 23;
        static final int INT_THREE_D_EMBOSS = 24;
        static final int INT_THREE_D_ENGRAVE = 25;
        static final int INT_OUTSET = 26;
        static final int INT_INSET = 27;
        static final int INT_APPLES = 28;
        static final int INT_ARCHED_SCALLOPS = 29;
        static final int INT_BABY_PACIFIER = 30;
        static final int INT_BABY_RATTLE = 31;
        static final int INT_BALLOONS_3_COLORS = 32;
        static final int INT_BALLOONS_HOT_AIR = 33;
        static final int INT_BASIC_BLACK_DASHES = 34;
        static final int INT_BASIC_BLACK_DOTS = 35;
        static final int INT_BASIC_BLACK_SQUARES = 36;
        static final int INT_BASIC_THIN_LINES = 37;
        static final int INT_BASIC_WHITE_DASHES = 38;
        static final int INT_BASIC_WHITE_DOTS = 39;
        static final int INT_BASIC_WHITE_SQUARES = 40;
        static final int INT_BASIC_WIDE_INLINE = 41;
        static final int INT_BASIC_WIDE_MIDLINE = 42;
        static final int INT_BASIC_WIDE_OUTLINE = 43;
        static final int INT_BATS = 44;
        static final int INT_BIRDS = 45;
        static final int INT_BIRDS_FLIGHT = 46;
        static final int INT_CABINS = 47;
        static final int INT_CAKE_SLICE = 48;
        static final int INT_CANDY_CORN = 49;
        static final int INT_CELTIC_KNOTWORK = 50;
        static final int INT_CERTIFICATE_BANNER = 51;
        static final int INT_CHAIN_LINK = 52;
        static final int INT_CHAMPAGNE_BOTTLE = 53;
        static final int INT_CHECKED_BAR_BLACK = 54;
        static final int INT_CHECKED_BAR_COLOR = 55;
        static final int INT_CHECKERED = 56;
        static final int INT_CHRISTMAS_TREE = 57;
        static final int INT_CIRCLES_LINES = 58;
        static final int INT_CIRCLES_RECTANGLES = 59;
        static final int INT_CLASSICAL_WAVE = 60;
        static final int INT_CLOCKS = 61;
        static final int INT_COMPASS = 62;
        static final int INT_CONFETTI = 63;
        static final int INT_CONFETTI_GRAYS = 64;
        static final int INT_CONFETTI_OUTLINE = 65;
        static final int INT_CONFETTI_STREAMERS = 66;
        static final int INT_CONFETTI_WHITE = 67;
        static final int INT_CORNER_TRIANGLES = 68;
        static final int INT_COUPON_CUTOUT_DASHES = 69;
        static final int INT_COUPON_CUTOUT_DOTS = 70;
        static final int INT_CRAZY_MAZE = 71;
        static final int INT_CREATURES_BUTTERFLY = 72;
        static final int INT_CREATURES_FISH = 73;
        static final int INT_CREATURES_INSECTS = 74;
        static final int INT_CREATURES_LADY_BUG = 75;
        static final int INT_CROSS_STITCH = 76;
        static final int INT_CUP = 77;
        static final int INT_DECO_ARCH = 78;
        static final int INT_DECO_ARCH_COLOR = 79;
        static final int INT_DECO_BLOCKS = 80;
        static final int INT_DIAMONDS_GRAY = 81;
        static final int INT_DOUBLE_D = 82;
        static final int INT_DOUBLE_DIAMONDS = 83;
        static final int INT_EARTH_1 = 84;
        static final int INT_EARTH_2 = 85;
        static final int INT_EARTH_3 = 86;
        static final int INT_ECLIPSING_SQUARES_1 = 87;
        static final int INT_ECLIPSING_SQUARES_2 = 88;
        static final int INT_EGGS_BLACK = 89;
        static final int INT_FANS = 90;
        static final int INT_FILM = 91;
        static final int INT_FIRECRACKERS = 92;
        static final int INT_FLOWERS_BLOCK_PRINT = 93;
        static final int INT_FLOWERS_DAISIES = 94;
        static final int INT_FLOWERS_MODERN_1 = 95;
        static final int INT_FLOWERS_MODERN_2 = 96;
        static final int INT_FLOWERS_PANSY = 97;
        static final int INT_FLOWERS_RED_ROSE = 98;
        static final int INT_FLOWERS_ROSES = 99;
        static final int INT_FLOWERS_TEACUP = 100;
        static final int INT_FLOWERS_TINY = 101;
        static final int INT_GEMS = 102;
        static final int INT_GINGERBREAD_MAN = 103;
        static final int INT_GRADIENT = 104;
        static final int INT_HANDMADE_1 = 105;
        static final int INT_HANDMADE_2 = 106;
        static final int INT_HEART_BALLOON = 107;
        static final int INT_HEART_GRAY = 108;
        static final int INT_HEARTS = 109;
        static final int INT_HEEBIE_JEEBIES = 110;
        static final int INT_HOLLY = 111;
        static final int INT_HOUSE_FUNKY = 112;
        static final int INT_HYPNOTIC = 113;
        static final int INT_ICE_CREAM_CONES = 114;
        static final int INT_LIGHT_BULB = 115;
        static final int INT_LIGHTNING_1 = 116;
        static final int INT_LIGHTNING_2 = 117;
        static final int INT_MAP_PINS = 118;
        static final int INT_MAPLE_LEAF = 119;
        static final int INT_MAPLE_MUFFINS = 120;
        static final int INT_MARQUEE = 121;
        static final int INT_MARQUEE_TOOTHED = 122;
        static final int INT_MOONS = 123;
        static final int INT_MOSAIC = 124;
        static final int INT_MUSIC_NOTES = 125;
        static final int INT_NORTHWEST = 126;
        static final int INT_OVALS = 127;
        static final int INT_PACKAGES = 128;
        static final int INT_PALMS_BLACK = 129;
        static final int INT_PALMS_COLOR = 130;
        static final int INT_PAPER_CLIPS = 131;
        static final int INT_PAPYRUS = 132;
        static final int INT_PARTY_FAVOR = 133;
        static final int INT_PARTY_GLASS = 134;
        static final int INT_PENCILS = 135;
        static final int INT_PEOPLE = 136;
        static final int INT_PEOPLE_WAVING = 137;
        static final int INT_PEOPLE_HATS = 138;
        static final int INT_POINSETTIAS = 139;
        static final int INT_POSTAGE_STAMP = 140;
        static final int INT_PUMPKIN_1 = 141;
        static final int INT_PUSH_PIN_NOTE_2 = 142;
        static final int INT_PUSH_PIN_NOTE_1 = 143;
        static final int INT_PYRAMIDS = 144;
        static final int INT_PYRAMIDS_ABOVE = 145;
        static final int INT_QUADRANTS = 146;
        static final int INT_RINGS = 147;
        static final int INT_SAFARI = 148;
        static final int INT_SAWTOOTH = 149;
        static final int INT_SAWTOOTH_GRAY = 150;
        static final int INT_SCARED_CAT = 151;
        static final int INT_SEATTLE = 152;
        static final int INT_SHADOWED_SQUARES = 153;
        static final int INT_SHARKS_TEETH = 154;
        static final int INT_SHOREBIRD_TRACKS = 155;
        static final int INT_SKYROCKET = 156;
        static final int INT_SNOWFLAKE_FANCY = 157;
        static final int INT_SNOWFLAKES = 158;
        static final int INT_SOMBRERO = 159;
        static final int INT_SOUTHWEST = 160;
        static final int INT_STARS = 161;
        static final int INT_STARS_TOP = 162;
        static final int INT_STARS_3_D = 163;
        static final int INT_STARS_BLACK = 164;
        static final int INT_STARS_SHADOWED = 165;
        static final int INT_SUN = 166;
        static final int INT_SWIRLIGIG = 167;
        static final int INT_TORN_PAPER = 168;
        static final int INT_TORN_PAPER_BLACK = 169;
        static final int INT_TREES = 170;
        static final int INT_TRIANGLE_PARTY = 171;
        static final int INT_TRIANGLES = 172;
        static final int INT_TRIANGLE_1 = 173;
        static final int INT_TRIANGLE_2 = 174;
        static final int INT_TRIANGLE_CIRCLE_1 = 175;
        static final int INT_TRIANGLE_CIRCLE_2 = 176;
        static final int INT_SHAPES_1 = 177;
        static final int INT_SHAPES_2 = 178;
        static final int INT_TWISTED_LINES_1 = 179;
        static final int INT_TWISTED_LINES_2 = 180;
        static final int INT_VINE = 181;
        static final int INT_WAVELINE = 182;
        static final int INT_WEAVING_ANGLES = 183;
        static final int INT_WEAVING_BRAID = 184;
        static final int INT_WEAVING_RIBBON = 185;
        static final int INT_WEAVING_STRIPS = 186;
        static final int INT_WHITE_FLOWERS = 187;
        static final int INT_WOODWORK = 188;
        static final int INT_X_ILLUSIONS = 189;
        static final int INT_ZANY_TRIANGLES = 190;
        static final int INT_ZIG_ZAG = 191;
        static final int INT_ZIG_ZAG_STITCH = 192;
        static final int INT_CUSTOM = 193;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("nil", INT_NIL),
            new Enum("none", INT_NONE),
            new Enum("single", INT_SINGLE),
            new Enum("thick", INT_THICK),
            new Enum("double", INT_DOUBLE),
            new Enum("dotted", INT_DOTTED),
            new Enum("dashed", INT_DASHED),
            new Enum("dotDash", INT_DOT_DASH),
            new Enum("dotDotDash", INT_DOT_DOT_DASH),
            new Enum("triple", INT_TRIPLE),
            new Enum("thinThickSmallGap", INT_THIN_THICK_SMALL_GAP),
            new Enum("thickThinSmallGap", INT_THICK_THIN_SMALL_GAP),
            new Enum("thinThickThinSmallGap", INT_THIN_THICK_THIN_SMALL_GAP),
            new Enum("thinThickMediumGap", INT_THIN_THICK_MEDIUM_GAP),
            new Enum("thickThinMediumGap", INT_THICK_THIN_MEDIUM_GAP),
            new Enum("thinThickThinMediumGap", INT_THIN_THICK_THIN_MEDIUM_GAP),
            new Enum("thinThickLargeGap", INT_THIN_THICK_LARGE_GAP),
            new Enum("thickThinLargeGap", INT_THICK_THIN_LARGE_GAP),
            new Enum("thinThickThinLargeGap", INT_THIN_THICK_THIN_LARGE_GAP),
            new Enum("wave", INT_WAVE),
            new Enum("doubleWave", INT_DOUBLE_WAVE),
            new Enum("dashSmallGap", INT_DASH_SMALL_GAP),
            new Enum("dashDotStroked", INT_DASH_DOT_STROKED),
            new Enum("threeDEmboss", INT_THREE_D_EMBOSS),
            new Enum("threeDEngrave", INT_THREE_D_ENGRAVE),
            new Enum("outset", INT_OUTSET),
            new Enum("inset", INT_INSET),
            new Enum("apples", INT_APPLES),
            new Enum("archedScallops", INT_ARCHED_SCALLOPS),
            new Enum("babyPacifier", INT_BABY_PACIFIER),
            new Enum("babyRattle", INT_BABY_RATTLE),
            new Enum("balloons3Colors", INT_BALLOONS_3_COLORS),
            new Enum("balloonsHotAir", INT_BALLOONS_HOT_AIR),
            new Enum("basicBlackDashes", INT_BASIC_BLACK_DASHES),
            new Enum("basicBlackDots", INT_BASIC_BLACK_DOTS),
            new Enum("basicBlackSquares", INT_BASIC_BLACK_SQUARES),
            new Enum("basicThinLines", INT_BASIC_THIN_LINES),
            new Enum("basicWhiteDashes", INT_BASIC_WHITE_DASHES),
            new Enum("basicWhiteDots", INT_BASIC_WHITE_DOTS),
            new Enum("basicWhiteSquares", INT_BASIC_WHITE_SQUARES),
            new Enum("basicWideInline", INT_BASIC_WIDE_INLINE),
            new Enum("basicWideMidline", INT_BASIC_WIDE_MIDLINE),
            new Enum("basicWideOutline", INT_BASIC_WIDE_OUTLINE),
            new Enum("bats", INT_BATS),
            new Enum("birds", INT_BIRDS),
            new Enum("birdsFlight", INT_BIRDS_FLIGHT),
            new Enum("cabins", INT_CABINS),
            new Enum("cakeSlice", INT_CAKE_SLICE),
            new Enum("candyCorn", INT_CANDY_CORN),
            new Enum("celticKnotwork", INT_CELTIC_KNOTWORK),
            new Enum("certificateBanner", INT_CERTIFICATE_BANNER),
            new Enum("chainLink", INT_CHAIN_LINK),
            new Enum("champagneBottle", INT_CHAMPAGNE_BOTTLE),
            new Enum("checkedBarBlack", INT_CHECKED_BAR_BLACK),
            new Enum("checkedBarColor", INT_CHECKED_BAR_COLOR),
            new Enum("checkered", INT_CHECKERED),
            new Enum("christmasTree", INT_CHRISTMAS_TREE),
            new Enum("circlesLines", INT_CIRCLES_LINES),
            new Enum("circlesRectangles", INT_CIRCLES_RECTANGLES),
            new Enum("classicalWave", INT_CLASSICAL_WAVE),
            new Enum("clocks", INT_CLOCKS),
            new Enum("compass", INT_COMPASS),
            new Enum("confetti", INT_CONFETTI),
            new Enum("confettiGrays", INT_CONFETTI_GRAYS),
            new Enum("confettiOutline", INT_CONFETTI_OUTLINE),
            new Enum("confettiStreamers", INT_CONFETTI_STREAMERS),
            new Enum("confettiWhite", INT_CONFETTI_WHITE),
            new Enum("cornerTriangles", INT_CORNER_TRIANGLES),
            new Enum("couponCutoutDashes", INT_COUPON_CUTOUT_DASHES),
            new Enum("couponCutoutDots", INT_COUPON_CUTOUT_DOTS),
            new Enum("crazyMaze", INT_CRAZY_MAZE),
            new Enum("creaturesButterfly", INT_CREATURES_BUTTERFLY),
            new Enum("creaturesFish", INT_CREATURES_FISH),
            new Enum("creaturesInsects", INT_CREATURES_INSECTS),
            new Enum("creaturesLadyBug", INT_CREATURES_LADY_BUG),
            new Enum("crossStitch", INT_CROSS_STITCH),
            new Enum("cup", INT_CUP),
            new Enum("decoArch", INT_DECO_ARCH),
            new Enum("decoArchColor", INT_DECO_ARCH_COLOR),
            new Enum("decoBlocks", INT_DECO_BLOCKS),
            new Enum("diamondsGray", INT_DIAMONDS_GRAY),
            new Enum("doubleD", INT_DOUBLE_D),
            new Enum("doubleDiamonds", INT_DOUBLE_DIAMONDS),
            new Enum("earth1", INT_EARTH_1),
            new Enum("earth2", INT_EARTH_2),
            new Enum("earth3", INT_EARTH_3),
            new Enum("eclipsingSquares1", INT_ECLIPSING_SQUARES_1),
            new Enum("eclipsingSquares2", INT_ECLIPSING_SQUARES_2),
            new Enum("eggsBlack", INT_EGGS_BLACK),
            new Enum("fans", INT_FANS),
            new Enum("film", INT_FILM),
            new Enum("firecrackers", INT_FIRECRACKERS),
            new Enum("flowersBlockPrint", INT_FLOWERS_BLOCK_PRINT),
            new Enum("flowersDaisies", INT_FLOWERS_DAISIES),
            new Enum("flowersModern1", INT_FLOWERS_MODERN_1),
            new Enum("flowersModern2", INT_FLOWERS_MODERN_2),
            new Enum("flowersPansy", INT_FLOWERS_PANSY),
            new Enum("flowersRedRose", INT_FLOWERS_RED_ROSE),
            new Enum("flowersRoses", INT_FLOWERS_ROSES),
            new Enum("flowersTeacup", INT_FLOWERS_TEACUP),
            new Enum("flowersTiny", INT_FLOWERS_TINY),
            new Enum("gems", INT_GEMS),
            new Enum("gingerbreadMan", INT_GINGERBREAD_MAN),
            new Enum("gradient", INT_GRADIENT),
            new Enum("handmade1", INT_HANDMADE_1),
            new Enum("handmade2", INT_HANDMADE_2),
            new Enum("heartBalloon", INT_HEART_BALLOON),
            new Enum("heartGray", INT_HEART_GRAY),
            new Enum("hearts", INT_HEARTS),
            new Enum("heebieJeebies", INT_HEEBIE_JEEBIES),
            new Enum("holly", INT_HOLLY),
            new Enum("houseFunky", INT_HOUSE_FUNKY),
            new Enum("hypnotic", INT_HYPNOTIC),
            new Enum("iceCreamCones", INT_ICE_CREAM_CONES),
            new Enum("lightBulb", INT_LIGHT_BULB),
            new Enum("lightning1", INT_LIGHTNING_1),
            new Enum("lightning2", INT_LIGHTNING_2),
            new Enum("mapPins", INT_MAP_PINS),
            new Enum("mapleLeaf", INT_MAPLE_LEAF),
            new Enum("mapleMuffins", INT_MAPLE_MUFFINS),
            new Enum("marquee", INT_MARQUEE),
            new Enum("marqueeToothed", INT_MARQUEE_TOOTHED),
            new Enum("moons", INT_MOONS),
            new Enum("mosaic", INT_MOSAIC),
            new Enum("musicNotes", INT_MUSIC_NOTES),
            new Enum("northwest", INT_NORTHWEST),
            new Enum("ovals", INT_OVALS),
            new Enum("packages", INT_PACKAGES),
            new Enum("palmsBlack", INT_PALMS_BLACK),
            new Enum("palmsColor", INT_PALMS_COLOR),
            new Enum("paperClips", INT_PAPER_CLIPS),
            new Enum("papyrus", INT_PAPYRUS),
            new Enum("partyFavor", INT_PARTY_FAVOR),
            new Enum("partyGlass", INT_PARTY_GLASS),
            new Enum("pencils", INT_PENCILS),
            new Enum("people", INT_PEOPLE),
            new Enum("peopleWaving", INT_PEOPLE_WAVING),
            new Enum("peopleHats", INT_PEOPLE_HATS),
            new Enum("poinsettias", INT_POINSETTIAS),
            new Enum("postageStamp", INT_POSTAGE_STAMP),
            new Enum("pumpkin1", INT_PUMPKIN_1),
            new Enum("pushPinNote2", INT_PUSH_PIN_NOTE_2),
            new Enum("pushPinNote1", INT_PUSH_PIN_NOTE_1),
            new Enum("pyramids", INT_PYRAMIDS),
            new Enum("pyramidsAbove", INT_PYRAMIDS_ABOVE),
            new Enum("quadrants", INT_QUADRANTS),
            new Enum("rings", INT_RINGS),
            new Enum("safari", INT_SAFARI),
            new Enum("sawtooth", INT_SAWTOOTH),
            new Enum("sawtoothGray", INT_SAWTOOTH_GRAY),
            new Enum("scaredCat", INT_SCARED_CAT),
            new Enum("seattle", INT_SEATTLE),
            new Enum("shadowedSquares", INT_SHADOWED_SQUARES),
            new Enum("sharksTeeth", INT_SHARKS_TEETH),
            new Enum("shorebirdTracks", INT_SHOREBIRD_TRACKS),
            new Enum("skyrocket", INT_SKYROCKET),
            new Enum("snowflakeFancy", INT_SNOWFLAKE_FANCY),
            new Enum("snowflakes", INT_SNOWFLAKES),
            new Enum("sombrero", INT_SOMBRERO),
            new Enum("southwest", INT_SOUTHWEST),
            new Enum("stars", INT_STARS),
            new Enum("starsTop", INT_STARS_TOP),
            new Enum("stars3d", INT_STARS_3_D),
            new Enum("starsBlack", INT_STARS_BLACK),
            new Enum("starsShadowed", INT_STARS_SHADOWED),
            new Enum("sun", INT_SUN),
            new Enum("swirligig", INT_SWIRLIGIG),
            new Enum("tornPaper", INT_TORN_PAPER),
            new Enum("tornPaperBlack", INT_TORN_PAPER_BLACK),
            new Enum("trees", INT_TREES),
            new Enum("triangleParty", INT_TRIANGLE_PARTY),
            new Enum("triangles", INT_TRIANGLES),
            new Enum("triangle1", INT_TRIANGLE_1),
            new Enum("triangle2", INT_TRIANGLE_2),
            new Enum("triangleCircle1", INT_TRIANGLE_CIRCLE_1),
            new Enum("triangleCircle2", INT_TRIANGLE_CIRCLE_2),
            new Enum("shapes1", INT_SHAPES_1),
            new Enum("shapes2", INT_SHAPES_2),
            new Enum("twistedLines1", INT_TWISTED_LINES_1),
            new Enum("twistedLines2", INT_TWISTED_LINES_2),
            new Enum("vine", INT_VINE),
            new Enum("waveline", INT_WAVELINE),
            new Enum("weavingAngles", INT_WEAVING_ANGLES),
            new Enum("weavingBraid", INT_WEAVING_BRAID),
            new Enum("weavingRibbon", INT_WEAVING_RIBBON),
            new Enum("weavingStrips", INT_WEAVING_STRIPS),
            new Enum("whiteFlowers", INT_WHITE_FLOWERS),
            new Enum("woodwork", INT_WOODWORK),
            new Enum("xIllusions", INT_X_ILLUSIONS),
            new Enum("zanyTriangles", INT_ZANY_TRIANGLES),
            new Enum("zigZag", INT_ZIG_ZAG),
            new Enum("zigZagStitch", INT_ZIG_ZAG_STITCH),
            new Enum("custom", INT_CUSTOM),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
