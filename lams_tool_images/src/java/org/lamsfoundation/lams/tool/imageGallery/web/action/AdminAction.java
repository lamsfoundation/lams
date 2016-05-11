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


package org.lamsfoundation.lams.tool.imageGallery.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryConfigItem;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.service.ImageGalleryServiceProxy;
import org.lamsfoundation.lams.tool.imageGallery.web.form.AdminForm;

/**
 * @author Andrey Balan
 * @version
 */
public class AdminAction extends Action {
    private IImageGalleryService imageGalleryService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------ImageGallery Author function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("saveContent")) {
	    return saveContent(mapping, form, request, response);
	}

	return start(mapping, form, request, response);
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up imageGalleryService
	if (imageGalleryService == null) {
	    imageGalleryService = ImageGalleryServiceProxy
		    .getImageGalleryService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	ImageGalleryConfigItem mediumImageDimensionsKey = imageGalleryService
		.getConfigItem(ImageGalleryConfigItem.KEY_MEDIUM_IMAGE_DIMENSIONS);
	if (mediumImageDimensionsKey != null) {
	    adminForm.setMediumImageDimensions(mediumImageDimensionsKey.getConfigValue());
	}

	ImageGalleryConfigItem thumbnailImageDimensionsKey = imageGalleryService
		.getConfigItem(ImageGalleryConfigItem.KEY_THUMBNAIL_IMAGE_DIMENSIONS);
	if (thumbnailImageDimensionsKey != null) {
	    adminForm.setThumbnailImageDimensions(thumbnailImageDimensionsKey.getConfigValue());
	}

	request.setAttribute("error", false);
	return mapping.findForward("config");
    }

    public ActionForward saveContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AdminForm adminForm = (AdminForm) form;

	ActionErrors errors = validateAdminForm(adminForm);
	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    return mapping.findForward("config");
	}

	// set up imageGalleryService
	if (imageGalleryService == null) {
	    imageGalleryService = ImageGalleryServiceProxy
		    .getImageGalleryService(this.getServlet().getServletContext());
	}

	ImageGalleryConfigItem mediumImageDimensionsKey = imageGalleryService
		.getConfigItem(ImageGalleryConfigItem.KEY_MEDIUM_IMAGE_DIMENSIONS);
	mediumImageDimensionsKey.setConfigValue(adminForm.getMediumImageDimensions());
	imageGalleryService.saveOrUpdateImageGalleryConfigItem(mediumImageDimensionsKey);

	ImageGalleryConfigItem thumbnailImageDimensionsKey = imageGalleryService
		.getConfigItem(ImageGalleryConfigItem.KEY_THUMBNAIL_IMAGE_DIMENSIONS);
	thumbnailImageDimensionsKey.setConfigValue(adminForm.getThumbnailImageDimensions());
	imageGalleryService.saveOrUpdateImageGalleryConfigItem(thumbnailImageDimensionsKey);

	request.setAttribute("savedSuccess", true);
	return mapping.findForward("config");

    }

    /**
     * Validate ImageGalleryConfigItems.
     *
     * @param adminForm
     * @return
     */
    private ActionErrors validateAdminForm(AdminForm adminForm) {
	ActionErrors errors = new ActionErrors();

	if ((adminForm.getMediumImageDimensions() != null) && !adminForm.getMediumImageDimensions().equals("")) {
	    if (!isParsableToInt(adminForm.getMediumImageDimensions())) {
		errors.add(ActionMessages.GLOBAL_MESSAGE,
			new ActionMessage(ImageGalleryConstants.ERROR_MSG_ENTERED_VALUES_NOT_INTEGERS));
	    }
	} else {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(ImageGalleryConstants.ERROR_MSG_REQUIRED_FIELDS_MISSING));
	}

	if ((adminForm.getThumbnailImageDimensions() != null) && !adminForm.getThumbnailImageDimensions().equals("")) {
	    if (!isParsableToInt(adminForm.getThumbnailImageDimensions())) {
		errors.add(ActionMessages.GLOBAL_MESSAGE,
			new ActionMessage(ImageGalleryConstants.ERROR_MSG_ENTERED_VALUES_NOT_INTEGERS));
	    }
	} else {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(ImageGalleryConstants.ERROR_MSG_REQUIRED_FIELDS_MISSING));
	}

	return errors;
    }

    /**
     * Checks if entered value is integer.
     *
     * @param i
     *            entered value
     * @return
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
