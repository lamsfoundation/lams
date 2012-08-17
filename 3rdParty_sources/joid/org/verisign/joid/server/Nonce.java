package org.verisign.joid.server;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.verisign.joid.INonce;


/**
 * A simple Nonce implementation.
 * 
 * @TODO Remove hibernate residue and fix the damn javadocs.
 */
public class Nonce implements INonce
{
    private Long id;
    private String nonce;
    private Date checkedDate;


    /** 
     * Hibernate mapping. 
     * 
     * @TODO Why in the world would the hibernate mapping key be 
     * exposed in the primary object? This is something that should 
     * be removed.
     */
    public Long getId()
    {
        return id;
    }


    /** Hibernate mapping. */
    public void setId( Long id )
    {
        this.id = id;
    }


    /** Hibernate mapping. */
    public String getNonce()
    {
        return nonce;
    }


    /** Hibernate mapping. */
    public void setNonce( String s )
    {
        nonce = s;
    }


    /** Hibernate mapping. */
    public Date getCheckedDate()
    {
        return checkedDate;
    }


    /** 
     * Set's the checkedDate property by cloning the date.
     */
    public void setCheckedDate( Date date )
    {
        this.checkedDate = ( Date ) date.clone();
    }

    
    /** 
     * Original flawed setter which used simple date format parsing and looses
     * the milliseconds argument which is used in LDAP timestamps.
     * 
     * @deprecated
     * @TODO - confirm no issues with replacing this then delete this method
     */
    public void _setCheckedDate( Date date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date tmp = date;
        sdf.format( tmp );
        this.checkedDate = tmp;
    }

    
    /**
     * Returns a string representation of this Nonce.
     *
     * @return a string representation of this Nonce.
     */
    public String toString()
    {
        return "[Nonce nonce=" + nonce + ", checked=" + checkedDate + "]";
    }
}
