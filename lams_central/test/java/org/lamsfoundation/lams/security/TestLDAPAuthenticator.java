/* =============================================================
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.security;
import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;


/* $Id$ */

/**
 * @author jliew
 *
 */
public class TestLDAPAuthenticator extends AbstractLamsTestCase {

	UserManagementService service;
	
	public TestLDAPAuthenticator(String name) {
		super(name);
	}
	
	protected void setUp()throws Exception{
		super.setUp();
		service = (UserManagementService)context.getBean("userManagementService");
	}
	
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";		
	}
	
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/localApplicationContext.xml",
				"org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
				"org/lamsfoundation/lams/toolApplicationContext.xml"};
	}
	
	public void testAuthenticator() {
		AuthenticationMethodConfigurer.setConfigFilePath("D:/jboss-4.0.2/server/default/conf/lamsauthentication.xml");
		
		User user = service.getUserByLogin("test");
		AuthenticationMethod method = user.getAuthenticationMethod();
		try {
			AuthenticationMethodConfigurer.configure(method);
		} catch(Exception e) {
			System.out.println("it's an exception: "+e);
		}
		LDAPAuthenticator authenticator = new LDAPAuthenticator(method);
		boolean isValid = authenticator.authenticate(user.getLogin(),"test");
		
		assertTrue(isValid);
	}
}
