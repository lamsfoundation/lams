package com.meterware.httpunit.protocol;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2007, Russell Gold
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
import com.meterware.httpunit.HttpUnitOptions;

import java.io.OutputStream;
import java.io.IOException;

/**
 * A POST method request message body which uses the default URL encoding.
 **/
class URLEncodedMessageBody extends MessageBody {


    URLEncodedMessageBody( String characterSet ) {
        super( characterSet );
    }


    /**
     * Returns the content type of this message body.
     **/
    public String getContentType() {
        return "application/x-www-form-urlencoded" +
                  (!HttpUnitOptions.isPostIncludesCharset() ? ""
                                                            : "; charset=" + getCharacterSet());
    }


    /**
     * Transmits the body of this request as a sequence of bytes.
     **/
    public void writeTo( OutputStream outputStream, ParameterCollection parameters ) throws IOException {
        outputStream.write( getParameterString( parameters ).getBytes() );
    }


    private String getParameterString( ParameterCollection parameters ) {
        try {
            URLEncodedString encoder = new URLEncodedString();
            parameters.recordParameters( encoder );
            return encoder.getString();
        } catch (IOException e) {
            throw new RuntimeException( "Programming error: " + e );   // should never happen
        }
    }
}
