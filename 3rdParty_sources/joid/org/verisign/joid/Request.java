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


/**
 * Represents an OpenID request. Valid for OpenID 1.1 and 2.0 namespace.
 */
public abstract class Request extends Message
{
    /**
     * @TODO delete this after re-factoring
     */
    Request( Map<String,String> map, Mode mode )
    {
        setMode( mode );

        if ( map != null )
        {
            setNamespace( ( String ) map.get( OpenIdConstants.OPENID_NS ) );
        }
    }


    /**
     * @TODO delete this after re-factoring
     */
    Map<String,String> toMap()
    {
        return super.toMap();
    }


    /**
     * @TODO delete this after re-factoring
     * 
     * Processes this request using the given store and crypto implementations.
     * This processing step should produce a valid response that can be
     * sent back to the requestor. Associations may be read from, written to,
     * or deleted from the store by way of this processing step.
     *
     * @param serverInfo information about this server's implementation.
     *
     * @return the response
     *
     * @throws OpenIdException unrecoverable errors happen.
     */
    public abstract Response processUsing( ServerInfo serverInfo )
        throws OpenIdException;

}
