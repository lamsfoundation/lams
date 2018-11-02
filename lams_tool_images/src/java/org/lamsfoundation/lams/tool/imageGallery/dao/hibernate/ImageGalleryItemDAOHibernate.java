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

package org.lamsfoundation.lams.tool.imageGallery.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryItemDAO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.springframework.stereotype.Repository;

@Repository
public class ImageGalleryItemDAOHibernate extends LAMSBaseDAO implements ImageGalleryItemDAO {

    private static final String FIND_AUTHORING_ITEMS = "from " + ImageGalleryItem.class.getName()
	    + " where imageGallery_uid = ? order by create_date asc";

    @Override
    public List<ImageGalleryItem> getAuthoringItems(Long imageGalleryUid) {
	return (List<ImageGalleryItem>) this.doFind(FIND_AUTHORING_ITEMS, imageGalleryUid);
    }

    @Override
    public ImageGalleryItem getByUid(Long imageGalleryItemUid) {
	return (ImageGalleryItem) this.getObject(ImageGalleryItem.class, imageGalleryItemUid);
    }
}
