
package com.example.managerfile.Exception;

public final class ExceptionCode {

    public static final int CLIENT_PROTOCOL_EXCEPTION = 1110;

    public static final int DATE_PARSE_ERROR = 2030;

    public static final int FILE_NOT_FOUND = 1001;

    public static final int JSON_PARSE_ERROR = 2020;

    public static final int NET_ISSUE = 1100;

    public static final int NET_I_O_EXCEPTION = 1102;

    public static final int NET_SOCKET_TIME_OUT = 1101;

    public static final int NET_UNCONNECTED = 1103;

    public static final int NOT_A_FILE = 1002;

    public static final int OAUTH_ADDITIONAL_AUTHORIZATION_REQUIRED = 60015;

    public static final int OAUTH_AUTHORIZATION_REQUIRED = 60000;

    public static final int OAUTH_CONSUMER_KEY_REFUSED = 60019;

    public static final int OAUTH_CONSUMER_KEY_REJECTED = 60014;

    public static final int OAUTH_CONSUMER_KEY_UNKNOWN = 60013;

    public static final int OAUTH_EXCEPTION = 2040;
    

    public static final int OAUTH_NONCE_USED = 60006;

    public static final int OAUTH_PARAMETER_ABSENT = 60002;

    public static final int OAUTH_PARAMETER_REJECTED = 60003;

    public static final int OAUTH_PERMISSION_DENIED = 60017;

    public static final int OAUTH_PERMISSION_UNKNOWN = 60016;

    public static final int OAUTH_SIGNATURE_INVALID = 60012;

    public static final int OAUTH_SIGNATURE_METHOD_REJECTED = 60005;

    public static final int OAUTH_TIMESTAMP_REFUSED = 60004;

    public static final int OAUTH_TOKEN_EXPIRED = 60008;

    public static final int OAUTH_TOKEN_NOT_AUTHORIZED = 60011;

    public static final int OAUTH_TOKEN_REJECTED = 60010;

    public static final int OAUTH_TOKEN_REVOKED = 60009;

    public static final int OAUTH_TOKEN_USED = 60007;

    public static final int OAUTH_USER_REFUSED = 60018;

    public static final int OAUTH_VERSION_REJECTED = 60001;

    public static final int PARAMETER_ERROR = 0;

    public static final int PARAMETER_NULL = 900;

    public static final int SC_REQUEST_TIMEOUT = 408;

    public static final int SC_OK = 200;// OK: Success!
    public static final int SC_MULTIPLE_CHOICES = 300;//
    public static final int SC_FOUND = 302;//
    public static final int SC_NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    public static final int SC_BAD_REQUEST = 400;// Bad Request: The request was invalid. An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    public static final int SC_UNAUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    public static final int SC_FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    public static final int SC_NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    public static final int SC_NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    public static final int SC_TOO_LONG = 413;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    /**
     * @see <a href="http://groups.google.com/group/twitter-api-announce/browse_thread/thread/3f3b0fd38deb9b0f?hl=en">Search API: new HTTP response code 420 for rate limiting starting 1/18/2010</a>
     */
    public static final int SC_ENHANCE_YOUR_CLAIM = 420;// Enhance Your Calm: Returned by the Search and Trends API  when you are being rate limited. Not registered in RFC.
    public static final int SC_INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken. Please post to the group so the Twitter team can investigate.
    public static final int SC_BAD_GATEWAY = 502;// Bad Gateway: Twitter is down or being upgraded.
    public static final int SC_SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.

    public static final int UNKNOWN_EXCEPTION = 255;

    public static final int UNSUPPORTED_API = 1000;

    public static final int URI_SYNTAX_ERROR = 2011;

    public static final int URL_MALFORMED_ERROR = 2010;
    
    public static final int OAUTH_EXCEPTION_DATE=2050;

    public final class MicroBlog {

        public static final int API_ALREADY_FOLLOWED = 40303;

        public static final int API_APPKEY_EMPTY = 40044;

        public static final int API_APPKEY_SOURCE_EMPTY = 40022;
        
        //add by cly @ 2012.2.29
        public static final int API_AUTH_FAILD = 40302;
        //add end

        public static final int API_AUTH_PASSWORD_INVALID = 40309;

        public static final int API_AUTH_PIN_VERIFY_FAILED = 40114;

        public static final int API_AUTH_REMOVED = 40072;

        public static final int API_AUTH_TIMESTAMP_ERROR = 40104;

        public static final int API_AUTH_TOKEN_EMPTY = 40042;

        public static final int API_AUTH_TOKEN_EXPIRED = 40111;

        public static final int API_AUTH_VERIFIER_ERROR = 40029;

        public static final int API_AUTH_VERIFY_OVER_THRESHOLD = 40306;

        public static final int API_CATEGORY_INVALID = 40082;

        public static final int API_COMMENT_ID_EMPTY = 40020;

        public static final int API_COMMENT_ILLEGAL = 40037;

        public static final int API_COMMENT_NOT_OWNER = 40015;

        public static final int API_COMMENT_OVER_THRESHOLD = 40305;

        public static final int API_CONTENT_EMPTY = 40012;

        public static final int API_CONTENT_ILLEGAL = 40076;

        public static final int API_CONTENT_OVER_LENGTH = 40013;

        public static final int API_DB_ERROR = 40060;

        public static final int API_DB_RECORD_EXISTS = 40059;

        public static final int API_DOMAIN_ERROR = 40043;

        public static final int API_FORMAT_UNSUPPORT = 40007;

        public static final int API_GEO_PARAM_ERROR = 40039;

        public static final int API_GROUP_PRIVATE_SUPPORT_ONLY = 40084;

        public static final int API_GROUP_PRIVATE_UNSUPPORT = 40073;

        public static final int API_HTTP_METHOD_UNSUPPORT = 40307;

        public static final int API_IDS_EMPTY = 40018;

        public static final int API_IDS_TOO_MANY = 40024;

        public static final int API_IMAGE_FORMAT_UNSUPPORT = 40045;

        public static final int API_IMAGE_OVER_SIZE = 40008;

        public static final int API_IMAGE_UPLOAD_ERROR = 40009;

        public static final int API_INSUFFICIENT_PRIVILEGES = 40053;

        public static final int API_INTERNAL_ERROR = 40028;

        public static final int API_INVOKE_RATE_TOO_QUICK = 40401;

        public static final int API_IP_EMPTY = 40065;

        public static final int API_IP_LIMITED = 40040;

        public static final int API_ITEM_NOT_EXIST = 40520;

        public static final int API_ITEM_REVIEWING = 40530;

        public static final int API_LIST_CREATE_FAILED = 40074;

        public static final int API_LIST_DESC_TOO_LONG_ = 40030;

        public static final int API_LIST_ID_TOO_LONG = 40062;

        public static final int API_LIST_NAME_DUPLICATED = 40061;

        public static final int API_LIST_NAME_TOO_LONG = 40032;

        public static final int API_LIST_NOT_EXIST = 40035;

        public static final int API_MESSAGE_ILLEGAL = 40019;

        public static final int API_MESSAGE_LIMITED = 40011;

        public static final int API_MESSAGE_NOT_EXIST = 40010;

        public static final int API_MESSAGE_NOT_OWNER = 40021;

        public static final int API_MESSAGE_RECEIVER_NOT_FOLLOWER = 40017;

        public static final int API_NOT_OWNER = 40002;

        public static final int API_PARAMS_ERROR = 40001;

        public static final int API_PARAM_ERROR = 40054;

        public static final int API_PERMISSION_ACCESS_DENIED = 40500;

        public static final int API_PERMISSION_ACCESS_LIMITED = 40070;

        public static final int API_PERMISSION_NEED_ADMIN = 40075;

        public static final int API_PERMISSION_NEED_MORE_AUTHORIZED = 40314;

        public static final int API_PERMISSION_REMIND_FAILED = 40084;

        public static final int API_RATE_LIMITED = 40312;

        public static final int API_RETWEET_SELF = 40034;

        public static final int API_SEARCH_RATE_LIMITED = 40402;

        public static final int API_SIGNATURE_INVALID = 40107;

        public static final int API_SOCIAL_GRAPH_OVER = 40304;

        public static final int API_STATUS_CODE_INVALID = 40083;

        public static final int API_TAG_EMPTY = 40027;

        public static final int API_TREND_ID_EMPTY = 40068;

        public static final int API_TREND_NAME_EMPTY = 40067;

        public static final int API_TWEET_ID_EMPTY = 40016;

        public static final int API_TWEET_ILLEGAL = 40038;

        public static final int API_TWEET_NOT_EXIST = 40031;

        public static final int API_TWEET_NOT_OWNER = 40036;

        public static final int API_TWEET_OVER_THRESHOLD = 40308;

        public static final int API_TWEET_REPEAT = 40025;

        public static final int API_UID_EMPTY = 40041;

        public static final int API_URLS_EMPTY = 40063;

        public static final int API_URLS_TOO_MANY = 40064;

        public static final int API_URL_EMPTY = 40066;

        public static final int API_USER_ID_EMPTY = 40069;

        public static final int API_USER_IS_FOLLOWING = 40510;

        public static final int API_USER_NOT_EXIST = 40023;

        public static final int API_USER_NOT_FOLLOWING = 40071;

        public static final int API_USER_PARAM_ERROR = 40026;

        public static final int API_USER_SCREEN_NAME_EXIST = 40511;

        public static final int API_USER_S_T_NOT_EXIST = 40033;
        
        //add by liyin       
        public static final int API_INVALID_TOKEN=34;
        public static final int API_FOLLOW_MUCH=90002;
        public static final int API_INTERFACE_CLOSE=11;
        public static final int API_NOT_MUTUAL_LISTEN=45;
        
        
    }
}
