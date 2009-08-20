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
package org.lamsfoundation.lams.themes.dao;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.test.AbstractCommonTestCase;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dao.hibernate.CSSThemeDAO;
import org.lamsfoundation.lams.themes.dto.CSSThemeDTO;

/**
 * @author Fiona Malikoff
 */
public class TestCSSThemeDAO extends AbstractCommonTestCase {

    private CSSThemeDAO themeDAO = null;
    private final static Long RUBY_THEME_ID = new Long(1);
    private final static String RUBY_THEME_NAME = "ruby";
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
		themeDAO =(CSSThemeDAO) context.getBean("themeDAO");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for TestCSSThemeDAO.
     * @param arg0
     */
    public TestCSSThemeDAO(String arg0) {
        super(arg0);
    }

    public void testGetAllThemes() {
        List list = themeDAO.getAllThemes();
        assertTrue("At least one theme returned", list != null && list.size() > 0 );
    }

    public void testGetThemeById() {
        Theme theme = themeDAO.getThemeById(RUBY_THEME_ID);
        assertTrue("Theme returned is Ruby theme",
                theme!=null && RUBY_THEME_NAME.equals(theme.getName()));
    }

    public void testGetThemeByName() {
        List list = themeDAO.getThemeByName(RUBY_THEME_NAME);
        assertTrue("At least one theme returned", list != null && list.size() > 0 );
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Theme element = (Theme) iter.next();
            assertEquals("Theme has correct name (ruby)",RUBY_THEME_NAME,element.getName());
        }
    } 

    public void testSaveOrUpdateTheme() {
        try {
            // create a new theme
            Theme theme = saveTheme("testSaveOrUpdateTheme", "testSaveOrUpdateTheme description");
            Long themeId = theme.getThemeId();

            // update the theme and check the changes
            theme.setName("testSaveOrUpdateTheme2");
            theme.setDescription("testSaveOrUpdateTheme2 description");
            themeDAO.saveOrUpdateTheme(theme);
            Theme dbTheme = themeDAO.getThemeById(themeId);
            checkTheme("testSaveOrUpdateTheme2", themeId, "testSaveOrUpdateTheme2 description", 
                    "extendingButton", dbTheme);

        } catch (ParseException e) {
            fail("ParseException thrown"+e.getMessage());
        }
    }

 
    public void testDeleteTheme() {
        Theme theme = null;
        try {
            theme = saveTheme("testDeleteTheme",  "testDeleteTheme description");
	    } catch (ParseException e) {
	        fail("ParseException thrown"+e.getMessage());
	    }
        Long themeId = theme.getThemeId();

        themeDAO.deleteTheme(theme);
        themeDAO.getHibernateTemplate().flush();
        Theme dbTheme = themeDAO.getThemeById(themeId);
        assertNull("Saved theme cannot be retrieved after deletion",dbTheme);
    }

    public void testDeleteThemeById() {
        Theme theme = null;
        try {
            theme = saveTheme("testDeleteThemeById", "testDeleteThemeById description");
	    } catch (ParseException e) {
	        fail("ParseException thrown"+e.getMessage());
	    }
        Long themeId = theme.getThemeId();

        themeDAO.deleteThemeById(themeId);
        themeDAO.getHibernateTemplate().flush();
        Theme dbTheme = themeDAO.getThemeById(themeId);
        assertNull("Saved theme cannot be retrieved after deletion",dbTheme);
    }
    
    /* ************* End of tests ***********************************/
    
    private Theme saveTheme(String themeName, String description) throws ParseException {
        
        Theme theme = createNewTheme(themeName, description, "button");
        themeDAO.saveOrUpdateTheme(theme);
        Long themeId = theme.getThemeId();
        themeDAO.getHibernateTemplate().flush();

        Theme dbTheme = themeDAO.getThemeById(themeId);
        assertNotNull("Saved theme can be retrieved",dbTheme);
        checkTheme(themeName, themeId, description, "button", dbTheme);
        assertEquals(themeName, dbTheme.getName());
        return dbTheme;
    }
    
    /* if you change any of the values in the new theme, please change checkTheme to match */
    private Theme createNewTheme(String themeName, String description, String elementName) {     
        Theme theme = new Theme();
        theme.setDescription(description);
        theme.setName(themeName);
        Theme element = new Theme();
        element.setName(elementName);
        return theme;
    }
    
    /* This attempts to check that the correct values have been written to the database. Doesn't really work
     * as it isn't the db object that it is returned - it is the cached Hibernate object. e.g. if the cascades
     * fail and no Theme are written to the database, the tested object contains them anyway!!!!
     *
     * @param themeName
     * @param themeId
     * @param description
     * @param elementName
     * @param theme
     * @throws ParseException
     */
    private void checkTheme(String themeName, Long themeId, String description, String elementName, Theme theme) throws ParseException {
      
        assertEquals(theme.getDescription(), description);
        assertEquals(theme.getName(),themeName);
        
    }
    

}
