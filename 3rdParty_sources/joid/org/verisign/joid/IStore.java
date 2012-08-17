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


/**
 * Represents a store that is used by JOID for persisting associations.
 */
public interface IStore
{
    /**
     * Generates and returns association. To store the association
     * use {@link IStore#saveAssociation(IAssociation) saveAssociation()}
     *
     * @param req the association request.
     * @param crypto the crypto implementation to use.
     * @return the generated assocation.
     *
     * @throws OpenIdException at unrecoverable errors.
     */
    IAssociation generateAssociation( AssociationRequest req, Crypto crypto ) throws OpenIdException;


    /**
     * Deletes an association from the store.
     *
     * @param a the association to delete.
     */
    void deleteAssociation( IAssociation a ) throws OpenIdException;


    /**
     * Saves an association in the store.
     *
     * @param a the association to store.
     */
    void saveAssociation( IAssociation a ) throws OpenIdException;


    /**
     * Finds an association in the store.
     *
     * @param handle the handle of the association to find.
     * @return the assocation if found; null otherwise.
     *
     * @throws OpenIdException at unrecoverable errors.
     */
    IAssociation findAssociation( String handle ) throws OpenIdException;


    /**
     * Finds a nonce in the store.
     *
     * @param nonce the nonce to find.
     * @return the nonce if found; null otherwise.
     *
     * @throws OpenIdException at unrecoverable errors.
     */
    INonce findNonce( String nonce ) throws OpenIdException;


    /**
     * Saves an nonce in the store.
     *
     * @param n the nonce to store.
     */
    void saveNonce( INonce n ) throws OpenIdException;


    /**
     * Generates and returns a nonce. To store the nonce
     * use {@link IStore#saveNonce(INonce) saveNonce()}
     *
     * @param nonce the nonce to use.
     * @return the generated nonce.
     *
     * @throws OpenIdException at unrecoverable errors.
     */
    INonce generateNonce( String nonce ) throws OpenIdException;
}
