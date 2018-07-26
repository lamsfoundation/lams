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

package org.lamsfoundation.lams.tool.imageGallery.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 */
public class AdminForm {
    
    @Autowired
    @Qualifier("laimagMessageService")
    private static MessageService messageService;
    
    
    private static final long serialVersionUID = 414425664356226L;

    private String mediumImageDimensions;

    private String thumbnailImageDimensions;

    public MultiValueMap<String, String> validate(ActionMapping arg0, HttpServletRequest arg1) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<String, String>();
	errorMap.add("GLOBAL", messageService.getMessage("this is an error"));
	return errorMap;
    }

    public String getMediumImageDimensions() {
	return mediumImageDimensions;
    }

    public void setMediumImageDimensions(String mediumImageDimensions) {
	this.mediumImageDimensions = mediumImageDimensions;
    }

    public String getThumbnailImageDimensions() {
	return thumbnailImageDimensions;
    }

    public void setThumbnailImageDimensions(String thumbnailImageDimensions) {
	this.thumbnailImageDimensions = thumbnailImageDimensions;
    }
}
