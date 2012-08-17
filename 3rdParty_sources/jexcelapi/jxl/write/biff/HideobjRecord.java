/*********************************************************************
*
*      Copyright (C) 2002 Andrew Khan
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
***************************************************************************/

package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

/**
 * Stores options selected in the Options dialog box
 */
class HideobjRecord extends WritableRecordData
{
  /**
   * Flag to hide everything
   */
  private boolean hideAll;
  /**
   * The binary data
   */
  private byte[] data;

  /**
   * Constructor
   * 
   * @param hide the hide all flag
   */
  public HideobjRecord(boolean hide)
  {
    super(Type.HIDEOBJ);

    hideAll = hide;
    data = new byte[2];

    if (hideAll)
    {
      IntegerHelper.getTwoBytes(2, data, 0);
    }
  }

  /**
   * Gets the binary data for output to file
   * 
   * @return the binary data
   */
  public byte[] getData()
  {
    return data;
  }
}
