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

package org.lamsfoundation.lams.tool.imageGallery.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryConfigItemDAO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryConfigItem;
import org.springframework.stereotype.Repository;

@Repository
public class ImageGalleryConfigItemDAOHibernate extends LAMSBaseDAO implements ImageGalleryConfigItemDAO {
    private static final String LOAD_CONFIG_ITEM_BY_KEY = "from ImageGalleryConfigItem configuration"
	    + " where configuration.configKey=:key";

    @Override
    public ImageGalleryConfigItem getConfigItemByKey(final String configKey) {
	return getSession().createQuery(LOAD_CONFIG_ITEM_BY_KEY, ImageGalleryConfigItem.class)
		.setParameter("key", configKey).setCacheable(true).uniqueResult();
    }

    @Override
    public void saveOrUpdate(ImageGalleryConfigItem mdlForumConfigItem) {
	getSession().saveOrUpdate(mdlForumConfigItem);
	getSession().flush();
    }
}
