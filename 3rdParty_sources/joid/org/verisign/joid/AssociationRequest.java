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
import java.util.HashMap;
import java.util.Set;


/**
 * Represents an OpenID association request.
 */
public class AssociationRequest extends Request
{
    private SessionType sessionType;
    private AssociationType associationType;
    private BigInteger dhModulus;
    private BigInteger dhGenerator;
    private BigInteger dhConsumerPublic;

    /**
     * @TODO Delete this after re-factoring.
     */
    Map<String,String> toMap()
    {
        Map<String,String> map = super.toMap();

        map.put( OpenIdConstants.OPENID_SESSION_TYPE, sessionType.toString() );
        map.put( OpenIdConstants.OPENID_ASSOCIATION_TYPE, associationType.toString() );
        map.put( OpenIdConstants.OPENID_DH_CONSUMER_PUBLIC, getDhConsumerPublicString() );

        return map;
    }


    /**
     * @TODO this looks like it is used only by the client/consumer and should
     * go somewhere else.
     * 
     * Creates a standard association request. Default values are
     * <code>HMAC-SHA1</code> for association type, and <code>DH-SHA1</code>
     * for session type.
     *
     * @param crypto the Crypto implementation to use.
     * @return an AssociationRequest.
     * @throws OpenIdException 
     */
    public static AssociationRequest create( Crypto crypto )
    {
        try
        {
            BigInteger pubKey = crypto.getPublicKey();
            Map<String,String> map = new HashMap<String,String>();
            map.put( OpenIdConstants.OPENID_MODE, Mode.ASSOCIATE.toString() );
            map.put( OpenIdConstants.OPENID_ASSOCIATION_TYPE, AssociationType.HMAC_SHA1.toString() );
            map.put( OpenIdConstants.OPENID_SESSION_TYPE, SessionType.DH_SHA1.toString() );
            map.put( OpenIdConstants.OPENID_NS, OpenIdConstants.OPENID_20_NAMESPACE );
            map.put( OpenIdConstants.OPENID_DH_CONSUMER_PUBLIC, Crypto.convertToString( pubKey ) );
            return new AssociationRequest( map, Mode.ASSOCIATE );
        }
        catch ( OpenIdException e )
        {
            throw new IllegalArgumentException( e.toString() );
        }
    }


    AssociationRequest( Map<String,String> map, Mode mode ) throws OpenIdException
    {
        super( map, mode );
        this.sessionType = SessionType.NO_ENCRYPTION; //default value
        this.associationType = AssociationType.HMAC_SHA1; //default value

        this.dhModulus = DiffieHellman.DEFAULT_MODULUS;
        this.dhGenerator = DiffieHellman.DEFAULT_GENERATOR;

        Set<Map.Entry<String, String>> set = map.entrySet();
        for ( Iterator<Map.Entry<String, String>> iter = set.iterator(); iter.hasNext(); )
        {
            Map.Entry<String,String> mapEntry = iter.next();
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();

            if ( OpenIdConstants.OPENID_SESSION_TYPE.equals( key ) )
            {
                this.sessionType = SessionType.parse( value );
            }
            else if ( OpenIdConstants.OPENID_ASSOCIATION_TYPE.equals( key ) )
            {
                this.associationType = AssociationType.parse( value );
            }
            else if ( OpenIdConstants.OPENID_DH_MODULUS.equals( key ) )
            {
                this.dhModulus = Crypto.convertToBigIntegerFromString( value );
            }
            else if ( OpenIdConstants.OPENID_DH_GENERATOR.equals( key ) )
            {
                this.dhGenerator = Crypto.convertToBigIntegerFromString( value );
            }
            else if ( OpenIdConstants.OPENID_DH_CONSUMER_PUBLIC.equals( key ) )
            {
                this.dhConsumerPublic = Crypto.convertToBigIntegerFromString( value );
            }
        }
        checkInvariants();
    }


    /**
     * Returns whether the session type in use is not encrypted.
     *
     * @return whether the session type is not encrypted.
     */
    public boolean isNotEncrypted()
    {
        return SessionType.NO_ENCRYPTION == sessionType;
    }


    private void checkInvariants() throws OpenIdException
    {
        if ( getMode() == null )
        {
            throw new OpenIdException( "Missing mode" );
        }
        if ( associationType == null )
        {
            throw new OpenIdException( "Missing association type" );
        }
        if ( sessionType == null )
        {
            throw new OpenIdException( "Missing session type" );
        }

        if ( ( sessionType == SessionType.DH_SHA1 && associationType != AssociationType.HMAC_SHA1 )
            ||
             ( sessionType == SessionType.DH_SHA256 && associationType != AssociationType.HMAC_SHA256 ) )
        {
            throw new OpenIdException( "Mismatch " + OpenIdConstants.OPENID_SESSION_TYPE
                      + " and " + OpenIdConstants.OPENID_ASSOCIATION_TYPE );
        }
        if ( sessionType == SessionType.DH_SHA1 || sessionType == SessionType.DH_SHA256 )
        {
            if ( dhConsumerPublic == null )
            {
                throw new OpenIdException( "Missing " + OpenIdConstants.OPENID_DH_CONSUMER_PUBLIC );
            }
        }
    }


    /**
     * @TODO delete this while refactoring.
     */
    public Response processUsing( ServerInfo si ) throws OpenIdException
    {
        IStore store = si.getStore();
        Crypto crypto = si.getCrypto();
        IAssociation a = store.generateAssociation( this, crypto );
        store.saveAssociation( a );
        return new AssociationResponse( this, a, crypto );
    }


    /**
     * Returns the DH modulus.
     *
     * @return the DH modulus.
     */
    public BigInteger getDhModulus()
    {
        return this.dhModulus;
    }


    /**
     * Returns the DH generator.
     *
     * @return the DH generator.
     */
    public BigInteger getDhGenerator()
    {
        return this.dhGenerator;
    }


    /**
     * Returns the DH public value.
     *
     * @return the DH public value.
     */
    public BigInteger getDhConsumerPublic()
    {
        return this.dhConsumerPublic;
    }

    
    public String getDhConsumerPublicString()
    {
        return Crypto.convertToString( dhConsumerPublic );
    }
    

    /**
     * Returns the association session type.
     *
     * @return the association session type.
     */
    public SessionType getSessionType()
    {
        return this.sessionType;
    }


    /**
     * Returns the association type of this request.
     *
     * @return the association type.
     */
    public AssociationType getAssociationType()
    {
        return this.associationType;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder( "[AssociationRequest " );
        sb.append( super.toString() ).append( ", session type=" ).append( sessionType.toString() );
        sb.append( ", association type=" ).append( associationType.toString() ).append( "]" );
        return sb.toString();
    }
    
    public void setAssociationType( AssociationType associationType )
    {
        this.associationType = associationType;
    }
    
    public void setDhConsumerPublic( BigInteger dhConsumerPublic )
    {
        this.dhConsumerPublic = dhConsumerPublic;
    }
    
    public void setDhModulus( BigInteger dhModulus )
    {
        this.dhModulus = dhModulus;
    }
    
    public void setSessionType( SessionType sessionType )
    {
        this.sessionType = sessionType;
    }
    
    public void setDhGenerator( BigInteger dhGenerator )
    {
        this.dhGenerator = dhGenerator;
    }
}
