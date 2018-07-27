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
import org.xml.sax.SAXException;

import java.net.URL;
import java.io.IOException;

/**
 * A front end to a DOM parser that can handle HTML.
 *
 * @since 1.5.2
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @author <a href="mailto:bw@xmlizer.biz">Bernhard Wagner</a>
 **/
public interface HTMLParser {

    /**
     * Parses the specified text string as a Document, registering it in the HTMLPage.
     * Any error reporting will be annotated with the specified URL.
     */
    public void parse( URL baseURL, String pageText, DocumentAdapter adapter ) throws IOException, SAXException;


    /**
     * Removes any string artifacts placed in the text by the parser. For example, a parser may choose to encode
     * an HTML entity as a special character. This method should convert that character to normal text.
     */
    public String getCleanedText( String string );


    /**
     * Returns true if this parser supports preservation of the case of tag and attribute names.
     */
    public boolean supportsPreserveTagCase();

    /**
     * Returns true if this parser supports forcing  the upper/lower case of tag and attribute names.
     */
    public boolean supportsForceTagCase();
    
    /**
     * Returns true if this parser can return an HTMLDocument object.
     */
    public boolean supportsReturnHTMLDocument();


    /**
     * Returns true if this parser can display parser warnings.
     */
    public boolean supportsParserWarnings();
}
