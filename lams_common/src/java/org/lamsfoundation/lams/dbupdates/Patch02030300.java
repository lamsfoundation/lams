/****************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.dbupdates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTaskSupport;
import com.tacitknowledge.util.migration.jdbc.DataSourceMigrationContext;

public class Patch02030300 extends MigrationTaskSupport {

    private static Logger log = Logger.getLogger(Patch02030300.class);

    private static final Integer LEVEL = new Integer(2030300);

    private static final String NAME = "Fix3rdPartyUserRoles";

    public Patch02030300() {
	setLevel(LEVEL);
	setName(NAME);
    }

    @Override
    public void migrate(MigrationContext context) throws MigrationException {

	// using data source defined in application container
	DataSourceMigrationContext ctx = (DataSourceMigrationContext) context;

	Connection conn = null;

	try {
	    conn = ctx.getConnection();
	    conn.setAutoCommit(false);

	    // get list of org ids
	    ArrayList<Integer> orgIds = new ArrayList<Integer>();
	    String queryStr = "select distinct uo.organisation_id "
		    + "from lams_user_organisation uo, lams_ext_user_userid_map eu " + "where eu.user_id=uo.user_id";
	    PreparedStatement query = conn.prepareStatement(queryStr);
	    if (query.execute()) {
		ResultSet rs = query.getResultSet();
		while (rs.next()) {
		    orgIds.add(rs.getInt(1));
		}
	    }

	    for (Integer orgId : orgIds) {
		log.debug("Found 3rd party organisation with id " + orgId + "...");
		// get staff user ids for given org id
		ArrayList<Integer> staffIds = new ArrayList<Integer>();
		queryStr = "select distinct ug.user_id "
			+ "from lams_lesson l, lams_grouping gg, lams_group g, lams_user_group ug "
			+ "where l.class_grouping_id=gg.grouping_id " + "and gg.staff_group_id=g.group_id "
			+ "and g.group_id=ug.group_id " + "and l.organisation_id=?";
		query = conn.prepareStatement(queryStr);
		query.setInt(1, orgId);
		if (query.execute()) {
		    ResultSet rs = query.getResultSet();
		    while (rs.next()) {
			staffIds.add(rs.getInt(1));
		    }
		}

		// get user ids in given org id
		ArrayList<Integer> userIds = new ArrayList<Integer>();
		queryStr = "select distinct uo.user_id "
			+ "from lams_user_organisation uo, lams_ext_user_userid_map eu "
			+ "where uo.user_id=eu.user_id " + "and uo.organisation_id=?";
		query = conn.prepareStatement(queryStr);
		query.setInt(1, orgId);
		if (query.execute()) {
		    ResultSet rs = query.getResultSet();
		    while (rs.next()) {
			userIds.add(rs.getInt(1));
		    }
		}

		for (Integer userId : userIds) {
		    if (!staffIds.contains(userId)) {
			// remove non-learner roles if not in staff list for
			// given org id
			queryStr = "delete uor.* " + "from lams_user_organisation_role uor, lams_user_organisation uo "
				+ "where uo.user_id=? " + "and uo.user_organisation_id=uor.user_organisation_id "
				+ "and uo.organisation_id=? " + "and uor.role_id!=5";
			query = conn.prepareStatement(queryStr);
			query.setInt(1, userId);
			query.setInt(2, orgId);
			int deleted = query.executeUpdate();
			log.info("Deleted " + deleted + " non-learner roles for user id " + userId + " in org id "
				+ orgId);
		    }
		}
	    }

	    ctx.commit();

	    // conn.close(); // container managed data source
	} catch (Exception e) {
	    ctx.rollback();
	    throw new MigrationException("Problem running update; ", e);
	}
    }

}