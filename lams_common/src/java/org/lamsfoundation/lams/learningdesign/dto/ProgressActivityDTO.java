/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
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
 * ***********************************************************************/

package org.lamsfoundation.lams.learningdesign.dto;


/**
 * The data transfer object to allow flash to view an activity from learner
 * progress bar or monitoring flash component.
 * 
 * @author Jacky Fang
 * @since  2005-3-15
 * @version 1.1
 * 
 */
public class ProgressActivityDTO
{
    //---------------------------------------------------------------------
    // attributes
    //---------------------------------------------------------------------
    private Long activityId;
    private String activityURL;
    
    
    //---------------------------------------------------------------------
    // Construtors
    //---------------------------------------------------------------------
    /**
     * Minimum constructor
     */
    public ProgressActivityDTO(Long activityId)
    {
        this(activityId,null);
    }
    
    /**
     * Full constructor
     */
    public ProgressActivityDTO(Long activityId,String activityURL)
    {
        this.activityId = activityId;
        this.activityURL = activityURL;
    }

    /**
     * @return Returns the activityId.
     */
    public Long getActivityId()
    {
        return activityId;
    }
    /**
     * @return Returns the activityURL.
     */
    public String getActivityURL()
    {
        return activityURL;
    }
    /**
     * @param activityURL The activityURL to set.
     */
    public void setActivityURL(String activityURL)
    {
        this.activityURL = activityURL;
    }
}
