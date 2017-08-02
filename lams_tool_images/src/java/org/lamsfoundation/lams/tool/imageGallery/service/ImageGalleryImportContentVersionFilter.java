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

package org.lamsfoundation.lams.tool.imageGallery.service;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;

/**
 * Import filter class for different version of ImageGallery content.
 */
public class ImageGalleryImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 20090819 version content to 20140102 version tool server.
     */
    public void up20090819To20140102() {
	this.removeField(ImageGallery.class, "runOffline");
	this.removeField(ImageGallery.class, "onlineInstructions");
	this.removeField(ImageGallery.class, "offlineInstructions");
	this.removeField(ImageGallery.class, "attachments");
    }

    /**
     * Import 20150217 version content to 20150416 version tool server.
     */
    public void up20150217To20150416() {
	this.addField(ImageGallery.class, "minimumRates", "0");
	this.addField(ImageGallery.class, "maximumRates", "0");
    }

    /**
     * Import 20150416 version content to 20151006 version tool server.
     */
    public void up20150416To20151006() {
	this.removeField(ImageGallery.class, "allowCommentImages");
    }
    
    /**
     * Import 20150416 version content to 20151006 version tool server.
     */
    public void up20170101To20170731() {
	this.removeField(ImageGalleryItem.class, "fileVersionId");
	this.removeField(ImageGalleryItem.class, "fileType");
    }
}