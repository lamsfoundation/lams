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

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.lamsfoundation.lams.util.JsonUtil;

import javax.persistence.*;

@Entity
@Table(name = "tl_lazoom10_api")
public class ZoomApi {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String email;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    public ZoomApi() {
    }

    public ZoomApi(ObjectNode apiJSON) {
	this.uid = JsonUtil.optLong(apiJSON, "uid");
	this.email = JsonUtil.optString(apiJSON, "email");
	this.accountId = JsonUtil.optString(apiJSON, "accountId");
	this.clientId = JsonUtil.optString(apiJSON, "clientId");
	this.clientSecret = JsonUtil.optString(apiJSON, "clientSecret");
    }

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

    public String getAccountId() {
	return accountId;
    }

    public void setAccountId(String accountId) {
	this.accountId = accountId;
    }

    public String getClientId() {
	return clientId;
    }

    public void setClientId(String clientId) {
	this.clientId = clientId;
    }

    public String getClientSecret() {
	return clientSecret;
    }

    public void setClientSecret(String value) {
	this.clientSecret = value;
    }

    public ObjectNode toJSON() {
	ObjectNode result = JsonNodeFactory.instance.objectNode();
	result.put("uid", uid);
	result.put("email", email);
	result.put("accountId", accountId);
	result.put("clientId", clientId);
	result.put("clientSecret", clientSecret);
	return result;
    }
}