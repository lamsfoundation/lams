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
using System.Collections.Generic;
using System.Text;
using Microsoft.SharePoint;

namespace LamsSharePointIntegration
{
    /// <summary>
    /// This class handles events to do with the site's LAMS lesson table
    /// </summary>
    class LAMSLessonListEventHandler : SPItemEventReceiver
    {
        private string FormatCompanyName(string value)
        {
            return value.ToUpper();
        }

        public override void ItemAdding(SPItemEventProperties properties)
        {
            // Validation handled by SharePoint
        }

        /// <summary>
        /// Handles the event when a new lesson is added to the LAMS lesson table for the site
        /// </summary>
        /// <param name="properties">Properties of the item to be added</param>
        public override void ItemAdded(SPItemEventProperties properties)
        {
            // Get the site context information
            SPEventContext context = properties.Context;
            SPSite site = new SPSite(properties.SiteId);
            SPWeb siteWeb = site.OpenWeb(properties.RelativeWebUrl);
            SPUser user = siteWeb.CurrentUser;
            DisableEventFiring();

            // Get the title for the lesson, not null
            string title = properties.ListItem["Title"].ToString();
            
            // Get the description for the leson
            string description = "";
            if (properties.ListItem["Description"] != null)
            {
                description = properties.ListItem["Description"].ToString();
            }

            // Get the sequence id for the lesson, not null
            string sequenceId = properties.ListItem["SequenceID"].ToString();
                        
            // The lesson id of the started lesson, if successful
            string lessonId;
            try
            {
                lessonId = LAMSSecurityUtil.startLesson(user, siteWeb, sequenceId, title, description, "start");
            }
            catch (System.Net.WebException)
            {
                properties.ErrorMessage = "Request to LAMS server to start lesson failed. Please Contact System Administrator";
                properties.Cancel = true;
                properties.Status = SPEventReceiverStatus.CancelWithError;
                properties.ListItem.Delete();
                return;
            }

            // Set the LessonID param for the item and the start and end date to null
            properties.ListItem["LessonID"] = lessonId;
            properties.ListItem["Start Date"] = null; 
            properties.ListItem["End Date"] = null; 
            
            properties.ListItem.Update();
            EnableEventFiring();
        }
    }
}
