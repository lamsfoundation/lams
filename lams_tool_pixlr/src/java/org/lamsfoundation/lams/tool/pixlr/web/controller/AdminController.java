package org.lamsfoundation.lams.tool.pixlr.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.tool.pixlr.model.PixlrConfigItem;
import org.lamsfoundation.lams.tool.pixlr.service.IPixlrService;
import org.lamsfoundation.lams.tool.pixlr.web.forms.AdminForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 * @version
 *
 *
 *
 *
 *
 */
@Controller
@RequestMapping("/lapixl10admin")
public class AdminController {
    @Autowired
    private IPixlrService pixlrService;

    @RequestMapping("")
    public String unspecified(@ModelAttribute("lapixl10AdminForm") AdminForm lapixl10AdminForm,
	    HttpServletRequest request, HttpServletResponse response) {
	// set up mdlForumService

	PixlrConfigItem pixlrKey = pixlrService.getConfigItem(PixlrConfigItem.KEY_LANGUAGE_CSV);
	if (pixlrKey != null) {
	    lapixl10AdminForm.setLanguagesCSV(pixlrKey.getConfigValue());
	}

	request.setAttribute("error", false);
	return "pages/admin/config";
    }

    @RequestMapping("/saveContent")
    public String saveContent(@ModelAttribute("lapixl10AdminForm") AdminForm lapixl10AdminForm,
	    HttpServletRequest request) {

	if (lapixl10AdminForm.getLanguagesCSV() != null && !lapixl10AdminForm.getLanguagesCSV().equals("")) {
	    // set up mdlForumService

	    PixlrConfigItem pixlrKey = pixlrService.getConfigItem(PixlrConfigItem.KEY_LANGUAGE_CSV);
	    pixlrKey.setConfigValue(lapixl10AdminForm.getLanguagesCSV());
	    pixlrService.saveOrUpdatePixlrConfigItem(pixlrKey);

	    request.setAttribute("savedSuccess", true);
	    return "pages/admin/config";
	} else {
	    request.setAttribute("error", true);
	    return "pages/admin/config";
	}
    }
}
