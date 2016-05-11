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


package org.lamsfoundation.lams.config.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.config.Registration;
import org.lamsfoundation.lams.config.dao.IRegistrationDAO;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;

public class RegistrationDAO extends BaseDAO implements IRegistrationDAO {

    private static final String LOAD_REG = "from registration in class " + Registration.class.getName();

    @Override
    public void saveOrUpdate(Registration reg) {
	getHibernateTemplate().saveOrUpdate(reg);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Registration get() {
	List list = getHibernateTemplate().find(LOAD_REG);

	if (list != null && list.size() > 0) {
	    return (Registration) list.get(0);
	} else {
	    return null;
	}
    }

}
