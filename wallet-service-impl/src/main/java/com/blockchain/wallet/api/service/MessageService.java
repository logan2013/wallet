package com.blockchain.wallet.api.service;

import com.blockchain.wallet.api.pojo.MessageResult;

import java.util.Map;

//短信service
public interface MessageService {

    MessageResult sendCode(Map<String, Object> params);
}
