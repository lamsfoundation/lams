/*
THIS SOURCE CODE IS PROVIDED "AS IS" AND "WITH ALL FAULTS", WITHOUT
ANY TECHNICAL SUPPORT OR ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING,
BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
FOR A PARTICULAR PURPOSE ARE DISCLAIMED. ALSO, THERE IS NO WARRANTY OF
NON-INFRINGEMENT, TITLE OR QUIET ENJOYMENT. IN NO EVENT SHALL MACROMEDIA
OR ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOURCE CODE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package pl.cfml.coldfusion.as3.utils
{
	import pl.cfml.coldfusion.as3.utils.ArrayUtils;
	
	/**
	 * Class reflecting ColdFusion query object.
	 */
	public dynamic class Recordset
	{
		
		/**
		 * @private
		 * Object containing arrays of values for Recordset columns.
		 */
		private var _columns:Object = {};
		/**
		 * @private 
		 * Number of rows in Recordset.
		 */
		private var _rowsNum:Number = 0;
		/** 
		 * @private
		 * Cursor pointing to Recordset current row.
		 */
		private var _currentRow:Number = 0;
		
		/**
		 * Constructor.
		 *
		 * @param columnList Comma-separated list of columns.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function Recordset(columnList:String=null)
		{
			if (columnList!=null)
			{
				var _cl:Array = columnList.split(",");
				for (var i:int=0; i<_cl.length; i++)
					this.addColumn(_cl[i],null,true);
			}
		}
		
		/**
		 * Comma-separated column list.
		 */
		public function get columnList():String
		{
			var lst:Array = [];
			for (var s:String in this._columns)
				lst.push( s );
			return lst.join(",");
		}
		
		/**
		 * Number of records in Recordset.
		 */
		public function get recordCount():Number
		{
			return this._rowsNum;
		}
		
		/**
		 * Current used row in Recordset.
		 */
		public function get currentRow():Number
		{
			return this._currentRow;
		}
		
		/**
		 * Adds column to Recordset.
		 *
		 * @param name The name of the column to be added.
		 * @param vals Array of simple values (String, Boolean, Number, Date) to set as default items for column.
		 * @param allowOverwrite If true and column exists it will be replaced; if false and column exists Error will be thrown.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function addColumn(name:String, vals:Array=null, allowOverwrite:Boolean=false):void
		{
			if (this._columns[name] != null && !allowOverwrite)
				throw new Error("Column "+name+" already exists.");
			this._columns[name] = [];
			if (vals!=null)
			{
				for (var i:int=0; i<vals.length; i++)
					if ((vals[i] is String) || (vals[i] is Boolean) || (vals[i] is Number) || (vals[i] is Date))
						(_columns[name] as Array).push(vals[i]);
					else
						throw new Error("Recordset can't store complex objects.");
			}
		}
		
		/**
		 * Adds row(s) to Recordset.
		 *
		 * @param count Number of rows to be added. By default each new field in Recordset will have value of "".
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function addRow(count:Number=1):void
		{
			for (var s:String in this._columns)
				for (var i:Number=0; i<count; i++)
					(this._columns[s] as Array).push("");
			this._rowsNum += count;
		}
		
		/**
		 * Deletes column with values from Recordset.
		 *
		 * @param name Name of the column to be deleted.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function dropColumn(name:String):void
		{
			this._columns[name] = null;
			delete this._columns[name];
		}
		
		/**
		 * Deletes rows where column value meets given condition.
		 *
		 * @param column Column name where search should be done. One column at time.
		 * @param value Value to be matched.
		 * @param likeType Type of search.
		 * @see pl.cfml.coldfusion.as3.utils.RecordsetLikeType
		 * @param likeCaseSensitive If true search will be case-sensitive.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function dropRowWhere(column:String, value:String, likeSearch:Boolean=false, likeType:int=0, likeCaseSensitive:Boolean=true):void
		{
			this.testColumn(column);
			var indexes:Array = [];
			for (var i:int=0; i<(this._columns[column] as Array).length; i++)
			{
				if (!likeSearch)
				{
					if ((this._columns[column] as Array)[i].toString() == value)
						indexes.push(i);
				}
				else
				{
					switch (likeType)
					{
						case 0:
							if (likeCaseSensitive)
							{
								if ((this._columns[column] as Array)[i].toString().indexf(value) > -1)
									indexes.push(i);
							}
							else
							{
								if ((this._columns[column] as Array)[i].toString().toLowerCase().indexf(value.toLowerCase()) > -1)
									indexes.push(i);
							}	
							break;
						case 1:
							if (likeCaseSensitive)
							{
								if ((this._columns[column] as Array)[i].toString().substr(0,value.length) == value)
									indexes.push(i);
							}
							else
							{
								if ((this._columns[column] as Array)[i].toString().toLowerCase().substr(0,value.length) == value.toLowerCase())
									indexes.push(i);
							}
							break;
						case 2:
							if (likeCaseSensitive)
							{
								if ((this._columns[column] as Array)[i].toString().substr( (this._columns[column] as Array)[i].toString().length-value.length ,value.length) == value)
									indexes.push(i);
							}
							else
							{
								if ((this._columns[column] as Array)[i].toString().substr( (this._columns[column] as Array)[i].toString().length-value.length ,value.length).toLowerCase() == value.toLowerCase())
									indexes.push(i);
							}
							break;
					}
				}
			}
			this.doDeleteRows( indexes );
		}
		
		/**
		 * Gets Recordset field value.
		 *
		 * @param column Recordset column.
		 * @param index Row number. If 0 row number is obtained by currentRow else index is used.
		 * @return Field value.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function getValue(column:String, index:Number=0):String
		{
			if (this._rowsNum == 0)
				throw new Error("Recordset doesn't contain any row. Use addRow().");
			if (index < 0)
				throw new Error("Minimum row number for Recordset is 1.");
			return (this._columns[column] as Array)[((index==0) ? this._currentRow-1 : index)];
		}
		
		/**
		 * Sets Recordset field value.
		 *
		 * @param column Recordset column.
		 * @param index Row number.
		 * @return Field value.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function setValue(column:String, value:String, row:Number=1):void
		{
			this.testColumn(column);
			if (row < 1)
				throw new Error("Minimum row number for Recordset is 1.");
			if (row > this._rowsNum)
				throw new Error("Row "+row+" doesn't exist. Use addRow().");
			this._columns[column][(row-1)] = value;
		}
		
		/**
		 * Returns string representation of Recordset.
		 *
		 * @return String representation of Recordset.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function toString():String
		{
			var ret:String = "Recordset:\n";
			for (var s:String in this._columns)
			{
				ret += "  " + s + ":\n";
				for(var i:Number=0; i<this._rowsNum; i++)
					ret += '    '+(i+1)+': "' + (this._columns[s] as Array)[i] + '"\n';
			}
			return ret;
		}
		
		/**
		 * Sets currentRow to first row in Recordset.
		 * 
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function reset():void
		{
			this._currentRow = 0;
		}
		
		/**
		 * Iterator-type method - returns next available currentRow.
		 * 
		 * @return Next currentRow or -1 if end of Recordset reached.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function next():Number
		{
			if ((this._currentRow+1)>this.recordCount)
				return -1;
			this._currentRow++;
			return this._currentRow;
		}
		
		/**
		 * Iterator-type method - returns prev available currentRow.
		 * 
		 * @return Prev currentRow or -1 if beggining of Recordset reached.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		public function prev():Number
		{
			if ((this._currentRow-1)<0)
				return -1;
			this._currentRow--;
			return this._currentRow;
		}
		
		/**
		 * Parses ColdFusion WDDX query representation to Recordset.
		 *
		 * @param data WDDX representation of ColdFusion query as XML.
		 * @return New Recordset.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 * @see pl.cfml.coldfusion.as3.utils.WDDX#fromWDDX()
		 */
		public static function fromWDDXXML(data:XML):Recordset
		{
			var r:Recordset = new Recordset(data.@fieldNames.toString());
			r.addRow( parseInt(data.@rowCount.toString()) );
			var _cl:Array = r.columnList.split(",");
			for (var i:Number=0; i<_cl.length; i++)
			{
				var values:XMLList = data.field.(@name.toString() == _cl[i]).children();
				var counter:Number = 1;
				for each (var val:XML in values)
				{
					if (val.name().toString() == "null")
						r.setValue(_cl[i], "", counter);
					else
						r.setValue(_cl[i], val.text().toString(), counter);
					counter++;
				}
			}
			return r;
		}
		
		/**
		 * @private
		 * Checks for column existence.
		 *
		 * @param cName Name of the column.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		private function testColumn(cName:String):void
		{
			if (this._columns[cName] == null)
				throw new Error("Column "+cName+" doesn't exists in Recordset.");
		}
		
		/**
		 * @private
		 * Delete rows.
		 *
		 * @param indexes Array of row numbers to be removed.
		 * @langversion ActionScript 3.0
		 * @playerversion Flash 9.0
		 * @tiptext
		 */
		private function doDeleteRows( indexes:Array ):void
		{
			var _tmpCols:Object = {};
			for (var s:String in this._columns)
			{
				_tmpCols[s] = [];
				for (var i:Number=0; i<this._rowsNum; i++)
					if (!ArrayUtils.inArray(indexes,i))
						(_tmpCols[s] as Array).push( this._columns[i] );
			}
			this._rowsNum -= indexes.length;
			this._columns = _tmpCols;
		}
		
	}
}