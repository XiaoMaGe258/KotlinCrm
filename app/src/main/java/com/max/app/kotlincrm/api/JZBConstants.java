package com.max.app.kotlincrm.api;

public class JZBConstants {
	
	/**是否开启打印功能  (true: 开启)*/
	public static final boolean DEBUG_MODE 				= true;/////////////// 上线要改为false ////////////////
	/**是否要切换host地址、推送appId等。  (true: 切换到测试服务器、推送)*/
	public static final boolean DEBUG_MODE_ADVANCED 	= true;/////////////// 上线要改为false ////////////////


	private static final String API_TEST 				= "http://123.207.163.197:4007/crm";
	private static final String API_ONLINE 				= "";

	public static final String API_IP 					= DEBUG_MODE_ADVANCED ? API_TEST : API_ONLINE;

	public static final String AKey 					= "12a34b56c78d90ef12a34b56c78d90ef";

	//用户基本信息
	public static final String SP_USERINFO 				= "userInfo";
	//用户登录信息
	public static final String SP_AUTHINFO				= "authInfo";
	//是否登录
	public static final String TAG_ISLOGIN 				= "isLogin";
	//用户名，密码
	public static final String TAG_AUTH_USERNAME 		= "userName";
	public static final String TAG_AUTH_PASSWORD 		= "password";

	//头像
	public static final String AVATAR 					= "avatar";
	//当前登录的用户的名字
	public static final String TRUENAME 				= "truename";
	//当前登录的用户的角色
	public static final String ROLENAME 				= "roleName";
	//角色roleNumber
	public static final String ROLENUMBER 				= "roleNum";
	//定位经度
	public static final String BD_LONGITUDE 			= "bd_longitude";
	//定位纬度
	public static final String BD_LATITUDE 				= "bd_latitude";
	//定位地址
	public static final String BD_ADDRESS 				= "bd_address";


	//个人客户池
	public static final int PRIVATE_LIBRARY 			= 700;
	//公共客户池
	public static final int PUBLIC_LIBRARY 				= 701;
}
