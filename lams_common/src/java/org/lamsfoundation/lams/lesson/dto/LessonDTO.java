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
    private Long lessonId;
    private String lessonTitle;
    private String learningDesignDescription;
    private Integer lessonStateId;

    //---------------------------------------------------------------------
    // Construtors
    //---------------------------------------------------------------------
    /**
     * Full constructor 
     */
    public LessonDTO(Long lessonId,
                         String learningDesignTitle,
                         String learningDesignDescription,
                         Integer lessonStateId)
    {
        this.lessonId = lessonId;
        this.lessonTitle = learningDesignTitle;
        this.learningDesignDescription = learningDesignDescription;
        this.lessonStateId = lessonStateId;

    }
    //---------------------------------------------------------------------
    // Getters
    //---------------------------------------------------------------------
    /**
     * Returns the learning design description that current lesson is based on.
     * @return Returns the description.
     */
    public String getLearningDesignDescription()
    {
        return learningDesignDescription;
    }
    /**
     * Returns the lesson id.
     * @return Returns the lessonId.
     */
    public Long getLessonId()
    {
        return lessonId;
    }
    /**
     * Returns the lesson state. Plese refer to the lesson object for lesson
     * state.
     * @return Returns the lessonStateId.
     */
    public Integer getLessonStateId()
    {
        return lessonStateId;
    }
    /**
     * Returns the learning design title that current lesson is based on.
     * @return Returns the title.
     */
    public String getLearningDesignTitle()
    {
        return lessonTitle;
    }
}
