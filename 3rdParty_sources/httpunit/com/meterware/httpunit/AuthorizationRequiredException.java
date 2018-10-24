package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2002,2006, Russell Gold
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
import java.util.Properties;
import java.util.Map;


/**
 * This exception is thrown when an unauthorized request is made for a page that requires authentication.
 **/
public class AuthorizationRequiredException extends RuntimeException {


    public static AuthorizationRequiredException createBasicAuthenticationRequiredException( String realm ) {
        Properties props = new Properties();
        props.put( "realm", realm );
        return new AuthorizationRequiredException( "Basic", props );
    }


    static AuthorizationRequiredException createException( String scheme, Map properties ) {
        return new AuthorizationRequiredException( scheme, properties );
    }


    private AuthorizationRequiredException( String scheme, Map properties ) {
        _scheme = scheme;
        _properties = properties;
    }


    public String getMessage() {
        return _scheme + " authentication required: " + _properties;
    }


    /**
     * Returns the name of the <a href="http://www.freesoft.org/CIE/RFC/Orig/rfc2617.txt">authentication scheme</a>.
     **/
    public String getAuthenticationScheme() {
        return _scheme;
    }


    /**
     * Returns the named authentication parameter. For Basic authentication, the only parameter is "realm".
     **/
    public String getAuthenticationParameter( String parameterName ) {
        return unQuote( (String) _properties.get( parameterName ) );
    }


    private String unQuote( String value ) {
        if (value == null || value.length() <= 1 || !value.startsWith( "\"" ) || !value.endsWith( "\"")) return value;

        return value.substring( 1, value.length()-1 );
    }

//------------------------------------- private members ------------------------------------------


    private String _scheme;
    private Map    _properties;
}
