package com.easemob.server.example.api;

import com.easemob.server.example.exception.HuanxinResponseException;

/**
 * This interface is created for RestAPI of User Integration, it should be
 * synchronized with the API list.
 * 
 * @author Eric23 2016-01-05
 * @link http://docs.easemob.com/
 */
public interface IMUserAPI {

	/**
	 * 注册IM用户[单个] <br>
	 * POST
	 * 
	 * @param payload
	 *            <code>{"username":"${用户名}","password":"${密码}", "nickname":"${昵称值}"}</code>
	 * @return
	 */
	Object createNewIMUserSingle(Object payload) throws HuanxinResponseException;

	/**
	 * 注册IM用户[批量] <br>
	 * POST
	 * 
	 * @param payload
	 *            <code>[{"username":"${用户名1}","password":"${密码}"},…,{"username":"${用户名2}","password":"${密码}"}]</code>
	 * @return
	 */
	Object createNewIMUserBatch(Object payload) throws HuanxinResponseException;

	/**
	 * 获取IM用户[单个] <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Object getIMUserByUserName(String userName) throws HuanxinResponseException;

	/**
	 * 获取IM用户[批量]，参数为空时默认返回最早创建的10个用户 <br>
	 * GET
	 * 
	 * @param limit
	 *            单页获取数量
	 * @param cursor
	 *            游标，大于单页记录时会产生
	 * @return
	 */
	Object getIMUsersBatch(Long limit, String cursor) throws HuanxinResponseException;

	/**
	 * 删除IM用户[单个] <br>
	 * DELETE
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Object deleteIMUserByUserName(String userName) throws HuanxinResponseException;

	/**
	 * 删除IM用户[批量]，随机删除 <br>
	 * DELETE
	 * 
	 * @param limit
	 *            删除数量，建议100-500
	 * @return
	 */
	Object deleteIMUserBatch(Long limit, String cursor) throws HuanxinResponseException;

	/**
	 * 重置IM用户密码 <br>
	 * PUT
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param payload
	 *            <code>{"newpassword" : "${新密码指定的字符串}"}</code>
	 * @return
	 */
	Object modifyIMUserPasswordWithAdminToken(String userName, Object payload) throws HuanxinResponseException;

	/**
	 * 修改用户昵称 <br>
	 * PUT
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param payload
	 *            <code>{"nickname" : "${昵称值}"}</code>
	 * @return
	 */
	Object modifyIMUserNickNameWithAdminToken(String userName, Object payload) throws HuanxinResponseException;

	/**
	 * 给IM用户的添加好友 <br>
	 * POST
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param friendName
	 *            好友用戶名或用戶ID
	 * @return
	 */
	Object addFriendSingle(String userName, String friendName) throws HuanxinResponseException;

	/**
	 * 解除IM用户的好友关系 <br>
	 * DELETE
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param friendName
	 *            好友用戶名或用戶ID
	 * @return
	 */
	Object deleteFriendSingle(String userName, String friendName) throws HuanxinResponseException;

	/**
	 * 查看某个IM用户的好友信息 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Object getFriends(String userName) throws HuanxinResponseException;

	/**
	 * 获取IM用户的黑名单 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Object getBlackList(String userName) throws HuanxinResponseException;

	/**
	 * 往IM用户的黑名单中加人 <br>
	 * POST
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param payload
	 *            <code>{"usernames":["5cxhactgdj", "mh2kbjyop1"]}</code>
	 * @return
	 */
	Object addToBlackList(String userName, Object payload) throws HuanxinResponseException;

	/**
	 * 从IM用户的黑名单中减人 <br>
	 * DELETE
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param blackListName
	 *            黑名单用戶名或用戶ID
	 * @return
	 */
	Object removeFromBlackList(String userName, String blackListName) throws HuanxinResponseException;

	/**
	 * 查看用户在线状态 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Object getIMUserStatus(String userName) throws HuanxinResponseException;

	/**
	 * 查询离线消息数 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Object getOfflineMsgCount(String userName) throws HuanxinResponseException;

	/**
	 * 查询某条离线消息状态 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @param msgId
	 *            消息ID
	 * @return
	 */
	Object getSpecifiedOfflineMsgStatus(String userName, String msgId) throws HuanxinResponseException;

	/**
	 * 用户账号禁用 <br>
	 * POST
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Object deactivateIMUser(String userName) throws HuanxinResponseException;

	/**
	 * 用户账号解禁 <br>
	 * POST
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Object activateIMUser(String userName) throws HuanxinResponseException;

	/**
	 * 强制用户下线 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 */
	Object disconnectIMUser(String userName) throws HuanxinResponseException;

	/**
	 * 获取用户参与的群组 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @link http://docs.easemob.com/doku.php?id=start:100serverintegration:
	 *      60groupmgmt
	 */
	Object getIMUserAllChatGroups(String userName) throws HuanxinResponseException;

	/**
	 * 获取用户所有参与的聊天室 <br>
	 * GET
	 * 
	 * @param userName
	 *            用戶名或用戶ID
	 * @return
	 * @link http://docs.easemob.com/doku.php?id=start:100serverintegration:
	 *      70chatroommgmt
	 */
	Object getIMUserAllChatRooms(String userName) throws HuanxinResponseException;
}
