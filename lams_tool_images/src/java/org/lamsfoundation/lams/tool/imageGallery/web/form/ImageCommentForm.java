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
package org.lamsfoundation.lams.tool.imageGallery.web.form;  

import java.util.Set;
import java.util.TreeSet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageComment;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageCommentComparator;
 
/**
 * ImageComment Form.
 * 
 * @struts.form name="imageCommentForm"
 * @author Andrey Balan
 */
public class ImageCommentForm extends ActionForm {

    private static final long serialVersionUID = 4594113811270724745L;
    
    private ImageGalleryItem image;
    private String sessionMapID;
    private String comment;

    /**
     * Returns ImageGallery order index.
     * 
     * @return ImageGallery order index
     */
    public ImageGalleryItem getImage() {
	return image;
    }

    /**
     * Sets ImageGallery order index.
     * 
     * @param imageUid
     *                ImageGallery order index
     */
    public void setImage(ImageGalleryItem image) {
	this.image = image;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }
}

 