package com.meterware.httpunit.cookies;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Iterator;
import java.util.TimeZone;
import java.net.URL;


/**
 * An HTTP client-side cookie.
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class Cookie {

    private String _name;

    private String _value;

    private String _path;

    private String _domain;
    
    private long _expiredTime;


    /**
     * @return the _expiredTime in milliseconds
     */
    public long getExpiredTime() {
        return _expiredTime;
    }


    /**
     * DateFormat to be used to format original Netscape cookies
     */
    private final static DateFormat originalCookieFormat = 
    	 new SimpleDateFormat("EEE,dd-MMM-yyyy HH:mm:ss z", Locale.US);
    
    static {
      originalCookieFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * Constructs a cookie w/o any domain or path restrictions.
     * @param name - the name of the cookie
     * @param value - the value of the cookie
     */
    Cookie( String name, String value ) {
        _name = name;
        _value = value;
    }
    
    /**
     * construct a cookie with domain and path restrictions
     * @param name
     * @param value
     * @param domain
     * @param path
     */
    Cookie( String name, String value, String domain, String path) {
    	this(name, value);
     	_path = path;
     	_domain = domain;
    }

    /**
     * Constructs a cookie w/o any domain or path restrictions.
     * @param name - the name of the cookie
     * @param value - the value of the cookie
     * @param attributes - a map of attributes for the cookie
     */
    Cookie( String name, String value, Map attributes ) {
        this( name, value );
        for (Iterator iterator = attributes.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            String attributeValue = (String) attributes.get( key );
            if (key.equalsIgnoreCase( "path" )) {
              _path = attributeValue;
            } else if (key.equalsIgnoreCase( "domain" )) {
              _domain = attributeValue;
            } else if (key.equalsIgnoreCase( "max-age" )) {
              _expiredTime = System.currentTimeMillis() + getAgeInMsec( attributeValue );
            } else if (key.equalsIgnoreCase( "expires" )) {
            	_expiredTime = getAgeInMsecFromDate( attributeValue );
            }            
        }
    }


    /**
     * get the age of the cookie in Milliseconds from a string representaiton in seconds
     * @param maxAgeValue - the string with the age in seconds
     * @return - the integer millisecond value or 0 if conversion fails
     */
    private int getAgeInMsec( String maxAgeValue ) {
        try {
            return 1000 * Integer.parseInt( maxAgeValue );
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * return the age of a cookie in milliesconds from a string formatted date value
     * @param dateValue - the string to parse
     * @return - milliseconds as integer or 0 if parsing fails
     */
    private long getAgeInMsecFromDate( String dateValue ) {
      try {
       	// SimpleDateFormat isn't thread-safe
        synchronized(originalCookieFormat) {
        	long age=originalCookieFormat.parse( dateValue ).getTime();
        	return age;
        }
    	} catch (ParseException e) {
    		return 0;
    	}
    }


    /**
     * Returns the name of this cookie.
     */
    public String getName() {
        return _name;
    }


    /**
     * Returns the value associated with this cookie.
     */
    public String getValue() {
        return _value;
    }

    /**
     * Sets the value associated with this cookie.
     */
    public void setValue(String value) {
      _value = value;
    }    

    /**
     * Returns the path to which this cookie is restricted.
     */
    public String getPath() {
        return _path;
    }


    /**
     * Returns the domain to which this cookie may be sent.
     */
    public String getDomain() {
        return _domain;
    }


    void setPath( String path ) {
        _path = path;
    }


    void setDomain( String domain ) {
        _domain = domain;
    }


    public int hashCode() {
        int hashCode = _name.hashCode();
        if (_domain != null) hashCode ^= _domain.hashCode();
        if (_path != null) hashCode ^= _path.hashCode();
        return hashCode;
    }


    public boolean equals( Object obj ) {
        return obj.getClass() == getClass() && equals( (Cookie) obj );
    }


    private boolean equals( Cookie other ) {
        return _name.equalsIgnoreCase( other._name ) &&
                equalProperties( getDomain(), other.getDomain() ) &&
                equalProperties( getPath(), other.getPath() );
    }


    private boolean equalProperties( String first, String second ) {
        return first == second || (first != null && first.equals( second ));
    }

    /**
     * check whether the cookie is expired
     * @return true if the _expiredTime is higher than the current System time
     */
    public boolean isExpired() {
    	boolean expired=_expiredTime != 0 && _expiredTime <= System.currentTimeMillis();
    	return expired;
    }
    
    /**
     * may this cookie be sent to the given url?
     * @param url - the unform resource locator to check
     * @return true if the cookie is not expired and the path is accepted if a domain is set
     */
    public boolean mayBeSentTo( URL url ) {
        if (getDomain() == null) return true;
        if (isExpired()) return false;

        return acceptHost( getDomain(), url.getHost() ) && acceptPath( getPath(), url.getPath() );
    }


    private boolean acceptPath( String pathPattern, String hostPath ) {
        return !CookieProperties.isPathMatchingStrict() || hostPath.startsWith( pathPattern );
    }

    /**
     * accept host if the given hostName fits to the given hostPattern
     * @param hostPattern
     * @param hostName
     * @return true if there is a fit
     */
    private static boolean acceptHost( String hostPattern, String hostName ) {
        return hostPattern.equalsIgnoreCase( hostName ) ||
               (hostPattern.startsWith( "." ) && hostName.endsWith( hostPattern ));
    }


}
