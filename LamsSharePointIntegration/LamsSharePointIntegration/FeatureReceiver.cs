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
using System.Xml;
using Microsoft.SharePoint;
using Microsoft.SharePoint.WebControls;
using Microsoft.SharePoint.WebPartPages;
using Microsoft.SharePoint.Navigation;
using System.Diagnostics;
using System.Collections.Generic;
using Microsoft.SharePoint.Administration;

namespace LamsSharePointIntegration
{
    /// <summary>
    /// The feature activator for the LAMSSharePointIntegration
    /// </summary>
    public class FeatureReceiver : SPFeatureReceiver
    {
        /// <summary>
        /// Activates the LAMS feature for a site
        /// </summary>
        /// <param name="properties"></param>
        public override void FeatureActivated(SPFeatureReceiverProperties properties)
        {
            // Get a hold of the site
            SPWeb site = (SPWeb)properties.Feature.Parent;
            
            // Create a name for the LAMS lesson list
            string listName;
            if (site.Name==null || site.Name== "" || site.IsRootWeb)
            {
               // root site of the site collection does not have a name
               listName = "LAMS Lesson List";
            }
            else
            {
                listName = "LAMS Lesson List - " + site.Name;
            }

            // Create the default server settings for the integration
            SPFarm myFarm = SPFarm.Local;
            if (myFarm.Properties["LAMSServerID"] == null)
            {
                myFarm.Properties["LAMSServerID"] = "lamssharepoint";
                myFarm.Properties["LAMSServerKey"] = "lamsserverkey";
                myFarm.Properties["LAMSUrl"] = "http://localhost:8080/lams/";
                myFarm.Properties["LAMSRequestSource"] = "SharePoint";
                myFarm.Properties["LAMSUseProfiles"] = "false";
                myFarm.Update();
            }
            
            //Get a hold of LAMS lesson list instance (schema and instance created through CAML)
            SPList lessons = null;
            try
            {
                site.Lists.Add(listName, listName, site.ListTemplates["LAMS Lesson List"]);
                lessons = site.Lists[listName];
                lessons.OnQuickLaunch = true;
                lessons.Hidden = false;
                lessons.Update();
                
            }
            catch (SPException) 
            {
                // lessonList already exists, use existing list
                try
                {
                    lessons = site.Lists[listName];
                    lessons.OnQuickLaunch = true;
                    lessons.Hidden = false;
                    lessons.Update();
                }
                catch (ArgumentException ex)
                {

                    throw new ApplicationException("LAMS Activation Failed, Could not find template to make LAMS lesson List - check Feature.xml", ex);
                }
            }

            // Add an event reciever for the created lesson
            lessons.EventReceivers.Add(SPEventReceiverType.ItemAdded,
                "LamsSharePointIntegration, Version=1.0.0.0, Culture=neutral, PublicKeyToken=2c5da6fd93fafb88",
                "LamsSharePointIntegration.LAMSLessonListEventHandler");

        }


        public override void FeatureDeactivating(SPFeatureReceiverProperties properties) { /* no op */ }
        public override void FeatureInstalled(SPFeatureReceiverProperties properties) { /* no op */ }
        public override void FeatureUninstalling(SPFeatureReceiverProperties properties) { /* no op */ }

    }
}
