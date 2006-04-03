/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.themes.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.themes.dao.ICSSThemeDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve CSSThemeVisualElement and other related objects.
 * 
 * @author Fiona Malikoff
*/
public class CSSThemeDAO extends HibernateDaoSupport implements ICSSThemeDAO {

	/** 
	 * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getAllThemes()
	 */
	public List getAllThemes() {
		return getHibernateTemplate().find("from CSSThemeVisualElement c where c.theme=true");
	}

	/**
	 * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getThemeById(java.lang.Long)
	 */
	public CSSThemeVisualElement getThemeById(Long themeId) {
	    String queryString = "from CSSThemeVisualElement c where c.id=?";
	    List list = getHibernateTemplate().find(queryString,themeId);
	    if ( list != null && list.size() > 0 )
			return (CSSThemeVisualElement) list.get(0);
	    else 
	        return null;
	}

	/** 
	 * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#getThemeByName(java.lang.String)
	 */
	public List getThemeByName(String name) {
		String queryString = "from CSSThemeVisualElement c where c.name=? and c.theme=true";
		return getHibernateTemplate().find(queryString,name);
	}

	/**
	 * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#saveOrUpdateTheme(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void saveOrUpdateTheme(CSSThemeVisualElement theme) {
		getHibernateTemplate().saveOrUpdate(theme);
	}

	/**
	 * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#deleteTheme(org.lamsfoundation.lams.usermanagement.User)
	 */
	public void deleteTheme(CSSThemeVisualElement theme) {
		getHibernateTemplate().delete(theme);
	}

	/**
	 * @see org.lamsfoundation.lams.themes.dao.ICSSThemeDAO#deleteUserById(java.lang.Integer)
	 */
	public void deleteThemeById(Long themeId) {
		getHibernateTemplate().delete(getThemeById(themeId));
	}

}
