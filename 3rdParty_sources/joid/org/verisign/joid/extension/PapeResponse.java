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

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Provider Authentication Policy Extension response message. See the
 * <a href="http://openid.net/specs/openid-provider-authentication-policy-extension-1_0.html">specification</a>.
 * <p>
 * Example of parsing incoming responses:
 * <pre>
 * Response resp = ResponseFactory.parse(s);
 * if (resp instanceof AuthenticationResponse) {
 *     AuthenticationResponse ar = (AuthenticationResponse) resp;
 *     PapeResponse pr = new PapeResponse(ar.getExtensions());
 *     if (pr.isValid()) {
 *         ...
 *     }
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * Example of inserting PAPE response to an outgoing reponse:
 * <pre>
 * Response resp = request.processUsing(serverInfo);
 * if (resp instanceof AuthenticationResponse) {
 *     AuthenticationResponse ar = (AuthenticationResponse)resp;
 *     PapeResponse pr = new PapeResponse();
 *     pr.setAuthAge(3600);
 *     pr.setAuthPolicies(new String[] 
 *         { "http://schemas.openid.net/pape/policies/2007/06/phishing-resistant",
 *           "http://schemas.openid.net/pape/policies/2007/06/multi-factor",
 *           "http://schemas.openid.net/pape/policies/2007/06/multi-factor-physical" });
 *     pr.setNistAuthLevel(4);
 *     ar.addExtension(pr);
 * }
 * </pre>
 * </p>
 */
public class PapeResponse extends Extension implements PapeConstants
{
    private static final long serialVersionUID = -4854806350672731345L;

    /**
     * PAPE response parameter: One or more authentication policy URIs
     * that the OP conformed to when authenticating the End User.  If
     * no policies were met though the OP wishes to convey other
     * information in the response, this parameter MUST be included
     * with the value of "none".
     */
    static String AUTH_POLICIES = "auth_policies";
    static String EMPTY_AUTH_POLICIES = "http://schemas.openid.net/pape/policies/2007/06/none";
    /**
     * PAPE response parameter: The most recent timestamp when the End
     * User has actively authenticated to the OP in a manner fitting
     * the asserted policies.  If the RP's request included the
     * "openid.max_auth_age" parameter then the OP MUST include
     * "openid.auth_time" in its response. If "openid.max_auth_age"
     * was not requested, the OP MAY choose to include
     * "openid.auth_time" in its response.
     */
    static String AUTH_TIME = "auth_time";
    /**
     * The name space for the custom Assurance Level defined by
     * various parties, such as a country or industry specific
     * standards body, or other groups or individuals.
     */
    static String AUTH_LEVEL_NS = "auth_level.ns.";
    /**
     * The Assurance Level as defined by the above standards body,
     * group, or individual that corresponds to the authentication
     * method and policies employed by the OP when authenticating the
     * End User.
     */
    static String AUTH_LEVEL_ASSURANCE = "auth_level.";


    /**
     * Creates a new <code>PapeResponse</code> instance with the
     * correct namespace and an empty set of
     * attributes. <code>auth_policies</code> is initialized to the
     * empty value.
     */
    public PapeResponse()
    {
        super( PAPE_NAMESPACE, PAPE_IDENTIFIER );
        // AUTH_POLICIES is a mandatory parameter
        setParam( AUTH_POLICIES, EMPTY_AUTH_POLICIES );
    }


    /**
     * Creates a new <code>PapeResponse</code> instance using the
     * given parameter mappings.  Get the <code>extensionMap</code>
     * parameter from
     * <code>AuthenticationResponse.getExtensions()</code>
     *
     * @param extensionMap a <code>Map<String, String></code> containing the parameter mappings
     */
    public PapeResponse( Map<String,String> extensionMap )
    {
        super( PAPE_NAMESPACE, extensionMap );
    }


    /**
     * Retrieve the <code>auth_time</code> parameter.
     *
     * @return the authentication age as a <code>Date</code> value
     * @exception OpenIdException if the parameter didn't parse to a Date
     * @see #AUTH_TIME
     */
    public Date getAuthTime() throws OpenIdException
    {
        return getDateParam( AUTH_TIME );
    }


    /**
     * Set the <code>auth_time</code> parameter with the given value.
     *
     * @param age authentication age in seconds as an <code>int</code> value
     * @see #AUTH_TIME
     */
    public void setAuthTime( int age )
    {
        Date now = new Date();
        long time = now.getTime();
        time -= age * 1000; // decrement time by age seconds
        setAuthTime( new Date( time ) );
    }


    /**
     * Set the <code>auth_time</code> parameter with the given value.
     * If <code>null</code> is specified as the value, the parameter
     * will be removed.  Remember to include auth_time in the response
     * if it was in the request.
     *
     * @param age authentication age as a <code>Date</code> value
     * @see #AUTH_TIME
     */
    public void setAuthTime( Date authTime )
    {
        if ( authTime == null )
        {
            // auth_time is optional, remove it if set to null
            clearParam( AUTH_TIME );
        }
        else
        {
            setDateParam( AUTH_TIME, authTime );
        }
    }


    /**
     * Retrieve the <code>auth_policies</code> parameter values.
     *
     * @return authentication policies as a <code>Set<String></code> value
     * @see #AUTH_POLICIES
     */
    public Set<String> getAuthPolicies()
    {
        if ( getParam( AUTH_POLICIES ).equals( EMPTY_AUTH_POLICIES ) )
        {
            return Collections.emptySet();
        }
        return getSetParam( AUTH_POLICIES, " " );
    }


    /**
     * Set the <code>auth_policies</code> parameter with the given
     * array of policy URI strings.  Duplicate URIs will be discarded.
     *
     * @param policies a set of policy URIs as a <code>String[]</code> value
     * @see #AUTH_POLICIES
     */
    public void setAuthPolicies( String[] policies )
    {
        setAuthPolicies( new LinkedHashSet<String>( Arrays.asList( policies ) ) );
    }


    /**
     * Set the <code>auth_policies</code> parameter with the given set
     * of policy URI strings.
     *
     * @param policies a set of policy URIs as a <code>Set<String></code> value
     * @see #AUTH_POLICIES
     */
    public void setAuthPolicies( Set<String> policies )
    {
        if ( policies.isEmpty() )
        {
            setParam( AUTH_POLICIES, EMPTY_AUTH_POLICIES );
        }
        else
        {
            setListParam( AUTH_POLICIES, policies, " " );
        }
    }


    /**
     * Retrieve the <code>auth_level_ns</code> namespaces and values.
     *
     * @return namespaces as a <code>Map<String, String></code>
     * @see #AUTH_LEVEL_NS
     */
    Map<String,String> getAuthLevelNS()
    {
        Map<String,String> map = new HashMap<String, String>();
        Iterator<String> iter = getParamMap().keySet().iterator();
        while ( iter.hasNext() )
        {
            String key = iter.next();
            int i = key.indexOf( AUTH_LEVEL_NS );
            if ( i >= 0 )
            {
                key = key.substring( i );
                map.put( key.substring( AUTH_LEVEL_NS.length() ), getParam( key ) );
            }
        }
        return map;
    }


    /**
     * Retrieve the <code>auth_level_ns</code> namespaces and values.
     *
     * @param namespace name of namespace
     * @return namespace value
     * @see #AUTH_LEVEL_NS
     */
    String getAuthLevelNS( String namespace )
    {
        return getParam( AUTH_LEVEL_NS + namespace );
    }


    /**
     * Set the <code>auth_level_ns</code> namespaces and values.
     *
     * @param authLevels a set of auth level namespaces as a <code>Map<String, String></code>
     * @see #AUTH_LEVEL_NS
     */
    void setAuthLevelNS( Map<String,String> authLevels ) throws OpenIdException
    {
        Iterator<String> iter = authLevels.keySet().iterator();
        while ( iter.hasNext() )
        {
            String key = iter.next();
            addAuthLevelNS( key, authLevels.get( key ) );
        }
    }


    /**
     * Add an <code>auth_level_ns</code> namespace.
     *
     * @param namespace the name of the namespace
     * @param value the value of the namespace's URL identifier
     * @see #AUTH_LEVEL_NS
     */
    void addAuthLevelNS( String namespace, String value ) throws OpenIdException
    {
        if ( "ns".equals( namespace ) )
        {
            throw new OpenIdException( "Invalid " + AUTH_LEVEL_NS + " parameter; \"ns\" not allowed" );
        }
        setParam( AUTH_LEVEL_NS + namespace, value );
    }


    /**
     * Gets the base assurance level value for the given assurance
     * level namespace alias.  The base assurance level parameter is
     * defined in section 5.2 as
     * <code>openid.pape.auth_level.<cust></code>.
     * 
     * @param namespace assurance level URL namespace alias
     * @return string value of the parameter
     * @see #AUTH_LEVEL_ASSURANCE
     */
    String getAuthNSAssuranceLevel( String namespace )
    {
        return getAuthNSAssuranceLevel( namespace, null );
    }


    /**
     * Gets an additional assurance level parameter value for the
     * given assurance level namespace alias.  Additional assurance
     * level parameters are defined in section 5.2 as
     * <code>openid.pape.auth_level.<cust>.<parameter name></code>.
     * 
     * @param namespace assurance level URL namespace alias
     * @param param the parameter name (if null, default to base parameter)
     * @return string value of the parameter
     * @see #AUTH_LEVEL_ASSURANCE
     */
    String getAuthNSAssuranceLevel( String namespace, String param )
    {
        String paramName = new String( AUTH_LEVEL_ASSURANCE + namespace );
        if ( param != null )
        {
            paramName += "." + param;
        }
        return getParam( paramName );
    }


    /**
     * Set the base assurance level value for the given assurance
     * level namespace alias.  The base assurance level parameter is
     * defined in section 5.2 as
     * <code>openid.pape.auth_level.<cust></code>.
     * 
     * @param namespace assurance level URL
     * @param value string defined according to this assurance level
     * @see #AUTH_LEVEL_ASSURANCE
     */
    void setAuthNSAssuranceLevel( String namespace, String value ) throws OpenIdException
    {
        setAuthNSAssuranceLevel( namespace, null, value );
    }


    /**
     * Sets an additional assurance level parameter value for the
     * given assurance level namespace alias.  Additional assurance
     * level parameters are defined in section 5.2 as
     * <code>openid.pape.auth_level.<cust>.<parameter name></code>.
     * 
     * @param namespace assurance level URL
     * @param param the parameter name (if null, default to base parameter)
     * @param value string defined according to this assurance level
     * @see #AUTH_LEVEL_ASSURANCE
     */
    void setAuthNSAssuranceLevel( String namespace, String param, String value ) throws OpenIdException
    {
        if ( "ns".equals( namespace ) )
        {
            throw new OpenIdException( "Invalid " + AUTH_LEVEL_NS + " parameter; \"ns\" not allowed" );
        }
        String paramName = new String( AUTH_LEVEL_ASSURANCE + namespace );
        if ( param != null )
        {
            paramName += "." + param;
        }
        setParam( paramName, value );
    }


    /**
     * Gets the base assurance level value for the given assurance
     * level namespace URL.  The base assurance level parameter is
     * defined in section 5.2 as
     * <code>openid.pape.auth_level.<cust></code>.
     * 
     * @param namespace assurance level URL
     * @return string value of the parameter
     * @see #AUTH_LEVEL_ASSURANCE
     */
    public String getAuthAssuranceLevel( String namespace )
    {
        return getAuthAssuranceLevel( namespace, null );
    }


    /**
     * Gets an additional assurance level parameter value for the
     * given assurance level namespace URL.  Additional assurance
     * level parameters are defined in section 5.2 as
     * <code>openid.pape.auth_level.<cust>.<parameter name></code>.
     * 
     * @param namespace assurance level URL
     * @param param the parameter name (if null, default to base parameter)
     * @return string value of the parameter
     * @see #AUTH_LEVEL_ASSURANCE
     */
    public String getAuthAssuranceLevel( String namespace, String param )
    {
        Map<String, String> nsmap = getAuthLevelNS();
        Iterator<Map.Entry<String, String>> it = nsmap.entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry<String,String> entry = it.next();
            if ( namespace.equals( entry.getValue() ) )
            {
                return getAuthNSAssuranceLevel( entry.getKey(), param );
            }
        }
        return null;
    }


    /**
     * Set the base assurance level value for the given assurance
     * level namespace URL.  The base assurance level parameter is
     * defined in section 5.2 as
     * <code>openid.pape.auth_level.<cust></code>.  This method will
     * define a namespace alias for the URL if one is not already
     * defined.
     * 
     * @param namespace assurance level URL
     * @param value string defined according to this assurance level
     * @see #AUTH_LEVEL_ASSURANCE
     */
    public void setAuthAssuranceLevel( String namespace, String value ) throws OpenIdException
    {
        setAuthAssuranceLevel( namespace, null, value );
    }


    /**
     * Sets an additional assurance level parameter value for the
     * given assurance level namespace URL.  Additional assurance
     * level parameters are defined in section 5.2 as
     * <code>openid.pape.auth_level.<cust>.<parameter name></code>.
     * This method will define a namespace alias for the URL if one is
     * not already defined.
     * 
     * @param namespace assurance level URL
     * @param param the parameter name (if null, default to base parameter)
     * @param value string defined according to this assurance level
     * @see #AUTH_LEVEL_ASSURANCE
     */
    public void setAuthAssuranceLevel( String namespace, String param, String value ) throws OpenIdException
    {
        Map<String, String> nsmap = getAuthLevelNS();
        Iterator<Map.Entry<String, String>> it = nsmap.entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry<String,String> entry = it.next();
            if ( namespace.equals( entry.getValue() ) )
            {
                setAuthNSAssuranceLevel( entry.getKey(), param, value );
                return;
            }
        }
        int i = 0;
        String ns;
        do
        {
            ns = "ns" + Integer.toString( i++, 16 );
        }
        while ( nsmap.keySet().contains( ns ) && ( i < Integer.MAX_VALUE ) );
        if ( i == Integer.MAX_VALUE )
        {
            throw new OpenIdException( "Couldn't find a free namespace" );
        }
        addAuthLevelNS( ns, namespace );
        setAuthNSAssuranceLevel( ns, param, value );
    }


    /**
     * Retrieve a set containing all of the defined assurance level
     * namespace URLs.
     *
     * @return assurance level URLs as a <code>Set<String></code> value
     */
    public Set<String> getAuthAssuranceLevelSet()
    {
        return new LinkedHashSet<String>( getAuthLevelNS().values() );
    }
}
