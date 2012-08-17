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

package org.verisign.joid;


import org.verisign.joid.extension.Extension;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 * Represents an OpenID authentication request.
 */
public class AuthenticationRequest extends Request
{
    private final static Log log = LogFactory.getLog( AuthenticationRequest.class );

    private Map<String,String> extendedMap;

    private String claimed_id;
    private String identity;
    private String handle;
    private String returnTo;
    private String trustRoot;
    private SimpleRegistration sreg;

    public final static String OPENID_CLAIMED_ID = "openid.claimed_id";
    public final static String OPENID_IDENTITY = "openid.identity";
    public final static String OPENID_ASSOC_HANDLE = "openid.assoc_handle";

    public final static String ID_SELECT = "http://specs.openid.net/auth/2.0/identifier_select";

    public final static String OPENID_RETURN_TO = "openid.return_to";
    /**
     * trust_root is the 1.x equivalent to trust_realm in 2.x
     */
    public final static String OPENID_TRUST_ROOT = "openid.trust_root";
    public final static String OPENID_REALM = "openid.realm";

    public static String OPENID_DH_CONSUMER_PUBLIC = "openid.dh_consumer_public";

    public static String OPENID_SESSION_TYPE = "openid.session_type";
    public final static String DH_SHA1 = "DH-SHA1";
    private static Map<String,String> statelessMap = new HashMap<String,String>();
    private static AssociationRequest statelessAr;

    static
    {
        statelessMap.put( AuthenticationRequest.OPENID_SESSION_TYPE,
                AuthenticationRequest.DH_SHA1 );
        // this value is not used for state-less, but it's not a valid
        // association request unless it's there
        //
        statelessMap.put( AuthenticationRequest.OPENID_DH_CONSUMER_PUBLIC,
                Crypto.convertToString( BigInteger.valueOf( 1 ) ) );
        try
        {
            // the request mode is irrelevant
            //
            statelessAr = new AssociationRequest( statelessMap, Mode.ASSOCIATE );
        }
        catch ( OpenIdException e )
        {
            // should not happen
            //
            throw new RuntimeException( e );
        }
    }


    /**
     * Creates a standard authentication request.
     *
     * @param identity    the openid identity.
     * @param returnTo    the return_to value.
     * @param trustRoot   the openid trust_root.
     * @param assocHandle the openid association handle.
     * @return an AuthenticationRequest.
     * @throws OpenIdException if the request cannot be created.
     */
    public static AuthenticationRequest create( String identity, String returnTo,
                                                String trustRoot, String assocHandle )
            throws OpenIdException
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put( "openid.mode", Mode.CHECKID_SETUP.toString() );
        map.put( OPENID_IDENTITY, identity );
        map.put( OPENID_CLAIMED_ID, identity );
        map.put( OPENID_RETURN_TO, returnTo );
        map.put( OPENID_TRUST_ROOT, trustRoot );
        map.put( OPENID_REALM, trustRoot );
        map.put( OpenIdConstants.OPENID_NS, OpenIdConstants.OPENID_20_NAMESPACE );
        map.put( OPENID_ASSOC_HANDLE, assocHandle );
        return new AuthenticationRequest( map, Mode.CHECKID_SETUP );
    }


    AuthenticationRequest( Map<String,String> map, Mode mode ) throws OpenIdException
    {
        super( map, mode );
        Set<Map.Entry<String, String>> set = map.entrySet();
        extendedMap = new HashMap<String,String>();
        for ( Iterator<Map.Entry<String, String>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String> mapEntry = iter.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();

            if ( OpenIdConstants.OPENID_NS.equals( key ) )
            {
                setNamespace( value );
            }
            else if ( OPENID_IDENTITY.equals( key ) )
            {
                this.identity = value;
            }
            else if ( OPENID_CLAIMED_ID.equals( key ) )
            {
                this.claimed_id = value;
            }
            else if ( OPENID_ASSOC_HANDLE.equals( key ) )
            {
                this.handle = value;
            }
            else if ( OPENID_RETURN_TO.equals( key ) )
            {
                this.returnTo = value;
            }
            else if ( OPENID_TRUST_ROOT.equals( key )
                    || OPENID_REALM.equals( key ) )
            {
                this.trustRoot = value;
            }
            else if ( key != null && key.startsWith( "openid." ) )
            {
                String foo = key.substring( 7 ); // remove "openid."
                if ( ( !( OpenIdConstants.OPENID_RESERVED_WORDS.contains( foo ) ) )
                        && ( !foo.startsWith( "sreg." ) ) )
                {
                    extendedMap.put( foo, value );
                }
            }
        }
        this.sreg = new SimpleRegistration( map );
        checkInvariants();
    }


    // public for unit tests
    public Map<String,String> toMap()
    {
        Map<String,String> map = super.toMap();

        if ( claimed_id != null )
        {
            map.put( AuthenticationRequest.OPENID_CLAIMED_ID, claimed_id );
        }
        map.put( AuthenticationRequest.OPENID_IDENTITY, identity );
        if ( handle != null )
        {
            map.put( AuthenticationRequest.OPENID_ASSOC_HANDLE, handle );
        }
        map.put( AuthenticationRequest.OPENID_RETURN_TO, returnTo );
        map.put( AuthenticationRequest.OPENID_TRUST_ROOT, trustRoot );
        map.put( AuthenticationRequest.OPENID_REALM, trustRoot );

        if ( extendedMap != null && !extendedMap.isEmpty() )
        {
            for ( Iterator<Map.Entry<String, String>> iter = extendedMap.entrySet().iterator(); iter.hasNext(); )
            {
                Map.Entry<String,String> mapEntry = iter.next();
                String key = mapEntry.getKey();
                String value = mapEntry.getValue();
                if ( value == null )
                {
                    continue;
                }
                // all keys start "openid." in the set
                map.put( "openid." + key, value );
            }
        }

        return map;
    }


    /**
     * Returns whether this request is immediate, that is, whether the
     * authentication mode is "CHECKID_IMMEDIATE".
     *
     * @return true if this request is immediate; false otherwise.
     */
    public boolean isImmediate()
    {
        return Mode.CHECKID_IMMEDIATE == getMode();
    }


    private void checkInvariants() throws OpenIdException
    {
        if ( getMode() == null )
        {
            throw new OpenIdException( "Missing mode" );
        }
        if ( identity == null )
        {
            throw new OpenIdException( "Missing identity" );
        }
        if ( claimed_id != null && !this.isVersion2() )
        {
            throw new OpenIdException( "claimed_id not valid in version 1.x" );
        }
        if ( trustRoot == null )
        {
            if ( returnTo != null )
            {
                trustRoot = returnTo;
            }
            else
            {
                throw new OpenIdException( "Missing trust root" );
            }
        }

        checkTrustRoot();

        Set<String> namespaces = new HashSet<String>();
        Set<String> entries = new HashSet<String>();
        Set<Map.Entry<String, String>> set = extendedMap.entrySet();
        for ( Iterator<Map.Entry<String, String>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String> mapEntry = iter.next();
            String key = mapEntry.getKey();
            // all keys start "openid." in the set

            if ( key.startsWith( "ns." ) )
            {
                key = key.substring( 3 );
                if ( OpenIdConstants.OPENID_RESERVED_WORDS.contains( key ) )
                {
                    throw new OpenIdException( "Cannot redefine: " + key );
                }
                if ( namespaces.contains( key ) )
                {
                    throw new OpenIdException( "Multiple definitions: " + key );
                }
                namespaces.add( key );
            }
            else
            {
                if ( entries.contains( key ) )
                {
                    throw new OpenIdException( "Multiple definitions: " + key );
                }
                entries.add( key );
            }
        }
        // don't check for invalid parameters on 1.x requests; just
        // silently ignore them
        if ( this.isVersion2() )
        {
            for ( Iterator<String> iter = entries.iterator(); iter.hasNext(); )
            {
                String key = iter.next();
                int period = key.indexOf( '.' );
                if ( period != -1 )
                {
                    key = key.substring( 0, period );
                }
                if ( !namespaces.contains( key ) )
                {
                    throw new OpenIdException( "No such namespace: " + key );
                }
            }
        }
    }


    private void checkTrustRoot() throws OpenIdException
    {
        if ( trustRoot == null )
        {
            throw new OpenIdException( "No " + OPENID_TRUST_ROOT + " given" );
        }

        // URI fragments are not allowed in trustroot
        //
        if ( trustRoot.indexOf( '#' ) > 0 )
        {
            throw new OpenIdException( "URI fragments are not allowed" );
        }

        // Matched if:
        // 1. trustroot and returnto are identical
        // 2. trustroot contains wild-card characters "*.", and the
        // trailing part of the returnto's domain is identical to the
        // part of the trustroot following the "*." wildcard
        //
        // Trust root           Return to
        // ----------           ---------
        // example.com      =>  example.com      ==> ok
        // *.example.com    =>  example.com      ==> ok
        // *.example.com    =>  a.example.com    ==> ok
        // www.example.com  =>  a.example.com    ==> not ok
        //
        URL r, t;
        try
        {
            r = new URL( returnTo );
            t = new URL( trustRoot );
        }
        catch ( MalformedURLException e )
        {
            throw new OpenIdException( "Malformed URL" );
        }

        String tHost = new StringBuffer( t.getHost() ).reverse().toString();
        String rHost = new StringBuffer( r.getHost() ).reverse().toString();

        String[] tNames = tHost.split( "\\." );
        String[] rNames = rHost.split( "\\." );
        int len = ( tNames.length > rNames.length )
                ? rNames.length : tNames.length;

        int i;
        for ( i = 0; i < len; i += 1 )
        {
            if ( !( tNames[i].equals( rNames[i] ) )
                    && ( !tNames[i].equals( "*" ) ) )
            {
                throw new OpenIdException( "returnTo not in trustroot set: " +
                        tNames[i] + ", " + rNames[i] );
            }
        }
        if ( ( i < tNames.length ) && ( !tNames[i].equals( "*" ) ) )
        {
            throw new OpenIdException( "returnTo not in trustroot set: " +
                    tNames[1] );
        }

        // The return to path is equal to or a sub-directory of the
        // realm's (trustroot's) path.
        //
        // Trust root     Return to
        // ----------     ---------
        // /a/b/c     =>  /a/b/c/d    ==> ok
        // /a/b/c     =>  /a/b        ==> not ok
        // /a/b/c     =>  /a/b/b      ==> not ok
        //

        String tPath = t.getPath();
        String rPath = r.getPath();

        int n = rPath.indexOf( tPath );
        if ( n != 0 )
        {
            throw new OpenIdException( "return to & trust root paths mismatch" );
        }

        // if we're here, we're good to go!
    }


    public Response processUsing( ServerInfo si ) throws OpenIdException
    {
        IStore store = si.getStore();
        Crypto crypto = si.getCrypto();
        IAssociation assoc = null;
        String invalidate = null;
        if ( handle != null )
        {
            assoc = store.findAssociation( handle );
            if ( assoc != null && assoc.hasExpired() )
            {
                log.info( "Association handle has expired: " + handle );
                assoc = null;
            }
        }
        if ( handle == null || assoc == null )
        {
            log.info( "Invalidating association handle: " + handle );
            invalidate = handle;
            assoc = store.generateAssociation( statelessAr, crypto );
            store.saveAssociation( assoc );
        }
        return new AuthenticationResponse( si, this, assoc, crypto, invalidate );
    }


    /**
     * Returns the identity used in this authentication request.
     *
     * @return the identity.
     */
    public String getIdentity()
    {
        return identity;
    }


    /**
     * Returns the extensions in this authentication request.
     *
     * @return the extensions; empty if none.
     */
    public Map<String,String> getExtensions()
    {
        return extendedMap;
    }


    /**
     * Add the extension map to the internal extensions map.
     * 
     * @param map Map<String, String> of name value pairs
     */
    public void addExtensions( Map<String,String> map )
    {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry<String,String> mapEntry = it.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            extendedMap.put( key, value );
        }
    }


    /**
     * Add extension object's parameters to the extensions map.
     */
    public void addExtension( Extension ext )
    {
        addExtensions( ext.getParamMap() );
    }


    /**
     * Returns whether the given identity equals {@link #ID_SELECT}.
     *
     * @return true if the identity equals {@link #ID_SELECT}.
     */
    public boolean isIdentifierSelect()
    {
        return AuthenticationRequest.ID_SELECT.equals( identity );
    }


    /**
     * Returns the claimed identity used in this authentication request.
     *
     * @return the claimed identity.
     */
    public String getClaimedIdentity()
    {
        return claimed_id;
    }


    /**
     * Sets the identity used in this authentication request.
     *
     * @param identity the identity.
     */
    public void setIdentity( String identity )
    {
        this.identity = identity;
    }


    /**
     * Returns the 'return to' address in this authentication request.
     *
     * @return the address.
     */
    public String getReturnTo()
    {
        return returnTo;
    }


    /**
     * Returns the handle used in this authentication request.
     *
     * @return the handle
     */
    public String getHandle()
    {
        return handle;
    }


    /**
     * Returns the trust root address in this authentication request.
     *
     * @return the address.
     */
    public String getTrustRoot()
    {
        return trustRoot;
    }


    /**
     * Returns the simple registration fields in this authentication request.
     *
     * @return the sreg fields; or null if none present.
     */
    public SimpleRegistration getSimpleRegistration()
    {
        return sreg;
    }


    /**
     * Sets the simple registration fields in this authentication request.
     *
     * @param sreg the registration fields.
     */
    public void setSimpleRegistration( SimpleRegistration sreg )
    {
        this.sreg = sreg;
    }


    public String toString()
    {
        return "[AuthenticationRequest "
                + super.toString()
                + ", sreg=" + sreg
                + ", claimed identity=" + claimed_id
                + ", identity=" + identity
                + ", handle=" + handle + ", return to=" + returnTo
                + ", trust root=" + trustRoot + "]";
    }

}
