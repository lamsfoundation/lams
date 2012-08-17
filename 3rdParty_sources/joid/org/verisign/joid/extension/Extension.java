//
// (C) Copyright 2007 VeriSign, Inc.  All Rights Reserved.
//
// VeriSign, Inc. shall have no responsibility, financial or
// otherwise, for any consequences arising out of the use of
// this material. The program material is provided on an "AS IS"
// BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied.
//
// Distributed under an Apache License
// http://www.apache.org/licenses/LICENSE-2.0
//

package org.verisign.joid.extension;


import org.verisign.joid.OpenIdException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;


/**
 * Base class for OpenID extension information classes, with utility
 * functions for getting and setting different parameter types.
 */
public class Extension implements Serializable
{
    private static final long serialVersionUID = 2222867117628997071L;

    /**
     * RFC3339 date format
     */
    private static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    /**
     * Namespace for this extension (OpenID 2.0)
     */
    protected String ns;
    /**
     * Preferred alias prefix for the extension parameters.
     */
    protected String prefix;
    /**
     * The list of parameters, in a <code>Map<String, String></code> object.
     */
    protected Map<String,String> params;


    /**
     * Create an extension object with the namespace and prefix.  This
     * constructor is intended to be used to construct extension
     * objects "from scratch" without parsing an existing message
     * object.
     *
     * @param ns the extension namespace as a <code>String</code> value
     * @param prefix the extension's prefix alias as a <code>String</code> value
     */
    public Extension( String ns, String prefix )
    {
        this.ns = ns;
        this.prefix = prefix;
        params = null;
    }


    /**
     * Create an extension object with the namespace and prefix.  This
     * constructor is intended to be used to construct extension
     * objects from parsing an existing message object.
     *
     * @param ns the extension namespace as a <code>String</code> value
     * @param extensionMap extension parameters in <code>Map<String, String></code> form
     */
    public Extension( String ns, Map<String,String> extensionMap )
    {
        this.ns = ns;
        getParams( extensionMap );
    }


    /**
     * Extract parameters for the extension from the given OpenID
     * parameter map based on the extension's namespace.
     *
     * @param extensionMap extension parameters in <code>Map<String, String></code> form
     */
    public void getParams( Map<String,String> extensionMap )
    {
        params = null;
        String prefix = getPrefix( extensionMap );
        if ( prefix == null )
        {
            return;
        }
        this.prefix = prefix;
        params = new HashMap<String,String>();
        Iterator<Map.Entry<String,String>> it = extensionMap.entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry<String,String> mapEntry = it.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            if ( key.startsWith( prefix ) )
            {
                params.put( key.substring( prefix.length() + 1 ), value );
            }
        }
        if ( params.isEmpty() )
        {
            params = null;
        }
    }


    /**
     * Return whether the extension object has any parameters assigned
     * to it.
     *
     * @return <code>true</code> if a parameter map exists
     */
    public boolean isValid()
    {
        return params != null;
    }


    /**
     * Returns whether there are any valid extension parameters in the
     * given parameter map.
     *
     * @param extensionMap extension parameters in <code>Map<String, String></code> form
     * @return <code>true</code> if there are parameters for this extension in the map
     */
    public boolean isValid( Map<String,String> extensionMap )
    {
        return getPrefix( extensionMap ) != null;
    }


    /**
     * Extract the extension's alias from the map.
     *
     * @param extensionMap a <code>Map</code> value
     * @return alias <code>String</code>
     */
    public String getPrefix( Map<String,String> extensionMap )
    {
        Iterator<Map.Entry<String,String>> it = extensionMap.entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry<String,String> mapEntry = it.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            if ( key.startsWith( "ns." ) && value.equals( ns ) )
            {
                return key.substring( 3 );
            }
        }
        return null;
    }


    /**
     * Remove extension parameter.
     *
     * @param name Parameter name
     */
    public void clearParam( String name )
    {
        if ( params != null )
        {
            params.remove( name );
        }
    }


    /**
     * Get <code>String</code> extension parameter value.
     *
     * @param name Parameter name
     * @return value <code>String</code>
     */
    public String getParam( String name )
    {
        return ( String ) params.get( name );
    }


    /**
     * Set extension parameter's <code>String</code> value.
     *
     * @param name Parameter name
     * @param value value <code>String</code>
     */
    public void setParam( String name, String value )
    {
        if ( params == null )
        {
            params = new HashMap<String,String>();
        }
        params.put( name, value );
    }


    /**
     * Get <code>Integer</code> extension parameter value.
     *
     * @param name Parameter name
     * @return value <code>Integer</code>
     * @exception OpenIdException if the parameter was not a valid Integer
     */
    public Integer getIntParam( String name ) throws OpenIdException
    {
        try
        {
            String paramStr = getParam( name );
            if ( paramStr != null )
            {
                return new Integer( paramStr );
            }
            return null;
        }
        catch ( NumberFormatException e )
        {
            // invalid parameter value, throw a protocol error
            throw new OpenIdException( "Invalid " + name + " parameter", e );
        }
    }


    /**
     * Set extension parameter's <code>Integer</code> value.
     *
     * @param name Parameter name
     * @param value value <code>Integer</code>
     */
    public void setIntParam( String name, Integer value )
    {
        setParam( name, value == null ? null : value.toString() );
    }


    /**
     * Get <code>Date</code> extension parameter value.
     *
     * @param name Parameter name
     * @return value <code>Date</code>
     * @exception OpenIdException if the parameter was not a valid Date
     */
    public Date getDateParam( String name ) throws OpenIdException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT );
        dateFormat.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
        try
        {
            String paramStr = getParam( name );
            if ( paramStr != null )
            {
                return dateFormat.parse( paramStr.toUpperCase() );
            }
            return null;
        }
        catch ( ParseException e )
        {
            // invalid parameter value, throw a protocol error
            throw new OpenIdException( "Invalid " + name + " parameter", e );
        }
    }


    /**
     * Set extension parameter's <code>Date</code> value.
     *
     * @param name Parameter name
     * @param value value <code>Date</code>
     */
    public void setDateParam( String name, Date value )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT );
        dateFormat.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
        setParam( name, dateFormat.format( value ) );
    }


    /**
     * Get extension parameter's <code>List</code> value.
     *
     * @param name Parameter name
     * @param separator Separation <code>String</code> between list values
     * @return value <code>List</code> of <code>String</code>s
     */
    public List<String> getListParam( String name, String separator )
    {
        List<String> paramList = new ArrayList<String>();
        String paramStr = getParam( name );
        if ( paramStr != null )
        {
            String[] paramArray = paramStr.split( separator );
            for ( int i = 0; i < paramArray.length; i++ )
            {
                paramList.add( paramArray[i] );
            }
        }
        return paramList;
    }


    /**
     * Get extension parameter's <code>Set</code> value.
     *
     * @param name Parameter name
     * @param separator Separation <code>String</code> between set values
     * @return value <code>Set</code> of <code>String</code>s
     */
    public Set<String> getSetParam( String name, String separator )
    {
        return new LinkedHashSet<String>( getListParam( name, separator ) );
    }


    /**
     * Set an extension parameter with multiple values.
     *
     * @param name Parameter name
     * @param paramList a <code>Collection</code> of <code>String</code> values
     * @param separator Separation <code>String</code> between list values
     */
    public void setListParam( String name, Collection<String> paramList, String separator )
    {
        StringBuffer paramStr = new StringBuffer( "" );
        Iterator<String> it = paramList.iterator();
        while ( it.hasNext() )
        {
            String param = it.next();
            paramStr.append( param );
            if ( it.hasNext() )
            {
                paramStr.append( separator );
            }
        }
        setParam( name, paramStr.toString() );
    }


    /**
     * Returns a map containing all the extension parameter name value
     * pairs.  All parameter names are prefixed by the specified
     * namespace alias.  In addition, the namespace alias is defined in a
     * name value pair of the form:
     * <code>ns.<namespace alias>=<namespace URI></code>
     *
     * @param nsSuffix the namespace alias for this extension
     * @return a <code>Map<String, String></code> containing all extension parameters
     */
    public Map<String,String> getParamMap( String nsSuffix )
    {
        if ( ( nsSuffix == null ) || ( nsSuffix.length() == 0 ) )
        {
            throw new IllegalArgumentException( "Missing namespace alias for " + ns );
        }

        Map<String,String> map = new HashMap<String,String>();
        // add all non empty params with the openid.ns prefix
        Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry<String,String> mapEntry = it.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            if ( value != null )
            {
                map.put( nsSuffix + "." + key, value );
            }
        }
        map.put( "ns." + nsSuffix, ns );
        return map;
    }


    /**
     * Returns a map containing all the extension parameters with the
     * namespace alias set to the current value.
     *
     * @return a <code>Map<String, String></code> containing all extension parameters
     * @see #getParamMap(String)
     */
    public Map<String,String> getParamMap()
    {
        return getParamMap( prefix );
    }


    /**
     * Returns a simple string representation of the object.
     *
     * @return a <code>String</code> containing the extension properties
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer( "" );
        sb.append( "[Extension " ).append( ns ).append( ", " );
        sb.append( "prefix=" ).append( prefix ).append( ", " );
        if ( params == null )
        {
            sb.append( "No extension params" );
        }
        else
        {
            Iterator<String> it = params.keySet().iterator();
            while ( it.hasNext() )
            {
                String key = it.next();
                sb.append( key ).append( "='" ).append( ( String ) params.get( key ) ).append( "'" );
                if ( it.hasNext() )
                {
                    sb.append( ", " );
                }
            }
        }
        sb.append( "]" );
        return sb.toString();
    }
}
