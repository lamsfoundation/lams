package org.verisign.joid.server;


import org.verisign.joid.IAssociation;
import org.verisign.joid.AssociationRequest;
import org.verisign.joid.Crypto;
import org.verisign.joid.INonce;
import org.verisign.joid.OpenIdException;
import org.verisign.joid.IStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;


public class MemoryStore implements IStore
{
    public static long DEFAULT_LIFESPAN = 300; // @TODO: should probably increase this
    private static List<IAssociation> associationList = new ArrayList<IAssociation>();
    private static List<INonce> nonceList = new ArrayList<INonce>();
    private long associationLifetime = DEFAULT_LIFESPAN;


    public IAssociation generateAssociation( AssociationRequest req, Crypto crypto )
        throws OpenIdException
    {
        // boldly reusing the db implementation of Association
        Association a = new Association();
        a.setHandle( Crypto.generateHandle() );
        a.setSessionType( req.getSessionType() );

        byte[] secret = null;
        if ( req.isNotEncrypted() )
        {
            secret = crypto.generateSecret( req.getAssociationType().toString() );
        }
        else
        {
            secret = crypto.generateSecret( req.getSessionType().toString() );
            crypto.setDiffieHellman( req.getDhModulus(), req.getDhGenerator() );
            byte[] encryptedSecret = crypto.encryptSecret( req.getDhConsumerPublic(), secret );
            a.setEncryptedMacKey( encryptedSecret );
            a.setPublicDhKey( crypto.getPublicKey() );
        }
        a.setMacKey( secret );
        a.setIssuedDate( new Date() );
        a.setLifetime( new Long( associationLifetime ) );

        a.setAssociationType( req.getAssociationType() );
        return a;
    }


    public void saveAssociation( IAssociation a )
    {
        associationList.add( a );
    }


    public void saveNonce( INonce n )
    {
        nonceList.add( n );
    }


    public void deleteAssociation( IAssociation a )
    {
        throw new RuntimeException( "not yet implemented" );
        // "associationList.delete(a)"
    }


    public IAssociation findAssociation( String handle ) throws OpenIdException
    {
        if ( handle == null )
            return null;
        ListIterator<IAssociation> li = associationList.listIterator();
        while ( li.hasNext() )
        {
            IAssociation a = li.next();
            if ( handle.equals( a.getHandle() ) )
            {
                return a;
            }
        }
        return null;
    }


    public INonce findNonce( String nonce ) throws OpenIdException
    {
        if ( nonce == null )
            return null;
        ListIterator<INonce> li = nonceList.listIterator();
        while ( li.hasNext() )
        {
            INonce n = li.next();
            if ( nonce.equals( n.getNonce() ) )
            {
                return n;
            }
        }
        return null;
    }


    public INonce generateNonce( String nonce ) throws OpenIdException
    {
        Nonce n = new Nonce();
        n.setNonce( nonce );
        n.setCheckedDate( new Date() );
        return n;
    }


    public void setAssociationLifetime( long associationLifetime )
    {
        this.associationLifetime = associationLifetime;
    }
}
