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
 * Information about this server.
 */
public class ServerInfo
{
    private String urlEndPoint;
    private IStore store;
    private Crypto crypto;


    /**
     * Creates an instance of the server information.
     *
     * @param urlEndPoint the URL endpoint for the service. 
     * @param store the store implementation to use.
     * @param crypto the crypto implementation to use.
     */
    public ServerInfo( String urlEndPoint, IStore store, Crypto crypto )
    {
        this.urlEndPoint = urlEndPoint;
        this.store = store;
        this.crypto = crypto;
    }


    public String getUrlEndPoint()
    {
        return urlEndPoint;
    }


    public IStore getStore()
    {
        return store;
    }


    public Crypto getCrypto()
    {
        return crypto;
    }
}
