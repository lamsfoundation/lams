/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.authoring.service;



import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dto.LicenseDTO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.WorkspaceFolderDAO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;

/**
 * @author Manpreet Minhas 
 */
public class TestAuthoringService extends AbstractLamsTestCase {
	
	private IAuthoringService authService;
	private LearningDesignDAO learningDesignDAO;
	private UserDAO userDAO;
	private WorkspaceFolderDAO workspaceFolderDAO;
	
	private static final Long TEST_THEME_ID = new Long(1);
	public TestAuthoringService(String name) {
		super(name);
	}
		
	
	protected void setUp()throws Exception{
		super.setUp();
		authService =(IAuthoringService)context.getBean("authoringService");
		learningDesignDAO = (LearningDesignDAO)context.getBean("learningDesignDAO");
		userDAO = (UserDAO)context.getBean("userDAO");
		workspaceFolderDAO =(WorkspaceFolderDAO)context.getBean("workspaceFolderDAO");
	}	
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";		
	}
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/localApplicationContext.xml",
							 "org/lamsfoundation/lams/authoring/authoringApplicationContext.xml"};	
	}
	public void testCopyLearningdesign()throws UserException, WorkspaceFolderException, LearningDesignException, IOException{			
		LearningDesign design = authService.copyLearningDesign(new Long(1),
															   new Integer(1),
															   new Integer(1),
															   new Integer(1),
															   true);
	} 
	// TODO Check that this packet structure is still what is coming from Flash, and change test case to actually check the values are updated.
	public void testGetLearningDesignDetails()throws Exception{
		String str = authService.getLearningDesignDetails(new Long(1));
		System.out.println("Design Details:" + str);
		
	}	
	public void testGetAllLearningDesignDetails() throws Exception{
		String str = authService.getAllLearningDesignDetails();
		System.out.println("Learning Design Deatils: " + str);
	} 
	public void testGetAllLearningLibraryDetails()throws Exception{
		String packet = authService.getAllLearningLibraryDetails();
		System.out.println("Library Details: "+ packet);
	}
	public void testStoreLearningDesignDetails() throws Exception{	
		String design = LEARNING_DESIGN_PART_A+"-111111"+LEARNING_DESIGN_PART_B;
		String str = authService.storeLearningDesignDetails(design);
		assertTrue("storeLearningDesignDetails returned WDDX packet", str!=null && str.startsWith("<wddxPacket"));
		System.out.println(str);		
		Long id = extractIdFromWDDXPacket(str);

		design = LEARNING_DESIGN_PART_A+id.toString()+LEARNING_DESIGN_PART_B;
		str = authService.storeLearningDesignDetails(design);
		assertTrue("storeLearningDesignDetails(2) returned WDDX packet", str!=null && str.startsWith("<wddxPacket"));
		System.out.println(str);
		Long id2 = extractIdFromWDDXPacket(str);

		assertEquals("Second store has updated as expected", id, id2);
	}
	
	public void testStoreTheme() throws Exception{		
		String str = authService.storeTheme(TEST_NEW_THEME_WDDX);
		assertTrue("storeTheme returned WDDX packet", str!=null && str.startsWith("<wddxPacket"));
		System.out.println(str);
		Long id = extractIdFromWDDXPacket(str);
		
		String updatePacket = TEST_NEW_THEME_WDDX2_PART1 + id.toString() + TEST_NEW_THEME_WDDX2_PART2;
		str = authService.storeTheme(updatePacket);
		assertTrue("storeTheme(2) returned WDDX packet", str!=null && str.startsWith("<wddxPacket"));
		System.out.println(str);
		Long id2 = extractIdFromWDDXPacket(str);
		
		assertEquals("Second store has updated as expected", id, id2);
	}


    public void testGetThemes() throws Exception{		
		String str = authService.getThemes();
		System.out.println(str);
		assertTrue("At least one theme found "+str, str!=null && str.length() > NO_THEME_RESPONSE.length());
		
    }

	public void testGetTheme() throws Exception{		
		String str = authService.getTheme(TEST_THEME_ID);
		System.out.println(str);
		assertTrue("Finds ruby theme", str.indexOf("ruby") != -1);
	} 

	public void testGetAvailableLicenses() throws Exception{
		String otherLicenseCode = "other";
		Set ccCodes = new HashSet();  // creative commons codes
		ccCodes.add("by-nc-sa");
		ccCodes.add("by-nd");
		ccCodes.add("by-nc-nd");
		ccCodes.add("by-nc");
		ccCodes.add("by-sa");
		
		Vector v = authService.getAvailableLicenses();
		assertNotNull("getAvailableLicenses() returns some values",v);
		assertTrue("getAvailableLicenses() returns 6 licenses", v.size() == 6);
		
		// now check that by-nc-sa is default and that the expected five 
		// commons licenses + other exists. For each one, check each field
		// is populated for the creative commons entries. Other has a blank
		// url and picture url
		Iterator iter = v.iterator();
		int numDefaultLicenses = 0;
		boolean otherLicenseCodeFound = false;
		while (iter.hasNext()) {
			
			LicenseDTO element = (LicenseDTO) iter.next();
			String code = element.getCode();
			
			assertNotNull("License has a code", code);
			assertNotNull("License "+code+" has an id ", element.getLicenseID());
			assertNotNull("License "+code+" has a name ", element.getName());
			if ( element.getDefaultLicense().booleanValue() ) {
				numDefaultLicenses ++;
			}
			
			if ( code.equals(otherLicenseCode) ) {
				otherLicenseCodeFound = true;
			} else if ( ccCodes.contains(code) ) {
				assertNotNull("License "+code+" has a URL ", element.getUrl());
				assertNotNull("License "+code+" has an pictureURL ", element.getPictureURL());
				ccCodes.remove(code);
			} else {
				fail("License has an unexpected or duplicated code "+code);
			}
				
		}
		assertTrue("OTHER license code found ",otherLicenseCodeFound);
		assertTrue("All expected creative commons licenses were found ",ccCodes.size()==0);
		
	} 

    /*  ******* WDDX Packets **************************************/
    private static final String LEARNING_DESIGN_PART_A = 
        "<wddxPacket version='1.0'><header/><data><struct>"
        +"<var name='workspaceFolderID'><number>6</number></var>"
        +"<var name='version'><string>1.0</string></var>"
        +"<var name='validDesign'><boolean value='true'/></var>"
        +"<var name='userID'><number>4</number></var>"
        +"<var name='transitions'><array length='2'><struct>"
        +"<var name='transitionUIID'><number>1</number></var>"
        +"<var name='transitionId'><number>-111111</number></var>"
        +"<var name='toUIID'><number>2</number></var>"
        +"<var name='toActivityID'><number>-111111</number></var>"
        +"<var name='title'><string>Submit Files to Noticeboard</string></var>"
        +"<var name='learningDesignID'><number>";
    
    private static final String LEARNING_DESIGN_PART_B = 
        "</number></var>"
        +"<var name='fromUIID'><number>1</number></var>"
        +"<var name='fromActivityID'><number>-111111</number></var>"
        +"<var name='description'><string>Submit files to Noticeboard</string></var>"
        +"<var name='createDateTime'><dateTime>2005-2-7T1:0:23</dateTime></var></struct><struct>"
        +"<var name='transitionUIID'><number>2</number></var>"
        +"<var name='transitionId'><number>-111111</number></var>"
        +"<var name='toUIID'><number>3</number></var>"
        +"<var name='toActivityID'><number>-111111</number></var>"
        +"<var name='title'><string>Noticeboard to Notebook</string></var>"
        +"<var name='learningDesignID'><number>1</number></var>"
        +"<var name='fromUIID'><number>2</number></var>"
        +"<var name='fromActivityID'><number>-111111</number></var>"
        +"<var name='description'><string>Noticeboard to notebook</string></var>"
        +"<var name='createDateTime'><dateTime>2005-2-7T1:0:23</dateTime></var></struct></array></var>"
        +"<var name='title'><string>Test Learning Design title</string></var>"
        +"<var name='readOnly'><boolean value='false'/></var>"
        +"<var name='originalLearningDesignID'><number>-111111</number></var>"
        +"<var name='maxID'><number>1</number></var>"
        +"<var name='licenseText'><string>string__value</string></var>"
        +"<var name='licenseID'><number>-111111</number></var>"
        +"<var name='lessonStartDateTime'><dateTime>1970-1-1T11:0:23</dateTime></var>"
        +"<var name='lessonOrgName'><string>string__value</string></var>"
        +"<var name='lessonOrgID'><number>-111111</number></var>"
        +"<var name='lessonName'><string>string__value</string></var>"
        +"<var name='lessonID'><number>-111111</number></var>"
        +"<var name='learningDesignUIID'><number>1</number></var>"
        +"<var name='learningDesignId'><number>-111111</number></var>"
        +"<var name='lastModifiedDateTime'><dateTime>1970-1-1T11:0:23</dateTime></var>"
        +"<var name='helpText'><string>Help Text</string></var>"
        +"<var name='firstActivityUIID'><number>1</number></var>"
        +"<var name='firstActivityID'><number>-111111</number></var>"
        +"<var name='duration'><number>-111111</number></var>"
        +"<var name='description'><string>Test Learning Design</string></var>"
        +"<var name='dateReadOnly'><dateTime>1970-1-1T11:0:23</dateTime></var>"
        +"<var name='createDateTime'><dateTime>2004-12-23T1:0:23</dateTime></var>"
        +"<var name='copyTypeID'><number>1</number></var>"
        +"<var name='activities'><array length='3'><struct>"
        +"<var name='ycoord'><number>20</number></var>"
        +"<var name='xcoord'><number>10</number></var>"
        +"<var name='toolID'><number>10</number></var>"
        +"<var name='toolContentID'><number>-111111</number></var>"
        +"<var name='title'><string>Submit Files</string></var>"
        +"<var name='runOffline'><boolean value='false'/></var>"
        +"<var name='parentUIID'><number>-111111</number></var>"
        +"<var name='parentActivityID'><number>-111111</number></var>"
        +"<var name='orderID'><number>0</number></var>"
        +"<var name='optionsInstructions'><string>string__value</string></var>"
        +"<var name='onlineInstructions'><string>Online Instructions</string></var>"
        +"<var name='offlineInstructions'><string>Submit Files</string></var>"
        +"<var name='minOptions'><number>-111111</number></var>"
        +"<var name='maxOptions'><number>-111111</number></var>"
        +"<var name='libraryActivityUiImage'><string>image</string></var>"
        +"<var name='libraryActivityID'><number>10</number></var>"
        +"<var name='learningLibraryID'><number>10</number></var>"
        +"<var name='learningDesignID'><number>-111111</number></var>"
        +"<var name='helpText'><string>Help Text for Activity</string></var>"
        +"<var name='groupingUIID'><number>-111111</number></var>"
        +"<var name='groupingType'><number>-111111</number></var>"
        +"<var name='groupingSupportType'><number>1</number></var>"
        +"<var name='groupingID'><number>-111111</number></var>"
        +"<var name='gateStartTimeOffset'><number>-111111</number></var>"
        +"<var name='gateStartDateTime'><dateTime>1970-1-1T11:0:23</dateTime></var>"
        +"<var name='gateOpen'><boolean value='false'/></var>"
        +"<var name='gateEndTimeOffset'><number>-111111</number></var>"
        +"<var name='gateEndDateTime'><dateTime>1970-1-1T11:0:23</dateTime></var>"
        +"<var name='gateActivityLevelID'><number>-111111</number></var>"
        +"<var name='description'><string>Submit Files</string></var>"
        +"<var name='defineLater'><boolean value='false'/></var>"
        +"<var name='createGroupingUIID'><number>-111111</number></var>"
        +"<var name='createGroupingID'><number>-111111</number></var>"
        +"<var name='createDateTime'><dateTime>2005-1-1T1:0:23</dateTime></var>"
        +"<var name='applyGrouping'><boolean value='false'/></var>"
        +"<var name='activityUIID'><number>1</number></var>"
        +"<var name='activityTypeID'><number>1</number></var>"
        +"<var name='activityID'><number>-111111</number></var>"
        +"<var name='activityCategoryID'><number>1</number></var></struct><struct>"
        +"<var name='ycoord'><number>20</number></var>"
        +"<var name='xcoord'><number>10</number></var>"
        +"<var name='toolID'><number>2</number></var>"
        +"<var name='toolContentID'><number>-111111</number></var>"
        +"<var name='title'><string>Notebook</string></var>"
        +"<var name='runOffline'><boolean value='false'/></var>"
        +"<var name='parentUIID'><number>-111111</number></var>"
        +"<var name='parentActivityID'><number>-111111</number></var>"
        +"<var name='orderID'><number>0</number></var>"
        +"<var name='optionsInstructions'><string>string__value</string></var>"
        +"<var name='onlineInstructions'><string>Online Instructions</string></var>"
        +"<var name='offlineInstructions'><string>string__value</string></var>"
        +"<var name='minOptions'><number>-111111</number></var>"
        +"<var name='maxOptions'><number>-111111</number></var>"
        +"<var name='libraryActivityUiImage'><string>notebookimage</string></var>"
        +"<var name='libraryActivityID'><number>2</number></var>"
        +"<var name='learningLibraryID'><number>2</number></var>"
        +"<var name='learningDesignID'><number>-111111</number></var>"
        +"<var name='helpText'><string>Help Text for Noticeboard</string></var>"
        +"<var name='groupingUIID'><number>-111111</number></var>"
        +"<var name='groupingType'><number>-111111</number></var>"
        +"<var name='groupingSupportType'><number>1</number></var>"
        +"<var name='groupingID'><number>-111111</number></var>"
        +"<var name='gateStartTimeOffset'><number>-111111</number></var>"
        +"<var name='gateStartDateTime'><dateTime>1970-1-1T11:0:23</dateTime></var>"
        +"<var name='gateOpen'><boolean value='false'/></var>"
        +"<var name='gateEndTimeOffset'><number>-111111</number></var>"
        +"<var name='gateEndDateTime'><dateTime>1970-1-1T11:0:23</dateTime></var>"
        +"<var name='gateActivityLevelID'><number>-111111</number></var>"
        +"<var name='description'><string>Notice Board</string></var>"
        +"<var name='defineLater'><boolean value='false'/></var>"
        +"<var name='createGroupingUIID'><number>-111111</number></var>"
        +"<var name='createGroupingID'><number>-111111</number></var>"
        +"<var name='createDateTime'><dateTime>2005-1-1T1:0:23</dateTime></var>"
        +"<var name='applyGrouping'><boolean value='false'/></var>"
        +"<var name='activityUIID'><number>2</number></var>"
        +"<var name='activityTypeID'><number>1</number></var>"
        +"<var name='activityID'><number>-111111</number></var>"
        +"<var name='activityCategoryID'><number>1</number></var></struct><struct>"
        +"<var name='ycoord'><number>20</number></var>"
        +"<var name='xcoord'><number>10</number></var>"
        +"<var name='toolID'><number>1</number></var>"
        +"<var name='toolContentID'><number>-111111</number></var>"
        +"<var name='title'><string>Notebook Activity Title</string></var>"
        +"<var name='runOffline'><boolean value='false'/></var>"
        +"<var name='parentUIID'><number>-111111</number></var>"
        +"<var name='parentActivityID'><number>-111111</number></var>"
        +"<var name='orderID'><number>0</number></var>"
        +"<var name='optionsInstructions'><string>string__value</string></var>"
        +"<var name='onlineInstructions'><string>Online Instructions</string></var>"
        +"<var name='offlineInstructions'><string>Offline  Instructions </string></var>"
        +"<var name='minOptions'><number>-111111</number></var>"
        +"<var name='maxOptions'><number>-111111</number></var>"
        +"<var name='libraryActivityUiImage'><string>image</string></var>"
        +"<var name='libraryActivityID'><number>1</number></var>"
        +"<var name='learningLibraryID'><number>1</number></var>"
        +"<var name='learningDesignID'><number>-111111</number></var>"
        +"<var name='helpText'><string>Help Text for Activity</string></var>"
        +"<var name='groupingUIID'><number>-111111</number></var>"
        +"<var name='groupingType'><number>-111111</number></var>"
        +"<var name='groupingSupportType'><number>1</number></var>"
        +"<var name='groupingID'><number>-111111</number></var>"
        +"<var name='gateStartTimeOffset'><number>-111111</number></var>"
        +"<var name='gateStartDateTime'><dateTime>1970-1-1T11:0:23</dateTime></var>"
        +"<var name='gateOpen'><boolean value='false'/></var>"
        +"<var name='gateEndTimeOffset'><number>-111111</number></var>"
        +"<var name='gateEndDateTime'><dateTime>1970-1-1T11:0:23</dateTime></var>"
        +"<var name='gateActivityLevelID'><number>-111111</number></var>"
        +"<var name='description'><string>Notebook Activity Description</string></var>"
        +"<var name='defineLater'><boolean value='false'/></var>"
        +"<var name='createGroupingUIID'><number>-111111</number></var>"
        +"<var name='createGroupingID'><number>-111111</number></var>"
        +"<var name='createDateTime'><dateTime>2005-1-1T1:0:23</dateTime></var>"
        +"<var name='applyGrouping'><boolean value='false'/></var>"
        +"<var name='activityUIID'><number>3</number></var>"
        +"<var name='activityTypeID'><number>1</number></var>"
        +"<var name='activityID'><number>-111111</number></var>"
        +"<var name='activityCategoryID'><number>1</number></var></struct></array></var></struct></data></wddxPacket>";
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
