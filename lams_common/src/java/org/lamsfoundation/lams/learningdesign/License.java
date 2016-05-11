/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learningdesign.dto.LicenseDTO;

/**
 * @author Manpreet Minhas
 */
public class License implements Serializable {

    /** persistent field */
    private Long licenseID;
    /** persistent field */
    private String name;
    /** persistent field */
    private String code;
    /** persistent field */
    private String url;
    /** persistent field */
    private Boolean defaultLicense;
    /** persistent field */
    private String pictureURL;

    /** default constructor */
    public License() {

    }

    /** full constructor */
    public License(Long licenseID, String name, String code, String url, Boolean defaultLicense, String pictureURL) {
	this.licenseID = licenseID;
	this.name = name;
	this.code = code;
	this.url = url;
	this.defaultLicense = defaultLicense;
	this.pictureURL = pictureURL;
    }

    /**
     * Get the standard DTO version of the license. Need the current serverURL
     * (e.g. http://localhost:8080/lams)
     * to construct a full address for the local images.
     */
    public LicenseDTO getLicenseDTO(String serverURL) {
	return new LicenseDTO(licenseID, name, code, url, defaultLicense, pictureURL, serverURL);
    }

    public Boolean getDefaultLicense() {
	return defaultLicense;
    }

    public void setDefaultLicense(Boolean defaultLicense) {
	this.defaultLicense = defaultLicense;
    }

    public Long getLicenseID() {
	return licenseID;
    }

    public void setLicenseID(Long licenseID) {
	this.licenseID = licenseID;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     * If it is a full url, then it should start with http://, ftp:// etc.
     * If it just starts with "/" then it will be assumed to be a url on the
     * local server and the server url (http://server/lams/ will be prepended).
     */
    public String getPictureURL() {
	return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
	this.pictureURL = pictureURL;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    /**
     * Is this license and another license the same license? The licenseID and name fields
     * are checked, with the name fields having any leading or trailing spaces stripped
     * before comparison. If both licenseID fields are null, then they are assumed to be
     * different licenses.
     * 
     * This is to be used when importing designs, to see if licenses match. If the id and
     * name fields match, then the imported design is linked to the license record. If
     * they don't match, the design should be attached to the "Other" license.
     * 
     * The user selects the license based on the name, hence we have chosen to check the name.
     * 
     * @param otherLicense
     * @return true if they are the same type of license
     */
    public boolean isSameLicenseType(License otherLicense) {
	if (licenseID != null && licenseID.equals(otherLicense.getLicenseID())) {
	    String name1 = (name != null ? StringUtils.strip(name) : "");
	    String name2 = (otherLicense.getName() != null ? StringUtils.strip(otherLicense.getName()) : "");
	    return name1.equals(name2);
	}
	return false;
    }
}
