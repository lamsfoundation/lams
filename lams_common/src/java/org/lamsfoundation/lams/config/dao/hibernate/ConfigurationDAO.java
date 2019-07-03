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

package org.lamsfoundation.lams.config.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.config.ConfigurationItem;
import org.lamsfoundation.lams.config.dao.IConfigurationDAO;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mitchell Seaton
 *
 */
@Repository
public class ConfigurationDAO extends LAMSBaseDAO implements IConfigurationDAO {
    private static final String LOAD_CONFIG_ITEM_BY_KEY = "from configuration in class "
	    + ConfigurationItem.class.getName() + " where configuration.key=:key";

    /**
     * @see org.lamsfoundation.lams.config.dao.IConfigurationlDAO#getAllItems()
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigurationItem> getAllItems() {
	List<ConfigurationItem> result = super.findAll(ConfigurationItem.class);
	// close session here because of LDEV-4801
	getSession().close();
	return result;
    }

    /**
     * @see org.lamsfoundation.lams.config.dao.IConfigurationlDAO#getConfigItemByKey()
     */
    @Override
    public ConfigurationItem getConfigItemByKey(final String configKey) {
	return (ConfigurationItem) getSession().createQuery(ConfigurationDAO.LOAD_CONFIG_ITEM_BY_KEY)
		.setString("key", configKey).uniqueResult();
    }
}