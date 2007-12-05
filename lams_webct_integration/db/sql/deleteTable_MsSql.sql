USE webctdatabase;

IF EXISTS
	(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'dbo.LAMS_LESSON') 
	AND type = (N'U'))    
	DROP TABLE dbo.LAMS_LESSON;