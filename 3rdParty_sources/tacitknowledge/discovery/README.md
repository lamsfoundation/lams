Discovery
=========

Discovery is used to detect resources (files or interface
implementations) in a Java classpath.

Maven
-----

    <dependency>
      <groupId>com.tacitknowledge</groupId>
      <artifactId>discovery</artifactId>
      <version>1.0.3</version>
    </dependency>


Simple Examples
---------------

To find any implementation of MigrationTask in com.example do:

    Class[] taskClasses = ClassDiscoveryUtil.getClasses("com.example", MigrationTask.class);

To find any resource named foo\*.sql in a "patch" folder within the classpath do:

    String[] scripts = ClassDiscoveryUtil.getResources("patch", "^foo.*\\.sql$");
