package com.easemob.server.example.api;


import com.easemob.server.example.exception.HuanxinResponseException;

/**
 * This interface is created for RestAPI of Sending Message, it should be
 * synchronized with the API list.
 * 
 * @author Eric23 2016-01-05
 * @link http://docs.easemob.com/
 */
public interface SendMessageAPI {

	Object sendMessage(Object payload) throws HuanxinResponseException;
}
