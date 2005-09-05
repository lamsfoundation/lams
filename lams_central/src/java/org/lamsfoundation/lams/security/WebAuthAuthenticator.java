/*
 * Created on 2004-12-15
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.lamsfoundation.lams.security;

/**
 * @author kevin
 *
 */
public class WebAuthAuthenticator
{
	public WebAuthAuthenticator()
	{
	}

	public boolean authenticate(String username, String inputPassword)
	{
		//as for now, alway return true for webauth authenticated users
		//TODO check session to see if the user is from webauth
		return true;
	}
}
