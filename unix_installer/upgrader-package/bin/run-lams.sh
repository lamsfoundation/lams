#   Copyright (C) 2005-2007 LAMS Foundation (http://lamsfoundation.org)
#   License Information: http://lamsfoundation.org/licensing/lams/2.0/
#
#   This program is free software; you can redistribute it and/or modify
#   it under the terms of the GNU General Public License version 2.0 
#   as published by the Free Software Foundation.
# 
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
# 
#   You should have received a copy of the GNU General Public License
#   along with this program; if not, write to the Free Software
#   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
#   USA
# 
#   http://www.gnu.org/licenses/gpl.txt 
#

# run script for LAMS unix version 2.0.2 and higher

export JAVA_HOME="@JAVA_HOME@"
export JAVA_OPTS="-server -Xms256m -Xmx512m -XX:MaxPermSize=256m"

echo " "
echo "LAMS will take a few minutes to startup..."
echo " "
echo "Check server/default/log/boot.log and server/default/log/server.log to see if LAMS has started correctly. The server.log will contain a line similar to 'JBoss (MX MicroKernel) [4.0.2 (build: CVSTag=JBoss_4_0_2 date=200505022023)] Started in 1m:6s:987ms' when LAMS has finished loading."
echo " "
nohup ./run.sh > /dev/null &

