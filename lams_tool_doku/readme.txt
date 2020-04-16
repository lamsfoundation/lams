Modifications required to be done for Etherpad server (version #1.8.0 - develop branch, snapshot made on 2020-04-16):
* Install ep_resize plugin
https://github.com/tiblu/ep_resize

* Copy contents of folder /lams_tool_doku/conf/etherpad-lite/ over to /${etherpad-lite-server-folder}/
It customises Etherpad and the plugin

* In order to hide Etherpad index page make the following file empty: /${etherpad-lite-server-folder}/src/templates/index.html.
Besides, one can turn on "editOnly" option in /${etherpad-lite-server-folder}/settings.json. This option's description: "Users may edit pads but not create new ones. Pad creation is only via the API. This applies both to group pads and regular pads."


----------Same domain policy---------
Etherpad server should be installed on the same domain as LAMS server. 
Although it's possible to install it on another subdomain, it has a few limitations in terms of format of supported domain names. 
Let's say domain name is domain.tld, then:
* tld part of the domain name should consist of only one part (e.g. lams.com, lams.au)
* if we need to support more complex tld parts (e.g. lams.com.ru) it needs to be added to the hardcoded regex in Java code - https://code.lamsfoundation.org/fisheye/browse/lams/lams_tool_doku/src/java/org/lamsfoundation/lams/tool/dokumaran/service/DokumaranService.java?hb=true#to892
Currently only complex tlds of Singapore, Australia and UK are added to this regex.  
** If we will decide to start supporting all possible domain tlds in the future - we will need to download full tld list (https://publicsuffix.org/list/effective_tld_names.dat) 
and make use of it using 3rd party Java library https://github.com/whois-server-list/public-suffix-list  
