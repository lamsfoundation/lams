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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Provider Authentication Policy Extension request message. See the
 * <a href="http://openid.net/specs/openid-provider-authentication-policy-extension-1_0.html">specification</a>.
 * <p>
 * Example of parsing incoming requests:
 * <pre>
 *     AuthenticationRequest ar = (AuthenticationRequest) req;
 *     PapeRequest pr = new PapeRequest(ar.getExtensions());
 *     if (pr.isValid()) {
 *         ...
 *     }
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * Example of inserting PAPE request to an outgoing request:
 * <pre>
 * AuthenticationRequest ar = AuthenticationRequest.create(identity,
 *                                                         returnTo,
 *                                                         trustRoot,
 *                                                         assocHandle);
 * PapeRequest pr = new PapeRequest();
 * pr.setMaxAuthAge(3600);
 * ar.addExtension(pr);
 * </pre>
 * </p>
 */
public class PapeRequest extends Extension implements PapeConstants
{
    private static final long serialVersionUID = 5360473914710207818L;

    /** 
     * PAPE request parameter: If the End User has not actively
     * authenticated to the OP within the number of seconds specified
     * in a manner fitting the requested policies, the OP SHOULD
     * authenticate the End User for this request.  Value: Integer
     * value greater than or equal to zero in seconds, and is
     * optional.
     */
    static String MAX_AUTH_AGE = "max_auth_age";
    /**
     * PAPE request parameter: Zero or more authentication policy URIs
     * that the OP SHOULD conform to when authenticating the user. If
     * multiple policies are requested, the OP SHOULD satisfy as many
     * as it can.
     */
    static String PREFERRED_AUTH_POLICIES = "preferred_auth_policies";
    /**
     * The name space for the custom Assurance Level. Assurance levels
     * and their name spaces are defined by various parties, such as
     * country or industry specific standards bodies, or other groups
     * or individuals.
     */
    static String AUTH_LEVEL_NS = "auth_level.ns.";
    /**
     * A list of the name space aliases for the custom Assurance Level
     * name spaces that the RP requests be present in the response, in
     * the order of its preference.
     */
    static String PREFERRED_AUTH_LEVELS = "preferred_auth_level_types";


    /**
     * Construct <code>PapeRequest</code> object with the correct
     * namespace and an empty set of attributes.
     * <code>preferred_auth_policies</code> is initialized to an empty
     * string.
     */
    public PapeRequest()
    {
        super( PAPE_NAMESPACE, PAPE_IDENTIFIER );
        // PREFERRED_AUTH_POLICIES is a mandatory parameter, default
        // to none
        setParam( PREFERRED_AUTH_POLICIES, "" );
    }


    /**
     * Construct <code>PapeRequest</code> object using the given
     * parameter mappings.  Get the <code>extensionMap</code>
     * parameter from
     * <code>AuthenticationRequest.getExtensions</code>.
     *
     * @param extensionMap a <code>Map<String, String></code> containing the parameter mappings
     */
    public PapeRequest( Map<String,String> extensionMap )
    {
        super( PAPE_NAMESPACE, extensionMap );
    }


    /**
     * Retrieve the <code>max_auth_age</code> parameter.
     *
     * @return the maximum authentication age as an <code>Integer</code> value
     * @exception OpenIdException if the parameter didn't parse to an integer
     * @see #MAX_AUTH_AGE
     */
    public Integer getMaxAuthAge() throws OpenIdException
    {
        return getIntParam( MAX_AUTH_AGE );
    }


    /**
     * Set the <code>max_auth_age</code> parameter with the given
     * value.
     *
     * @param age maximum authentication age as an <code>int</code> value
     * @see #MAX_AUTH_AGE
     */
    public void setMaxAuthAge( int age )
    {
        setMaxAuthAge( new Integer( age ) );
    }


    /**
     * Set the <code>max_auth_age</code> parameter with the given
     * value.  If <code>null</code> is specified as the value, the
     * parameter will be removed.
     *
     * @param age maximum authentication age as an <code>Integer</code> value
     * @see #MAX_AUTH_AGE
     */
    public void setMaxAuthAge( Integer age )
    {
        if ( age == null )
        {
            // max_auth_age is optional, remove it if set to null
            clearParam( MAX_AUTH_AGE );
        }
        else
        {
            Integer minVal = new Integer( 0 );
            if ( age.compareTo( minVal ) < 0 )
            {
                setIntParam( MAX_AUTH_AGE, minVal );
            }
            else
            {
                setIntParam( MAX_AUTH_AGE, age );
            }
        }
    }


    /**
     * Retrieve the <code>preferred_auth_policies</code> parameter
     * values.
     *
     * @return preferred authentication policies as a <code>Set<String></code> value
     * @see #PREFERRED_AUTH_POLICIES
     */
    public Set<String> getPreferredAuthPolicies()
    {
        return getSetParam( PREFERRED_AUTH_POLICIES, " " );
    }


    /**
     * Set the <code>preferred_auth_policies</code> parameter with the
     * given array of policy URI strings.  Duplicate URIs will be
     * discarded.
     *
     * @param policies a set of policy URIs as a <code>String[]</code> value
     * @see #PREFERRED_AUTH_POLICIES
     */
    public void setPreferredAuthPolicies( String[] policies )
    {
        setPreferredAuthPolicies( new LinkedHashSet<String>( Arrays.asList( policies ) ) );
    }


    /**
     * Set the <code>preferred_auth_policies</code> parameter with the
     * given set of policy URI strings.
     *
     * @param policies a set of policy URIs as a <code>Set<String></code> value
     * @see #PREFERRED_AUTH_POLICIES
     */
    public void setPreferredAuthPolicies( Set<String> policies )
    {
        setListParam( PREFERRED_AUTH_POLICIES, policies, " " );
    }


    /**
     * Retrieve the <code>auth_level_ns</code> namespaces and values.
     *
     * @return namespaces as a <code>Map<String, String></code>
     * @see #AUTH_LEVEL_NS
     */
    Map<String, String> getAuthLevelNS()
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
    void setAuthLevelNS( Map<String,String> authLevels )
    {
        Iterator<String> iter = authLevels.keySet().iterator();
        while ( iter.hasNext() )
        {
            String key = iter.next();
            addAuthLevelNS( key, ( String ) authLevels.get( key ) );
        }
    }


    /**
     * Add an <code>auth_level_ns</code> namespace.
     *
     * @param namespace the name of the namespace
     * @param value the value of the namespace's URL identifier
     * @see #AUTH_LEVEL_NS
     */
    void addAuthLevelNS( String namespace, String value )
    {
        setParam( AUTH_LEVEL_NS + namespace, value );
    }


    /**
     * Retrieve the <code>preferred_auth_levels</code> parameter
     * values.
     *
     * @return preferred authentication levels as a <code>List<String></code> value
     * @see #PREFERRED_AUTH_LEVELS
     */
    List<String> getPreferredAuthLevelNSs()
    {
        return getListParam( PREFERRED_AUTH_LEVELS, " " );
    }


    /**
     * Set the <code>preferred_auth_levels</code> parameter with the
     * given array of policy URI strings.  Duplicate URIs will be
     * discarded.
     *
     * @param levels a set of policy URIs as a <code>String[]</code> value
     * @see #PREFERRED_AUTH_LEVELS
     */
    void setPreferredAuthLevelNSs( String[] levels )
    {
        setPreferredAuthLevels( new ArrayList<String>( Arrays.asList( levels ) ) );
    }


    /**
     * Set the <code>preferred_auth_levels</code> parameter with the
     * given set of policy URI strings.
     *
     * @param levels a list of policy namespaces ordered by preference as a <code>List<String></code> value
     * @see #PREFERRED_AUTH_LEVELS
     */
    void setPreferredAuthLevelNSs( List<String> levels )
    {
        setListParam( PREFERRED_AUTH_LEVELS, levels, " " );
    }


    /**
     * Retrieve the <code>preferred_auth_levels</code> parameter
     * values, translated to the values in
     * <code>auth_level.ns.*</code>.
     *
     * @return preferred authentication levels as a <code>List<String></code> value
     * @see #PREFERRED_AUTH_LEVELS
     * @see #AUTH_LEVEL_NS
     */
    public List<String> getPreferredAuthLevels()
    {
        Iterator<String> it = getPreferredAuthLevelNSs().iterator();
        List<String> levels = new ArrayList<String>();
        while ( it.hasNext() )
        {
            levels.add( getAuthLevelNS( ( String ) it.next() ) );
        }
        return levels;
    }


    /**
     * Set the <code>preferred_auth_levels</code> and
     * <code>auth_level.ns.*</code> parameter with the given array of
     * policy URI strings.  Duplicate URIs will be discarded.
     *
     * @param levels a set of policy URIs as a <code>String[]</code> value
     * @see #PREFERRED_AUTH_LEVELS
     * @see #AUTH_LEVEL_NS
     */
    public void setPreferredAuthLevels( String[] levels )
    {
        setPreferredAuthLevels( new ArrayList<String>( Arrays.asList( levels ) ) );
    }


    /**
     * Set the <code>preferred_auth_levels</code> and
     * <code>auth_level.ns.*</code> parameter with the given ordered
     * list of policy URI strings.
     *
     * @param levels a list of policy namespaces ordered by preference as a <code>List<String></code> value
     * @see #PREFERRED_AUTH_LEVELS
     * @see #AUTH_LEVEL_NS
     */
    public void setPreferredAuthLevels( List<String> levels )
    {
        int i = 0;
        List<String> prefNS = new ArrayList<String>();
        Iterator<String> it = levels.iterator();
        while ( it.hasNext() )
        {
            String level = it.next();
            String ns = "ns" + Integer.toString( i++, 16 );
            addAuthLevelNS( ns, level );
            prefNS.add( ns );
        }
        setPreferredAuthLevelNSs( prefNS );
    }
}
