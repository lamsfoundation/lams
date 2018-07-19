package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SupportedLocale implements Serializable, Comparable {

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

    /** persistent field */
    private String fckLanguageMapping;

    /** full constructor */
    public SupportedLocale(String languageIsoCode, String countryIsoCode, String description, String direction,
	    String fckLanguageMapping) {
	this.languageIsoCode = languageIsoCode;
	this.countryIsoCode = countryIsoCode;
	this.description = description;
	this.direction = direction;
	this.fckLanguageMapping = fckLanguageMapping;
    }

    /** default constructor */
    public SupportedLocale() {
    }
    
    /**
     * Return locale name in the format xx_XX, where xx - language ISO code and XX - country ISO code
     */
    public String getLocaleName() {
	return languageIsoCode + "_" + countryIsoCode;
    }

    public Integer getLocaleId() {
	return this.localeId;
    }

    public void setLocaleId(Integer localeId) {
	this.localeId = localeId;
    }

    public String getLanguageIsoCode() {
	return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
	this.languageIsoCode = languageIsoCode;
    }

    public String getCountryIsoCode() {
	return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
	this.countryIsoCode = countryIsoCode;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getDirection() {
	return direction;
    }

    public void setDirection(String direction) {
	this.direction = direction;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("localeId", getLocaleId())
		.append("languageIsoCode", getLanguageIsoCode()).append("countryIsoCode", getCountryIsoCode())
		.append("description", getDescription()).append("direction", getDirection())
		.append("fckLanguageMapping", getFckLanguageMapping()).toString();
    }

    @Override
    public int compareTo(Object o) {
	SupportedLocale locale = (SupportedLocale) o;
	return description.compareTo(locale.getDescription());
    }

    public String getFckLanguageMapping() {
	return fckLanguageMapping;
    }

    public void setFckLanguageMapping(String fckLanguageMapping) {
	this.fckLanguageMapping = fckLanguageMapping;
    }

}
