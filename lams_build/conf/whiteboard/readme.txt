Whiteboard is required for Whiteboard tool

1. Get code from 
https://github.com/cracker0dks/whiteboard
Current version is 1.7 with all changes up to date 2023-02-10
It requires Node.js version 16 as version 14 has a problem with image loading
https://stackoverflow.com/questions/63610932/express-static-network-requests-stuck-on-pending

2. Follow installation instructions from the GitHub page

3. Apply customisations from this folder:

   3.1 In main.js we allow cloning content from another Whiteboard canvas even if target canvas is not empty.
       There is also some processing of source data - all images are put in background, so target canvas' drawings go on top.

   3.2 In server.js and webpack.base.js we set up default port to 9003 instead of 8080,
   	   so it does not collide with WildFly development mode.
   	   Whiteboard in development mode seems to always run on 8081 port, though.
   
   3.3 In server-backend.js we introduce hashing of wid + accesstoken to improve security.
   	   Also an API method is added to upload Whiteboard canvas content after tool content import
   	   and a method to copy Whiteboard canvas on tool content copy.
   	      
   3.4 In s_whiteboard.js we introduce methods for copying and saving canvas contents.
   
   3.5 In index.html we hide some buttons
       In index.html and index.js we hide Whiteboard contents until everything loads, otherwise the UI looks messed up at first.
       
   3.6 In main.css we make buttons on iPad smaller. Otherwise they obstruct pretty much whole canvas.