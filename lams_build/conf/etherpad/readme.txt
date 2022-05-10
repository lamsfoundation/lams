Etherpad is required for Dokumaran tool and for Etherpad tag

1) Download Etherpad at 1.8.14, available at
	https://github.com/ether/etherpad-lite/tree/1.8.14
	
	For Windows 10 installation, follow
	https://github.com/ether/etherpad-lite/issues/4800#issuecomment-780780133
	Then in node_modules folder create a symbolic link with
	mklink /D ep_etherpad-lite ..\src

2) In order to hide Etherpad index page make the following file empty: /${etherpad-lite-server-folder}/src/templates/index.html.
	Besides, one can turn on "editOnly" option in /${etherpad-lite-server-folder}/settings.json. This option's description: "Users may edit pads but not create new ones. Pad creation is only via the API. This applies both to group pads and regular pads."

3) Install plugins with exact version using this example command
   npm install --no-save --legacy-peer-deps ep_resize@0.0.15
   Newer versions of plugins can be incompatible with Etherpad 1.8.14 and customisations

	adminpads2: 		plugin to list and delete pads in /admin 2.1.38
	authornames:		Adds author names to span titles (shows on hover) 0.2.0
	author_neat2:		Neat author display 2.0.1
	comments_page:		Adds comments on sidebar and link it to the text 0.1.63
	headings2:			Adds heading support 0.2.30
	// ice_tables:		Add tables to etherpad 0.0.3
						This plugin has been abandoned and is not production ready.
						If a new version appears, customisations from "incompatible-plugins" directory can be used
					    
	image_upload:    	Add images to etherpad 1.0.51
						After installing set up ep_image_upload section at the botton of settings.json
						
						If this plugin installs correctly but does not display in Etherpad,
						manually add following section to package-lock.json after all other plugins (for example after ep_stats section)
						
						"ep_image_upload": {
					      "version": "1.0.51",
					      "resolved": "https://registry.npmjs.org/ep_image_upload/-/ep_image_upload-1.0.51.tgz",
					      "integrity": "sha512-zQvEJO4vPVh/GSBGg/8sus+VcF7R6nO1ZjQPkC+Ks1zPbplZU3R7vng56uCpxReJ/ABK8CKm4Zliq0fp8++uiw=="
					    },
					    
	previewimages:      Preview image when pasting a link to an image 0.0.13
	
						If this plugin installs correctly but does not display in Etherpad,
						manually add following section to package-lock.json after all other plugins (for example after ep_stats section)
						
						"ep_previewimages": {
					      "version": "0.0.13",
					      "resolved": "https://registry.npmjs.org/ep_previewimages/-/ep_previewimages-0.0.13.tgz",
					      "integrity": "sha512-UYAe9dpBXpqRVRDHFHUJ0qH2BGcbCdp/e4A2lNbuxEpel7fXXmlnwwZAco3W8iGmxOy0wTDVeOh01CzdWFLc/A=="
					    },
					    
	resize:				Notifies about pad size 0.0.15
	stats:				See pad Stats 0.0.11

4) Copy contents of folder /lams_buil/conf/etherpad/etherpad-lite/ over to /${etherpad-lite-server-folder}/
	It customises Etherpad and the plugins

5) Install Tidy HTML and set path to it in "tidyHtml" setting in settings.json
	
6) Install LibreOffice and set path to it in "soffice" setting in settings.json
	
7) To run Etherpad in iframes SSL is required. On localhost for development follow this guide
   https://github.com/ether/etherpad-lite/wiki/Providing-encrypted-web-access-to-Etherpad-Lite-using-SSL-certificates
   Then set up "ssl" secion in settings.json.
   Then set up Etherpad in sysadmin panel to use HTTPS, for example https://localhost:9001

----------Known issues---------------

https://github.com/ether/etherpad-lite/issues/5041
https://github.com/ether/etherpad-lite/issues/4525
	
----------Same domain policy---------
Etherpad server should be installed on the same domain as LAMS server. 
Although it's possible to install it on another subdomain, it has a few limitations in terms of format of supported domain names. 
Let's say domain name is domain.tld, then:
* tld part of the domain name should consist of only one part (e.g. lams.com, lams.au)
* if we need to support more complex tld parts (e.g. lams.com.ru) it needs to be added to the hardcoded regex in Java code - https://code.lamsfoundation.org/fisheye/browse/lams/lams_tool_doku/src/java/org/lamsfoundation/lams/tool/dokumaran/service/DokumaranService.java?hb=true#to892
Currently only complex tlds of Singapore, Australia and UK are added to this regex.  
** If we will decide to start supporting all possible domain tlds in the future - we will need to download full tld list (https://publicsuffix.org/list/effective_tld_names.dat) 
and make use of it using 3rd party Java library https://github.com/whois-server-list/public-suffix-list