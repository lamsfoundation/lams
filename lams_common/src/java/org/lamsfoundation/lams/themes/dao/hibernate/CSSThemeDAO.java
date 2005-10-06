/*
 * Created on Nov 20, 2004
 *
 * Last modified on 20 Nov, 2004 
 */
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
