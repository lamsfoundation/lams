/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.admin.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.ThemeForm;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.service.IThemeService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.util.CSSThemeUtil;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Actions for maintaining and altering system themes
 * 
 * @author Luke Foxton
 */
public class ThemeManagementAction extends LamsDispatchAction {

    private static IThemeService themeService;
    private static Configuration configurationService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// check permission
	if (!request.isUserInRole(Role.SYSADMIN)) {
	    request.setAttribute("errorName", "ThemeManagementAction");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	if (ThemeManagementAction.themeService == null) {
	    ThemeManagementAction.themeService = AdminServiceProxy.getThemeService(getServlet().getServletContext());
	}

	// Get all the themes
	List<Theme> themes = ThemeManagementAction.themeService.getAllThemes();

	// Flag the default and un-editable themes
	String currentCSSTheme = Configuration.get(ConfigurationKeys.DEFAULT_THEME);
	for (Theme theme : themes) {
	    theme.setCurrentDefaultTheme(theme.getName().equals(currentCSSTheme));
	    theme.setNotEditable(theme.getName().equals(CSSThemeUtil.DEFAULT_HTML_THEME));
	}

	request.setAttribute("themes", themes);
	return mapping.findForward("success");
    }

    public ActionForward addOrEditTheme(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThemeForm themeForm = (ThemeForm) form;

	// Update the theme
	Theme theme = null;
	if ((themeForm.getId() != null) && (themeForm.getId() != 0)) {
	    theme = ThemeManagementAction.themeService.getTheme(themeForm.getId());
	} else {
	    theme = new Theme();
	}
	updateThemeFromForm(theme, themeForm);
	ThemeManagementAction.themeService.saveOrUpdateTheme(theme);

	// Set the theme as default, or disable it as default.
	// Disabling restores the system default
	if ((themeForm.getCurrentDefaultTheme() != null) && (themeForm.getCurrentDefaultTheme() == true)) {
	    Configuration.updateItem(ConfigurationKeys.DEFAULT_THEME, themeForm.getName());
	    getConfiguration().persistUpdate();
	} else {
	    String currentTheme = Configuration.get(ConfigurationKeys.DEFAULT_THEME);
	    if (themeForm.getName().equals(currentTheme)) {
		Configuration.updateItem(ConfigurationKeys.DEFAULT_THEME, CSSThemeUtil.DEFAULT_HTML_THEME);
		getConfiguration().persistUpdate();
	    }
	}
	themeForm.clear();
	return unspecified(mapping, themeForm, request, response);
    }

    public ActionForward removeTheme(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// Remove the theme
	ThemeForm themeForm = (ThemeForm) form;
	if (themeForm.getId() != null) {
	    ThemeManagementAction.themeService.removeTheme(themeForm.getId());
	}

	String currentTheme = Configuration.get(ConfigurationKeys.DEFAULT_THEME);
	if (themeForm.getName().equals(currentTheme)) {
	    Configuration.updateItem(ConfigurationKeys.DEFAULT_THEME, CSSThemeUtil.DEFAULT_HTML_THEME);
	    getConfiguration().persistUpdate();
	}

	themeForm.clear();
	return unspecified(mapping, themeForm, request, response);
    }

    public ActionForward setAsDefault(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ThemeForm themeForm = (ThemeForm) form;
	if (themeForm.getName() != null) {
	    Configuration.updateItem(ConfigurationKeys.DEFAULT_THEME, themeForm.getName());
	    getConfiguration().persistUpdate();
	}
	themeForm.clear();
	return unspecified(mapping, themeForm, request, response);
    }

    private Theme updateThemeFromForm(Theme theme, ThemeForm form) {
	theme.setName(form.getName());
	theme.setDescription(form.getDescription());
	theme.setImageDirectory(form.getImageDirectory());
	// theme.setType(Integer.parseInt(form.getType())); no longer in form see LDEV-3674
	return theme;
    }

    private Configuration getConfiguration() {
	if (ThemeManagementAction.configurationService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    ThemeManagementAction.configurationService = (Configuration) ctx.getBean("configurationService");

	}
	return ThemeManagementAction.configurationService;
    }
}
