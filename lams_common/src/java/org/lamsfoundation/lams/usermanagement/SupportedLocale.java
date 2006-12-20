package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_supported_locale"
 *     
*/
public class SupportedLocale implements Serializable,Comparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4096710015819299886L;

	/** identifier field */
    private Integer localeId;

    /** persistent field */
    private String languageIsoCode;

    /** persistent field */
    private String countryIsoCode;

    /** persistent field */
    private String description;

    /** persistent field */
    private String direction;

    /** full constructor */
    public SupportedLocale(String languageIsoCode, String countryIsoCode, String description, String direction) {
        this.languageIsoCode = languageIsoCode;
        this.countryIsoCode = countryIsoCode;
        this.description = description;
        this.direction = direction;
    }

    /** default constructor */
    public SupportedLocale() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Integer"
     *             column="locale_id"
     *         
     */
    public Integer getLocaleId() {
        return this.localeId;
    }

    public void setLocaleId(Integer localeId) {
        this.localeId = localeId;
    }

	/** 
     *            @hibernate.property
     *             column="language_iso_code"
     *             unique="false"
     *             length="2"
     *             not-null="true"
     *         
     */
	public String getLanguageIsoCode() {
		return languageIsoCode;
	}

	public void setLanguageIsoCode(String languageIsoCode) {
		this.languageIsoCode = languageIsoCode;
	}

    /** 
     *            @hibernate.property
     *             column="country_iso_code"
     *             unique="false"
     *             length="2"
     *             not-null="false"
     *         
     */
 	public String getCountryIsoCode() {
		return countryIsoCode;
	}

	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
	}

	/** 
     *            @hibernate.property
     *             column="description"
     *             unique="false"
     *             length="255"
     *             not-null="true"
     *         
     */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
	/** 
     *            @hibernate.property
     *             column="direction"
     *             unique="false"
     *             length="3"
     *             not-null="true"
     *         
     */
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}


	public String toString() {
		return new ToStringBuilder(this)
	            .append("localeId", getLocaleId())
	            .append("languageIsoCode", getLanguageIsoCode())
	            .append("countryIsoCode", getCountryIsoCode())
	            .append("description", getDescription())
	            .append("direction", getDirection())
	            .toString();
	}

	public int compareTo(Object o) {
		SupportedLocale locale = (SupportedLocale)o;
		return description.compareTo(locale.getDescription());
	}


}
