/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.integration.security;

import java.security.NoSuchAlgorithmException;

import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.util.HashUtil;

/**
 * <p>
 * <a href="Authenticator.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class Authenticator {
	public static void authenticate(ExtServerOrgMap map, String datetime, String username, String method, String hashValue) throws AuthenticationException{
		if(map==null) throw new AuthenticationException("The third party server is not configured on LAMS server");
		String plaintext = datetime.toLowerCase().trim() +
		                   username.toLowerCase().trim() +
		                   method.toLowerCase().trim() +
		                   map.getServerid().toLowerCase().trim() + 
		                   map.getServerkey().toLowerCase().trim();
		checkHash(plaintext, hashValue);
	}
    
	public static void authenticate(ExtServerOrgMap map, String datetime, String username, String hashValue) throws AuthenticationException{
		if(map==null) throw new AuthenticationException("The third party server is not configured on LAMS server");
		String plaintext = datetime.toLowerCase().trim()+username.toLowerCase().trim()+map.getServerid().toLowerCase().trim()+map.getServerkey().toLowerCase().trim();
		checkHash(plaintext, hashValue);
	}
	
	public static void authenticate(ExtServerOrgMap map, String datetime, String hashValue) throws AuthenticationException{
		if(map==null) throw new AuthenticationException("The third party server is not configured on LAMS server");
		String plaintext = datetime.toLowerCase().trim()+map.getServerid().toLowerCase().trim()+map.getServerkey().toLowerCase().trim();
		checkHash(plaintext, hashValue);
	}
	
	private static void checkHash(String plaintext, String hashValue) throws AuthenticationException {
		try{
			if(!hashValue.equals(HashUtil.sha1(plaintext))){
			    throw new AuthenticationException("Authentication failed!");
			}
		}catch(NoSuchAlgorithmException e){
			throw new AuthenticationException("The system does not support the required algorithm to do authentication!");
		}
	}

}
