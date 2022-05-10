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

package org.lamsfoundation.lams.themes.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dao.IThemeDAO;
import org.springframework.stereotype.Repository;

/**
 * This class retrieve CSSTheme and other related objects.
 *
 * @author Fiona Malikoff
 */
@Repository
public class ThemeDAO extends LAMSBaseDAO implements IThemeDAO {

    /**
     * @see org.lamsfoundation.lams.themes.dao.IThemeDAO#getAllThemes()
     */
    @Override
    public List getAllThemes() {
	return doFindCacheable("from Theme c");
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.IThemeDAO#getThemeById(java.lang.Long)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Theme getThemeById(Long themeId) {
	String queryString = "from Theme c where c.themeId=?";
	List<Theme> list = doFind(queryString, themeId);
	if ((list != null) && (list.size() > 0)) {
	    return list.get(0);
	} else {
	    return null;
	}
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.IThemeDAO#getThemeByName(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Theme> getThemeByName(String name) {
	String queryString = "from Theme c where c.name=?";
	return doFindCacheable(queryString, name);
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.IThemeDAO#saveOrUpdateTheme(org.lamsfoundation.lams.usermanagement.User)
     */
    @Override
    public void saveOrUpdateTheme(Theme theme) {
	getSession().saveOrUpdate(theme);
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.IThemeDAO#deleteTheme(org.lamsfoundation.lams.usermanagement.User)
     */
    @Override
    public void deleteTheme(Theme theme) {
	getSession().delete(theme);
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.IThemeDAO#deleteUserById(java.lang.Integer)
     */
    @Override
    public void deleteThemeById(Long themeId) {
	getSession().delete(getThemeById(themeId));
    }
}