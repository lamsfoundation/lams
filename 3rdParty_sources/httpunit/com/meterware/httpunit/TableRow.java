package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2007, Russell Gold
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
* documentation files (the "Software"), to deal in the Software without restriction, including without limitation
* the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
* to permit persons to whom the Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial portions
* of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
* THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
* CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*******************************************************************************************************************/
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;

import java.util.ArrayList;

import com.meterware.httpunit.scripting.ScriptableDelegate;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
public class TableRow extends HTMLElementBase {

    private ArrayList _cells = new ArrayList();
    private WebTable _webTable;
    private HTMLTableRowElement _element;


    TableRow( WebTable webTable, HTMLTableRowElement element ) {
        super( element );
        _element = element;
        _webTable = webTable;
    }


    TableCell[] getCells() {
        
        return (TableCell[]) _cells.toArray( new TableCell[ _cells.size() ]);
    }


    TableCell newTableCell( HTMLTableCellElement element ) {
        return _webTable.newTableCell( element );
    }


    void addTableCell( TableCell cell ) {
        _cells.add( cell );
    }


    public ScriptableDelegate newScriptable() {
        return new HTMLElementScriptable( this );
    }


    public ScriptableDelegate getParentDelegate() {
        return _webTable.getParentDelegate();
    }
}
