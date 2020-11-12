package binplus.foodiswill.Config;


public class BaseURL {
    static final String APP_NAME = "FoodWill ";
    public static final String PREFS_NAME = "GroceryLoginPrefs";
    public static final String PREFS_NAME2 = "GroceryLoginPrefs2";
    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_NAME = "user_fullname";
    public static final String KEY_EMAIL = "user_email";
    public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
    public static final String WALLET_TOTAL_AMOUNT = "WALLET_TOTAL_AMOUNT";
    public static final String COUPON_TOTAL_AMOUNT = "COUPON_TOTAL_AMOUNT";
    public static final String KEY_ID = "user_id";
    public static final String KEY_MOBILE = "user_phone";
    public static final String KEY_IMAGE = "user_image";
    public static final String KEY_WALLET_Ammount = "wallet_ammount";
    public static final String KEY_REWARDS_POINTS = "rewards_points";
    public static final String KEY_PAYMENT_METHOD = "payment_method";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_SOCITY_ID = "Socity_id";
    public static final String KEY_REWARDS = "rewards";
    public static final String KEY_SOCITY_NAME = "socity_name";
    public static final String KEY_HOUSE = "house_no";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_CNT = "img_count";
    public static final String KEY_CAT = "category_id";



    //Store Selection

    public static final String KEY_STORE_COUNT = "STORE_COUNT";
    public static final String KEY_NOTIFICATION_COUNT = "NOTIFICATION_COUNT";

    //Firebase
    public static final String SHARED_PREF = "ah_firebase";
    public static final String TOPIC_GLOBAL = "global";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    public static final String KEY_PASSWORD = "password";

    //City and Store Id
    public static final String CITY_ID = "CITY_ID";
    public static final String STORE_ID = "STORE_ID";

    public static String BASE_URL = "https://www.myjewelstuff.in/";


    public static String IMG_SLIDER_URL = BASE_URL + "uploads/sliders/";
    public static String IMG_CATEGORY_URL = BASE_URL + "uploads/category/";
    public static String IMG_PRODUCT_URL = BASE_URL + "uploads/products/";
    public static String IMG_EXTRA_URL = BASE_URL + "uploads/order_notification/";

    public static String IMG_PROFILE_URL = BASE_URL + "uploads/profile/";
    public static String GET_SLIDER_URL = BASE_URL + "index.php/api/get_sliders";
    public static String GET_FEAATURED_SLIDER_URL = BASE_URL + "index.php/api/get_feature_banner";
    public static String GET_BANNER_URL = BASE_URL + "index.php/api/get_banner";

    public static String WALLET_REFRESH = BASE_URL + "index.php/api/wallet?user_id=";
    public static String REWARDS_REFRESH = BASE_URL + "index.php/api/rewards?user_id=";



    public static String PUT_SUGGESTION_URL = BASE_URL + "index.php/api/put_suggestion";
    public static String GET_CATEGORY_URL = BASE_URL + "index.php/api/get_categories";
    public static String GET_ALL_CATEGORY_URL = BASE_URL + "index.php/api/get_all_categories";
    public static String GET_SLIDER_CATEGORY_URL = BASE_URL + "index.php/api/get_sub_cat";
    public static String GET_CATEGORY_ICON_URL = BASE_URL + "index.php/api/icon";
    public static String COUPON_CODE = BASE_URL + "index.php/api/get_coupons";
    public static String GET_LINKS = BASE_URL + "index.php/api/links";
    public static String GET_VERSTION_DATA = BASE_URL + "index.php/api/getVersionData";
    public static String URL_SEND_OTP = BASE_URL + "index.php/api/login_with_otp";
    public static String URL_REG_OTP = BASE_URL + "index.php/api/register_with_otp";

    //Home PAGE

    public static String GET_MENU_PRODUCTS = BASE_URL + "index.php/api/icon";
    public static String GET_MENU_ICON_PRODUCT_URL = BASE_URL + "index.php/api/get_header_products";
    public static String GET_DEAL_OF_DAY_PRODUCTS = BASE_URL + "index.php/api/deal_product";
    public static String GET_ALL_DEAL_OF_DAY_PRODUCTS = BASE_URL + "index.php/api/get_all_deal_product";
    public static String GET_TOP_SELLING_PRODUCTS = BASE_URL + "index.php/api/top_selling_product";
    public static String GET_TOP_PRODUCTS = BASE_URL + "index.php/api/top_products";
    public static String GET_NEW_PRODUCTS = BASE_URL + "index.php/api/new_product";
    public static String GET_ALL_TOP_SELLING_PRODUCTS = BASE_URL + "index.php/api/get_all_top_selling_product";


    public static String GET_PRODUCT_URL = BASE_URL + "index.php/api/get_products";

    public static String GET_PRODUCT_DESC = BASE_URL + "index.php/api/get_products_high";
    public static String GET_PRODUCT_ASC = BASE_URL + "index.php/api/get_products_low";
    public static String GET_PRODUCT_NEWEST = BASE_URL + "index.php/api/get_products_new";



    public static String GET_ABOUT_URL = BASE_URL + "index.php/api/aboutus";

    public static String GET_SUPPORT_URL = BASE_URL + "index.php/api/support";

    public static String GET_TERMS_URL = BASE_URL + "index.php/api/terms";
    public static String GET_PRIVACY_URL = BASE_URL + "index.php/api/privacy_policy";
    public static String GET_RETURN_POLICY_URL = BASE_URL + "index.php/api/return_policy";

    public static String GET_TIME_SLOT_URL = BASE_URL + "index.php/api/get_time_slt";

    public static String LOGIN_URL = BASE_URL + "index.php/api/login";

    public static String REGISTER_URL = BASE_URL + "index.php/api/signup";

    public static String GET_SOCITY_URL = BASE_URL + "index.php/api/get_society";

    public static String EDIT_PROFILE_URL = BASE_URL + "index.php/api/update_userdata";
    public static String WALLET_AMOUNT_URL = BASE_URL + "index.php/api/wallet_amount";

    public static String ADD_ORDER_URL = BASE_URL + "index.php/api/send_order";
    public static String Wallet_CHECKOUT = BASE_URL + "index.php/api/wallet_at_checkout";
    public static String GET_ORDER_URL = BASE_URL + "index.php/api/my_orders";

    public static String GET_DELIVERD_ORDER_URL = BASE_URL + "index.php/api/delivered_complete";

    public static String ORDER_DETAIL_URL = BASE_URL + "index.php/api/order_details";

    public static String DELETE_ORDER_URL = BASE_URL + "index.php/api/cancel_order";

    public static String GET_LIMITE_SETTING_URL = BASE_URL + "index.php/api/get_limit_settings";

    public static String ADD_ADDRESS_URL = BASE_URL + "index.php/api/add_address";

    public static String GET_ADDRESS_URL = BASE_URL + "index.php/api/get_address";

    public static String FORGOT_URL = BASE_URL + "index.php/api/forgot_password";

    public static String JSON_RIGISTER_FCM = BASE_URL + "index.php/api/register_fcm";

    public static String CHANGE_PASSWORD_URL = BASE_URL + "index.php/api/change_password";

    public static String DELETE_ADDRESS_URL = BASE_URL + "index.php/api/delete_address";

    public static String EDIT_ADDRESS_URL = BASE_URL + "index.php/api/edit_address";


    // global topic to receive app wide push notifications

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public static final String PUSH_NOTIFICATION = "pushNotification";

    // product details

    public static String PRODUCT_DETAILS = BASE_URL + "index.php/api/get_products_details";
    public static String GET_UPLOAD = BASE_URL + "index.php/api/upload_images";
    public static String GET_CANCEL_ORDERS = BASE_URL + "index.php/api/my_cancel_orders";
    public static String URL_GEN_OTP = BASE_URL + "index.php/api/fun_gen_otp";
    public static String URL_REGISTER_OTP = BASE_URL + "index.php/api/fun_register_with_otp";
    public static String URL_VERIFY_OTP = BASE_URL + "index.php/api/mobile_otp_verification";
    public static String URL_VERIFY_REGISTER_OTP = BASE_URL + "index.php/api/mobile_register_otp_verification";
    public static String URL_CANCEL_ORDER_DETAILS = BASE_URL + "index.php/api/order_cancel_details";
    public static String URL_GET_COUPON = BASE_URL + "index.php/api/get_coupon";
    public static String URL_GENERATE_PDF = BASE_URL + "index.php/api/pdf_details";
    public static String URL_MASTER_SEARCH = BASE_URL + "index.php/api/master_search";
    public static String GET_USER_STATUS = BASE_URL + "index.php/api/get_user_status";


    //Payment Gateway urls
//    public static String PAY_REDIRECT_URL = "https://trolleyxpress.com/ccavenue/ccavResponseHandler.php";
    public static String PAY_REDIRECT_URL = "https://www.myjewelstuff.in/merchant/ccavResponseHandler.php";
//    public static String PAY_REDIRECT_URL = "http://51.89.128.9/merchant/ccavResponseHandler.jsp";
    public static String PAY_CANCEL_URL = "https://www.myjewelstuff.in/merchant/ccavResponseHandler.php";
//    public static String PAY_CANCEL_URL = "https://trolleyxpress.com/ccavenue/ccavResponseHandler.php";
//    public static String PAY_CANCEL_URL = "http://51.89.128.9/merchant/ccavResponseHandler.jsp";
    public static String PAY_RSA_URL = "https://www.myjewelstuff.in/merchant/GetRSA.php";
//    public static String PAY_RSA_URL = "https://trolleyxpress.com/ccavenue/GetRSA.php";

}
