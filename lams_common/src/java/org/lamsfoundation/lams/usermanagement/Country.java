package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_country"
 *     
*/
public class Country implements Serializable {

    private static final long serialVersionUID = -5148511001078176566L;

	/** identifier field */
    private Byte countryId;

    /** persistent field */
    private String isoCode;

    /** full constructor */
    public Country(String isoCode) {
        this.isoCode = isoCode;
    }

    /** default constructor */
    public Country() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Byte"
     *             column="country_id"
     *         
     */
    public Byte getCountryId() {
        return this.countryId;
    }

    public void setCountryId(Byte countryId) {
        this.countryId = countryId;
    }

    /** 
     *            @hibernate.property
     *             column="iso_code"
     *             unique="true"
     *             length="2"
     *             not-null="true"
     *         
     */
    public String getIsoCode() {
        return this.isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("countryId", getCountryId())
            .toString();
    }

}
