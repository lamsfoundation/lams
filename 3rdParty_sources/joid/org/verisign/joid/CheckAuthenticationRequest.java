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


import java.util.Map;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 * Represents an OpenID check authentication request.
 */
public class CheckAuthenticationRequest extends Request
{
    public final static String OPENID_ASSOC_HANDLE = "openid.assoc_handle";

    private final static Log log = LogFactory.getLog( CheckAuthenticationRequest.class );

    private AuthenticationResponse ar;
    private String handle;


    /**
     * Creates a check_authentication request.
     *
     * TODO: Made public to be accessible from unit tests only. Need
     * to rework that to change access level during test time.
     *
     * @param map the map of incoming openid parameters
     * @param mode always "check_authentication"
     */
    public CheckAuthenticationRequest( Map<String,String> map, Mode mode ) throws OpenIdException
    {
        super( map, mode );
        ar = new AuthenticationResponse( map );
        handle = ar.getAssociationHandle();
        checkInvariants();
    }


    private void checkInvariants() throws OpenIdException
    {
        if ( handle == null )
        {
            throw new OpenIdException( "Missing " + CheckAuthenticationRequest.OPENID_ASSOC_HANDLE );
        }
    }


    public Response processUsing( ServerInfo si ) throws OpenIdException
    {
        String invalidate = null;
        IStore store = si.getStore();
        String nonceStr = ar.getNonce();
        if ( nonceStr != null )
        {
            INonce n = store.findNonce( nonceStr );
            if ( n != null )
            {
                String s = "Nonce has already been checked";
                log.debug( s );
                throw new OpenIdException( s );
            }
            else
            {
                n = store.generateNonce( nonceStr );
                store.saveNonce( n );
            }
        }
        IAssociation assoc = store.findAssociation( handle );
        if ( ( assoc == null ) || ( assoc.hasExpired() ) )
        {
            invalidate = handle;
        }
        Crypto crypto = si.getCrypto();
        return new CheckAuthenticationResponse( ar, assoc,
                           crypto, invalidate );
    }


    public String toString()
    {
        return "[CheckAuthenticationRequest "
            + super.toString()
            + ", handle=" + handle
            + ", authentication response=" + ar
            + "]";
    }


    Map<String,String> toMap()
    {
        // need to send all values exactly from AuthenticationResponse...
        Map<String,String> map = ar.toMap();
        // ... except mode
        map.putAll( super.toMap() );

        return map;
    }
}
