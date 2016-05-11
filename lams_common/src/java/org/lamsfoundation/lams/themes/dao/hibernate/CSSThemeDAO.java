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

import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dao.ICSSThemeDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Theme and other related objects.
 *
 * @author Fiona Malikoff
 */
public class CSSThemeDAO extends HibernateDaoSupport implements ICSSThemeDAO {

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getAllThemes()
     */
    @Override
    public List getAllThemes() {
	return getHibernateTemplate().find("from Theme c");
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getThemeById(java.lang.Long)
     */
    @Override
    public Theme getThemeById(Long themeId) {
	String queryString = "from Theme c where c.themeId=?";
	List list = getHibernateTemplate().find(queryString, themeId);
	if (list != null && list.size() > 0) {
	    return (Theme) list.get(0);
	} else {
	    return null;
	}
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getThemeByName(java.lang.String)
     */
    @Override
    public List getThemeByName(String name) {
	String queryString = "from Theme c where c.name=?";
	return getHibernateTemplate().find(queryString, name);
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#saveOrUpdateTheme(org.lamsfoundation.lams.usermanagement.User)
     */
    @Override
    public void saveOrUpdateTheme(Theme theme) {
	getHibernateTemplate().saveOrUpdate(theme);
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#deleteTheme(org.lamsfoundation.lams.usermanagement.User)
     */
    @Override
    public void deleteTheme(Theme theme) {
	getHibernateTemplate().delete(theme);
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#deleteUserById(java.lang.Integer)
     */
    @Override
    public void deleteThemeById(Long themeId) {
	getHibernateTemplate().delete(getThemeById(themeId));
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getAllCSSThemes()
     */
    @Override
    public List getAllCSSThemes() {
	return getHibernateTemplate().find("from Theme c where c.type=1");
    }

    /**
     * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getAllFlashThemes()
     */
    @Override
    public List getAllFlashThemes() {
	return getHibernateTemplate().find("from Theme c where c.type=2");
    }

}
