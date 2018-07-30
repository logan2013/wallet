package com.blockchain.wallet.api.service.impl;

import com.blockchain.wallet.api.dao.walletBussinessMqMsgPOMapper;
import com.blockchain.wallet.api.po.walletBussinessMqMsgPOWithBLOBs;
import com.blockchain.wallet.api.service.walletBussinessMqMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Administrator
 * @date 2018/7/24
 */
@Component
public class walletBussinessMqMsgServiceImpl implements walletBussinessMqMsgService {

    @Autowired
    walletBussinessMqMsgPOMapper mapper;

    @Override
    public int insert(walletBussinessMqMsgPOWithBLOBs po) {
        return mapper.insertSelective(po);
    }
}
