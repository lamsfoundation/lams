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


import java.util.Date;


/**
 * A nonce.
 *
 * Implement this interface to represent an OpenID association's nonce.
 */
public interface INonce
{

    /**
     * Returns the nonce.
     *
     * @return nonce as a string.
     */
    public String getNonce();


    /**
     * Sets the date this nonce was checked.
     *
     * @param checkedDate the timestamp of check.
     */
    public void setCheckedDate( Date checkedDate );


    public Date getCheckedDate();
}
