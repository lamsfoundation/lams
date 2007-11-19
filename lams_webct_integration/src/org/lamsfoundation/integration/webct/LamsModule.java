package org.lamsfoundation.integration.webct;

import java.io.StringWriter;
import java.net.URLEncoder;
import java.net.URL;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webct.platform.sdk.context.gen.*;
import com.webct.platform.sdk.context.client.*;
import com.webct.platform.sdk.security.authentication.module.AuthenticationModule;
import com.webct.platform.sdk.proxytool.common.ProcessCallback;
import com.webct.platform.sdk.security.authentication.module.WebCTSSOContext;
import com.webct.platform.sdk.context.client.ContextSDK;
import com.webct.platform.sdk.context.gen.SessionVO;
import com.webct.platform.sdk.context.gen.LearningCtxtVO;
import com.webct.platform.sdkext.authmoduledata.*;
import com.webct.platform.sdk.context.gen.SessionVO;

//-------- Velocity imports --------------
import java.io.StringWriter; 
import java.rmi.RemoteException;
import java.sql.Date;

import org.apache.velocity.VelocityContext; 
import org.apache.velocity.Template; 
import org.apache.velocity.app.VelocityEngine; 
import org.apache.velocity.exception.*;
//import org.apache.axis.AxisFault;

import java.net.URL;
import org.apache.log4j.Logger;

import org.lamsfoundation.integration.webct.LamsSecurityUtil;
import org.lamsfoundation.integration.dao.LamsLessonDaoJDBC;
import org.lamsfoundation.integration.util.Constants;


/**
 * @author Luke Foxton
 *
 */
public class LamsModule extends AuthenticationModule
{	
    
	public static final String VERSION = "1.0.0";
	public static final String JARSTR = "lams2-webct-integration-" + VERSION + ".jar";
    private HttpServletRequest request = null;
    private Map settings = null;
    private String lamsServerUrl;
    private String lamsServerId;
    private String lamsServerSecretKey;
    private String webctRequestSource;

    

    
    private static final Logger log = Logger.getLogger(LamsModule.class);
    
    
    
    private static final String GUTENBERG_QUERY_URL = "http://www.gutenberg.org/catalog/world/results";
    
    
    public LamsModule()
    {
        super();	
    }
    
    public LamsModule(Hashtable hashtable)
    {
        super(hashtable);	
    }
    
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options)
    {
        super.initialize(subject, callbackHandler, sharedState, options);
    }
    
    public boolean validate() 
    {
    	return true;    
    }
    
    public boolean login() throws LoginException
    {
    	return true;
    }
    	
    public boolean abort() throws LoginException
    {
    	return super.abort();
    }
    
    public boolean commit() throws LoginException
    {        
    	if(!super.getCurrentMode().equals(super.OUTGOING_MODE))
    	{
    		// shouldn't be here
    		return false;
    	}
    	

    	
    	Map settings = super.getSettings();
    	//user = super.getUserId();
		lamsServerUrl = (String)settings.get(Constants.SETTING_LAMS_SERVER_URL);
        lamsServerId = (String)settings.get(Constants.SETTING_SERVER_ID);
        lamsServerSecretKey = (String)settings.get(Constants.SETTING_SECRET_KEY);
        webctRequestSource = (String)settings.get(Constants.SETTING_REQUEST_SRC);
    	

		UserVO user = null;
		List roles;
		Long lcID = super.getCurrentLearningContextId();
		
		request = super.getRequest();
		Map params = new HashMap();            
        String html=null;
        String authorUrl = null;
        String learningDesigns = null;
        String learnerUrl = "";
        String monitorUrl = "";
		
		String ptid = request.getParameter("id");
		if(ptid==null)
	    {
	        // the id param was null, so PT was probably called from My WebCT
	        ptid = request.getParameter("tid");
	        
	        params.put("id_param_name", "tid");
	    }
		else
		{
			 params.put("id_param_name", "id");
		}
		params.put("pt_id", ptid);
		params.put("page_id", request.getParameter("page_id"));

	    
		boolean isTeacher = false;
		boolean isStudent = false;
		try {
    		user = UserService.getInstance().getUser(super.getUserId(), lcID);
    		roles = user.getUserRoles();
    		Iterator it = roles.iterator();
    		while (it.hasNext())
    		{
    			String role = it.next().toString().trim();
    			
    			System.out.println("ROLE: " + role);
    			if (role.equals(UserRole.COURSE_INSTRUCTOR_ROLE.toString()) || role.equals(UserRole.SECTION_INSTRUCTOR_ROLE.toString()) )
    			{
    				isTeacher=true;
    			}
    			else if (role.equals(UserRole.STUDENT_ROLE.toString()))
    			{
    				isStudent=true;
    			}			
    		}
    		if (!isStudent && !isTeacher)
    		{
				throw new LoginException("User's role does not have access to these pages.");
			}	
		}
		catch (VistaDataException e)
		{
			log.error("Problem getting user info:", e);
        	e.printStackTrace();
        	return false;
		}
		
        
        
        String action = request.getParameter("form_action");
        
        if(action==null || action.trim().length()==0 ||action.equals("preview"))
        {
        	
        	try{
        		learnerUrl = generateRequestURL(user, lcID, "learner");
        	}
        	catch (Exception e)
        	{
        		throw new LoginException("Bad learner request: " + e.getMessage());
        	}
        	
        	if (isTeacher)
            {
            	// generate teacher page
        		// ie list of running lessons, and a create new lesson button
        		try{
        			LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);
        			List lessons = lessonDao.getDBLessons(lcID.longValue(), Long.parseLong(ptid));

        			monitorUrl = generateRequestURL(user, lcID, "monitor");
        			
        			params.put("lessons", lessons);
        			params.put("learnerUrl", learnerUrl);
        			params.put("monitorUrl", monitorUrl);
        			params.put("liveEditUrl", authorUrl);
        			
        			html = this.generatePage("web/teach.vm", params);
            	}
            	catch (Exception e)
            	{
            		log.error("Error creating LAMS teach page", e);
            		throw new LoginException("Error creating LAMS teach page");
            	}
            }
            else
            {
            	// generate student page
            	// ie list of running lessons
            	try{
            		LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);

        			// test
        			//List lessons = lessonDao.getDBLessons(1);
        			
        			List lessons = lessonDao.getDBLessons(lcID.longValue(), Long.parseLong(ptid));
        			
        			params.put("lessons", lessons);
        			
        			params.put("learnerUrl", learnerUrl);
        			
            		html = this.generatePage("web/learner.vm", params);
            	}
            	catch (Exception e)
            	{
            		throw new LoginException("Error creating LAMS learner page");
            	}
            }
        }
        else if (action.equalsIgnoreCase("create_lesson"))
        {
        	// goto create lesson form
        	try {
            	// get the authorUrl for the author link
            	authorUrl = generateRequestURL(user, lcID, "author");
            	params.put("authorUrl", authorUrl);
            	
            	
            	
            	String previewUrl = LamsSecurityUtil.generatePreviewUrl(
            			lamsServerUrl, 
            			lamsServerId, 
            			lamsServerSecretKey,
            			webctRequestSource,
            			user.getUserId(), 
            			"" +lcID, 
            			"en", 
            			"US", 
            			user.getFirstname(), 
            			user.getLastname(), 
            			user.getEmail());
            	params.put("previewUrl", previewUrl);

            	
            	// get the learning designs to display the workspace tree
            	learningDesigns = getLearningDesigns(user, lcID);
            	params.put("learningDesigns", learningDesigns);
            	
            	html = this.generatePage("web/create.vm", params);
        	
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();
        		log.error("Error creating LAMS create lesson page: ", e);
        		throw new LoginException("Error creating LAMS create lesson page: " + e.getMessage());
        	}
        }
        else if (action.equals("delete_lesson"))
        {
        	String successMessage="LAMS lesson deleted.";
        	
        	String lsID = request.getParameter("lsID");
        	LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);

        	boolean success = lessonDao.deleteDbLesson(Long.parseLong(lsID));

        	if (!success)
        	{
        		successMessage="Failed to delete LAMS lesson.";
        	}
        	
        	
        	params.put("successMessage", successMessage);
        	
        	try{
        		html = this.generatePage("web/lessonCreated.vm", params);

        	}
        	catch (Exception e)
        	{
        		throw new LoginException("Error creating LAMSpage: " + e.getMessage());
        	}
        	
        }
        else if (action.equalsIgnoreCase("start_lesson"))
        {
        	String seqID = request.getParameter("sequence_id");
        	String title = request.getParameter("title");
        	String description = request.getParameter("description");
        	String successMessage="";
        	
        	long lsID = LamsSecurityUtil.startLesson(
        			lamsServerUrl, 
        			lamsServerId, 
        			lamsServerSecretKey,
        			webctRequestSource, 
        			user.getUserId(), 
        			"" + seqID, 
        			"en", 
        			"US", 
        			user.getFirstname(), 
        			user.getLastname(), 
        			user.getEmail(), 
        			seqID, 
        			title, 
        			description);
        	
        	
        	if (lsID==-1)
        	{
        		successMessage="Failed to create LAMS lesson.";
        	}
        	else
        	{
            	LamsLessonDaoJDBC lessonDao = new LamsLessonDaoJDBC(settings);
            	LamsLesson lesson = new LamsLesson(
            			lsID, 
            			Long.parseLong(ptid),
            			lcID.longValue(), 
    					Long.parseLong(seqID), 
    					title,
    					description, 
    					user.getUserId(), 
    					user.getFirstname(),
    					user.getLastname(), 
    					false, 
    					false,
    					new Date(0), 
    					new Date(0)
            			);
            	
            	try{
            		// create the lesson
            		lessonDao.createDbLesson(lesson);
            	}
            	catch (Exception e)
            	{
            		log.error("Could not create LAMS lesson", e);
            		throw new LoginException("Could not create LAMS lesson");
            	}
        	
            	successMessage="LAMS lesson started.";
        	}
        	
        	params.put("successMessage", successMessage);
        	
        	try{
        		html = this.generatePage("web/lessonCreated.vm", params);
        	
        	
        	}
        	catch (Exception e)
        	{
        		log.error("Error creating LAMS lesson created page: ", e);
        		throw new LoginException("Error creating LAMS lesson created page: " + e.getMessage());
        	}
        }
        
        
        super.setResponseContent(html);

        return true;
    
    }
    

    private String generatePage(String htmlTemplate, Map contextObjects) throws Exception
    {
       String jar_base = "./deployablecomponents/lams2/";
        
        VelocityEngine ve = new VelocityEngine();
        
        Properties props = new Properties();
        props.setProperty("resource.loader", "file, jar");
        props.setProperty("file.resource.loader.path", jar_base);
        props.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
        props.setProperty("jar.resource.loader.path", "jar:file:" + jar_base + JARSTR);            
        ve.init(props);
        
        VelocityContext context = new VelocityContext();
        
        for(Iterator it = contextObjects.keySet().iterator(); it.hasNext();)
        {
            String key = (String)it.next();
            context.put(key, contextObjects.get(key));
        }
        
        context.put("request", request);
        context.put("settings", settings);
        context.put("authmod", this);
    
        Template template = null; 
        template = ve.getTemplate(htmlTemplate);
        StringWriter html = new StringWriter();
        template.merge(context, html);
        return html.toString();
    }
    
    public String generateRequestURL(UserVO user, Long lcID, String method) throws LoginException
    {
    	String authorUrl="";
    	
    	try
        {
        	authorUrl = LamsSecurityUtil.generateRequestURL(
        						lamsServerUrl, 
        						lamsServerId,
        						lamsServerSecretKey,
        						webctRequestSource, 
        						user.getUserId(), 
        						"" + lcID, 
        						"en", 
        						"AU",         						
        						user.getFirstname(),
        						user.getLastname(),
        						user.getEmail(),
        						method);
        
        	
        	log.info("LAMS AUTHOR REQUEST: " + authorUrl);
        	System.out.println("LAMS AUTHOR REQUEST: " + authorUrl);
            return authorUrl;
        }
        catch (Exception e)
        {
        	log.error("Problem generating request url:", e);
        	System.out.println("Problem generating request url: " + e.getMessage() );
        	e.printStackTrace();
        	throw new LoginException("Problem getting author url:" + e.getMessage());
        }
    }
    
    public String getLearningDesigns(UserVO user, Long lcID) throws LoginException
    {
    	String learningDesigns = "";
    	try
        {
            learningDesigns = LamsSecurityUtil.getLearningDesigns(
            					lamsServerUrl, 
            					lamsServerId, 
            					lamsServerSecretKey, 
            					user.getUserId(), 
            					"" + lcID, 
            					"en", 
            					"AU", 
            					user.getFirstname(),
        						user.getLastname(),
        						user.getEmail(),
            					"1");
            
            log.info("LAMS LEARNING DESIGNS: " + learningDesigns);
        	System.out.println("LAMS LEARNING DESIGN: " + learningDesigns);
            return learningDesigns;
        
        }
        catch (Exception e)
        {
        	log.error("Problem getting learning desings:", e);
        	System.out.println("Problem getting learning desings: " + e.getMessage() );
        	e.printStackTrace();
        	throw new LoginException("Problem getting learning desings: " + e.getMessage());
        }
    }
    
    
    public boolean logout() throws LoginException
    {
        return super.logout();
    }
}
		
	