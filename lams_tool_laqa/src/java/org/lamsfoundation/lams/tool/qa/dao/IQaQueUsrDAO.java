/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.dao;

import java.util.List;

import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;

/**
 * @author Ozgur Demirtas
 */
public interface IQaQueUsrDAO {

    QaQueUsr getQaUserBySession(final Long queUsrId, final Long qaSessionId);

    void createUsr(QaQueUsr usr);

    void updateUsr(QaQueUsr usr);

    void deleteQaQueUsr(QaQueUsr qaQueUsr);

    int countSessionUser(QaSession qaSession);

    List getUserBySessionOnly(final QaSession qaSession);
    
    // Used for the user reflections tablesorter
    List<Object[]> getUserReflectionsForTablesorter(final Long toolSessionId, int page, int size, int sorting,
	    String searchString, ICoreNotebookService coreNotebookService);
    int getCountUsersBySessionWithSearch(final Long toolSessionId, String searchString);
    
}
