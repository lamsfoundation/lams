package org.lamsfoundation.lams.tool.mc;

import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McOptionsContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McQueContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McSessionDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McUserDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McUsrAttemptDAO;

/**
 * @author ozgurd
 */
public class McDataAccessTestCase extends AbstractLamsTestCase
{
	//These both refer to the same entry in the db.
	protected final Long DEFAULT_CONTENT_ID = new Long(10);
	protected final Long TEST_CONTENT_ID = new Long(2);
	protected final Long TEST_CONTENT_ID_OTHER = new Long(3);
	
	protected final Long TEST_SESSION_ID = new Long(20);
	protected final Long TEST_SESSION_ID_OTHER = new Long(21);
	
	protected final Long TEST_QUE_ID1 = new Long(1);
	protected final Long TEST_QUE_OPTION_ID1 = new Long(1);
	protected final Long TEST_QUE_OPTION_ID2 = new Long(2);
	protected final Long TEST_QUE_OPTION_ID3 = new Long(3);
	
	protected final Long TEST_NEW_USER_ID = new Long(100);
	protected final Long TEST_MY_USER_ID = new Long(77);
		
    protected final long ONE_DAY = 60 * 60 * 1000 * 24;
    
    public final String NOT_ATTEMPTED = "NOT_ATTEMPTED";
    public final String INCOMPLETE = "INCOMPLETE";
    public static String COMPLETED = "COMPLETED";
	
	protected McContentDAO mcContentDAO;
	protected McSessionDAO mcSessionDAO;
	protected McUserDAO    mcUserDAO;
	protected McQueContentDAO mcQueContentDAO;
	protected McOptionsContentDAO mcOptionsContentDAO;
	protected McUsrAttemptDAO  mcUsrAttemptDAO;
	
	
	protected McSession mcSession;
	protected McQueContent mcQueContent;
	
	
	public McDataAccessTestCase(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        mcContentDAO = (McContentDAO) this.context.getBean("mcContentDAO");
        mcSessionDAO = (McSessionDAO) this.context.getBean("mcSessionDAO");
        mcUserDAO = (McUserDAO) this.context.getBean("mcUserDAO");
        mcQueContentDAO = (McQueContentDAO) this.context.getBean("mcQueContentDAO");
        mcOptionsContentDAO =(McOptionsContentDAO) this.context.getBean("mcOptionsContentDAO");
        mcUsrAttemptDAO = (McUsrAttemptDAO) this.context.getBean("mcUsrAttemptDAO");
    }

    protected String[] getContextConfigLocation()
    {
    	return new String[] {"/org/lamsfoundation/lams/tool/mc/testmcApplicationContext.xml" };
    }
    
    protected String getHibernateSessionFactoryName()
    {
    	return "mcSessionFactory";
    }
    
    protected void tearDown() throws Exception
    {
    	super.tearDown();
    }
    
    public void testDummy()
    {    
    	System.out.println("dummy McDataAccessTestCase");
    }

}
