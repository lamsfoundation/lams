/*
 * Created on Nov 22, 2004
 *
 * Last modified on Nov 22, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.Date;
import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;


/**
 * TODO Add description here
 *
 * <p>
 * <a href="UserDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class UserDAOTest extends AbstractLamsTestCase{
	private User user = null;
	private UserDAO userDAO = null;
	protected RoleDAO roleDAO;
	protected OrganisationDAO organisationDAO;
	protected OrganisationTypeDAO organisationTypeDAO;	
	protected AuthenticationMethodDAO authenticationMethodDAO;
	
	protected UserOrganisationDAO userOrganisationDAO;
	protected UserOrganisationRoleDAO userOrganisationRoleDAO;
	
	
	public UserDAOTest(String name){
		super(name);
	}	
	protected void setUp() throws Exception{	
		super.setUp();
		userDAO = (UserDAO)context.getBean("userDAO");
		organisationDAO = (OrganisationDAO)context.getBean("organisationDAO");	
		
		organisationTypeDAO =(OrganisationTypeDAO)context.getBean("organisationTypeDAO");		
		authenticationMethodDAO =(AuthenticationMethodDAO)context.getBean("authenticationMethodDAO");
		
		roleDAO = (RoleDAO)context.getBean("roleDAO");		
		userOrganisationDAO = (UserOrganisationDAO)context.getBean("userOrganisationDAO");
		userOrganisationRoleDAO = (UserOrganisationRoleDAO)context.getBean("userOrganisationRoleDAO");
	}	
	public void testGetUser(){
		user = userDAO.getUserById(new Integer(2));
		assertNotNull(user.getLogin());	
	}
	public void testIsMember(){
		Organisation organisation = organisationDAO.getOrganisationById(new Integer(4));
		user = userDAO.getUserById(new Integer(4));		
		boolean member = user.isMember(organisation);
		assertTrue(member);
	}
	protected String[] getContextConfigLocation() {
		return new String[] {"WEB-INF/spring/applicationContext.xml","WEB-INF/spring/learningDesignApplicationContext.xml"};
	}	
	public void testSaveUser(){
		User user = new User();
		user.setLogin("MiniMinhas");
		user.setPassword("MiniMinhas");
		user.setDisabledFlag(new Boolean(false));
		user.setCreateDate(new Date());
		user.setAuthenticationMethod(authenticationMethodDAO.getAuthenticationMethodById(new Integer(2)));
		user.setBaseOrganisation(organisationDAO.getOrganisationById(new Integer(1)));
		user.setUserOrganisationID(new Integer(1));
		userDAO.saveUser(user);		
		user.setUserOrganisationID(createUserOrganisation(user));
		userDAO.updateUser(user);		
	}
	private Integer createUserOrganisation(User user){
		UserOrganisation userOrganisation = new UserOrganisation();
		userOrganisation.setOrganisation(user.getBaseOrganisation());
		userOrganisation.setUser(user);		
		userOrganisationDAO.saveUserOrganisation(userOrganisation);	
		userOrganisation.addUserOrganisationRole(createUserOrganisationRole(userOrganisation));
		userOrganisationDAO.saveOrUpdateUserOrganisation(userOrganisation);
		return userOrganisation.getUserOrganisationId();
	}
	private UserOrganisationRole createUserOrganisationRole(UserOrganisation userOrganisation){
		UserOrganisationRole userOrganisationRole = new UserOrganisationRole();
		userOrganisationRole.setUserOrganisation(userOrganisation);
		userOrganisationRole.setRole(roleDAO.getRoleByName(Role.STAFF));
		userOrganisationRoleDAO.saveUserOrganisationRole(userOrganisationRole);
		return userOrganisationRole;
	}
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
	 */
	protected String getHibernateSessionFactoryName() {
		// TODO Auto-generated method stub
		return null;
	}
}
