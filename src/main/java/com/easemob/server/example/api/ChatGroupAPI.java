package com.easemob.server.example.api;

import com.easemob.server.example.exception.HuanxinResponseException;

/**
 * This interface is created for RestAPI of Chat Group, it should be
 * synchronized with the API list.
 * 
 * @author Eric23 2016-01-05
 * @link http://docs.easemob.com/:
 *      60groupmgmt
 */
public interface ChatGroupAPI {

	/**
	 * 获取群组，参数为空时获取所有群组 <br>
	 * GET
	 * 
	 * @param limit
	 *            单页数量
	 * @param cursor
	 *            游标，存在更多记录时产生
	 * @return
	 */
	Object getChatGroups(Long limit, String cursor) throws HuanxinResponseException;

	/**
	 * 获取一个或者多个群组的详情 <br>
	 * GET
	 * 
	 * @param groupIds
	 *            群组ID数组
	 * @return
	 */
	Object getChatGroupDetails(String[] groupIds) throws HuanxinResponseException;

	/**
	 * 创建一个群组 <br>
	 * POST
	 * 
	 * @param payload
	 *            <code>{"groupname":"testrestgrp12","desc":"server create group","public":true,"maxusers":300,"approval":true,"owner":"jma1","members":["jma2","jma3"]}</code>
	 * @return
	 */
	Object createChatGroup(Object payload) throws HuanxinResponseException;

	/**
	 * 修改群组信息 <br>
	 * PUT
	 * 
	 * @param groupId
	 *            群组标识
	 * @param payload
	 *            <code>{"groupname":"testrestgrp12",description":"update groupinfo","maxusers":300}</code>
	 * @return
	 */
	Object modifyChatGroup(String groupId, Object payload) throws HuanxinResponseException;

	/**
	 * 删除群组 <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @return
	 */
	Object deleteChatGroup(String groupId) throws HuanxinResponseException;

	/**
	 * 获取群组所有用户 <br>
	 * GET
	 * 
	 * @param groupId
	 *            群组标识
	 * @return
	 */
	Object getChatGroupUsers(String groupId) throws HuanxinResponseException;

	/**
	 * 群组加人[单个] <br>
	 * POST
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 */
	Object addSingleUserToChatGroup(String groupId, String userId) throws HuanxinResponseException;

	/**
	 * 群组加人[批量] <br>
	 * POST
	 * 
	 * @param groupId
	 *            群组标识
	 * @param payload
	 *            用户ID或用户名，数组形式
	 * @return
//	 * @see com.easemob.server.example.comm.body.UserNamesBody
	 */
	Object addBatchUsersToChatGroup(String groupId, Object payload) throws HuanxinResponseException;

	/**
	 * 群组减人[单个] <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 */
	Object removeSingleUserFromChatGroup(String groupId, String userId) throws HuanxinResponseException;

	/**
	 * 群组减人[批量] <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userIds
	 *            用户ID或用户名，数组形式
	 * @return
	 */
	Object removeBatchUsersFromChatGroup(String groupId, String[] userIds) throws HuanxinResponseException;

	/**
	 * 群组转让 <br>
	 * PUT
	 * 
	 * @param groupId
	 *            群组标识
	 * @param payload
	 *            新群主ID或用户名
	 * @return
//     * @see com.easemob.server.example.comm.body.GroupOwnerTransferBody
	 */
	Object transferChatGroupOwner(String groupId, Object payload) throws HuanxinResponseException;

	/**
	 * 查询群组黑名单 <br>
	 * GET
	 * 
	 * @param groupId
	 *            群组标识
	 * @return
	 */
	Object getChatGroupBlockUsers(String groupId) throws HuanxinResponseException;

	/**
	 * 群组黑名单个添加 <br>
	 * POST
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 */
	Object addSingleBlockUserToChatGroup(String groupId, String userId) throws HuanxinResponseException;

	/**
	 * 群组黑名单批量添加 <br>
	 * POST
	 * 
	 * @param groupId
	 *            群组标识
	 * @param payload
	 *            用户ID或用户名，数组形式
	 * @return
//     * @see com.easemob.server.example.comm.body.UserNamesBody
	 */
	Object addBatchBlockUsersToChatGroup(String groupId, Object payload) throws HuanxinResponseException;

	/**
	 * 群组黑名单单个删除 <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userId
	 *            用户ID或用户名
	 * @return
	 */
	Object removeSingleBlockUserFromChatGroup(String groupId, String userId) throws HuanxinResponseException;

	/**
	 * 群组黑名单批量删除 <br>
	 * DELETE
	 * 
	 * @param groupId
	 *            群组标识
	 * @param userIds
	 *            用户ID或用户名，数组形式
	 * @return
	 */
	Object removeBatchBlockUsersFromChatGroup(String groupId, String[] userIds) throws HuanxinResponseException;
}
