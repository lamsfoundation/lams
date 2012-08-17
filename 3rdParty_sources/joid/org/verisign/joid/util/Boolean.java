//
// (C) Copyright 2008 VeriSign, Inc.  All Rights Reserved.
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

package org.verisign.joid.util;


import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 * Boolean in order to comply with Java 1.4.
 */
public class Boolean
{
    private final static Log log = LogFactory.getLog( Boolean.class );


    /**
     *
     */
    public static boolean parseBoolean( String s )
    {
        if ( s != null )
        {
            if ( s.equals( "true" ) )
            {
                return true;
            }
            else if ( s.equals( "false" ) )
            {
                return false;
            }
            else
            {
                // TBD: Throw an exception!
                log.error( "No such value: " + s );
                return false;
            }
        }
        // TBD: Throw an exception!
        log.error( "No such value: " + s );
        return false;
    }
}
