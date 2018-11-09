/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.zoom.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.lamsfoundation.lams.util.JsonUtil;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity
@Table(name = "tl_lazoom10_api")
public class ZoomApi {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String email;

    @Column(name = "api_key")
    private String key;

    @Column(name = "api_secret")
    private String secret;

    public ZoomApi() {
    }

    public ZoomApi(String email, String key, String value) {
	this.email = email;
	this.key = key;
	this.secret = value;
    }

    public ZoomApi(ObjectNode apiJSON) {
	this.uid = JsonUtil.optLong(apiJSON, "uid");
	this.email = JsonUtil.optString(apiJSON, "email");
	this.key = JsonUtil.optString(apiJSON, "key");
	this.secret = JsonUtil.optString(apiJSON, "secret");
    }

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getSecret() {
	return secret;
    }

    public void setSecret(String value) {
	this.secret = value;
    }

    public ObjectNode toJSON() {
	ObjectNode result = JsonNodeFactory.instance.objectNode();
	result.put("uid", uid);
	result.put("email", email);
	result.put("key", key);
	result.put("secret", secret);
	return result;
    }
}