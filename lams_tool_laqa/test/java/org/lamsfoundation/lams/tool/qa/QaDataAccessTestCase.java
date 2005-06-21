package org.lamsfoundation.lams.tool.qa;

import java.util.Date;
import java.util.TreeSet;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.qa.dao.hibernate.QaContentDAO;
import org.lamsfoundation.lams.tool.qa.dao.hibernate.QaQueContentDAO;
import org.lamsfoundation.lams.tool.qa.dao.hibernate.QaQueUsrDAO;
import org.lamsfoundation.lams.tool.qa.dao.hibernate.QaSessionDAO;
import org.lamsfoundation.lams.tool.qa.dao.hibernate.QaUsrRespDAO;



/**
 * @author ozgurd
 */
public class QaDataAccessTestCase extends AbstractLamsTestCase
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
	
	protected QaContentDAO qaContentDAO;
	protected QaSessionDAO qaSessionDAO;
	protected QaQueUsrDAO  qaQueUsrDAO;
	protected QaQueContentDAO qaQueContentDAO;
	protected QaUsrRespDAO  qaUsrRespDAO;
	
	
	protected QaSession qaSession;
	protected QaQueContent qaQueContent;
	
	
	public QaDataAccessTestCase(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        qaContentDAO = (QaContentDAO) this.context.getBean("qaContentDAO");
        qaSessionDAO = (QaSessionDAO) this.context.getBean("qaSessionDAO");
        qaQueUsrDAO = (QaQueUsrDAO) this.context.getBean("qaQueUsrDAO");
        qaQueContentDAO = (QaQueContentDAO) this.context.getBean("qaQueContentDAO");
        qaUsrRespDAO = (QaUsrRespDAO) this.context.getBean("qaUsrRespDAO");
        
    }

    protected String[] getContextConfigLocation()
    {
    	System.out.println("QaDataAccessTestCase will be configured");
        return new String[] {"qaCompactApplicationContext.xml"};
    }
    
    protected String getHibernateSessionFactoryName()
    {
    	return "qaCompactSessionFactory";
    }
    
    protected void tearDown() throws Exception
    {
    	super.tearDown();
    }
    
    
    protected QaQueUsr getExistingUser2(String username, String fullname)
    {
    	QaQueContent qaQueContent = qaQueContentDAO.getQaQueById(TEST_NEW_QUE_CONTENT_ID);
		QaSession qaSession = qaSessionDAO.getQaSessionById(TEST_NEW_SESSION_ID);
    	
        QaQueUsr qaQueUsr= new QaQueUsr(new Long(TEST_NEW_USER_ID),
    									username,
										fullname,
										qaQueContent, 
										qaSession, 
										new TreeSet());
    	qaQueUsrDAO.createUsr(qaQueUsr);
    	return qaQueUsr;
    }
    
    
    protected QaQueUsr getExistingUser(String username, String fullname)
    {
    	QaQueContent qaQueContent = qaQueContentDAO.getQaQueById(TEST_EXISTING_QUE_CONTENT_ID);
		QaSession qaSession = qaSessionDAO.getQaSessionById(TEST_EXISTING_SESSION_ID);
    	
        QaQueUsr qaQueUsr= new QaQueUsr(new Long(TEST_NEW_USER_ID),
    									username,
										fullname,
										qaQueContent, 
										qaSession, 
										new TreeSet());
    	qaQueUsrDAO.createUsr(qaQueUsr);
    	return qaQueUsr;
    }
    
    
    protected QaUsrResp getNewResponse(String response, QaQueContent qaQueContent)
    {
    	QaUsrResp qaUsrResp= new QaUsrResp(response, false,
											new Date(System.currentTimeMillis()),
											qaQueContent,
											getExistingUser("randomstriker","Michael Random"));
		qaUsrRespDAO.createUserResponse(qaUsrResp);
		return qaUsrResp;
    }
    
    protected QaUsrResp getExistingResponse(String response)
    {
    	QaQueContent qaQueContent = qaQueContentDAO.getQaQueById(TEST_EXISTING_QUE_CONTENT_ID);
    	
		QaUsrResp qaUsrResp= new QaUsrResp(response, false,
											new Date(System.currentTimeMillis()),
											qaQueContent,
											getExistingUser("randomstriker","Michael Random"));
		qaUsrRespDAO.createUserResponse(qaUsrResp);
		return qaUsrResp;
    }
    
    
    
    

}
