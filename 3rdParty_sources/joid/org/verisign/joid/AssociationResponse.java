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


import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Represents an OpenID association response.
 */
public class AssociationResponse extends Response
{
    // package scope so that ResponseFactory can trigger on this key
    static String OPENID_SESSION_TYPE = "session_type";
    static String OPENID_ASSOCIATION_TYPE = "assoc_type";

    private static String OPENID_ASSOC_NS = "ns";
    private static String OPENID_ERROR_CODE = "error_code";
    private static String OPENID_ASSOCIATION_HANDLE = "assoc_handle";
    private static String OPENID_MAC_KEY = "mac_key";
    // package scope so that ResponseFactory can trigger on this key
    static String OPENID_ENC_MAC_KEY = "enc_mac_key";
    private static String OPENID_DH_SERVER_PUBLIC = "dh_server_public";
    private static String OPENID_EXPIRES_IN = "expires_in";

    private SessionType sessionType;
    private AssociationType associationType;
    private String associationHandle;
    private int expiresIn;
    private byte[] macKey;
    private BigInteger dhServerPublic;
    private byte[] encryptedMacKey;
    private String errorCode;


    /** 
     * Returns the error code (if any) occurred while processing this response.
     * @return the error code; null if none.
     */
    public String getErrorCode()
    {
        return errorCode;
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
     * Returns the Diffie-Hellman public server key in this response.
     * @return the Diffie-Hellman public server key in this response.
     */
    public BigInteger getDhServerPublic()
    {
        return dhServerPublic;
    }


    /** 
     * Returns the MAC key in this response. See also 
     * {@link #getEncryptedMacKey()}
     * @return the MAC key in this response; null if none.
     */
    public byte[] getMacKey()
    {
        return macKey;
    }


    /** 
     * Returns the encrypted MAC key in this response. See also
     * {@link #getMacKey()}
     * @return the encrypted MAC key in this response; null if none.
     */
    public byte[] getEncryptedMacKey()
    {
        return encryptedMacKey;
    }


    /** 
     * Returns the static number of seconds this association expires in.
     * @return the number of seconds until expiration.
     */
    public int getExpiresIn()
    {
        return expiresIn;
    }


    /** 
     * Returns the association type in this response.
     * @return the association type in this response.
     */
    public AssociationType getAssociationType()
    {
        return associationType;
    }


    /** 
     * Returns the session type in this response.
     * @return the session type in this response.
     */
    public SessionType getSessionType()
    {
        return sessionType;
    }


    Map<String,String> toMap()
    {
        Map<String,String> map = super.toMap();

        // remove "openid.ns" from map and replace with just "ns"
        // openid prefix is invalid for association responses
        String ns = ( String ) map.get( OpenIdConstants.OPENID_NS );
        if ( ns != null )
        {
            map.put( OPENID_ASSOC_NS, ns );
            map.remove( OpenIdConstants.OPENID_NS );
        }

        if ( errorCode != null )
        {
            map.put( AssociationResponse.OPENID_ERROR_CODE, errorCode );
        }
        else
        {
            if ( !( !isVersion2() // OpenID 1.x
            && SessionType.NO_ENCRYPTION == sessionType ) )
            {
                // do not send session type for 1.1 responses if it is no-encryption
                map.put( AssociationResponse.OPENID_SESSION_TYPE, sessionType.toString() );
            }
            map.put( AssociationResponse.OPENID_ASSOCIATION_HANDLE,
                associationHandle );
            map.put( AssociationResponse.OPENID_ASSOCIATION_TYPE,
                associationType.toString() );
            map.put( AssociationResponse.OPENID_EXPIRES_IN, "" + expiresIn );
            if ( macKey != null )
            {
                map.put( AssociationResponse.OPENID_MAC_KEY,
                    Crypto.convertToString( macKey ) );
            }
            else if ( encryptedMacKey != null )
            {
                map.put( AssociationResponse.OPENID_DH_SERVER_PUBLIC,
                    Crypto.convertToString( dhServerPublic ) );
                map.put( AssociationResponse.OPENID_ENC_MAC_KEY,
                    Crypto.convertToString( encryptedMacKey ) );
            }
        }
        return map;
    }


    AssociationResponse( AssociationRequest ar, IAssociation a, Crypto crypto )
    {
        super( null );
        setNamespace( ar.getNamespace() );
        if ( a.isSuccessful() )
        {
            this.sessionType = a.getSessionType();
            this.associationHandle = a.getHandle();
            this.associationType = a.getAssociationType();
            this.expiresIn = a.getLifetime().intValue();
            this.dhServerPublic = a.getPublicDhKey();
            if ( a.isEncrypted() )
            {
                this.encryptedMacKey = a.getEncryptedMacKey();
            }
            else
            {
                this.macKey = a.getMacKey();
            }
        }
        else
        {
            this.errorCode = a.getErrorCode();
            this.error = a.getError();
        }
    }


    AssociationResponse( Map<String,String> map ) throws OpenIdException
    {
        super( map );
        Set<Map.Entry<String, String>> set = map.entrySet();
        for ( Iterator<Map.Entry<String, String>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String> mapEntry = iter.next();
            String key = ( String ) mapEntry.getKey();
            String value = ( String ) mapEntry.getValue();

            if ( AssociationResponse.OPENID_SESSION_TYPE.equals( key ) )
            {
                sessionType = SessionType.parse( value );
            }
            else if ( AssociationResponse.OPENID_ASSOCIATION_TYPE.equals( key ) )
            {
                associationType = AssociationType.parse( value );
            }
            else if ( OPENID_DH_SERVER_PUBLIC.equals( key ) )
            {
                dhServerPublic = Crypto.convertToBigIntegerFromString( value );
            }
            else if ( OPENID_ASSOCIATION_HANDLE.equals( key ) )
            {
                associationHandle = value;
            }
            else if ( OPENID_EXPIRES_IN.equals( key ) )
            {
                expiresIn = Integer.parseInt( value );
            }
            else if ( OPENID_MAC_KEY.equals( key ) )
            {
                macKey = Crypto.convertToBytes( value );
            }
            else if ( OPENID_ENC_MAC_KEY.equals( key ) )
            {
                encryptedMacKey = Crypto.convertToBytes( value );
            }
            else if ( OPENID_ERROR_CODE.equals( key ) )
            {
                errorCode = value;
            }
            // set namespace using association ns key
            else if ( OPENID_ASSOC_NS.equals( key ) )
            {
                setNamespace( value );
            }
        }
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder( "[AssociationResponse " );
        sb.append( super.toString() ).append( ", session type=" );
        sb.append( sessionType.toString() ).append( ", association type=" ); 
        sb.append( associationType.toString() ).append( ", association handle=" );
        sb.append( associationHandle ).append( ", expires in=" );
        sb.append( expiresIn );
        
        if ( dhServerPublic != null )
        {
            sb.append( ", server public key=" ).append( Crypto.convertToString( dhServerPublic ) );
        }
        if ( macKey != null )
        {
            sb.append( ", MAC key=" ).append( Crypto.convertToString( macKey ) );
        }
        if ( encryptedMacKey != null )
        {
            sb.append( ", encrypted MAC key=" ).append( Crypto.convertToString( encryptedMacKey ) );
        }
        if ( errorCode != null )
        {
            sb.append( ", error code=" ).append( errorCode );
        }
        
        sb.append( "]" );
        return sb.toString();
    }

}
