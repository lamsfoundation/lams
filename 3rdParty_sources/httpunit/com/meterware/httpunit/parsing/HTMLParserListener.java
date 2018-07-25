package com.meterware.httpunit.parsing;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002, Russell Gold
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
import java.net.URL;

/**
 * A listener for messages from the HTMLParser. This provides a mechanism to watch for errors and warnings generated
 * during parsing.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @author <a href="mailto:bx@bigfoot.com">Benoit Xhenseval</a>
 **/
public interface HTMLParserListener {

    /**
     * Invoked when the parser wishes to report a warning.
     * @param url    the location of the document to which the warning applies.
     * @param msg    the warning message
     * @param line   the line in the document on which the problematic HTML was found
     * @param column the column in the document on which the problematic HTML was found
     */
    void warning( URL url, String msg, int line, int column );


    /**
     * Invoked when the parser wishes to report an error.
     * @param url    the location of the document to which the error applies.
     * @param msg    the warning message
     * @param line   the line in the document on which the problematic HTML was found
     * @param column the column in the document on which the problematic HTML was found
     */
    void error( URL url, String msg, int line, int column );
}
