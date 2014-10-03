/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.security;


public interface ISecurityService {
    
    /**
     * Checks if the user is a learner in the given lesson.
     */
    void checkIsLessonLearner(Long lessonId, Integer userId) throws SecurityException;
    
    /**
     * Checks if the user is a staff member in the given lesson.
     */
    void checkIsLessonMonitor(Long lessonId, Integer userId) throws SecurityException;
    
    /**
     * Checks if the user is either a learner or a staff member in the given lesson.
     */
    void checkIsLessonParticipant(Long lessonId, Integer userId) throws SecurityException;
    
    /**
     * Checks if the user has a global role of SYSADMIN.
     */
    void checkIsSysadmin(Integer userId);
    
    /**
     * Checks if the user has any of the given roles in the given organisation. 
     */
    void hasOrgRole(Integer orgId, Integer userId, String... roles) throws SecurityException;
}