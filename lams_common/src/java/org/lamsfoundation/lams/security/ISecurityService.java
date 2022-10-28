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
     * Checks if the is a manager or monitor in the organisation. This is just a convenience method for hasOrgRole()
     * with MONITOR and GROUP_MANAGER.
     */
    boolean isGroupMonitor(Integer orgId, Integer userId, String action);

    boolean isGroupMonitor(Integer orgId, Integer userId, String action, boolean skipLog);

    boolean ensureGroupMonitor(Integer orgId, Integer userId, String action) throws SecurityException;

    /**
     * Checks if the user is a learner in the given lesson.
     */
    boolean isLessonLearner(Long lessonId, Integer userId, String action);

    boolean isLessonLearner(Long lessonId, Integer userId, String action, boolean skipLog);

    boolean ensureLessonLearner(Long lessonId, Integer userId, String action) throws SecurityException;

    /**
     * Checks if the user is a monitor or owner of the given lesson, or a group manager of the organisation.
     */
    boolean isLessonMonitor(Long lessonId, Integer userId, String action);

    boolean isLessonMonitor(Long lessonId, Integer userId, String action, boolean skipLog);

    boolean ensureLessonMonitor(Long lessonId, Integer userId, String action) throws SecurityException;

    /**
     * Checks if the user is the owner of the given lesson.
     */
    boolean isLessonOwner(Long lessonId, Integer userId, String action);

    boolean isLessonOwner(Long lessonId, Integer userId, String action, boolean skipLog);

    boolean ensureLessonOwner(Long lessonId, Integer userId, String action) throws SecurityException;

    /**
     * Checks if the user is either a learner or a staff member in the given lesson.
     */
    boolean isLessonParticipant(Long lessonId, Integer userId, String action);

    boolean isLessonParticipant(Long lessonId, Integer userId, String action, boolean skipLog);

    boolean ensureLessonParticipant(Long lessonId, Integer userId, String action) throws SecurityException;

    /**
     * Checks if the user has a global role of SYSADMIN.
     */
    boolean isSysadmin(Integer userId, String action);

    boolean isSysadmin(Integer userId, String action, boolean skipLog);

    boolean ensureSysadmin(Integer userId, String action) throws SecurityException;

    /**
     * Checks if the user has any of the given roles in the given organisation. If GROUP_MANAGER is
     * given for class type organisation, their parent organisations are checked.
     */
    boolean hasOrgRole(Integer orgId, Integer userId, String[] roles, String action);

    boolean hasOrgRole(Integer orgId, Integer userId, String[] roles, String action, boolean skipLog);

    boolean ensureOrgRole(Integer orgId, Integer userId, String[] roles, String action) throws SecurityException;
}