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
package org.lamsfoundation.lams.theme.service;

import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.themes.service.IThemeService;

public class TestThemeService extends AbstractLamsTestCase {
	
	private IThemeService themeService;
	
	private static final Long TEST_THEME_ID = new Long(1);
	public TestThemeService(String name) {
		super(name);
	}
		
	protected void setUp()throws Exception{
		super.setUp();
		themeService =(IThemeService)context.getBean("themeService");
	}	
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";		
	}
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/localApplicationContext.xml" };
	}

	public void testStoreTheme() throws Exception{		
		String str = themeService.storeTheme(TEST_NEW_THEME_WDDX);
		assertTrue("storeTheme returned WDDX packet", str!=null && str.startsWith("<wddxPacket"));
		System.out.println(str);
		Long id = extractIdFromWDDXPacket(str);
		
		String updatePacket = TEST_NEW_THEME_WDDX2_PART1 + id.toString() + TEST_NEW_THEME_WDDX2_PART2;
		str = themeService.storeTheme(updatePacket);
		assertTrue("storeTheme(2) returned WDDX packet", str!=null && str.startsWith("<wddxPacket"));
		System.out.println(str);
		Long id2 = extractIdFromWDDXPacket(str);
		
		assertEquals("Second store has updated as expected", id, id2);
	}

    public void testGetThemes() throws Exception{		
		String str = themeService.getThemes();
		System.out.println(str);
		assertTrue("At least one theme found "+str, str!=null && str.length() > NO_THEME_RESPONSE.length());
		
    }

	public void testGetTheme() throws Exception{		
		String str = themeService.getTheme(TEST_THEME_ID);
		System.out.println(str);
		assertTrue("Finds ruby theme", str.indexOf("ruby") != -1);
	} 

    /*  ******* WDDX Packets **************************************/

	private static final String TEST_NEW_THEME_WDDX = 
	    "<wddxPacket version=\"1.0\"><header /><data>"+
			"<struct>"+
			"<var name=\"baseStyleObject\">"+
			"<struct>"+
				"<var name=\"borderStyle\"><string>outset</string></var>"+
				"<var name=\"rollOverColor\"><number>16711680</number></var>"+
				"<var name=\"selectionColor\"><number>16711680</number></var>"+
				"<var name=\"themeColor\"><number>16711680</number></var>"+
				"<var name=\"_tf\">"+
				"<struct>"+
					"<var name=\"font\"><string>_sans</string></var>"+
					"<var name=\"size\"><number>10</number></var>"+
					"<var name=\"color\"><number>12452097</number></var>"+
					"<var name=\"display\"><string>block</string></var>"+
				"</struct>"+
				"</var>"+
			"</struct>"+
			"</var>"+ 
			"<var name=\"name\"><string>AuthoringTestTheme</string></var>"+
			"<var name=\"description\"><string>theme used for TestAuthoringService</string></var>"+
			"<var name=\"visualElements\">"+
				"<array length=\"1\">"+
				"<struct>"+
					"<var name=\"name\"><string>button</string></var>"+
					"<var name=\"styleObject\">"+
					"<struct>"+
					"<var name=\"borderStyle\"><string>outset</string></var>"+
					"<var name=\"rollOverColor\"><number>16711680</number></var>"+
					"<var name=\"selectionColor\"><number>16711680</number></var>"+
					"<var name=\"themeColor\"><number>16711680</number></var>"+
					"<var name=\"_tf\">"+
						"<struct>"+
							"<var name=\"color\"><number>7174353</number></var>"+
							"<var name=\"display\"><string>block</string></var>"+
						"</struct>"+
						"</var>"+
					"</struct>"+
					"</var>"+
				"</struct>"+
				"</array>"+
			"</var>"+ 
		"</struct></data></wddxPacket>";

	private static final String TEST_NEW_THEME_WDDX2_PART1 = 
	    "<wddxPacket version=\"1.0\"><header /><data>"+
			"<struct>"+
			"<var name=\"baseStyleObject\">"+
			"<struct>"+
				"<var name=\"borderStyle\"><string>outset2</string></var>"+
				"<var name=\"rollOverColor\"><number>16711681</number></var>"+
				"<var name=\"selectionColor\"><number>16711681</number></var>"+
				"<var name=\"themeColor\"><number>16711681</number></var>"+
				"<var name=\"_tf\">"+
				"<struct>"+
					"<var name=\"font\"><string>_sans</string></var>"+
					"<var name=\"size\"><number>11</number></var>"+
					"<var name=\"color\"><number>12452098</number></var>"+
					"<var name=\"display\"><string>block2</string></var>"+
				"</struct>"+
				"</var>"+
			"</struct>"+
			"</var>"+ 
			"<var name=\"id\"><number>";

	private static final String TEST_NEW_THEME_WDDX2_PART2 = 
	    	"</number></var>"+
			"<var name=\"name\"><string>AuthoringTestTheme2</string></var>"+
			"<var name=\"description\"><string>theme used for TestAuthoringService2</string></var>"+
			"<var name=\"visualElements\">"+
				"<array length=\"1\">"+
				"<struct>"+
					"<var name=\"name\"><string>button</string></var>"+
					"<var name=\"styleObject\">"+
					"<struct>"+
					"<var name=\"borderStyle\"><string>outset2</string></var>"+
					"<var name=\"rollOverColor\"><number>16711681</number></var>"+
					"<var name=\"selectionColor\"><number>16711681</number></var>"+
					"<var name=\"themeColor\"><number>16711681</number></var>"+
					"<var name=\"_tf\">"+
						"<struct>"+
							"<var name=\"color\"><number>7174354</number></var>"+
							"<var name=\"display\"><string>block2</string></var>"+
						"</struct>"+
						"</var>"+
					"</struct>"+
					"</var>"+
				"</struct>"+
				"</array>"+
			"</var>"+ 
		"</struct></data></wddxPacket>";

	private static final String NO_THEME_RESPONSE = 
	    "<wddxPacket version='1.0'><header/><data><struct type='Lorg.lamsfoundation.lams.util.wddx.FlashMessage;'>"
	    +"<var name='messageKey'><string>getThemes</string></var>"
	    +"<var name='messageType'><number>3.0</number></var>"
	    +"<var name='messageValue'><struct></struct></var></struct></data></wddxPacket>";

	
}
