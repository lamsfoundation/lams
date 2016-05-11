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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.themes.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dao.IThemeDAO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 *
 * @author Mitchell Seaton
 *
 */
public class ThemeService implements IThemeService {

    protected Logger log = Logger.getLogger(ThemeService.class);

    /** Required DAO's */
    protected IThemeDAO themeDAO;
    protected IUserManagementService userManagementService;

    public ThemeService() {

    }

    /**********************************************
     * Setter Methods
     *******************************************/

    /**
     * @return Returns the themeDAO.
     */
    public IThemeDAO getThemeDAO() {
	return themeDAO;
    }

    /**
     *
     * @param themeDAO
     *            The ICSSThemeDAO to set.
     */
    public void setThemeDAO(IThemeDAO themeDAO) {
	this.themeDAO = themeDAO;
    }

    /**
     *
     * @param IUserManagementService
     *            The userManagementService to set.
     */
    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    /**
     * Returns a theme
     */
    @Override
    public Theme getTheme(Long themeId) {
	return themeDAO.getThemeById(themeId);
    }

    /**
     * Returns a theme based on the name.
     */
    @Override
    public Theme getTheme(String themeName) {
	List<Theme> themes = themeDAO.getThemeByName(themeName);
	if ((themes != null) && (themes.size() > 0)) {
	    return themes.get(0);
	} else {
	    return null;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#getAllThemes()
     */
    @Override
    public List<Theme> getAllThemes() {
	return themeDAO.getAllThemes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#removeTheme(java.lang.Long)
     */
    @Override
    public void removeTheme(Long themeId) {
	themeDAO.deleteThemeById(themeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#saveOrUpdateTheme(org.lamsfoundation.lams.themes.Theme)
     */
    @Override
    public void saveOrUpdateTheme(Theme theme) {
	themeDAO.saveOrUpdateTheme(theme);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.themes.service.IThemeService#getDefaultTheme()
     */
    @Override
    public Theme getDefaultTheme() {
	List<Theme> themes = getAllThemes();
	String defaultTheme = Configuration.get(ConfigurationKeys.DEFAULT_THEME);
	for (Theme theme : themes) {
	    if (theme.getName().equals(defaultTheme)) {
		return theme;
	    }
	}

	return null;
    }
}