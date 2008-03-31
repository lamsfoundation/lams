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
using System.Web.UI.WebControls.WebParts;
using System.Web;
using System.Net;
using System.IO;
using System.Xml;
using System.Text;
using System.Security.Cryptography;
using Microsoft.SharePoint;
using Microsoft.SharePoint.WebControls;
using Microsoft.SharePoint.WebPartPages;
using Microsoft.SharePoint.Navigation;
using System.Diagnostics;
using System.Collections.Generic;
using Microsoft.SharePoint.Administration;
using System.Web.Security;
using System.Text.RegularExpressions;
//using Microsoft.Office.Server;
//using Microsoft.Office.Server.UserProfiles;

using System.Threading;


namespace LamsSharePointIntegration
{
    /// <summary>
    /// This class handles all requests to the LAMS server
    /// There are three types of requests
    /// 1) Login requests for learner/monitor/preview
    /// 2) Starting lessons through the lesson manager
    /// 3) Getting the learning design repository
    /// 
    /// Author: Luke Foxton
    /// </summary>
    public class LAMSSecurityUtil
    {
        // Request paths for the three different types of requests to the LAMS server
        public static string LOGIN_REQUEST = "/LoginRequest?";                                          
        public static string LEARNING_DESIGN_REPOSITORY = "/services/xml/LearningDesignRepository?";
        public static string LESSON_MANAGER = "/services/xml/LessonManager?";

        
        /// <summary>
        /// Generates the base requests with parameters for all three request types
        /// </summary>
        /// <param name="requestType">The type of request LOGIN_REQUEST/LEARNING_DESIGN_REPOSITORY/LESSON_MANAGER</param>
        /// <param name="user">The user who is making the request to LAMS</param>
        /// <param name="site">The site teh request is made from</param>
        /// <param name="method">The method (Only used for LOGIN_REQUEST) author/monitor/learner</param>
        /// <returns>The base url for a request to the LAMS server</returns>
        public static string generateBaseRequestURL(string requestType, SPUser user, SPWeb site, string method)
        {
            // getting config settings
            SPFarm myFarm = SPFarm.Local;

            // Generating the timestamp (must be the same as a java timestamp, ie milliseconds since 1/1/1970
            DateTime origin = new DateTime(1970, 1, 1, 0, 0, 0, 0);
            DateTime now = DateTime.UtcNow;
            TimeSpan diff = now - origin;
            long millis = System.Convert.ToInt64(diff.TotalMilliseconds);
            string timestamp = millis.ToString();

            // Getting rid of the SERVERNAME//USERNAME format
            string username = splitUsername(user.LoginName);

            // Getting the language and locale strings
            string[] langLocale = LAMSLanguageMap.getLanguageMap(site.Language);

            // Creating a meaninful and unique courseName from the site name
            string courseId = site.Name != null && site.Name != "" ? site.Name + "_" + site.ID.ToString() : site.ID.ToString();
            
            // fixing up the parameter name mappings and setting up the hash
            string hash;
            string userParamName;
            string timestampParamName;
            string serverIdParamName;
            string courseIdParamName;
            string hashParamName;
            if (requestType == (string)LAMSSecurityUtil.LOGIN_REQUEST)
            {
                userParamName = "&uid=";
                timestampParamName = "&ts=";
                serverIdParamName = "&sid=";
                courseIdParamName = "&courseid=";
                hashParamName = "&hash=";

                // sha1 hash of timestamp, login, method, serverid, secret key
                hash = hashForLoginRequest(timestamp, username, method, (string)myFarm.Properties["LAMSServerId"], (string)myFarm.Properties["LAMSServerKey"]);
            }
            else
            {
                userParamName = "&username=";
                timestampParamName = "&datetime=";
                serverIdParamName = "&serverId=";
                courseIdParamName = "&courseId=";
                hashParamName = "&hashValue=";

                // sha1 hash of timestamp, login, serverid, secret key
                hash = hashLearningDesingsAndLessons(timestamp, username, (string)myFarm.Properties["LAMSServerId"], (string)myFarm.Properties["LAMSServerKey"]);
            }

            // This is the base url for the LAMS server request
            string url = (string)myFarm.Properties["LAMSUrl"] + requestType
                        + serverIdParamName +   (string)myFarm.Properties["LAMSServerId"]
                        + courseIdParamName +   HttpUtility.UrlEncode(courseId, System.Text.Encoding.UTF8)
                        + userParamName +       HttpUtility.UrlEncode(username, System.Text.Encoding.UTF8)
                        + timestampParamName +  HttpUtility.UrlEncode(timestamp, System.Text.Encoding.UTF8)
                        + hashParamName +       HttpUtility.UrlEncode(hash, System.Text.Encoding.UTF8)
                        + "&lang=" +            langLocale[0]
                        + "&country=" +         langLocale[1];

            
            // Check what sort of user information the server is using
            if ((string)myFarm.Properties["LAMSUseProfiles"] == "False" && requestType != LAMSSecurityUtil.LESSON_MANAGER)
            {
                string firstname = "";
                string lastname = "";
                string email = "";

                // if this was unselected in the settings, get the user information implicitly
                if (user.Name == null || user.Name == "")
                {
                    // No records found for the user at all on sharepoint, use login name as default
                    firstname = username;
                    lastname = " ";
                }
                else
                {
                    char[] splitter = { ' ' };
                    string[] fullname = splitUsername(user.Name).Split(splitter);
                    if (fullname.Length < 2)
                    {
                        firstname = fullname[0];
                        lastname = " ";
                    }
                    else
                    {
                        firstname = fullname[0];
                        lastname = splitUsername(fullname[fullname.Length - 1]);
                    }
                }

                if (user.Email == null || user.Email == "")
                {
                    email = username + "@" + username + ".com";
                    email = email.Replace(" ", "");
                }
                else
                {
                    email = user.Email;
                    
                }
                
                url += "&firstName=" + HttpUtility.UrlEncode(firstname, System.Text.Encoding.UTF8)
                    + "&lastName=" + HttpUtility.UrlEncode(lastname, System.Text.Encoding.UTF8)
                    + "&email=" + HttpUtility.UrlEncode(email, System.Text.Encoding.UTF8);

            }
            else if (requestType != LAMSSecurityUtil.LESSON_MANAGER)
            {

                

                //SPSecurity.RunWithElevatedPrivileges(delegate()
                //{
                //    ServerContext sContext = ServerContext.GetContext(SPContext.Current.Site);
                //    UserProfileManager profileMgr = new UserProfileManager(sContext, false);

                //    if (profileMgr.UserExists(user.LoginName))
                //    {
                //        UserProfile prof = profileMgr.GetUserProfile(user.LoginName);

                //        string firstNameProf = prof["FirstName"] != null ? (string)prof["FirstName"].Value : "";
                //        string lastNameProf = prof["LastName"] != null ? (string)prof["LastName"].Value : " ";
                //        string emailProf = prof["WorkEmail"] != null ? (string)prof["WorkEmail"].Value : "";

                //        if (firstNameProf == null || firstNameProf == "")
                //        {
                //            url = "";
                //        }
                //        else
                //        {
                //            url += "&firstName=" + HttpUtility.UrlEncode(firstNameProf, System.Text.Encoding.UTF8)
                //                + "&lastName=" + HttpUtility.UrlEncode(lastNameProf, System.Text.Encoding.UTF8)
                //                + "&email=" + HttpUtility.UrlEncode(emailProf, System.Text.Encoding.UTF8);
                //        }
                //    }
                //    else
                //    {
                //        url = "";
                //    }
                //});
                
                
            }

            return url;
        }

        /// <summary>
        /// Creates the url for the login request monitor/learner/author
        /// </summary>
        /// <param name="user">The user making the request</param>
        /// <param name="site">The site the request is made from</param>
        /// <param name="method">The method of the request monitor/learner/author</param>
        /// <returns>A url that requests teh loginRequestServlet on the LAMS server</returns>
        public static string generateLoginRequestUrl(SPUser user, SPWeb site, string method)
        {
            // Getting config settings
            SPFarm myFarm = SPFarm.Local;

            // Get the base url
            string url = generateBaseRequestURL(LOGIN_REQUEST, user, site, method);

            if (url == null || url == "") { return ""; }

            return url + "&requestSrc=" + HttpUtility.UrlEncode((string)myFarm.Properties["LAMSRequestSource"], System.Text.Encoding.UTF8)
                + "&method=" + HttpUtility.UrlEncode(method, System.Text.Encoding.UTF8) + "&siteLang=" + site.Language;
        }

        /// <summary>
        /// Gets the LAMS learning design repository for the user and prints it in tigra format
        /// </summary>
        /// <param name="user">The user makingthe request</param>
        /// <param name="site">The site the request is made from</param>
        /// <returns>A string in tigra format that represents the user's workspace of learning designs</returns>
        public static string getLearningDesigns(SPUser user, SPWeb site)
        {

            // Get the base url
            string url = generateBaseRequestURL(LEARNING_DESIGN_REPOSITORY, user, site, "") + "&mode=2";
            
            // If there was an error, return an empty string
            if (url == null || url == "") { return ""; }

            // Requesting the LAMS server for the learning design repository and getting the response
            HttpWebRequest req = (HttpWebRequest)WebRequest.Create(url);
            WebResponse resp = req.GetResponse();
            System.IO.Stream inStream = resp.GetResponseStream();
            System.IO.StreamReader respStream = new System.IO.StreamReader(resp.GetResponseStream());
            string response = respStream.ReadToEnd();

            // Create a DOM parser from the response from the LAMS server (the learning design repository)
            XmlDocument document = new XmlDocument();
            document.Load(new StringReader(response));

            // Convert the string to tigra format
            string learningDesignsTigraFormat = "[" + convertToTigraFormat(document.DocumentElement) + "]";

            // Place the javascript methods in the tigra-formatted string
            learningDesignsTigraFormat = Regex.Replace(learningDesignsTigraFormat, @"'(\d+)'", "'javascript:selectSequence($1)'");
             
            return learningDesignsTigraFormat;
 
        }

        /// <summary>
        /// Starts lessons on the LAMS server and returns the lesson ID
        /// </summary>
        /// <param name="user">The user who is making the request</param>
        /// <param name="site">This site the request was made from</param>
        /// <param name="learningDesignId">The ID of the learning design that will be used to start the lesson</param>
        /// <param name="title">The title of the lesson</param>
        /// <param name="description">The description of the lesson</param>
        /// <param name="method">The method to start the lesson preview/start</param>
        /// <returns>The lesson ID if the lesson started successfully</returns>
        public static string startLesson(SPUser user, SPWeb site, string learningDesignId, string title, string description, string method)
        {
            // Create the base url
            string url = generateBaseRequestURL(LESSON_MANAGER, user, site, "");
            
            if (url == null || url == "") { return ""; }
            
            // Add more parameters to the url
            url += "&method=" + method + "&title=" +
                HttpUtility.UrlEncode(title, System.Text.Encoding.UTF8) +
                "&desc=" + HttpUtility.UrlEncode(description, System.Text.Encoding.UTF8) +
                "&ldId=" + learningDesignId;

            // Request the LAMS server to start the lesson and get the response
            HttpWebRequest req = (HttpWebRequest)WebRequest.Create(url);
            WebResponse resp = req.GetResponse();
            System.IO.Stream inStream = resp.GetResponseStream();
            System.IO.StreamReader respStream = new System.IO.StreamReader(resp.GetResponseStream());
            string response = respStream.ReadToEnd();
            
            // Create and xml DOM parser to get the reponse data
            XmlDocument document = new XmlDocument();
            document.Load(new StringReader(response));

            // return the lesson id if the lesson started correctly
            return document.GetElementsByTagName("Lesson")[0].Attributes["lessonId"].Value; ;
        }

        /// <summary>
        /// Generates the hash used for talking between the servers for learning designs and lessons
        /// </summary>
        /// <param name="timestamp">The currnent time</param>
        /// <param name="login">The user's login</param>
        /// <param name="serverId">Server ID from the server settings</param>
        /// <param name="serverKey">Server Key from the server settings</param>
        /// <returns>A sha1 hash of the strings</returns>
        public static string hashLearningDesingsAndLessons(string timestamp, string login, string serverId, string serverKey)
        {
            string plaintext = timestamp.ToLower().Trim() + login.ToLower().Trim() + serverId.ToLower().Trim() + serverKey.ToLower().Trim();
            return sha1(plaintext);
        }

        /// <summary>
        /// Generates the hash used for talking between the servers for login requests
        /// </summary>
        /// <param name="timestamp">The currnent time</param>
        /// <param name="login">The user's login</param>
        /// <param name="serverId">Server ID from the server settings</param>
        /// <param name="serverKey">Server Key from the server settings</param>
        /// <param name="method">The method of the login request</param>
        /// <returns>A sha1 hash of the strings</returns>
        public static string hashForLoginRequest(string timestamp, string login, string method, string serverId, string serverKey)
        {
            string plaintext = timestamp.ToLower().Trim() + login.ToLower().Trim() + method.ToLower().Trim() + serverId.ToLower().Trim() + serverKey.ToLower().Trim();
            return sha1(plaintext);
        }

        
        /// <summary>
        /// Splits the username so it is not in the form SERVERNAME\\USERNAME
        /// </summary>
        /// <param name="username">The username to be prettied up</param>
        /// <returns>A prettied up user name</returns>
        public static string splitUsername(String username)
        {
            char[] split = { '/', '\\' };
            string[] splitUsername = username.Split(split);
            return splitUsername[splitUsername.Length - 1];
        }

        /// <summary>
        /// Hashes the given string in sha1
        /// </summary>
        /// <param name="text">The string to be hashed</param>
        /// <returns>A sha1 hash of the string</returns>
        public static string sha1(string text)
        {
            Byte[] clearBytes;
            Byte[] hashedBytes;

            clearBytes = Encoding.UTF8.GetBytes(text);
            SHA1CryptoServiceProvider sha1 = new SHA1CryptoServiceProvider();
            sha1.ComputeHash(clearBytes);
            hashedBytes = sha1.Hash;
            sha1.Clear();
            return BitConverter.ToString(hashedBytes).Replace("-", "").ToLower();

        }


        /// <summary>
        /// Convets a DOM node into a tigra formatted string
        /// </summary>
        /// <param name="node">A DOM node</param>
        /// <returns>A tigra formatted representaion of the DOM</returns>
        public static string convertToTigraFormat(XmlNode node)
        {
            StringBuilder sb = new StringBuilder();

            if (node.Name == "Folder")
            {
                sb.Append("['");
                StringBuilder attribute = new StringBuilder(node.Attributes["name"].Value.Replace("'", "\\'"));
                sb.Append(attribute.Append("',").Append("null").Append(','));
                XmlNodeList children = node.ChildNodes;
                if (children.Count == 0)
                {
                    sb.Append("['',null]");
                }
                else
                {
                    sb.Append(convertToTigraFormat(children[0]));
                    for (int i = 1; i < children.Count; i++)
                    {
                        sb.Append(',').Append(convertToTigraFormat(children[i]));
                    }
                }
                sb.Append(']');
            }
            else if (node.Name=="LearningDesign")
            {
                sb.Append('[');
                StringBuilder attrName = new StringBuilder(node.Attributes["name"].Value.Replace("'", "\\'"));
                StringBuilder attrResId = new StringBuilder(node.Attributes["resourceId"].Value.Replace("'", "\\'"));    
                sb.Append('\'')
                    .Append(attrName
                    .Append('\'')
                    .Append(',')
                    .Append('\'')
                    .Append(attrResId.Append('\'')));

                sb.Append(']');
            }
            return sb.ToString();
        }


    }
}
