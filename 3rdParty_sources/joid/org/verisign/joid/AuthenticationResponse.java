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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.tsik.datatypes.DateTime;


/**
 * Represents an OpenID authentication response.
 */
public class AuthenticationResponse extends Response
{
    private static Log log = LogFactory.getLog( AuthenticationResponse.class );

    public static String OPENID_PREFIX = "openid.";
    public static String OPENID_RETURN_TO = "openid.return_to";
    public static String OPENID_OP_ENDPOINT = "openid.op_endpoint";
    public static String OPENID_IDENTITY = "openid.identity";
    public static String OPENID_ERROR = "openid.error";
    public static String OPENID_NONCE = "openid.response_nonce";
    public static String OPENID_INVALIDATE_HANDLE = "openid.invalidate_handle";
    public static String OPENID_ASSOCIATION_HANDLE = "openid.assoc_handle";
    public static String OPENID_SIGNED = "openid.signed";
    // package scope so that ResponseFactory can trigger on this key
    public static String OPENID_SIG = "openid.sig";

    private Map<String,String> extendedMap;

    private String claimed_id;
    private String identity;
    private String returnTo;
    private String nonce;
    private String invalidateHandle;
    private String associationHandle;
    private String signed;
    private AssociationType algo;
    private String signature;
    private SimpleRegistration sreg;
    private String urlEndPoint;
    private byte[] key;


    /** 
     * Returns the signature in this response.
     * @return the signature in this response.
     */
    public String getSignature()
    {
        return signature;
    }


    /** 
     * Returns the list of signed elements in this response.
     * @return the comma-separated list of signed elements in this response.
     */
    public String getSignedList()
    {
        return signed;
    }


    /** 
     * Returns the association handle in this response.
     * @return the association handle in this response.
     */
    public String getAssociationHandle()
    {
        return associationHandle;
    }


    /**
     * Returns the internal elements mapped to a map. The keys used
     * are those defined by the specification, for example 
     * <code>openid.mode</code>.
     *
     * TODO: Made public only for unit tests. Needs to package-scope
     * limit this method.
     *
     * @return a map with all internal values mapped to their specification
     * keys.
     */
    public Map<String,String> toMap()
    {
        Map<String,String> map = super.toMap();

        if ( isVersion2() )
        {
            map.put( AuthenticationResponse.OPENID_OP_ENDPOINT, urlEndPoint );
        }
        map.put( OpenIdConstants.OPENID_MODE, getMode().getValue() );
        map.put( AuthenticationResponse.OPENID_IDENTITY, identity );
        map.put( AuthenticationResponse.OPENID_RETURN_TO, returnTo );
        map.put( AuthenticationResponse.OPENID_NONCE, nonce );
        
        if ( claimed_id != null )
        {
            map.put( AuthenticationRequest.OPENID_CLAIMED_ID, claimed_id );
        }
        
        if ( invalidateHandle != null )
        {
            map.put( AuthenticationResponse.OPENID_INVALIDATE_HANDLE,
                invalidateHandle );
        }
        map.put( AuthenticationResponse.OPENID_ASSOCIATION_HANDLE, associationHandle );
        
        if ( signed != null )
        {
            map.put( AuthenticationResponse.OPENID_SIGNED, signed );
        }
        map.put( AuthenticationResponse.OPENID_SIG, signature );

        Map<String,String> sregMap = sreg.getSuppliedValues();
        log.debug( "sreg in authnresp = " + sreg );
        Set<Map.Entry<String, String>> set = sregMap.entrySet();
        for ( Iterator<Map.Entry<String, String>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String> mapEntry = iter.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            map.put( SimpleRegistration.OPENID_SREG + "." + key, value );
        }
        if ( !set.isEmpty() && isVersion2() )
        {
            map.put( OpenIdConstants.OPENID_NS + ".sreg", sreg.getNamespace() );
        }

        if ( extendedMap != null && !extendedMap.isEmpty() )
        {
            set = extendedMap.entrySet();
            for ( Iterator<Map.Entry<String,String>> iter = set.iterator(); iter.hasNext(); )
            {
                Map.Entry<String,String> mapEntry = iter.next();
                String key = ( String ) mapEntry.getKey();
                String value = ( String ) mapEntry.getValue();
                map.put( OPENID_PREFIX + key, value );
            }
        }

        return map;
    }


    private String generateNonce()
    {
        String crumb = Crypto.generateCrumb();
        return DateTime.formatISODateTime( new Date() ) + crumb;
    }


    /**
     * Unrolls this response as a string. This string will use encoding
     * suitable for URLs. The string will use the same namespace as the
     * incoming request.
     *
     * @param req the original request.
     * @param e any exception that occured while processing <code>req</code>,
     * may be null.
     *
     * @return the response as a string.
     */
    public static String toUrlStringResponse( Request req, OpenIdException e )
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put( OpenIdConstants.OPENID_MODE, "error" );
        if ( req != null )
        {
            if ( req.isVersion2() )
            {
                map.put( OpenIdConstants.OPENID_NS, req.getNamespace() );
            }
            map.put( AuthenticationResponse.OPENID_ERROR, e.getMessage() );
        }
        else
        {
            map.put( AuthenticationResponse.OPENID_ERROR, "OpenID request error" );
        }
        try
        {
            return new AuthenticationResponse( map ).toUrlString();
        }
        catch ( OpenIdException ex )
        {
            // this should never happen
            log.error( ex );
            return "internal error";
        }
    }


    /**
     * Only public for unit tests. Do not use.
     */
    public String sign( byte[] key, String signed ) throws OpenIdException
    {
        return sign( this.algo, key, signed );
    }


    /**
     * Signs the elements designated by the signed list with the given key and
     * returns the result encoded to a string.
     *
     * @param algorithm the algorithm to use (HMAC-SHA1, HMAC-SHA256)
     * @param key the key to sign with (HMAC-SHA1, HMAC-SHA256)
     * @param signed the comma-separated list of elements to sign. The elements
     * must be mapped internally.
     * @return the Base 64 encoded result.
     * @throws OpenIdException at signature errors, or if the signed list 
     * points to elements that are not mapped.
     */
    public String sign( AssociationType algorithm, byte[] key, String signed ) throws OpenIdException
    {
        Map<String,String> map = toMap();
        log.debug( "in sign() map=" + map );
        log.debug( "in sign() signed=" + signed );
        StringTokenizer st = new StringTokenizer( signed, "," );
        StringBuffer sb = new StringBuffer();
        while ( st.hasMoreTokens() )
        {
            String s = st.nextToken();
            String name = "openid." + s;
            String value = ( String ) map.get( name );
            if ( value == null )
            {
                throw new OpenIdException( "Cannot sign non-existent mapping: "
                      + s );
            }
            sb.append( s );
            sb.append( ':' );
            sb.append( value );
            sb.append( '\n' );
        }
        try
        {
            byte[] b;
            if ( algorithm == null )
            {
                algorithm = AssociationType.HMAC_SHA1;
            }
            if ( algorithm.equals( AssociationType.HMAC_SHA1 ) )
            {
                b = Crypto.hmacSha1( key, sb.toString().getBytes( "UTF-8" ) );
            }
            else if ( algorithm.equals( AssociationType.HMAC_SHA256 ) )
            {
                b = Crypto.hmacSha256( key, sb.toString().getBytes( "UTF-8" ) );
            }
            else
            {
                throw new OpenIdException( "Unknown signature algorithm" );
            }
            return Crypto.convertToString( b );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new OpenIdException( e );
        }
        catch ( InvalidKeyException e )
        {
            throw new OpenIdException( e );
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new OpenIdException( e );
        }
    }


    /**
     * throws at errors in signature creation
     */
    AuthenticationResponse( ServerInfo serverInfo,
               AuthenticationRequest ar,
               IAssociation a, Crypto crypto,
               String invalidateHandle )
        throws OpenIdException
    {
        super( null );
        setMode( Mode.ID_RES );
        claimed_id = ar.getClaimedIdentity();
        identity = ar.getIdentity();
        returnTo = ar.getReturnTo();
        setNamespace( ar.getNamespace() );
        nonce = generateNonce();
        this.urlEndPoint = serverInfo.getUrlEndPoint();
        this.invalidateHandle = invalidateHandle; //may be null
        associationHandle = a.getHandle();
        signed = "assoc_handle,identity,response_nonce,return_to";
        if ( claimed_id != null )
        {
            signed += ",claimed_id";
        }
        if ( isVersion2() )
        {
            signed += ",op_endpoint";
        }
        sreg = ar.getSimpleRegistration();
        log.debug( "sreg=" + sreg );
        if ( sreg != null )
        {
            Map<String,String> map = sreg.getSuppliedValues();
            log.debug( "sreg supplied values=" + map );
            Set<Map.Entry<String,String>> set = map.entrySet();
            if ( !set.isEmpty() && isVersion2() )
            {
                signed += ",ns.sreg";
            }
            for ( Iterator<Map.Entry<String,String>> iter = set.iterator(); iter.hasNext(); )
            {
                Map.Entry<String,String> mapEntry = iter.next();
                String key = mapEntry.getKey();
                signed += ",sreg." + key;
            }
        }
        key = a.getMacKey();
        this.algo = a.getAssociationType();
        signature = sign( key, signed );
        extendedMap = new HashMap<String,String>();
    }


    public AuthenticationResponse( Map<String,String> map ) throws OpenIdException
    {
        super( map );
        Set<Map.Entry<String,String>> set = map.entrySet();
        extendedMap = new HashMap<String,String>();
        for ( Iterator<Map.Entry<String,String>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String> mapEntry = iter.next();
            String key = ( String ) mapEntry.getKey();
            String value = ( String ) mapEntry.getValue();

            if ( OpenIdConstants.OPENID_MODE.equals( key ) )
            {
                setMode( Mode.parse( value ) );
            }
            else if ( AuthenticationResponse.OPENID_IDENTITY.equals( key ) )
            {
                identity = value;
            }
            else if ( AuthenticationRequest.OPENID_CLAIMED_ID.equals( key ) )
            {
                claimed_id = value;
            }
            else if ( AuthenticationResponse.OPENID_RETURN_TO.equals( key ) )
            {
                returnTo = value;
            }
            else if ( OPENID_NONCE.equals( key ) )
            {
                nonce = value;
            }
            else if ( OPENID_INVALIDATE_HANDLE.equals( key ) )
            {
                invalidateHandle = value;
            }
            else if ( OPENID_ASSOCIATION_HANDLE.equals( key ) )
            {
                associationHandle = value;
            }
            else if ( OPENID_SIGNED.equals( key ) )
            {
                signed = value;
            }
            else if ( OPENID_SIG.equals( key ) )
            {
                signature = value;
            }
            else if ( OPENID_OP_ENDPOINT.equals( key ) )
            {
                urlEndPoint = value;
                // we get op_endpoint without a 2.0 ns for some
                // check_auth requests
                // since we use this class to recalculate the
                // signature we need to set version 2 explicitly or we
                // won't include the op_endpoint in the map
                // (op_endpoint isn't allowed in 1.x responses)
                if ( getNamespace() == null )
                {
                    setNamespace( OpenIdConstants.OPENID_20_NAMESPACE );
                }
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
        this.sreg = SimpleRegistration.parseFromResponse( map );
        log.debug( "authn resp constr sreg=" + sreg );
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
    public void addExtensions( Map<String,String> map ) throws OpenIdException
    {
        Iterator<Map.Entry<String,String>> it = map.entrySet().iterator();
        while ( it.hasNext() )
        {
            Map.Entry<String,String> mapEntry = it.next();
            String key = ( String ) mapEntry.getKey();
            String value = ( String ) mapEntry.getValue();
            extendedMap.put( key, value );
            // add items to signature
            // signed should already contain a list of base params to sign
            signed += "," + key;
        }
        // recalculate signature
        signature = sign( key, signed );
    }


    /**
     * Add extension object's parameters to the extensions map.
     */
    public void addExtension( Extension ext ) throws OpenIdException
    {
        addExtensions( ext.getParamMap() );
    }


    public String toString()
    {
        String s = "[AuthenticationResponse "
            + super.toString();
        if ( sreg != null )
        {
            s += ", sreg=" + sreg;
        }
        s += ", mode=" + getMode()
            + ", algo=" + algo
            + ", nonce=" + nonce
            + ", association handle=" + associationHandle
            + ", invalidation handle=" + invalidateHandle
            + ", signed=" + signed
            + ", signature=" + signature
            + ", identity=" + identity
            + ", return to=" + returnTo
            + "]";
        return s;
    }


    public String getClaimedId()
    {
        return claimed_id;
    }


    public String getIdentity()
    {
        return identity;
    }


    public String getReturnTo()
    {
        return returnTo;
    }


    public String getNonce()
    {
        return nonce;
    }


    public String getInvalidateHandle()
    {
        return invalidateHandle;
    }


    public String getSigned()
    {
        return signed;
    }


    public AssociationType getAlgo()
    {
        return algo;
    }


    public SimpleRegistration getSreg()
    {
        return sreg;
    }


    public String getUrlEndPoint()
    {
        return urlEndPoint;
    }
}
