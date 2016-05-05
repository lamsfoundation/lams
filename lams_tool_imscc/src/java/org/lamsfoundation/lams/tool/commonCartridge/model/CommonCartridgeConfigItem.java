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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.commonCartridge.model;

/**
 * @hibernate.class table="tl_laimsc11_configuration"
 */
public class CommonCartridgeConfigItem implements java.io.Serializable {

    private static final long serialVersionUID = 6360672537352753361L;

    public static final String KEY_EXPOSE_USER_NAME = "allowExposeUserName";
    public static final String KEY_EXPOSE_USER_EMAIL = "allowExposeUserEmail";

    Long id;
    String configKey;
    String configValue;

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     *
     */
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @hibernate.property column="config_key" length="30" not-null="false" unique="true"
     */
    public String getConfigKey() {
	return configKey;
    }

    public void setConfigKey(String configKey) {
	this.configKey = configKey;
    }

    /**
     * @hibernate.property column="config_value" length="255" not-null="false"
     */
    public String getConfigValue() {
	return configValue;
    }

    public void setConfigValue(String configValue) {
	this.configValue = configValue;
    }
}
