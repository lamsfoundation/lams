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
import java.util.Date;


/**
 * An association between a relying party and an OpenID provider.
 *
 * Implement this interface to represent an OpenID association.
 */
public interface IAssociation
{
    /** Returns whether this association is valid (was successful).
     *
     * @return true is successful; false otherwise.
     */
    boolean isSuccessful();


    /**
     * Returns error as a string.
     *
     * @return error as a string, null if no error.
     */
    String getError();


    /**
     * Returns error code as a string.
     *
     * @return error code as a string, null if no error string.
     */
    String getErrorCode();

    
    /**
     * Get's the secret for this IAssociation.
     *
     * @return the secret
     */
    String getSecret();


    /**
     * Returns the association's handle. This handle should be suitable
     * to put on the wire as part of the OpenID protocol.
     *
     * @return handle as a string, null if no handle yet available.
     */
    String getHandle();


    /**
     * Gets the lifetime of this association. This is static, that is,
     * current time is not taken into consideration.
     *
     * @return lifetime the lifetime in seconds.
     */
    Long getLifetime();


    /**
     * Gets the OpenID protocol association type, for example "HMAC-SHA1".
     *
     * @return the association type.
     */
    AssociationType getAssociationType();


    /**
     * Gets the OpenID protocol session type, for example "DH-SHA1".
     *
     * @return the session type.
     */
    SessionType getSessionType();


    /**
     * Returns the MAC key for this association.
     *
     * @return the MAC key; null if key doesn't exist.
     */
    byte[] getMacKey();


    /**
     * Returns the public Diffie-Hellman key in use.
     *
     * @return the DH key.
     */
    BigInteger getPublicDhKey();


    /**
     * Returns whether the association negotiates an encrypted secret.
     *
     * @return true if the secret is encrypted; false otherwise.
     */
    boolean isEncrypted();


    /**
     * Returns the encrypted MAC key for this association.
     *
     * @return the encrypted MAC key; null if key doesn't exist.
     */
    byte[] getEncryptedMacKey();


    /**
     * Returns whether this association has expired.
     *
     * @return whether this association has expired.
     */
    boolean hasExpired();


    Date getIssuedDate();
}
