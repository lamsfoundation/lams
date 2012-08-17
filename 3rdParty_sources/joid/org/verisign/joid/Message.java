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


import java.util.HashMap;
import java.util.Map;


/**
 * Represents an OpenID message. 
 */
public abstract class Message
{
    private Mode mode;
    private String ns;
    private Boolean version2 = null;


    /**
     * Returns whether this request is an OpenID 2.0 request.
     *
     * @return true if this request is an OpenID 2.0 request.
     */
    public boolean isVersion2()
    {
        if ( version2 == null )
        {
            version2 = OpenIdConstants.OPENID_20_NAMESPACE.equals( this.ns );
        }
        
        return version2;
    }
    
    
    public Mode getMode()
    {
        return mode;
    }
    
    
    public void setMode( Mode mode )
    {
        this.mode = mode;
    }


    /**
     * Returns the namespace of this message. For OpenID 2.0 messages,
     * this namespace will be <code>http://specs.openid.net/auth/2.0</code>.
     *
     * @return the namespace, or null if none (OpenID 1.x).
     */
    public String getNamespace()
    {
        return ns;
    }

    
    public void setNamespace( String ns )
    {
        this.ns = ns;
    }
    

    /** 
     * Returns a string representation of this message.
     *
     * @return a string representation of this message.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder( "version=" );

        if ( isVersion2() )
        {
            sb.append( "2.0" );
        }
        else
        {
            sb.append( "1.x" );
        }
        
        if ( ns != null )
        {
            sb.append( ", namespace=" ).append( ns );
        }
        
        return sb.toString();
    }


    /**
     * TODO delete me after re-factoring.
     * Unrolls this message as a string. This string will use the
     * <code>name:value</code> format of the specification. See also
     * {@link #toUrlString()}.
     *
     * @return the message as a string.
     */
    public String toPostString() throws OpenIdException
    {
        return MessageParser.toPostString( this );
    }


    /**
     * TODO delete me after re-factoring.
     * Unrolls this message as a string. This string will use encoding
     * suitable for URLs. See also {@link #toPostString()}.
     *
     * @return the message as a string.
     */
    public String toUrlString() throws OpenIdException
    {
        return MessageParser.toUrlString( this );
    }


    /**
     * TODO delete me after re-factoring.
     *
     * @return
     */
    Map<String,String> toMap()
    {
        Map<String,String> map = new HashMap<String,String>();
        if ( ns != null )
        {
            map.put( OpenIdConstants.OPENID_NS, ns );
        }
        if ( mode != null )
        {
            map.put( OpenIdConstants.OPENID_MODE, mode.toString() );
        }
        return map;
    }
}
