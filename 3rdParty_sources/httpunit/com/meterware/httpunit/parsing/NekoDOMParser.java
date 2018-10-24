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
import com.meterware.httpunit.scripting.ScriptingHandler;
import com.meterware.httpunit.dom.HTMLDocumentImpl;

import java.net.URL;
import java.io.IOException;
import java.util.Enumeration;

import org.cyberneko.html.HTMLConfiguration;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.XNIException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * 
 *
 * @author <a href="russgold@httpunit.org">Russell Gold</a>
 * @author <a href="mailto:Artashes.Aghajanyan@lycos-europe.com">Artashes Aghajanyan</a>
 **/
class NekoDOMParser extends org.apache.xerces.parsers.DOMParser implements ScriptHandler {

    // private static final String HTML_DOCUMENT_CLASS_NAME = "org.apache.html.dom.HTMLDocumentImpl";

    /** Error reporting feature identifier. */
    private static final String REPORT_ERRORS = "http://cyberneko.org/html/features/report-errors";

    /** Augmentations feature identifier. */
    private static final String AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";

    /** Filters property identifier. */
    private static final String FILTERS = "http://cyberneko.org/html/properties/filters";

    /** Element case settings. possible values: "upper", "lower", "match" */
    private static final String TAG_NAME_CASE = "http://cyberneko.org/html/properties/names/elems";

    /** Attribute case settings. possible values: "upper", "lower", "no-change" */
    private static final String ATTRIBUTE_NAME_CASE = "http://cyberneko.org/html/properties/names/attrs";

    private DocumentAdapter _documentAdapter;


    /**
     * construct a new NekoDomParser with the given adapter and url
     * @param adapter
     * @param url
     * @return - the new parser
     * patch [ 1211154 ] NekoDOMParser default to lowercase by Dan Allen
     * patch [ 1176688 ] Allow configuration of neko parser properties by James Abley
     */
    static NekoDOMParser newParser( DocumentAdapter adapter, URL url ) {
        final HTMLConfiguration configuration = new HTMLConfiguration();
        if (!HTMLParserFactory.getHTMLParserListeners().isEmpty() || HTMLParserFactory.isParserWarningsEnabled()) {
            configuration.setErrorHandler( new ErrorHandler( url ) );
            configuration.setFeature( REPORT_ERRORS, true);
        }
        configuration.setFeature( AUGMENTATIONS, true );
        final ScriptFilter javaScriptFilter = new ScriptFilter( configuration );
        configuration.setProperty( FILTERS, new XMLDocumentFilter[] { javaScriptFilter } );
        if (HTMLParserFactory.isPreserveTagCase()) {
          configuration.setProperty( TAG_NAME_CASE, "match" );
          configuration.setProperty( ATTRIBUTE_NAME_CASE, "no-change" );
        } else {
        	configuration.setProperty( TAG_NAME_CASE, "lower" );
        	configuration.setProperty( ATTRIBUTE_NAME_CASE, "lower" );
        	
        	if (HTMLParserFactory.getForceUpperCase()) {
        		configuration.setProperty(TAG_NAME_CASE, "upper");
        		configuration.setProperty(ATTRIBUTE_NAME_CASE, "upper");
        	}
        	// this is the default as of patch [ 1211154 ] ... just for people who rely on patch [ 1176688 ]
        	if (HTMLParserFactory.getForceLowerCase()) {
        		configuration.setProperty(TAG_NAME_CASE, "lower");
        		configuration.setProperty(ATTRIBUTE_NAME_CASE, "lower");
        	}
        }	

        try {
            final NekoDOMParser domParser = new NekoDOMParser( configuration, adapter );
            domParser.setFeature( DEFER_NODE_EXPANSION, false );
            if (HTMLParserFactory.isReturnHTMLDocument()) domParser.setProperty( DOCUMENT_CLASS_NAME, HTMLDocumentImpl.class.getName() );
            javaScriptFilter.setScriptHandler( domParser );
            return domParser;
        } catch (SAXNotRecognizedException e) {
            throw new RuntimeException( e.toString() );
        } catch (SAXNotSupportedException e) {
            throw new RuntimeException( e.toString() );
        }

    }


    private Element getCurrentElement() {
        try {
            return (Element) getProperty( CURRENT_ELEMENT_NODE );
        } catch (SAXNotRecognizedException e) {
            throw new RuntimeException( CURRENT_ELEMENT_NODE + " property not recognized" );
        } catch (SAXNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException( CURRENT_ELEMENT_NODE + " property not supported" );
        }
    }


    NekoDOMParser( HTMLConfiguration configuration, DocumentAdapter adapter ) {
        super( configuration );
        _documentAdapter = adapter;
    }


    public String getIncludedScript( String srcAttribute ) {
        try {
            return _documentAdapter.getIncludedScript( srcAttribute );
        } catch (IOException e) {
            throw new ScriptException( e );
        }
    }


    public boolean supportsScriptLanguage( String language ) {
        return getScriptingHandler().supportsScriptLanguage( language );
    }


    public String runScript( final String language, final String scriptText ) {
        getScriptingHandler().clearCaches();
        return getScriptingHandler().runScript( language, scriptText );
    }


    private ScriptingHandler getScriptingHandler() {
        _documentAdapter.setDocument( (HTMLDocument) getCurrentElement().getOwnerDocument() );
        return _documentAdapter.getScriptingHandler();
    }


    static class ScriptException extends RuntimeException {
        private IOException _cause;

        public ScriptException( IOException cause ) {
            _cause = cause;
        }

        public IOException getException() {
            return _cause;
        }
    }
}


class ErrorHandler implements XMLErrorHandler {

    private URL _url = null;

    ErrorHandler( URL url ) {
        _url = url;
    }

    public void warning( String domain, String key, XMLParseException warningException ) throws XNIException {
        if (HTMLParserFactory.isParserWarningsEnabled()) {
            System.out.println( "At line " + warningException.getLineNumber() + ", column " + warningException.getColumnNumber() + ": " + warningException.getMessage() );
        }

        Enumeration listeners = HTMLParserFactory.getHTMLParserListeners().elements();
        while (listeners.hasMoreElements()) {
            ((HTMLParserListener) listeners.nextElement()).warning( _url, warningException.getMessage(), warningException.getLineNumber(), warningException.getColumnNumber() );
        }
    }


    public void error( String domain, String key, XMLParseException errorException ) throws XNIException {
        Enumeration listeners = HTMLParserFactory.getHTMLParserListeners().elements();
        while (listeners.hasMoreElements()) {
            ((HTMLParserListener) listeners.nextElement()).error( _url, errorException.getMessage(), errorException.getLineNumber(), errorException.getColumnNumber() );
        }
    }


    public void fatalError( String domain, String key, XMLParseException fatalError ) throws XNIException {
        error( domain, key, fatalError );
        throw fatalError;
    }
}