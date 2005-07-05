package org.lamsfoundation.lams.tool.forum.util;

import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.*;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.File;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 4/07/2005
 * Time: 12:28:05
 * To change this template use File | Settings | File Templates.
 */
public class ContentHandler {
  private String repositoryWorkspaceName = "forumworkspace";
  private String repositoryUser = "forum";
  private char[] repositoryId = {'l','a','m','s','-','f','o','r','u','m'};
  private static IRepositoryService repService;
  private static ICredentials cred;
  private static ITicket ticket;
  private static ContentHandler contentHandler;

  private static Logger log = Logger.getLogger(ContentHandler.class.getName());

  private ContentHandler() throws Exception {
    this.configureContentRepository();
    this.getTicket();
  }

  public static ContentHandler getInstance() {
    try {
        if (contentHandler == null) {
            contentHandler = new ContentHandler();
        }
    } catch (Exception e) {
        log.error("could not initialize contenthandler");
    }
      return contentHandler;
  }

   private void configureContentRepository() throws Exception {
      repService = RepositoryProxy.getLocalRepositoryService();
      cred = new SimpleCredentials(repositoryUser, repositoryId);
        try {
            repService.createCredentials(cred);
	        repService.addWorkspace(cred,repositoryWorkspaceName);
        } catch (ItemExistsException ie) {
            log.warn("Tried to configure repository but it "
	        		+" appears to be already configured. Exception thrown by repository being ignored. ", ie);
        } catch (RepositoryCheckedException e) {
            String error = "Error occured while trying to configure repository."
				+" Unable to recover from error: "+e.getMessage();
		    log.error(error, e);
			throw new Exception(error,e);
        }
    }

    private ITicket getTicket( ) throws Exception {
	    //repService = RepositoryProxy.getLocalRepositoryService();
		ICredentials credentials = new SimpleCredentials(repositoryUser, repositoryId);
		try {
			ticket = repService.login(credentials, repositoryWorkspaceName);
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new Exception("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new Exception("Workspace not found in repository."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new Exception("Login failed in repository." + e.getMessage());
		}
	}

    public static NodeKey uploadFile(InputStream stream, String fileName, String mimeType) throws Exception {
	    return repService.addFileItem(ticket, stream, fileName, mimeType, null);
    }

    public static void deleteFile(Long id) throws Exception {
         repService.deleteNode(ticket, id);
    }

    public static File getFile(String name, Long uuid) throws Exception, FileException, ItemNotFoundException {
        //IVersionedNode node = repService.getFileItem(this.ticket, uuid, new Long("0"));
        IVersionedNode node = repService.getFileItem(ticket, uuid, null);
        return FileUtils.getFile(name, node.getFile());
    }

    public static Set getFileProperties(Long uuid) throws AccessDeniedException, FileException, ItemNotFoundException {
       IVersionedNode node = repService.getFileItem(ticket, uuid, null);
        return node.getProperties();
    }


}
