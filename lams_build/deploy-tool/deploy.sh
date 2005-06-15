#!/bin/sh


java -classpath lib/lams-tool-deploy.jar:lib/commons-configuration-1.1.jar:lib/commons-lang-2.0.jar:lib/commons-collections.jar:lib/commons-logging.jar:lib/commons-io-1.0.jar:lib/commons-dbutils-1.0.jar:lib/mysql-connector-java-3.1.7-bin.jar org.lamsfoundation.lams.tool.deploy.Deploy ./deploy.properties
