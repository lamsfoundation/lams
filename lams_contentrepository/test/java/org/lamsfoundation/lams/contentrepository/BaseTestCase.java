package org.lamsfoundation.lams.contentrepository;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.IRepository;
import org.lamsfoundation.lams.contentrepository.IRepositoryAdmin;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.SimpleCredentials;
import org.lamsfoundation.lams.contentrepository.SimpleVersionedNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BaseTestCase extends TestCase {
	protected static ApplicationContext context = null;
	
	protected static IRepositoryAdmin repository = null;
	protected static ITicket ticket = null;
	
	// two workspaces exist initially, and atool has access to both.
	protected static final String INITIAL_WORKSPACE = "atoolWorkspace";
	protected static final Long INITIAL_WORKSPACE_ID = new Long(1);
	protected static final String INITIAL_WORKSPACE_USER = "atool";
	protected static final char[] INITIAL_WORKSPACE_PASSWORD = {'a','t','o','o','l'};
	
	protected static final String SECONDARY_WORKSPACE = "atoolWorkspace2";

	protected static final String TEST_NODE_PATH = "/test";
	protected static final Long TEST_DATA_NODE_ID = new Long(1); // A datanode that should already be in db
	protected static final Long TEST_FILE_NODE_ID = new Long(2); // A filenode that should already be in db	

	protected final String TEXT_FILEPATH = "D:\\eclipse\\notice.html";
	protected final String TEXT_FILENAME = "notice.html";
	protected final String BINARY_FILEPATH = "D:\\eclipse\\startup.jar";
	protected final String BINARY_FILENAME = "startup.jar";

	// directory containing index.html and related files
	// PACKAGE_NUM_FILES should be the number of files in the
	// package excluding the directories e.g 1 html & 6 images = 7
	// no matter how many directories they may be spread across.
	protected final String PACKAGE_DIR_PATH = "C:\\temp\\girakool2003";
	protected final String PACKAGE_TEST_FILE = "images/girafalls3.jpg";
	protected final int PACKAGE_NUM_FILES = 7;
	
	public BaseTestCase(){
		super();
		// Uncomment the following line to get debuggging.
		// BasicConfigurator.configure();
		
		// this is run for each test so once we have it, we don't
		// want to get it again!
		if ( context == null ) {
			context = new ClassPathXmlApplicationContext("/org/lamsfoundation/lams/contentrepository/testContentRepositoryApplicationContext.xml");
		}
		
		if ( repository == null ) {
			// get repository object from bean factory
			repository =(IRepositoryAdmin)context.getBean(IRepository.REPOSITORY_SERVICE_ID);
			ICredentials cred = new SimpleCredentials(INITIAL_WORKSPACE_USER, INITIAL_WORKSPACE_PASSWORD);
			try { 
				ticket = repository.login(cred, INITIAL_WORKSPACE);
			} catch ( Exception e ) {
				failUnexpectedException(e);
			}
		}
	}
	
	protected void failUnexpectedException(Exception e) {
		System.out.println("Unexpected exception: ");
		e.printStackTrace();
		fail("Unexpected exception thrown."+e.getMessage());
	}

	protected void failUnexpectedException(String testName, Exception e) {
		System.out.println(testName+": unexpected exception: ");
		e.printStackTrace();
		fail("Unexpected exception thrown."+e.getMessage());
	}
	
	/** Normally this functionality is handled by the ticket */
	protected SimpleVersionedNode getNode(Long workspaceId, Long nodeId) {
		SimpleVersionedNode loadNode =(SimpleVersionedNode)context.getBean("node", SimpleVersionedNode.class);
		try {
			loadNode.loadData(workspaceId, nodeId, null); // loads the latest version
		} catch (ItemNotFoundException e) {
			e.printStackTrace();
			fail("Latest version of test node not found, id="+nodeId);
		}
		return loadNode;
	}
	
	protected SimpleVersionedNode getTestNode() {
		return getNode(INITIAL_WORKSPACE_ID, TEST_DATA_NODE_ID);
	}
	

}
