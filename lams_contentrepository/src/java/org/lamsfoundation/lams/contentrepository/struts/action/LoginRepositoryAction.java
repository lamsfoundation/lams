//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.contentrepository.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.IRepository;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.ItemExistsException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.SimpleCredentials;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.struts.form.LoginRepositoryForm;


/** 
 * MyEclipse Struts
 * Creation date: 01-13-2005
 * 
 * The exceptions will be handled by struts but are listed explicitly
 * here so (1) I can log them and (2) you can see what exceptions are thrown.
 * 
 * XDoclet definition:
 * @struts:action path="/loginRepository" name="loginRepositoryForm" input="/loginRepository.jsp" scope="request" validate="true"  parameter="method"
 */
public class LoginRepositoryAction extends RepositoryDispatchAction {
	
	protected Logger log = Logger.getLogger(LoginRepositoryAction.class);

	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	/** 
	 * Login to the repository
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward loginToWorkspace(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws RepositoryCheckedException {
		return process(mapping, form, request, response, false);
	}

	/** 
	 * Create and login to a new workspace
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward createNewWorkspace(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws RepositoryCheckedException {
		return process(mapping, form, request, response, true);
	}

	/* Get the repository bean and connect to the desired workspace. May need to create the
	 * workspace first - depends on createWorkspaceFirst flag.
	 * RepositoryCheckedException is the superclass for all the other repository checked exceptions
	 * thrown by this method.
	 */
	private ActionForward process(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response,
		boolean createWorkspaceFirst) throws RepositoryCheckedException 
		{
		log.debug("In process, createWorkspaceFirst="+createWorkspaceFirst);
		
		LoginRepositoryForm loginRepositoryForm = (LoginRepositoryForm) form;
		if ( log.isDebugEnabled() ) {
			log.debug("Form : "+form);
		}
		
		// login to repository
		String toolName = loginRepositoryForm.getToolName();
		char[] toolId = loginRepositoryForm.getIndentificationString().toCharArray();
		String workspaceName = loginRepositoryForm.getWorkspaceName();
		ICredentials cred =  new SimpleCredentials(toolName, toolId); 

		IRepository repository = Download.getRepository();
		
		if ( createWorkspaceFirst ) {
			try {
				// add the tool credential, then create the workspace
				// if the credential isn't added first, then the addWorkspace
				// call will fail - only a valid credential can add a workspace.
				repository.createCredential(cred);
				repository.addWorkspace(cred, workspaceName);
			} catch (LoginException e) {
				log.error("LoginException occured ",e);
				throw e;
			} catch (ItemExistsException e) {
				log.error("Workspace already exists Exception occured ",e);
				throw e;
			} catch (RepositoryCheckedException e) {
				log.error("Some other repository error (usually internal error) occured ",e);
				throw e;
			}
		}
		
		ITicket ticket;
		try {
			ticket = repository.login(cred, workspaceName);
		} catch (LoginException e) {
			log.error("LoginException occured ",e);
			throw e;
		} catch (AccessDeniedException e) {
			log.error("Not allowed to do that type exception occured ",e);
			throw e;
		} catch (WorkspaceNotFoundException e) {
			log.error("Workspace was not found exception occured ",e);
			throw e;
		}
		
		// add the ticket to the session. this ticket will need to kept somewhere
		// by the application as it is the method of accessing the repository.
		log.debug("New ticket being added to session: "+ticket);
		setTicket(request, ticket);

		log.debug("Login succeeded, forwarding to "+mapping.findForward(SUCCESS_PATH));
		return mapping.findForward(SUCCESS_PATH);
		
	}

	

}