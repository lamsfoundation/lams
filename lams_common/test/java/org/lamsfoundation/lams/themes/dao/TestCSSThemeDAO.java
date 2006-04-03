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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
import org.lamsfoundation.lams.themes.CSSProperty;
import org.lamsfoundation.lams.themes.CSSStyle;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
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
        CSSThemeVisualElement theme = themeDAO.getThemeById(RUBY_THEME_ID);
        assertTrue("Theme returned is Ruby theme",
                theme!=null && RUBY_THEME_NAME.equals(theme.getName()));
    }

    public void testGetThemeByName() {
        List list = themeDAO.getThemeByName(RUBY_THEME_NAME);
        assertTrue("At least one theme returned", list != null && list.size() > 0 );
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            CSSThemeVisualElement element = (CSSThemeVisualElement) iter.next();
            assertEquals("Theme has correct name (ruby)",RUBY_THEME_NAME,element.getName());
        }
    } 

    public void testSaveOrUpdateTheme() {
        try {
            // create a new theme
            CSSThemeVisualElement theme = saveTheme("testSaveOrUpdateTheme", "testSaveOrUpdateTheme description");
            Long themeId = theme.getId();
            CSSStyle baseStyle = theme.getStyle();
            Set elements = theme.getElements();
            Iterator iter = elements.iterator();
            CSSThemeVisualElement element = (CSSThemeVisualElement) iter.next();

            // update the theme and check the changes
            theme.setName("testSaveOrUpdateTheme2");
            theme.setDescription("testSaveOrUpdateTheme2 description");
            element.setName("extendingButton");
            themeDAO.saveOrUpdateTheme(theme);
            CSSThemeVisualElement dbTheme = themeDAO.getThemeById(themeId);
            checkTheme("testSaveOrUpdateTheme2", themeId, "testSaveOrUpdateTheme2 description", 
                    "extendingButton", dbTheme);

        } catch (ParseException e) {
            fail("ParseException thrown"+e.getMessage());
        }
    }

 
    public void testDeleteTheme() {
        CSSThemeVisualElement theme = null;
        try {
            theme = saveTheme("testDeleteTheme",  "testDeleteTheme description");
	    } catch (ParseException e) {
	        fail("ParseException thrown"+e.getMessage());
	    }
        Long themeId = theme.getId();

        themeDAO.deleteTheme(theme);
        themeDAO.getHibernateTemplate().flush();
        CSSThemeVisualElement dbTheme = themeDAO.getThemeById(themeId);
        assertNull("Saved theme cannot be retrieved after deletion",dbTheme);
    }

    public void testDeleteThemeById() {
        CSSThemeVisualElement theme = null;
        try {
            theme = saveTheme("testDeleteThemeById", "testDeleteThemeById description");
	    } catch (ParseException e) {
	        fail("ParseException thrown"+e.getMessage());
	    }
        Long themeId = theme.getId();

        themeDAO.deleteThemeById(themeId);
        themeDAO.getHibernateTemplate().flush();
        CSSThemeVisualElement dbTheme = themeDAO.getThemeById(themeId);
        assertNull("Saved theme cannot be retrieved after deletion",dbTheme);
    }
    
    /* ************* End of tests ***********************************/
    
    private CSSThemeVisualElement saveTheme(String themeName, String description) throws ParseException {
        
        CSSThemeVisualElement theme = createNewTheme(themeName, description, "button");
        themeDAO.saveOrUpdateTheme(theme);
        Long themeId = theme.getId();
        themeDAO.getHibernateTemplate().flush();

        CSSThemeVisualElement dbTheme = themeDAO.getThemeById(themeId);
        assertNotNull("Saved theme can be retrieved",dbTheme);
        checkTheme(themeName, themeId, description, "button", dbTheme);
        assertEquals(themeName, dbTheme.getName());
        return dbTheme;
    }
    
    /* if you change any of the values in the new theme, please change checkTheme to match */
    private CSSThemeVisualElement createNewTheme(String themeName, String description, String elementName) {
        
        CSSThemeVisualElement theme = new CSSThemeVisualElement();

        theme.setDescription(description);
        theme.setName(themeName);
        theme.setTheme(true);

        CSSStyle baseStyle = new CSSStyle();
        CSSProperty property = new CSSProperty("borderStyle", "outset", null);
        baseStyle.addProperty(property);
        property = new CSSProperty("rollOverColor", new Long(16711680), null);
        baseStyle.addProperty(property);
        // try it as a double, as that is what WDDX creates
        property = new CSSProperty("color", new Double(12452097), CSSThemeDTO.TEXT_FORMAT_TAG);
        baseStyle.addProperty(property);

        theme.setStyle(baseStyle);

        CSSStyle elementStyle = new CSSStyle();
        property = new CSSProperty("selectionColor", new Double(16711681), null);
        elementStyle.addProperty(property);
        property = new CSSProperty("display", "block", CSSThemeDTO.TEXT_FORMAT_TAG);
        elementStyle.addProperty(property);
        
        CSSThemeVisualElement element = new CSSThemeVisualElement();
        element.setName(elementName);
        element.setStyle(elementStyle);
        element.setTheme(false);

        theme.addElement(element);

        return theme;
    }
    
    /* This attempts to check that the correct values have been written to the database. Doesn't really work
     * as it isn't the db object that it is returned - it is the cached Hibernate object. e.g. if the cascades
     * fail and no CSSThemeVisualElement are written to the database, the tested object contains them anyway!!!!
     *
     * @param themeName
     * @param themeId
     * @param description
     * @param elementName
     * @param theme
     * @throws ParseException
     */
    private void checkTheme(String themeName, Long themeId, String description, String elementName, CSSThemeVisualElement theme) throws ParseException {
      
        assertEquals(theme.getDescription(), description);
        assertEquals(theme.getName(),themeName);
        assertTrue("Theme flag for theme is true", theme.isTheme());
        
        CSSStyle baseStyle = theme.getStyle();
        assertNotNull(baseStyle);
        Set bsproperties = baseStyle.getProperties();
        assertNotNull(bsproperties);
        Iterator iter = bsproperties.iterator();
        while (iter.hasNext()) {
            CSSProperty property = (CSSProperty) iter.next();
            if ( "borderStyle".equals(property.getName()) ) {
                assertEquals(property.getValueAsObject(), "outset");
                assertNull(property.getStyleSubset());
            } else if ( "rollOverColor".equals(property.getName()) ) {
                assertEquals(property.getValueAsObject(), new Long(16711680));
                assertNull(property.getStyleSubset());
            } else if ( "color".equals(property.getName()) ) {
                assertEquals(property.getValueAsObject(), new Double(12452097));
                assertEquals(property.getStyleSubset(), CSSThemeDTO.TEXT_FORMAT_TAG);
            } else {
                fail("Unexpected property in baseStyle "+property);
            }
        }

        Set elements = theme.getElements();
        assertNotNull(elements);
        iter = elements.iterator();
        while (iter.hasNext()) {
            CSSThemeVisualElement element = (CSSThemeVisualElement) iter.next();
            assertEquals(element.getName(), elementName);
            assertNull("Description is null",element.getDescription());
            assertFalse("Theme flag for element is false", element.isTheme());
            assertNotNull(element.getStyle());
            assertNotNull(element.getStyle().getProperties());
            Iterator iter2 = element.getStyle().getProperties().iterator();
            while (iter2.hasNext()) {
                CSSProperty property= (CSSProperty) iter2.next();
	            if ( "selectionColor".equals(property.getName()) ) {
	                assertEquals(property.getValueAsObject(), new Double(16711681));
	                assertNull(property.getStyleSubset());
	            } else if ( "display".equals(property.getName()) ) {
	                assertEquals(property.getValueAsObject(), "block");
	                assertEquals(property.getStyleSubset(), CSSThemeDTO.TEXT_FORMAT_TAG);
	            } else {
	                fail("Unexpected property in baseStyle "+property);
	            }
            }
        }
        
    }
    

}
