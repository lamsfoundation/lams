/*
 * Created on Nov 26, 2004
 *
 * Last modified on Nov 26, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;
import org.springframework.context.ApplicationContext;
import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.usermanagement.Role;


/**
 * TODO Add description here
 *
 * <p>
 * <a href="RoleDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class RoleDAOTest extends AbstractLamsTestCase {

	private Role role = null;
	private String errorMessage = "";
	private RoleDAO roleDAO = null;
	private ApplicationContext ctx;
	
	public RoleDAOTest(String name){
		super(name);
	}
	protected void setUp() throws Exception{
		super.setUp();		
		roleDAO = (RoleDAO)context.getBean("roleDAO");
	}
	
	protected void tearDown() throws Exception{
		roleDAO = null;
	}
	
	public void testGetAllRoles(){
		assertTrue(roleDAO.getAllRoles().size()>0);
	}

	public void testGetRoleById(){
		errorMessage = "The name of the role gotten by Id 1 is not SYSADMIN";
		role = roleDAO.getRoleById(new Integer(1));
		assertEquals(errorMessage,"SYSADMIN",role.getName());
	}
	
	public void testGetRoleByName(){
		errorMessage = "The Id of the role gotten by name 'SYSADMIN' is not 1";
		role = roleDAO.getRoleByName("SYSADMIN");
		assertEquals(errorMessage,new Integer(1),role.getRoleId());
	}
	/**
     * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
     */
    protected String getHibernateSessionFactoryName()
    {
        return "coreSessionFactory";
    }
    /**
     * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
     */
    protected String[] getContextConfigLocation()
    {
    	return new String[] {"WEB-INF/spring/learningDesignApplicationContext.xml",
		 "WEB-INF/spring/applicationContext.xml"};
    }
}
