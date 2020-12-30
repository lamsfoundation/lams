Etherpad is required for Dokumaran tool and for Etherpad tag

1) Modifications are required for Etherpad server version 1.8.5, available at
	https://github.com/ether/etherpad-lite/tree/1.8.5

2) In order to hide Etherpad index page make the following file empty: /${etherpad-lite-server-folder}/src/templates/index.html.
	Besides, one can turn on "editOnly" option in /${etherpad-lite-server-folder}/settings.json. This option's description: "Users may edit pads but not create new ones. Pad creation is only via the API. This applies both to group pads and regular pads."

3) Install plugins with exact version using this example command
   npm install ep_resize@0.0.7
   Newer versions of plugins can be incompatible with Etherpad 1.8.5.

	adminpads2: 		plugin to list and delete pads in /admin 2.1.16
	authornames:		Adds author names to span titles (shows on hover) 0.2.0
	author_neat:		Neat author display 0.0.31
	comments_page:		Adds comments on sidebar and link it to the text 0.1.41
	export_authors:		Etherpad plugin to add author information to the html export 0.1.1
	headings2:			Adds heading support 0.2.7
	message_all:		Use the Etherpad admin backend to send a message to all connected users 1.1.0
	resize				Notifies about pad size 0.0.7
	stats:				See pad Stats 0.0.6

4) Copy contents of folder /lams_tool_doku/conf/etherpad-lite/ over to /${etherpad-lite-server-folder}/
	It customises Etherpad and the plugins

	
----------Same domain policy---------
Etherpad server should be installed on the same domain as LAMS server. 
Although it's possible to install it on another subdomain, it has a few limitations in terms of format of supported domain names. 
Let's say domain name is domain.tld, then:
* tld part of the domain name should consist of only one part (e.g. lams.com, lams.au)
* if we need to support more complex tld parts (e.g. lams.com.ru) it needs to be added to the hardcoded regex in Java code - https://code.lamsfoundation.org/fisheye/browse/lams/lams_tool_doku/src/java/org/lamsfoundation/lams/tool/dokumaran/service/DokumaranService.java?hb=true#to892
Currently only complex tlds of Singapore, Australia and UK are added to this regex.  
** If we will decide to start supporting all possible domain tlds in the future - we will need to download full tld list (https://publicsuffix.org/list/effective_tld_names.dat) 
and make use of it using 3rd party Java library https://github.com/whois-server-list/public-suffix-list