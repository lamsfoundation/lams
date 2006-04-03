/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.lesson.dto;


/**
 * <p>This is a cut down version of Lesson domain object. This data transfer object
 * is design for the data interaction between flash and java. As the flash and
 * java communication is expensive, DTO pattern works fine here to save all
 * unecessary data transfer.</p>
 * 
 * <p>As DTO is only used for data transfer purpose, we should make it immutable
 * to avoid any unecessary error state.</p>
 * 
 * @author Jacky Fang 2005-3-3
 * @version 1.1
 * 
 */
public class LessonDTO
{
    //---------------------------------------------------------------------
    // attributes
    //---------------------------------------------------------------------
    private Long lessonID;
    private String lessonName;
    private String lessonDescription;
    private Integer lessonStateID;

    //---------------------------------------------------------------------
    // Construtors
    //---------------------------------------------------------------------
    /**
     * Full constructor 
     */
    public LessonDTO(Long lessonId,
                         String lessonName,
                         String lessonDescription,
                         Integer lessonStateId)
    {
        this.lessonID = lessonId;
        this.lessonName = lessonName;
        this.lessonDescription = lessonDescription;
        this.lessonStateID = lessonStateId;

    }
    //---------------------------------------------------------------------
    // Getters
    //---------------------------------------------------------------------
    /**
     * Returns the lesson id.
     * @return Returns the lessonID.
     */
    public Long getLessonID()
    {
        return lessonID;
    }    
    
    /**
     * Returns the name of the lesson..
     * @return Returns the title.
     */
    public String getLessonName()
    {
        return lessonName;
    }   
    /**
     * Returns the lesson description.
     * @return Returns the description.
     */
    public String getLessonDescription()
    {
        return lessonDescription;
    }

    /**
     * Returns the lesson state. Plese refer to the lesson object for lesson
     * state.
     * @return Returns the lessonStateID.
     */
    public Integer getLessonStateID()
    {
        return lessonStateID;
    }

    /**
     * Returns the String representation of lesson data transfer object.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer(getClass().getName() + ": ");
        sb.append("lessonID='" + getLessonID() + "'; ");
        sb.append("lessonName='" + getLessonName()+"';");
        sb.append("lessonDescription='" + getLessonDescription() + "'; ");
        sb.append("lessonStateID='" + getLessonStateID() + "'; ");
        return sb.toString();
    }
}
