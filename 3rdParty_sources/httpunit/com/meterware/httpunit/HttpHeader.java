package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2006, Russell Gold
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
import java.util.*;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
public class HttpHeader {

    private String _label;
    private Map    _properties;


    public HttpHeader( String headerString ) {
        this( headerString, null );
    }


    public HttpHeader( String headerString, String defaultLabel ) {
        if (headerString != null) {
            final int index = headerString.indexOf( ' ' );
            if (index < 0) {  // non-conforming header
                _label      = defaultLabel;
                _properties = loadProperties( headerString );
            } else {
                _label      = headerString.substring( 0, index );
                _properties = loadProperties( headerString.substring( index+1 ) );
            }
        }
    }


    public boolean equals( Object obj ) {
        if (!getClass().equals( obj.getClass() ) ) return false;
        return getLabel().equals( ((HttpHeader) obj).getLabel() ) &&
               getProperties().equals( ((HttpHeader) obj).getProperties() );
    }


    public String toString() {
        return getLabel() + " " + getProperties();
    }


    protected String getProperty( String key ) {
        return unQuote( (String) getProperties().get( key ) );
    }


    private String unQuote( String value ) {
        if (value == null || value.length() <= 1 || !value.startsWith( "\"" ) || !value.endsWith( "\"")) return value;

        return value.substring( 1, value.length()-1 );
    }


// Headers have the general format (ignoring unquoted white space):
// header ::= property-def | property-def ',' header
// property-def ::= name '=' value
// name         ::= ID
// value        ::= ID | QUOTED-STRING
//
    static private Map loadProperties( String parameterString ) {
        Properties properties = new Properties();
        char[] chars = parameterString.toCharArray();
        int i = 0;
        StringBuffer sb = new StringBuffer();

        while (i < chars.length) {
            while (i < chars.length && Character.isWhitespace( chars[i] ) ) i++;
            while (i < chars.length && Character.isJavaIdentifierPart( chars[i] ) ) sb.append( chars[i++] );
            String name = sb.toString();
            sb.setLength( 0 );
            while (i < chars.length && chars[i] != '=' ) i++;
            if (i == chars.length) break;
            i++; // skip '='
            while (i < chars.length && Character.isWhitespace( chars[i] ) ) i++;
            if (i == chars.length) break;
            if (chars[i] == '"') {
                sb.append( chars[i++] );
                while (i < chars.length && chars[i] != '"' ) sb.append( chars[i++] );
                sb.append( '"' );
                if (i < chars.length) i++; // skip close quote
            } else {
                while (i < chars.length && Character.isJavaIdentifierPart( chars[i] ) ) sb.append( chars[i++] );
            }
            properties.setProperty( name, sb.toString() );
            sb.setLength( 0 );
            while (i < chars.length && chars[i] != ',' ) i++;
            if (i == chars.length) break;
            i++; // skip '='
        }
        return properties;
    }


    public String getLabel() {
        return _label;
    }


    public Map getProperties() {
        return _properties;
    }
}
