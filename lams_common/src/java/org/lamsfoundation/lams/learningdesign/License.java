/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;

import org.lamsfoundation.lams.learningdesign.dto.LicenseDTO;

/**
 * @author Manpreet Minhas
 */
public class License implements Serializable{
	
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
	
	/** default constructor*/
	public License(){
		
	}
	/** full constructor*/
	public License(Long licenseID,
				   String name,
				   String code,
				   String url,
				   Boolean defaultLicense,
				   String pictureURL){
		this.licenseID = licenseID;
		this.name = name;
		this.code = code;
		this.url = url;
		this.defaultLicense = defaultLicense;
		this.pictureURL = pictureURL;
	}
	
	/** Get the standard DTO version of the license. Need the current serverURL
	 * (e.g. http://localhost:8080/lams)
	 * to construct a full address for the local images. */
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
	/** If it is a full url, then it should start with http://, ftp:// etc. 
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
}
