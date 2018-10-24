package com.meterware.httpunit.parsing;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2004,2008 Russell Gold
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
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLDocument;

import java.net.URL;
import java.io.IOException;
import java.io.StringReader;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @author <a href="mailto:bw@xmlizer.biz">Bernhard Wagner</a>
 * @author <a href="mailto:Artashes.Aghajanyan@lycos-europe.com">Artashes Aghajanyan</a>
 **/
class NekoHTMLParser implements HTMLParser {

	/**
	 * parse the given URL with the given pageText using the given document adapter
	 * @param pageURL
	 * @param pageText
	 * @param adapter
	 */
  public void parse( URL pageURL, String pageText, DocumentAdapter adapter ) throws IOException, SAXException {
      try {
          NekoDOMParser parser = NekoDOMParser.newParser( adapter, pageURL );
          parser.parse( new InputSource( new StringReader( pageText ) ) );
          Document doc=parser.getDocument();
         	adapter.setDocument( (HTMLDocument)doc );
      } catch (NekoDOMParser.ScriptException e) {
           throw e.getException();
      }     
  }


    public String getCleanedText( String string ) {
        return (string == null) ? "" : string.replace( NBSP, ' ' );
    }


    public boolean supportsPreserveTagCase() {
        return false;
    }

    public boolean supportsForceTagCase() {
      return false;
    }
    
    public boolean supportsReturnHTMLDocument() {
        return true;
    }


    public boolean supportsParserWarnings() {
        return true;
    }


    final private static char NBSP = (char) 160;   // non-breaking space, defined by nekoHTML
}



