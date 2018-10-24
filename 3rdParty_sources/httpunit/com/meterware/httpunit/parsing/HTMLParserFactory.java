package com.meterware.httpunit.parsing;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2008, Russell Gold
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 *******************************************************************************************************************/
import java.util.Vector;


/**
 * Factory for creating HTML parsers. Parser customization properties can be specified but do not necessarily work
 * for every parser type.
 *
 * @since 1.5.2
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @author <a href="mailto:bw@xmlizer.biz">Bernhard Wagner</a>
 **/
abstract public class HTMLParserFactory {

    private static Vector     _listeners = new Vector();
    private static HTMLParser _jtidyParser;
    private static HTMLParser _nekoParser;

    private static HTMLParser _htmlParser;
    private static boolean    _preserveTagCase;
    private static boolean    _returnHTMLDocument;
    private static boolean    _parserWarningsEnabled;
    // for parsers that support forcing Case
    private static boolean	  _forceUpper;
    private static boolean    _forceLower;    


    /**
     * Resets all settings to their default values. This includes the parser selection.
     */
    public static void reset() {
        _preserveTagCase = false;
        _returnHTMLDocument = true;
        _parserWarningsEnabled = false;
        _htmlParser = null;
        _forceUpper = false;
        _forceLower = false;        
    }


    /**
     * Selects the JTidy parser, if present.
     */
    public static void useJTidyParser() {
        if (_jtidyParser == null) throw new RuntimeException( "JTidy parser not available" );
        _htmlParser = _jtidyParser;
    }


    /**
     * Selects the NekoHTML parser, if present.
     */
    public static void useNekoHTMLParser() {
        if (_nekoParser == null) throw new RuntimeException( "NekoHTML parser not available" );
        _htmlParser = _nekoParser;
    }


    /**
     * Specifies the parser to use.
     */
    public static void setHTMLParser( HTMLParser htmlParser ) {
        _htmlParser = htmlParser;
    }


    /**
     * Returns the current selected parser.
     */
    public static HTMLParser getHTMLParser() {
        if (_htmlParser == null) {
            if (_nekoParser != null) {
                _htmlParser = _nekoParser;
            } else if (_jtidyParser != null) {
                _htmlParser = _jtidyParser;
            } else {
                throw new RuntimeException( "No HTML parser found. Make sure that either nekoHTML.jar or Tidy.jar is in the in classpath" );
            }
        }
        return _htmlParser;
    }


    /**
     * Returns true if the current parser will preserve the case of HTML tags and attributes.
     */
    public static boolean isPreserveTagCase() {
        return _preserveTagCase && getHTMLParser().supportsPreserveTagCase();
    }


    /**
     * Specifies whether the parser should preserve the case of HTML tags and attributes. Not every parser can
     * support this capability.  Note that enabling this will disable support for the HTMLDocument class.
     * override any previous behaviour configured by calling {@link #setForceUpperCase(boolean)} or 
     * {@link #setForceLowerCase(boolean)}
     * @see #setReturnHTMLDocument
     * @see #setForceUpperCase(boolean)
     * @see #setForceLowerCase(boolean)
     */
    public static void setPreserveTagCase( boolean preserveTagCase ) {
        _preserveTagCase = preserveTagCase;
        if (preserveTagCase) {
        	_forceLower = false;
        	_forceUpper = false;
        }	
    }


    /**
     * Returns true if the current parser will return an HTMLDocument object rather than a Document object.
     */
    public static boolean isReturnHTMLDocument() {
        return _returnHTMLDocument && !isPreserveTagCase() && getHTMLParser().supportsReturnHTMLDocument();
    }


    /**
     * Specifies whether the parser should return an HTMLDocument object rather than a Document object.
     * Not every parser can support this capability.  Note that enabling this will disable preservation of tag case.
     * and/or the forcing of the tag case to upper or lower case.
     * @see #setPreserveTagCase
     * @see #setForceUpperCase(boolean)
     * @see #setForceLowerCase(boolean)
     */
    public static void setReturnHTMLDocument( boolean returnHTMLDocument ) {
        _returnHTMLDocument = returnHTMLDocument;
        if (returnHTMLDocument) {
        	_preserveTagCase = false;
         	_forceLower = false;
        	_forceUpper = false;        	
        }
    }

    /**
     * Specifies whether the parser should force the case of HTML tags and attributes to be upper case. Not 
     * every parser can support this capability.  Note that enabling this will disable support for the 
     * HTMLDocument class and override any previous behaviour configured by enabling 
     * {@link #setPreserveTagCase(boolean)} or {@link #setForceLowerCase(boolean)}
     * @see #setReturnHTMLDocument
     * @see #setPreserveTagCase(boolean)
     * @see #setForceLowerCase(boolean)
     * @param forceUpper 
     * 				boolean indicating whether to enable this functionality
     */
    public static void setForceUpperCase(boolean forceUpper) {
        _forceUpper = forceUpper;
        if (_forceUpper) {
            _forceLower = false;
            _preserveTagCase = false;
            // _returnHTMLDocument = false;
        }
    }

    /**
     * Return true if the current parser will support forcing the tags and attributes to upper case
     * @return boolean flag
     */
    public static boolean getForceUpperCase() {
        return _forceUpper && getHTMLParser().supportsPreserveTagCase();
    }


    /**
     * Specifies whether the parser should force the case of HTML tags and attributes to lower case. Not
     * every parser can support this capability.  Note that enabling this will disable support for the 
     * HTMLDocument class and override any previous behaviour configured by enabling 
     * {@link #setPreserveTagCase(boolean)} or {@link #setForceUpperCase(boolean)} 
     * @see #setReturnHTMLDocument
     * @see #setPreserveTagCase(boolean)
     * @see #setForceUpperCase(boolean)
     * @param forceLower
     * 				boolean indicating whether to enable this functionality
     */
    public static void setForceLowerCase(boolean forceLower) {
        _forceLower = forceLower;
        if (_forceLower) {
            _forceUpper = false;
            _preserveTagCase = false;
            // _returnHTMLDocument = false;
        }
    }

    /**
     * Return true if the current parser will support forcing the tags and attributes to lower case
     * @return boolean flag
     */
    public static boolean getForceLowerCase() {
        return _forceLower && getHTMLParser().supportsPreserveTagCase();
    }

    /**
     * Returns true if parser warnings are enabled.
     **/
    public static boolean isParserWarningsEnabled() {
        return _parserWarningsEnabled && getHTMLParser().supportsParserWarnings();
    }


    /**
     * If true, tells the parser to display warning messages. The default is false (warnings are not shown).
     **/
    public static void setParserWarningsEnabled( boolean enabled ) {
        _parserWarningsEnabled = enabled;
    }


    /**
     * Remove an HTML Parser listener.
     **/
    public static void removeHTMLParserListener( HTMLParserListener el ) {
        _listeners.removeElement( el );
    }


    /**
     * Add an HTML Parser listener.
     **/
    public static void addHTMLParserListener( HTMLParserListener el ) {
        _listeners.addElement( el );
    }


//------------------------------------- package protected members ------------------------------------------------------


    /**
     * Get the list of Html Error Listeners
     **/
    static Vector getHTMLParserListeners() {
        return _listeners;
    }


    private static HTMLParser loadParserIfSupported( final String testClassName, final String parserClassName ) {
        try {
            Class.forName( testClassName );
            return (HTMLParser) Class.forName( parserClassName ).newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassNotFoundException e) {
        }
        return null;
    }


    static {
        _jtidyParser = loadParserIfSupported( "org.w3c.tidy.Parser", "com.meterware.httpunit.parsing.JTidyHTMLParser" );
        _nekoParser  = loadParserIfSupported( "org.cyberneko.html.HTMLConfiguration", "com.meterware.httpunit.parsing.NekoHTMLParser" );
        reset();
    }


}
