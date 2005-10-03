package org.lamsfoundation.lams.tool.mc;

import java.util.Date;
import java.util.TreeSet;

import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McQueContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McQueUsrDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McSessionDAO;
import org.lamsfoundation.lams.tool.mc.dao.hibernate.McUsrRespDAO;



/**
 * @author ozgurd
 */
public class McDataAccessTestCase extends AbstractLamsTestCase
{
	//These both refer to the same entry in the db.
	protected final long TEST_EXISTING_CONTENT_ID = 10;
	protected final long DEFAULT_CONTENT_ID = 10;
	 
	protected final long TEST_NEW_CONTENT_ID = 11;
	
	protected final long TEST_EXISTING_SESSION_ID = 101;
	protected final long TEST_NEW_SESSION_ID = 102;
	
	protected final long TEST_NEW_USER_ID = 700;
	protected final long TEST_EXISTING_QUE_CONTENT_ID = 20;
	protected final long TEST_NEW_QUE_CONTENT_ID = 23;
	
	protected final long TEST_NONEXISTING_CONTENT_ID=2475733396382404l;
	
    protected final long ONE_DAY = 60 * 60 * 1000 * 24;
    
    public final String NOT_ATTEMPTED = "NOT_ATTEMPTED";
    public final String INCOMPLETE = "INCOMPLETE";
    public static String COMPLETED = "COMPLETED";
	
	protected McContentDAO mcContentDAO;
	protected McSessionDAO mcSessionDAO;
	protected McQueUsrDAO  mcQueUsrDAO;
	protected McQueContentDAO mcQueContentDAO;
	protected McUsrRespDAO  mcUsrRespDAO;
	
	
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
        mcQueUsrDAO = (McQueUsrDAO) this.context.getBean("mcQueUsrDAO");
        mcQueContentDAO = (McQueContentDAO) this.context.getBean("mcQueContentDAO");
        mcUsrRespDAO = (McUsrRespDAO) this.context.getBean("mcUsrRespDAO");
        
    }

    protected String[] getContextConfigLocation()
    {
    	System.out.println("McDataAccessTestCase will be configured");
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

}
