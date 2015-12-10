/*
  Copyright (c) 2002, 2015, Oracle and/or its affiliates. All rights reserved.

  The MySQL Connector/J is licensed under the terms of the GPLv2
  <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most MySQL Connectors.
  There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
  this software, see the FOSS License Exception
  <http://www.mysql.com/about/legal/licensing/foss-exception.html>.

  This program is free software; you can redistribute it and/or modify it under the terms
  of the GNU General Public License as published by the Free Software Foundation; version 2
  of the License.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this
  program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
  Floor, Boston, MA 02110-1301  USA

 */

package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;

/**
 * Represents an in-memory result set
 */
public class RowDataStatic implements RowData {
    private Field[] metadata;

    private int index;

    ResultSetImpl owner;

    private List<ResultSetRow> rows;

    /**
     * Creates a new RowDataStatic object.
     * 
     * @param rows
     */
    public RowDataStatic(List<ResultSetRow> rows) {
        this.index = -1;
        this.rows = rows;
    }

    public void addRow(ResultSetRow row) {
        this.rows.add(row);
    }

    /**
     * Moves to after last.
     */
    public void afterLast() {
        if (this.rows.size() > 0) {
            this.index = this.rows.size();
        }
    }

    /**
     * Moves to before first.
     */
    public void beforeFirst() {
        if (this.rows.size() > 0) {
            this.index = -1;
        }
    }

    public void beforeLast() {
        if (this.rows.size() > 0) {
            this.index = this.rows.size() - 2;
        }
    }

    public void close() {
    }

    public ResultSetRow getAt(int atIndex) throws SQLException {
        if ((atIndex < 0) || (atIndex >= this.rows.size())) {
            return null;
        }

        return (this.rows.get(atIndex)).setMetadata(this.metadata);
    }

    public int getCurrentRowNumber() {
        return this.index;
    }

    /**
     * @see com.mysql.jdbc.RowData#getOwner()
     */
    public ResultSetInternalMethods getOwner() {
        return this.owner;
    }

    public boolean hasNext() {
        boolean hasMore = (this.index + 1) < this.rows.size();

        return hasMore;
    }

    /**
     * Returns true if we got the last element.
     */
    public boolean isAfterLast() {
        return this.index >= this.rows.size() && this.rows.size() != 0;
    }

    /**
     * Returns if iteration has not occurred yet.
     */
    public boolean isBeforeFirst() {
        return this.index == -1 && this.rows.size() != 0;
    }

    public boolean isDynamic() {
        return false;
    }

    public boolean isEmpty() {
        return this.rows.size() == 0;
    }

    public boolean isFirst() {
        return this.index == 0;
    }

    public boolean isLast() {
        //
        // You can never be on the 'last' row of an empty result set
        //
        if (this.rows.size() == 0) {
            return false;
        }

        return (this.index == (this.rows.size() - 1));
    }

    public void moveRowRelative(int rowsToMove) {
        if (this.rows.size() > 0) {
            this.index += rowsToMove;
            if (this.index < -1) {
                beforeFirst();
            } else if (this.index > this.rows.size()) {
                afterLast();
            }
        }
    }

    public ResultSetRow next() throws SQLException {
        this.index++;

        if (this.index > this.rows.size()) {
            afterLast();
        } else if (this.index < this.rows.size()) {
            ResultSetRow row = this.rows.get(this.index);

            return row.setMetadata(this.metadata);
        }

        return null;
    }

    public void removeRow(int atIndex) {
        this.rows.remove(atIndex);
    }

    public void setCurrentRow(int newIndex) {
        this.index = newIndex;
    }

    /**
     * @see com.mysql.jdbc.RowData#setOwner(com.mysql.jdbc.ResultSetInternalMethods)
     */
    public void setOwner(ResultSetImpl rs) {
        this.owner = rs;
    }

    public int size() {
        return this.rows.size();
    }

    public boolean wasEmpty() {
        return (this.rows != null && this.rows.size() == 0);
    }

    public void setMetadata(Field[] metadata) {
        this.metadata = metadata;
    }
}
