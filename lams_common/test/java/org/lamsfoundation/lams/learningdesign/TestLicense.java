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
package org.lamsfoundation.lams.learningdesign;

import org.lamsfoundation.lams.learningdesign.dao.ILicenseDAO;
import org.lamsfoundation.lams.test.AbstractCommonTestCase;

public class TestLicense extends AbstractCommonTestCase {

	protected ILicenseDAO licenseDAO;
	
	private static final Long DEFAULT_BYNCSA_LICENSE_ID=new Long(1);
	private static final String BYNCSA_LICENSE_CODE="by-nc-sa";
	private static final String BYNCSA_LICENSE_NAME="Attribution-Noncommercial-ShareAlike 2.5";
	private static final String BYNCSA_URL ="http://creativecommons.org/licenses/by-nc-sa/2.5/";
	private static final String BYNCSA_PICTURE_URL ="/images/license/byncsa.jpg";
	
    protected void setUp() throws Exception
    {
        super.setUp();
        licenseDAO =(ILicenseDAO) context.getBean("licenseDAO");
		
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestActivity.
     * @param arg0
     */
    public TestLicense(String arg0)
    {
        super(arg0);
    }

    public void testRetrieveLicense()
    {
    	License byncsa = licenseDAO.getLicenseByID(DEFAULT_BYNCSA_LICENSE_ID);
    	assertNotNull("Found license with test id", byncsa);
    	assertEquals(BYNCSA_LICENSE_CODE, byncsa.getCode());
    	assertEquals(BYNCSA_LICENSE_NAME, byncsa.getName());
    	assertTrue("Found license is the default license", byncsa.getDefaultLicense().booleanValue());
    	assertEquals(BYNCSA_URL, byncsa.getUrl());
    	assertEquals(BYNCSA_PICTURE_URL, byncsa.getPictureURL());
    }

    public void testIsSameLicenseType()
    {
    	License byncsa = licenseDAO.getLicenseByID(DEFAULT_BYNCSA_LICENSE_ID);
    	
    	// expect this to match exactly. Only the id and name matter, and the name should have leading and trailing spaces ignored
    	License matchingLicense = new License(DEFAULT_BYNCSA_LICENSE_ID,BYNCSA_LICENSE_NAME,null, null, Boolean.FALSE, null);
    	assertTrue("Manually created license matches as expected",matchingLicense.isSameLicenseType(byncsa));
    	matchingLicense.setName("   "+BYNCSA_LICENSE_NAME+"  ");
    	
    	// Should fail as matching license has different name id
    	License diffNameLicense = new License(DEFAULT_BYNCSA_LICENSE_ID,null,null, null, Boolean.FALSE, null);
    	assertFalse("Test License with no name does not match as expected",diffNameLicense.isSameLicenseType(matchingLicense));
    	diffNameLicense.setName("Attribution-Noncommercial-ShareAlike");
    	assertFalse("Test License with wrong name does not match as expected",diffNameLicense.isSameLicenseType(matchingLicense));

    	// Should fail as matching license has null id
    	License nullIDLicense = new License(null,BYNCSA_LICENSE_NAME,null, null, Boolean.FALSE, null);
    	assertFalse("Test License with null id does not match as expected",nullIDLicense.isSameLicenseType(matchingLicense));

    	// Should fail as both licenses have a null id
    	License nullIDLicense2 = new License(null,BYNCSA_LICENSE_NAME,null, null, Boolean.FALSE, null);
    	assertFalse("Licenses with null ids do not match as expected",nullIDLicense2.isSameLicenseType(nullIDLicense));
}

}
