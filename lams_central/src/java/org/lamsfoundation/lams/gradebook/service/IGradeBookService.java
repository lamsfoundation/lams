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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */ 
 
/* $Id$ */ 
package org.lamsfoundation.lams.gradebook.service; 

import java.util.List;

import org.lamsfoundation.lams.gradebook.dto.GBActivityGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradeBookGridRowDTO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
 
public interface IGradeBookService {
    
    
    public GBActivityGridRowDTO getGradeBookActivityDTO(Activity activity, User learner, LearnerProgress learnerProgress);
    
    public List<GradeBookGridRowDTO> getUserGradeBookActivityDTOs(Lesson lesson, User learner);
    
    public List<GradeBookGridRowDTO> getUserGradeBookActivityDTOs(Lesson lesson, Activity activity);
    
    public List<GradeBookGridRowDTO> getUserGradeBookActivityDTOsActivityView(Lesson lesson, User learner);
    
    public List<GradeBookGridRowDTO> getActivityGradeBookUserDTOs(Lesson lesson);
    
    public List<GBUserGridRowDTO> getGradeBookLessonData(Lesson lesson);

    public void updateUserLessonGradeBookMark(Lesson lesson, User learner, Double mark);
    
    public void updateUserActivityGradeBookMark(Lesson lesson, User learner, Activity activity, Double mark);
    
    public void updateUserActivityGradeBookFeedback(Activity activity, User learner, String feedback);
    
    public void updateUserLessonGradeBookFeedback(Lesson lesson, User learner, String feedback);
    
}
 