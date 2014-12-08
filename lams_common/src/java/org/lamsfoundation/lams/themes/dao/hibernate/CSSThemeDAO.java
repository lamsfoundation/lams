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
/* $$Id$$ */
package org.lamsfoundation.lams.themes.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dao.ICSSThemeDAO;
import org.springframework.stereotype.Repository;

/**
 * This class retrieve CSSTheme and other related objects.
 * 
 * @author Fiona Malikoff
 */
@Repository
public class CSSThemeDAO extends LAMSBaseDAO implements ICSSThemeDAO {

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getAllThemes()
     */
    public List getAllThemes() {
	return doFind("from Theme c");
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getThemeById(java.lang.Long)
     */
    public Theme getThemeById(Long themeId) {
	String queryString = "from Theme c where c.themeId=?";
	List list = doFind(queryString, themeId);
	if (list != null && list.size() > 0)
	    return (Theme) list.get(0);
	else
	    return null;
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getThemeByName(java.lang.String)
     */
    public List getThemeByName(String name) {
	String queryString = "from Theme c where c.name=?";
	return doFind(queryString, name);
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#saveOrUpdateTheme(org.lamsfoundation.lams.usermanagement.User)
     */
    public void saveOrUpdateTheme(Theme theme) {
    	getSession().saveOrUpdate(theme);
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#deleteTheme(org.lamsfoundation.lams.usermanagement.User)
     */
    public void deleteTheme(Theme theme) {
    	getSession().delete(theme);
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#deleteUserById(java.lang.Integer)
     */
    public void deleteThemeById(Long themeId) {
    	getSession().delete(getThemeById(themeId));
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getAllCSSThemes()
     */
    public List getAllCSSThemes() {
	return doFind("from Theme c where c.type=1");
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getAllFlashThemes()
     */
    public List getAllFlashThemes() {
	return doFind("from Theme c where c.type=2");
    }

}
