package org.lamsfoundation.lams.util;

public class CommonConstants {

    // params passed from the jqGrid
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_ROWS = "rows";
    public static final String PARAM_SIDX = "sidx";
    public static final String PARAM_SORD = "sord";

    public static final String PARAM_SEARCH = "_search";
    public static final String PARAM_SEARCH_FIELD = "searchField";
    public static final String PARAM_SEARCH_OPERATION = "searchOper";
    public static final String PARAM_SEARCH_STRING = "searchString";

    // default coordinate used if the entry came from Flash is 0 or less.
    public static final Integer DEFAULT_COORD = new Integer(10);

    // XML Elemetns
    public static final String ELEMENT_ROWS = "rows";
    public static final String ELEMENT_PAGE = "page";
    public static final String ELEMENT_TOTAL = "total";
    public static final String ELEMENT_RECORDS = "records";
    public static final String ELEMENT_ROW = "row";
    public static final String ELEMENT_ID = "id";
    public static final String ELEMENT_CELL = "cell";

    public static final String PARAM_LEARNING_DESIGN_ID = "ldId";
    public static final String PARAM_USE_PREFIX = "usePrefix";
    public static final String PARAM_MODE = "mode";

    public static int PORTRAIT_LARGEST_DIMENSION_LARGE = 200;
    public static int PORTRAIT_LARGEST_DIMENSION_MEDIUM = 80;
    public static int PORTRAIT_LARGEST_DIMENSION_SMALL = 35;

    //used by all tool authoring action class to mark the success flag.
    public static final String LAMS_AUTHORING_SUCCESS_FLAG = "LAMS_AUTHORING_SUCCESS_FLAG";

    // used for tool content folder creation.
    public static final String LAMS_WWW_FOLDER = "www/";

    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String RESPONSE_CONTENT_TYPE_DOWNLOAD = "application/x-download";
    public static final String HEADER_CONTENT_ATTACHMENT = "attachment;filename=";

    public static final String TOOL_SIGNATURE_ASSESSMENT = "laasse10";
    public static final String TOOL_SIGNATURE_DOKU = "ladoku11";
    public static final String TOOL_SIGNATURE_FORUM = "lafrum11";
    public static final String TOOL_SIGNATURE_LEADERSELECTION = "lalead11";
    public static final String TOOL_SIGNATURE_MCQ = "lamc11";
    public static final String TOOL_SIGNATURE_SCRATCHIE = "lascrt11";
    public static final String TOOL_SIGNATURE_PEER_REVIEW = "laprev11";

    public static final String[] COUNTRY_CODES = { "AD", "AE", "AF", "AG", "AI", "AL", "AM", "AO", "AQ", "AR", "AS",
	    "AT", "AU", "AI", "AL", "AM", "AO", "AQ", "AR", "AS", "AT", "AU", "AW", "AX", "AZ", "BA", "BB", "BD", "BE",
	    "BF", "BG", "BH", "BI", "BJ", "BL", "BM", "BN", "BO", "BQ", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA",
	    "CC", "CD", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CX", "CW", "CY", "CZ",
	    "DE", "DJ", "DK", "DM", "DO", "DZ", "EC", "EE", "EG", "EH", "ER", "ES", "ET", "FI", "FJ", "FK", "FM", "FO",
	    "FR", "GA", "GB", "GD", "GE", "GF", "GG", "GH", "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU",
	    "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU", "ID", "IE", "IL", "IM", "IN", "IO", "IQ", "IR", "IS", "IT",
	    "JE", "JM", "JO", "JP", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC",
	    "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MF", "MG", "MH", "MK", "ML", "MM",
	    "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG",
	    "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR",
	    "PS", "PT", "PW", "PY", "QA", "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SX", "SH",
	    "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "SS", "ST", "SV", "SY", "SZ", "TC", "TD", "TF", "TG", "TH",
	    "TJ", "TK", "TL", "TM", "TN", "TO", "TR", "TT", "TV", "TW", "TZ", "UA", "UG", "UM", "US", "UY", "UZ", "VA",
	    "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "YE", "YT", "ZA", "ZM", "ZW" };
}
