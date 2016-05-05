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
package org.lamsfoundation.lams.tool.qa;

/**
 *
 */
public class QaConfigItem {

    public static final String KEY_ENABLE_QAWIZARD = "enableQaWizard";

    private Long uid;
    private String configKey;
    private String configValue;

    public QaConfigItem() {
    }

    public QaConfigItem(Long uid, String configKey, String configValue) {
	super();
	this.uid = uid;
	this.configKey = configKey;
	this.configValue = configValue;
    }

    /**
     *
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     */
    public String getConfigKey() {
	return configKey;
    }

    public void setConfigKey(String configKey) {
	this.configKey = configKey;
    }

    /**
     *
     */
    public String getConfigValue() {
	return configValue;
    }

    public void setConfigValue(String configValue) {
	this.configValue = configValue;
    }
}
