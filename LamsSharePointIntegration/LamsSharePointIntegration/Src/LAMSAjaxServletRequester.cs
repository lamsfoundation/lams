/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


using System;
using System.Web;
using System.Collections.Generic;
using System.Text;
using Microsoft.SharePoint;

namespace LamsSharePointIntegration
{
    /// <summary>
    /// This class handles ajax requests for author, preview and learningDesigneRepository
    /// Requests are made from NewForm.aspx
    /// Author: Luke Foxton
    /// </summary>
    class LAMSAjaxServletRequester : IHttpHandler
    {
        public bool IsReusable
        {
            get { return false; }
        }

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            
            // Get the site context info
            SPWeb site = new SPSite(context.Request.Params["siteUrl"]).OpenWeb();
            SPWeb root = SPContext.Current.Site.RootWeb;
            SPUser user = site.CurrentUser;
            
            string responseStr = ""; // will return the result of ajax request

            string requestMethod = context.Request.QueryString["method"]; // the method of the request author/preview/learningDesignRepository

            if (requestMethod == "learningDesignRepository")
            {
                responseStr = LAMSSecurityUtil.getLearningDesigns(user, site);
            }
            else if (requestMethod == "preview")
            {
                responseStr = root.Url + "/_layouts/LamsSharePointIntegration/LAMSOpenPopup.aspx?method=preview&siteUrl=" + context.Request.Params["siteUrl"];
            }
            else if (requestMethod == "author")
            {
                responseStr = LAMSSecurityUtil.generateLoginRequestUrl(user, site, requestMethod);
            }
            else
            {
                // invalid response
                context.Response.StatusCode = 400;
                context.Response.Write("Bad request, no parameter for method found.");
                return;
            }

            context.Response.Write(responseStr);

        }
    }
}
