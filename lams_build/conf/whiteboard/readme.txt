Whiteboard is required for Whiteboard tool

1. Get code from 
https://github.com/cracker0dks/whiteboard
Current version is 1.6 with all changes up to date 2021-04-23

2. Follow installation instructions from the GitHub page

3. Apply customisations from this folder:

   3.1 In main.js we allow cloning content from another Whiteboard canvas even if target canvas is not empty.
       There is also some processing of source data - all images are put in background, so target canvas' drawings go on top.

   3.2 In server.js we set up default port to 9003 instead of 8080, so it does not collide with WildFly development mode.