/*
 * Created on Feb 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.dao.ILicenseDAO;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LicenseDAO extends BaseDAO implements ILicenseDAO{
	
	public License getLicenseByID(Long licenseID){
		return(License)super.find(License.class,licenseID);
	}

}
