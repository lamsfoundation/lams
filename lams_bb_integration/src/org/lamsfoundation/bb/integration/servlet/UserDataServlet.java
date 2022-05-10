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
package org.lamsfoundation.bb.integration.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.bb.integration.Constants;
import org.lamsfoundation.bb.integration.util.CSVUtil;
import org.lamsfoundation.bb.integration.util.LamsPluginUtil;
import org.lamsfoundation.bb.integration.util.LamsSecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blackboard.data.user.User;
import blackboard.persist.PersistenceException;
import blackboard.persist.user.UserDbLoader;

/**
 * @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class UserDataServlet extends HttpServlet {

    private static final long serialVersionUID = 2L;
    private static Logger logger = LoggerFactory.getLogger(UserDataServlet.class);

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	// get Parameter values
	String usernameParam = request.getParameter(Constants.PARAM_USER_ID);
	String tsParam = request.getParameter(Constants.PARAM_TIMESTAMP);
	String hash = request.getParameter(Constants.PARAM_HASH);

	// check paramaeters
	if (usernameParam == null || tsParam == null || hash == null) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing expected parameters");
	    return;
	}

	String secretKey = LamsPluginUtil.getServerSecretKey();
	String serverId = LamsPluginUtil.getServerId();
	String plaintext = tsParam.toLowerCase() + usernameParam.toLowerCase() + serverId.toLowerCase()
		+ secretKey.toLowerCase();
	String parametersHash = null;
	if (hash.length() == LamsSecurityUtil.SHA1_HEX_LENGTH) {
	    // for some time support SHA-1 for authentication
	    parametersHash = LamsSecurityUtil.sha1(plaintext);
	} else {
	    parametersHash = LamsSecurityUtil.sha256(plaintext);
	}

	if (!hash.equals(parametersHash)) {
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "authentication failed");
	    return;
	}

	// get user list, but no role info since there are no course info
	User user;
	try {
	    UserDbLoader userLoader = UserDbLoader.Default.getInstance();
	    user = userLoader.loadByUserName(usernameParam);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	}

	if (user == null) {
	    throw new ServletException("user not found");
	}

	// construct the address
	String address = user.getStreet1() + (user.getStreet1().length() == 0 ? "" : " ");
	address += user.getStreet2() + (address.length() == 0 ? "" : " ");
	address += user.getState() + (address.length() == 0 ? "" : " ");
	address += user.getCountry() + (address.length() == 0 ? "" : " ");
	address += user.getZipCode();
	// String username = u.getUserName().replaceAll();

	String countryCode = LamsSecurityUtil.getCountryCode(user.getCountry());

	// The CSV list should be the format below
	// <Title>,<First name>,<Last name>,<Address>,<City>,<State>,
	// <Postcode>,<Country ISO code>,<Day time number>,<Mobile number>,
	// <Fax number>,<Email>,<Locale>
	String[] valList = { user.getTitle(), user.getGivenName(), user.getFamilyName(),
		user.getStreet1() + user.getStreet2(), user.getCity(), user.getState(), user.getZipCode(), countryCode,
		user.getHomePhone1(), user.getMobilePhone(), user.getBusinessFax(), user.getEmailAddress(),
		user.getLocale() };

	PrintWriter out = response.getWriter();
	out.println(CSVUtil.write(valList));
    }

}
