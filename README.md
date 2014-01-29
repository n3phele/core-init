core-init
=========

Initialize core for dev environment

run the executable inside the 'last' folder.

To run cloud creation and test user run it like:

  java -jar n3phele-init.jar init 3
  
for a non localhost address add a third parameter

  java -jar n3phele-init.jar init 3 https://n3phele-dev.appspot.com/resources
  
The second parameter specify the password of the root user. The root password is created using a default '3' password, but can be changed setting the 'serviceSecret' variable inside the service.properties file (for local running) or inside the GlobalSettings kind using the appengine datastore viewer. The root is generated when n3phele runs and there is no root inside the database, that occurs in the first run time or if you delete it from the database and restart.

