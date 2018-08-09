package org.lamsfoundation.lams.tool.dokumaran.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranConfigItem;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;
import org.lamsfoundation.lams.tool.dokumaran.web.form.AdminForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/ladoku11admin")
public class AdminController {

    @Autowired
    @Qualifier("dokumaranService")
    private IDokumaranService dokumaranService;

    @RequestMapping("")
    public String unspecified(@ModelAttribute("ladoku11adminForm") AdminForm ladoku11adminForm,
	    HttpServletRequest request) {

	DokumaranConfigItem etherpadUrl = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_ETHERPAD_URL);
	if (etherpadUrl != null) {
	    ladoku11adminForm.setEtherpadUrl(etherpadUrl.getConfigValue());
	}

	DokumaranConfigItem apiKey = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_API_KEY);
	if (apiKey != null) {
	    ladoku11adminForm.setApiKey(apiKey.getConfigValue());
	}

	request.setAttribute("error", false);
	return "pages/admin/config";
    }

    @RequestMapping("/saveContent")
    public String saveContent(@ModelAttribute("ladoku11adminForm") AdminForm ladoku11adminForm,
	    HttpServletRequest request) {

	if (ladoku11adminForm.getApiKey() != null && !ladoku11adminForm.getApiKey().equals("")) {

	    DokumaranConfigItem etherpadUrl = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_ETHERPAD_URL);
	    etherpadUrl.setConfigValue(ladoku11adminForm.getEtherpadUrl());
	    dokumaranService.saveOrUpdateDokumaranConfigItem(etherpadUrl);

	    DokumaranConfigItem apiKey = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_API_KEY);
	    apiKey.setConfigValue(ladoku11adminForm.getApiKey());
	    dokumaranService.saveOrUpdateDokumaranConfigItem(apiKey);

	    request.setAttribute("savedSuccess", true);
	    return "pages/admin/config";
	} else {
	    request.setAttribute("error", true);
	    return "pages/admin/config";
	}
    }
}
