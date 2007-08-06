/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.ld.integration.blackboard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.xml.rpc.ServiceException;
import lamsws.LDRepository;
import lamsws.LDRepositoryServiceLocator;
import lamsws.LearningSessionManager;
import lamsws.LearningSessionManagerServiceLocator;
import lamsws.SimpleLearningDesignVO;
import lamsws.UserManager;
import lamsws.UserManagerServiceLocator;
import org.lamsfoundation.ld.integration.Constants;
import blackboard.platform.plugin.PlugInException;

/**
 *  @author <a href="mailto:lfoxton@melcoe.mq.edu.au">Luke Foxton</a>
 */
public class LamsServiceUtil {
    public static final String FILE_WS_PATH = "/lams/services/";
    private static final String WS_LEARNING_DESIGN = "LearningDesignService";
    private static final String WS_LEARNING_SESSION = "LearningSessionService";
    

    public static SimpleLearningDesignVO[] getAllLearningDesign(String login)
	throws  RemoteException, ServiceException 
	{
	
	    LDRepository ldr = getLearningDesignRepository();
	
	    String timestamp = Long.toString(new Date().getTime());
	
	    String serverId = LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_SERVER_ID);
	    String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, login, serverId);
	
	    SimpleLearningDesignVO[] res = null;
	            
	    res = ldr.getAllLearningDesigns(serverId, timestamp, hash, login);
	
	    return res;
	}
    
    public static long createLearningSession(String login, long ldId, String courseId, String title, String desc) throws RemoteException, ServiceException
    {
        LearningSessionManager lsmanager = getLearningSessionManager();
        
        String timestamp = Long.toString(new Date().getTime());
	    String serverId = LamsPluginUtil.getProperties().getProperty(LamsPluginUtil.PROP_LAMS_SERVER_ID);
	    String hash = LamsSecurityUtil.generateAuthenticationHash(timestamp, login, serverId);
        
	    long learningSessionId = lsmanager.createLearningSession(serverId, timestamp, hash, login, ldId, courseId, title, desc, LearningSessionManager.NORMAL);
	    return learningSessionId;
	}
    
    

    
    private static LearningSessionManager getLearningSessionManager() throws RemoteException, ServiceException{
        //get lamsServerUrl
        String lamsServerUrl = LamsPluginUtil.getServerUrl();
    
        //call the wsdl generated stubs
        LearningSessionManagerServiceLocator lssService = new LearningSessionManagerServiceLocator();
        LearningSessionManager lsm = null;
        
        String serviceUrl = lamsServerUrl + FILE_WS_PATH + WS_LEARNING_SESSION;
        
        lssService.setEndpointAddress(WS_LEARNING_SESSION, serviceUrl);
        lsm = lssService.getLearningSessionService();            
    
        return lsm;
    }
    
    
    private static LDRepository getLearningDesignRepository() throws RemoteException, ServiceException{
        //get lamsServerUrl
        String lamsServerUrl = LamsPluginUtil.getServerUrl();
    
        //call the wsdl generated stubs
        LDRepositoryServiceLocator ldrService = new LDRepositoryServiceLocator();
        LDRepository ldr = null;
        
        String serviceUrl = lamsServerUrl + FILE_WS_PATH + WS_LEARNING_DESIGN;
        System.out.println("trace: " + serviceUrl);
        
        ldrService.setEndpointAddress(WS_LEARNING_DESIGN, serviceUrl);
       	ldr = ldrService.getLearningDesignService();            
    
        return ldr;
    }
}
