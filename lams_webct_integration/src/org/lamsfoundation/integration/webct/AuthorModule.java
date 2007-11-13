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
import com.webct.platform.sdkext.authmoduledata.UserVO;
import com.webct.platform.sdk.context.gen.SessionVO;

//-------- Velocity imports --------------
import java.io.StringWriter; 
import java.rmi.RemoteException;
import org.apache.velocity.VelocityContext; 
import org.apache.velocity.Template; 
import org.apache.velocity.app.VelocityEngine; 
import org.apache.velocity.exception.*;
//import org.apache.axis.AxisFault;

import java.net.URL;
import org.apache.log4j.Logger;

import org.lamsfoundation.integration.webct.LamsSecurityUtil;
import org.lamsfoundation.integration.util.Constants;


/**
 * @author Luke Foxton
 *
 */
public class AuthorModule extends AuthenticationModule
{	
    
	public static final String VERSION = "1.0.0";
	public static final String JARSTR = "lams2-webct-integration-" + VERSION + ".jar";
	
	
	private boolean loginSucceeded = false;
    private HttpServletRequest request = null;
    private Map settings = null;
    private WebCTSSOContext ssoContext;
    
    
    
    private String lamsServerUrl;
    private String lamsServerId;
    private String lamsServerSecretKey;
    private String webctRequestSource;
    
    private String guid = null;
    private String user = null;
    
    private static final Logger log = Logger.getLogger(AuthorModule.class);
    
    
    
    private static final String GUTENBERG_QUERY_URL = "http://www.gutenberg.org/catalog/world/results";
    
    
    public AuthorModule()
    {
        super();	
    }
    
    public AuthorModule(Hashtable hashtable)
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

    	if(super.getCurrentMode().equals(super.OUTGOING_MODE))
    	{
    		UserVO user = null;
    		List roles;
    		Long lcID = super.getCurrentLearningContextId();
    		boolean isTeacher = false;
    		boolean isStudent = false;
    		try {
    			
        		user = UserService.getInstance().getUser(super.getUserId(), lcID);
        		roles = user.getUserRoles();
        		
        		System.out.println("ROLES!!!!!!:" + roles.toString());
        		
        		System.out.println("COURSE_INSTRUCTOR: " + UserRole.COURSE_INSTRUCTOR_ROLE);
        		System.out.println("SECTION_INSTRUCTOR: " + UserRole.SECTION_INSTRUCTOR_ROLE);
        		System.out.println("STUDENT_ROLE: " +UserRole.STUDENT_ROLE);
        		
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
    		
    		
    		
    		Map settings = super.getSettings();
        	//user = super.getUserId();
    		lamsServerUrl = (String)settings.get("lamsServerUrl");
            lamsServerId = (String)settings.get("lamsServerId");
            lamsServerSecretKey = (String)settings.get("lamsServerSecretKey");
            webctRequestSource = (String)settings.get("webctRequestSource");
    		
            
    		
    		
            Map params = new HashMap();            
            params.put("lamsServerUrl", lamsServerUrl);
            params.put("lamsServerId", lamsServerId);
            params.put("lamsServerSecretKey", lamsServerSecretKey);
            params.put("webctRequestSource", webctRequestSource);
            params.put("user", user);
            params.put("isTeacher", "" + isTeacher);
            
            String authorUrl = null;
            String learningDesigns = null;
            
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
            						"author");
            
            	
            	log.info("LAMS AUTHOR REQUEST: " + authorUrl);
            	System.out.println("LAMS AUTHOR REQUEST: " + authorUrl);
                params.put("authorUrl", authorUrl);
                
                
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
                params.put("learningDesigns", learningDesigns);
            
            }
            catch (Exception e)
            {
            	log.error("Problem generating request url:", e);
            	System.out.println("Problem generating request url: " + e.getMessage() );
            	e.printStackTrace();
            	return false;
            }
            	
            

            String html=null;
            try
            {
                if (isTeacher)
                {
                	html = this.generatePage("web/create.vm", params);
                }
                else
                {
                	html = this.generatePage("web/learnerMonitor.vm", params);
               }
            }
            catch(Exception e)
            {
            	log.error("Problem generating page:", e);
            	e.printStackTrace();
            	throw new LoginException("Error generating page: " + e.toString());
            }
            super.setResponseContent(html);

            return true;
        }
        /*else if(super.getCurrentMode().equals(super.INCOMING_MODE))
        {
        	//try {
	        	//ContextSDK context = new ContextSDK();
	        	//SessionVO session = context.getCurrentSession();
	        	//Long lcid = super.getCurrentLearningContextId();
	        	// get the learning context object that corresponds to the LCID
	        	//LearningCtxtVO lc = context.getLearningContext(session, lcid.longValue());
	        	
        		
        	
        	
	        	Map params = new HashMap();            
	        	params.put("title", "");			// not implemented by WebCt
	        	params.put("fname", super.getUserId());
	            params.put("lname", super.getUserId());
	            params.put("adress", ""); 			// not implemented by WebCt
	            params.put("city", "");				// not implemented by WebCt
	            params.put("state", "");			// not implemented by WebCt
	            params.put("postcode", "");			// not implemented by WebCt
	            params.put("country", "");			// not implemented by WebCt
	            params.put("phnumber", "");			// not implemented by WebCt
	            params.put("mphnumber", "");		// not implemented by WebCt
	            params.put("fnumber", "");			// not implemented by WebCt
	            params.put("email", "");			// not implemented by WebCt
	            params.put("localelanguage", "en");
	            params.put("localecountry", "AU");
	            
	            String html=null;
	            try
	            {
	                html = this.generatePage("web/userdataservlet.vm", params);
	            }
	            catch(Exception e)
	            {
	                e.printStackTrace();
	            	throw new LoginException("Error generating page: " + e.toString());
	            }
	            super.setResponseContent(html);
	            
	            return true;
            
        	//}
            //catch (RemoteException e)
            //{	
            //	e.printStackTrace();
            //	throw new LoginException("Error getting user session: " + e.toString());
            //}
        }*/
    	else 
    	{
    		// shouldn't be here
    		return false;
    	}
                
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
    
    public boolean logout() throws LoginException
    {
        return super.logout();
    }
}
		
	