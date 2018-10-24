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

package org.lamsfoundation.lams.tool.imageGallery.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryConfigItem;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.web.form.AdminForm;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/laimag10admin")
public class AdminController {

    @Autowired
    @Qualifier("laimagImageGalleryService")
    private IImageGalleryService igService;

    @Autowired
    @Qualifier("laimagMessageService")
    private MessageService messageService;

    @RequestMapping("/start")
    public String start(@ModelAttribute AdminForm adminForm, HttpServletRequest request) {

	ImageGalleryConfigItem mediumImageDimensionsKey = igService
		.getConfigItem(ImageGalleryConfigItem.KEY_MEDIUM_IMAGE_DIMENSIONS);
	if (mediumImageDimensionsKey != null) {
	    adminForm.setMediumImageDimensions(mediumImageDimensionsKey.getConfigValue());
	}

	ImageGalleryConfigItem thumbnailImageDimensionsKey = igService
		.getConfigItem(ImageGalleryConfigItem.KEY_THUMBNAIL_IMAGE_DIMENSIONS);
	if (thumbnailImageDimensionsKey != null) {
	    adminForm.setThumbnailImageDimensions(thumbnailImageDimensionsKey.getConfigValue());
	}

	request.setAttribute("error", false);
	return "pages/admin/config";
    }

    @RequestMapping("/saveContent")
    public String saveContent(@ModelAttribute AdminForm adminForm, HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = validateAdminForm(adminForm);
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/admin/config";
	}

	ImageGalleryConfigItem mediumImageDimensionsKey = igService
		.getConfigItem(ImageGalleryConfigItem.KEY_MEDIUM_IMAGE_DIMENSIONS);
	mediumImageDimensionsKey.setConfigValue(adminForm.getMediumImageDimensions());
	igService.saveOrUpdateImageGalleryConfigItem(mediumImageDimensionsKey);

	ImageGalleryConfigItem thumbnailImageDimensionsKey = igService
		.getConfigItem(ImageGalleryConfigItem.KEY_THUMBNAIL_IMAGE_DIMENSIONS);
	thumbnailImageDimensionsKey.setConfigValue(adminForm.getThumbnailImageDimensions());
	igService.saveOrUpdateImageGalleryConfigItem(thumbnailImageDimensionsKey);

	request.setAttribute("savedSuccess", true);
	return "pages/admin/config";

    }

    /**
     * Validate ImageGalleryConfigItems.
     */
    private MultiValueMap<String, String> validateAdminForm(AdminForm adminForm) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	if ((adminForm.getMediumImageDimensions() != null) && !adminForm.getMediumImageDimensions().equals("")) {
	    if (!isParsableToInt(adminForm.getMediumImageDimensions())) {
		errorMap.add("GLOBAL", messageService.getMessage("error.entered.values.not.integers"));
	    }
	} else {
	    errorMap.add("GLOBAL", messageService.getMessage("error.required.fields.missing"));
	}

	if ((adminForm.getThumbnailImageDimensions() != null) && !adminForm.getThumbnailImageDimensions().equals("")) {
	    if (!isParsableToInt(adminForm.getThumbnailImageDimensions())) {
		errorMap.add("GLOBAL", messageService.getMessage("error.entered.values.not.integers"));
	    }
	} else {
	    errorMap.add("GLOBAL", messageService.getMessage("error.required.fields.missing"));
	}

	return errorMap;
    }

    /**
     * Checks if entered value is integer.
     */
    private boolean isParsableToInt(String i) {
	try {
	    Integer.parseInt(i);
	    return true;
	} catch (NumberFormatException nfe) {
	    return false;
	}
    }
}
