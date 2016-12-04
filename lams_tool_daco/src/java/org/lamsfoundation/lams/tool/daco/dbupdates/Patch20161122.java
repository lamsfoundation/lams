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

package org.lamsfoundation.lams.tool.daco.dbupdates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.daco.DacoConstants;

import com.tacitknowledge.util.migration.MigrationContext;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.MigrationTaskSupport;
import com.tacitknowledge.util.migration.jdbc.DataSourceMigrationContext;

public class Patch20161122 extends MigrationTaskSupport {
    private static final Integer LEVEL = new Integer(20161122);

    private static final String NAME = "FixDateFormatting";

    private static final DateFormat OLD_DATE_FORMAT = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");

    public Patch20161122() {
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

	    // read dates one by one and convert them to new format
	    PreparedStatement readQuery = conn.prepareStatement("SELECT a.uid, a.answer FROM tl_ladaco10_answers AS a "
		    + "JOIN tl_ladaco10_questions AS q ON a.question_uid = q.uid WHERE q.question_type = 4");
	    PreparedStatement updateQuery = conn
		    .prepareStatement("UPDATE tl_ladaco10_answers SET answer=? WHERE uid=?");
	    if (readQuery.execute()) {
		ResultSet rs = readQuery.getResultSet();
		while (rs.next()) {
		    String answer = rs.getString(2);
		    if (StringUtils.isBlank(answer)) {
			continue;
		    }
		    Date date = OLD_DATE_FORMAT.parse(answer);
		    String newFormat = DacoConstants.DEFAULT_DATE_FORMAT.format(date);
		    updateQuery.setString(1, newFormat);
		    updateQuery.setLong(2, rs.getLong(1));
		    updateQuery.executeUpdate();
		}
	    }

	    ctx.commit();
	} catch (Exception e) {
	    ctx.rollback();
	    throw new MigrationException("Problem running update; ", e);
	}
    }
}