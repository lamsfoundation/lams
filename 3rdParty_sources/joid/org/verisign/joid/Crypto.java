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
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.tsik.datatypes.Base64;
import org.apache.tsik.uuid.UUID;
import org.verisign.joid.Crypto;


/**
 * Implements the cryptography needed for OpenID.
 */
public class Crypto
{
    private DiffieHellman dh;
    private final static Log LOG = LogFactory.getLog( Crypto.class );


    /**
     * Creates an instance of the crypto library.
     */
    public Crypto()
    {
        LOG.debug( "Creating new instance." );
    }


    /**
     * Digests a message using SHA-1.
     * @param text the bytes to digest.
     * @return the digested bytes.
     * @throws NoSuchAlgorithmException if SHA-1 is not available.
     */
    public static byte[] sha1( byte[] text ) throws NoSuchAlgorithmException
    {
        MessageDigest d = MessageDigest.getInstance( "SHA-1" );
        return d.digest( text );
    }


    /**
     * Digests a message using SHA-256.
     * @param text the bytes to digest.
     * @return the digested bytes.
     * @throws NoSuchAlgorithmException if SHA-256 is not available.
     */
    public static byte[] sha256( byte[] text ) throws NoSuchAlgorithmException
    {
        MessageDigest d = MessageDigest.getInstance( "SHA-256" );
        return d.digest( text );
    }


    /**
     * Signs a message using HMAC SHA1.
     *
     * @param key the key to sign with.
     * @param text the bytes to sign.
     * @return the signed bytes.
     * @throws InvalidKeyException if <code>key</code> is not a good HMAC key.
     * @throws NoSuchAlgorithmException if HMACSHA1 is not available.
     */
    public static byte[] hmacSha1( byte[] key, byte[] text )
        throws InvalidKeyException, NoSuchAlgorithmException
    {
        return hmacShaX( "HMACSHA1", key, text );
    }


    /**
     * Signs a message using HMAC SHA256.
     *
     * @param key the key to sign with.
     * @param text the bytes to sign.
     * @return the signed bytes.
     * @throws InvalidKeyException if <code>key</code> is not a good HMAC key.
     * @throws NoSuchAlgorithmException if HMACSHA1 is not available.
     */
    public static byte[] hmacSha256( byte[] key, byte[] text )
        throws InvalidKeyException, NoSuchAlgorithmException
    {
        return hmacShaX( "HMACSHA256", key, text );
    }


    private static byte[] hmacShaX( String keySpec, byte[] key, byte[] text )
        throws InvalidKeyException, NoSuchAlgorithmException
    {
        SecretKey sk = new SecretKeySpec( key, keySpec );
        Mac m = Mac.getInstance( sk.getAlgorithm() );
        m.init( sk );
        return m.doFinal( text );
    }

    private static SecureRandom random;
    static
    {
        try
        {
            random = SecureRandom.getInstance( "SHA1PRNG" );
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new RuntimeException( "No secure random available." );
        }
    }


    /**
     * Generates a random handle, in UUID format.
     *
     * @return a random handle.
     */
    public static String generateHandle()
    {
        return UUID.generate().toString();
    }


    /**
     * Generates a random 'crumb' value.
     *
     * @return a four-byte random string.
     */
    public static String generateCrumb()
    {
        byte[] b = new byte[4];
        random.nextBytes( b );
        return convertToString( b );
    }


    /** 
     * Generates random bytes.
     *
     * @param s the session or association type 
     * @return random bytes, length fitting to the given session or
     * association type (length 0 to 32 bytes).
     */
    public byte[] generateRandom( String s )
    {
        int len = 0;
        if ( SessionType.DH_SHA1.toString().equals( s ) )
        {
            len = 20;
        }
        else if ( SessionType.DH_SHA256.toString().equals( s ) )
        {
            len = 32;
        }
        else if ( SessionType.NO_ENCRYPTION.toString().equals( s ) )
        {
            len = 0;
        }
        else if ( AssociationType.HMAC_SHA1.toString().equals( s ) )
        {
            len = 20;
        }
        else if ( AssociationType.HMAC_SHA256.toString().equals( s ) )
        {
            len = 32;
        }
        byte[] result = new byte[len];
        random.nextBytes( result );
        return result;
    }


    /**
     * Sets the Diffie-Hellman key values.
     *
     * @param mod the Diffie-Hellman modulus.
     * @param gen the Diffie-Hellman generator.
     */
    public void setDiffieHellman( BigInteger mod, BigInteger gen )
    {
        this.dh = new DiffieHellman( mod, gen );
    }


    /**
     * Sets the Diffie-Hellman key values.
     *
     * @param dh the Diffie-Hellman value.
     */
    public void setDiffieHellman( DiffieHellman dh )
    {
        this.dh = dh;
    }


    /**
     * Returns the Diffie-Hellman public key set 
     * by {@link #setDiffieHellman(BigInteger, BigInteger)}.
     *
     * @return the Diffie-Hellman public key.
     * @throws IllegalArgumentException if this crypto instance has not
     * yet been initialized.
     */
    public BigInteger getPublicKey()
    {
        if ( dh == null )
        {
            throw new IllegalArgumentException( "DH not yet initialized" );
        }
        return dh.getPublicKey();
    }


    /** 
     * Generates random bytes. TODO: this is a duplicate 
     * of {@link #generateRandom(String)} -- need to remove one.
     *
     * @param sessionType the session or association type 
     * @return random bytes, length fitting to the given session or
     * association type (length 0 to 32 bytes).
     */
    public byte[] generateSecret( String sessionType )
    {
        return generateRandom( sessionType );
    }


    /**
     * Decrypts a secret using Diffie-Hellman.
     *
     * @param consumerPublic the public key used to decrypt.
     * @param secret the value to decrypt.
     * @return the decrypted value.
     */
    public byte[] decryptSecret( BigInteger consumerPublic, byte[] secret )
        throws OpenIdException
    {
        return encryptSecret( consumerPublic, secret );
    }


    /**
     * Encrypts a secret using Diffie-Hellman.
     *
     * @param consumerPublic the public key used to encrypt.
     * @param secret the value to encrypt.
     * @return the encrypted secret value.
     */
    public byte[] encryptSecret( BigInteger consumerPublic, byte[] secret )
        throws OpenIdException
    {
        if ( dh == null )
        {
            throw new IllegalArgumentException( "No DH implementation set" );
        }
        byte[] xoredSecret = null;
        try
        {
            xoredSecret = dh.xorSecret( consumerPublic, secret );
            return xoredSecret;
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new OpenIdException( e );
        }
    }


    /**
     * Converts a string to bytes.
     *
     * @param s the string to convert.
     * @return the base 64 decoded bytes.
     */
    public static byte[] convertToBytes( String s )
    {
        return Base64.decode( s );
    }


    /**
     * Converts bytes to string.
     *
     * @param b the bytes to encode.
     * @return the base 64 encoded string.
     */
    public static String convertToString( byte[] b )
    {
        String s = Base64.encode( b );
        s = s.replaceAll( "\n", "" );
        return s;
    }


    /**
     * Converts a big integer to string.
     *
     * @param b the big integer to convert.
     * @return the base 64 encoded string.
     */
    public static String convertToString( BigInteger b )
    {
        return convertToString( b.toByteArray() );
    }


    /**
     * Converts a base64-encoded string to big int.
     *
     * @param s the string to convert, by way of base64 decoding.
     * @return the converted big int.
     */
    public static BigInteger convertToBigIntegerFromString( String s )
    {
        return new BigInteger( Base64.decode( s ) );
    }
}
