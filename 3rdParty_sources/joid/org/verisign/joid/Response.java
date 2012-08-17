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
 * Represents an OpenID response. Valid for OpenID 1.1 and 2.0 namespace. Adds
 * a simple error property in addition to standard {@link Message} properties
 * and this is to handle openid.mode=error type messages.
 */
public abstract class Response extends Message
{
    static String OPENID_ERROR = "error";

    String error;


    String getError()
    {
        return error;
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
     */
    Response( Map<String,String> map )
    {
        if ( map != null )
        {
            if ( map != null )
            {
                setNamespace( ( String ) map.get( OpenIdConstants.OPENID_NS ) );
            }

            this.error = ( String ) map.get( Response.OPENID_ERROR );
        }
    }


    /** 
     * Returns a string representation of this response.
     *
     * @return a string representation of this response.
     */
    public String toString()
    {
        String s = super.toString();
        if ( error != null )
        {
            s += ", error=" + error;
        }
        return s;
    }
}
