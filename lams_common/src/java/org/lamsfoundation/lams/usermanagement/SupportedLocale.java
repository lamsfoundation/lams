package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lams_supported_locale")
public class SupportedLocale implements Serializable, Comparable<SupportedLocale> {

    private static final long serialVersionUID = 4096710015819299886L;

    @Id
    @Column(name = "locale_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer localeId;

    @Column(name = "language_iso_code")
    private String languageIsoCode;

    @Column(name = "country_iso_code")
    private String countryIsoCode;

    @Column
    private String description;

    @Column
    private String direction;

    @Column(name = "fckeditor_code")
    private String fckLanguageMapping;

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
    public int compareTo(SupportedLocale locale) {
	return description.compareTo(locale.getDescription());
    }

    public String getFckLanguageMapping() {
	return fckLanguageMapping;
    }

    public void setFckLanguageMapping(String fckLanguageMapping) {
	this.fckLanguageMapping = fckLanguageMapping;
    }
}