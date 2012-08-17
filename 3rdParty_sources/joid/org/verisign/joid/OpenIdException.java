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
 * The main exception class of the OpenId librbary.
 */
public class OpenIdException extends Exception
{
    private static final long serialVersionUID = 28732439387623L;


    /** 
     * Creates an exception.
     * @param s a string value to encapsulate.
     */
    public OpenIdException( String s )
    {
        super( s );
    }


    /**
     * Creates an exception.
     * @param e a exception to encapsulate.
     */
    public OpenIdException( Exception e )
    {
        super( e );
    }


    public OpenIdException( String s, Exception e )
    {
        super( s, e );
    }


    /**
     * Returns this exception's message.
     * @return a string message of this exception (either the encapsulated string,
     * or the encapsulated exception).
     */
    public String getMessage()
    {
        Throwable t = getCause();
        if ( t != null )
        {
            return t.getMessage();
        }
        else
        {
            return super.getMessage();
        }
    }
}
