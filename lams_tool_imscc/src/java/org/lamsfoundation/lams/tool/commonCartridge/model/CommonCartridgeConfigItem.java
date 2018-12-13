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

package org.lamsfoundation.lams.tool.commonCartridge.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tl_laimsc11_configuration")
public class CommonCartridgeConfigItem implements java.io.Serializable {
    private static final long serialVersionUID = 6360672537352753361L;

    public static final String KEY_EXPOSE_USER_NAME = "allowExposeUserName";
    public static final String KEY_EXPOSE_USER_EMAIL = "allowExposeUserEmail";

    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "config_key")
    String configKey;

    @Column(name = "config_value")
    String configValue;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getConfigKey() {
	return configKey;
    }

    public void setConfigKey(String configKey) {
	this.configKey = configKey;
    }

    public String getConfigValue() {
	return configValue;
    }

    public void setConfigValue(String configValue) {
	this.configValue = configValue;
    }
}
