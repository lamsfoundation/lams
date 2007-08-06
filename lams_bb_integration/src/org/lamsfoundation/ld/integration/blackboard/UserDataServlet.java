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

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.ServletException;

import org.apache.commons.codec.binary.Hex;
//import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
//import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
//import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.ld.util.CSVUtil;
import org.lamsfoundation.ld.integration.Constants;

import blackboard.persist.BbPersistenceManager;
import blackboard.persist.user.UserDbLoader;
import blackboard.platform.BbServiceManager;
import blackboard.data.user.User;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.Context;
import org.apache.log4j.Logger;


/**
 *  @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class UserDataServlet extends HttpServlet {
          
	static Logger logger = Logger.getLogger(UserDataServlet.class);
	
	/**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        
        ContextManager ctxMgr = null;
        Context ctx = null;
        //get Blackboard context
        try{
            ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
            ctx = ctxMgr.setContext(request);    
            
            
            //get Parameter values
            String usernameParam = request.getParameter(Constants.PARAM_USER_ID);
            String tsParam = request.getParameter(Constants.PARAM_TIMESTAMP);
            String hashParam = request.getParameter(Constants.PARAM_HASH);
                       
            //check paramaeters
            if(usernameParam == null || tsParam == null || hashParam == null){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "missing expected parameters");
                return;
            }
            
            String secretKey = LamsPluginUtil.getSecretKey();
            String serverId = LamsPluginUtil.getServerId();
            
            if(!sha1(tsParam+ usernameParam + serverId + secretKey).equals(hashParam)){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "authentication failed");
            }

            //get the persistence manager
            BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
          
            
            
            //get user list, but no role info since there are no course info
            UserDbLoader userLoader = (UserDbLoader) bbPm.getLoader(UserDbLoader.TYPE);
            User u = userLoader.loadByUserName(usernameParam);
            
            if(u==null){
                throw new ServletException("user not found");
            }
            
           
            
            //construct the address
            String address = u.getStreet1() + (u.getStreet1().length()==0?"":" ");
            	   address+= u.getStreet2() + (address.length()==0?"":" "); 
            	   address+= u.getState() + (address.length()==0?"":" ");
            	   address+= u.getCountry() + (address.length()==0?"":" "); 
            	   address+= u.getZipCode();
            //String username = u.getUserName().replaceAll();
            	   
            
            PrintWriter out = response.getWriter();
            
            String loc_lang="en";
            String loc_cntry="US";
           
            /*
            String locale = u.getLocale();
            String loc_lang;
            String loc_cntry;
            if (u.getLocale()==null)
            {
            	loc_lang="en";
            	loc_cntry="US";
            }
            else
            {
            	String [] loc = locale.split("_");
            	loc_lang=loc[0];
            	loc_cntry=loc[1];
            }
            */
            
            // The CSV list should be the format below	
            // <Title>,<First name>,<Last name>,<Address>,<City>,<State>,
            // <Postcode>,<Country>,<Day time number>,<Mobile number>,
            // <Fax number>,<Email>,<Locale language>,<Locale country>
            String[] valList = {
            		u.getTitle(),
				    u.getGivenName(),
				    u.getFamilyName(),
				    u.getStreet1()+u.getStreet2(),
				    u.getCity(),
				    u.getState(),
				    u.getZipCode(),
				    u.getCountry(),
				    u.getHomePhone1(),
				    u.getMobilePhone(),
				    u.getBusinessFax(),
				    u.getEmailAddress(),
				    loc_lang,
				    loc_cntry
    			};
            // DEBUGGING
            System.out.println(valList[0]);
            System.out.println(valList[1]);
            System.out.println(valList[2]);
            System.out.println(valList[3]);
            System.out.println(valList[4]);
            System.out.println(valList[5]);
            System.out.println(valList[6]);
            System.out.println(valList[7]);
            System.out.println(valList[8]);
            System.out.println(valList[9]);
            System.out.println(valList[10]);
            System.out.println(valList[11]);
            System.out.println(valList[12]);
            System.out.println(valList[13]);
            System.out.println("LUKE IS AWESOME: " + CSVUtil.write(valList));
            
        
            out.println(CSVUtil.write(valList));
            
        } catch (Exception e){
        	e.printStackTrace();
        	logger.error("Problem with user data servlet: " + e.getMessage(), e);
        	logger.error(e.getStackTrace().toString());
            throw new ServletException("Failed to fetch user", e);
        } finally{
            //make sure context is released
	        if (ctxMgr != null)
	            ctxMgr.releaseContext();
        }
    }
    
	private String sha1(String str){
	    try{
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return new String(Hex.encodeHex(md.digest(str.getBytes())));
	    } catch(NoSuchAlgorithmException e){
	        throw new RuntimeException(e);
	    }
	}
}
