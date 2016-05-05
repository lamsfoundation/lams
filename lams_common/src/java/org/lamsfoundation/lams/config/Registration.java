/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.config;

/**
 *
 * @hibernate.class table="lams_registration"
 */
public class Registration {
    private long uid;

    private String siteName;
    private String organisation;
    private String name;
    private String email;
    private String serverCountry;
    private boolean publicDirectory;
    private boolean enableLamsCommunityIntegration;
    private String serverKey;
    private String serverID;

    public Registration() {
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     *
     */
    public long getUid() {
	return uid;
    }

    public void setUid(long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="site_name" length="255"
     *
     */
    public String getSiteName() {
	return siteName;
    }

    public void setSiteName(String siteName) {
	this.siteName = siteName;
    }

    /**
     * @hibernate.property column="organisation" length="255"
     *
     */
    public String getOrganisation() {
	return organisation;
    }

    public void setOrganisation(String organisation) {
	this.organisation = organisation;
    }

    /**
     * @hibernate.property column="name" length="255"
     *
     */
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     * @hibernate.property column="email" length="255"
     *
     */
    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    /**
     * @hibernate.property column="server_country" length="2"
     *
     */
    public String getServerCountry() {
	return serverCountry;
    }

    public void setServerCountry(String serverCountry) {
	this.serverCountry = serverCountry;
    }

    /**
     * @hibernate.property column="public_directory" length="1"
     *
     */
    public boolean isPublicDirectory() {
	return publicDirectory;
    }

    public void setPublicDirectory(boolean publicDirectory) {
	this.publicDirectory = publicDirectory;
    }

    /**
     * @hibernate.property column="enable_lams_community" length="1"
     *
     */
    public boolean isEnableLamsCommunityIntegration() {
	return enableLamsCommunityIntegration;
    }

    public void setEnableLamsCommunityIntegration(boolean enableLamsCommunityIntegration) {
	this.enableLamsCommunityIntegration = enableLamsCommunityIntegration;
    }

    /**
     * @hibernate.property column="server_key" length="255"
     *
     */
    public String getServerKey() {
	return serverKey;
    }

    public void setServerKey(String serverKey) {
	this.serverKey = serverKey;
    }

    /**
     * @hibernate.property column="server_id" length="255"
     *
     */
    public String getServerID() {
	return serverID;
    }

    public void setServerID(String serverID) {
	this.serverID = serverID;
    }

}
