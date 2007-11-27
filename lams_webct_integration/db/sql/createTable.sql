USE webctdatabase;

IF EXISTS
	(SELECT 1 FROM sys.objects WHERE OBJECT_ID = OBJECT_ID(N'[dbo].[LAMS_LESSON]') 
	AND type = (N'U'))    
	DROP TABLE [dbo].[LAMS_LESSON];

CREATE TABLE [dbo].[LAMS_LESSON](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[pt_id] [bigint] NOT NULL,
	[lesson_id] [bigint] NOT NULL,
	[learning_context_id] [bigint] NOT NULL,
	[sequence_id] [bigint] NULL,
	[owner_id] [varchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	[owner_first_name] [varchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	[owner_last_name] [varchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	[title] [varchar](255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	[description] [text] COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	[hidden] [bit] NOT NULL CONSTRAINT [DF_LAMS_LESSON_hidden]  DEFAULT ((0)),
	[schedule] [bit] NOT NULL CONSTRAINT [DF_LAMS_LESSON_schedule]  DEFAULT ((0)),
	[start_date_time] [datetime] NULL,
	[end_date_time] [datetime] NULL,
 CONSTRAINT [PK_LAMS_LESSON] PRIMARY KEY CLUSTERED 
(
	[lesson_id] ASC
)WITH (IGNORE_DUP_KEY = OFF) ON [WEBCT_DATA]
) ON [WEBCT_DATA] TEXTIMAGE_ON [WEBCT_DATA];

