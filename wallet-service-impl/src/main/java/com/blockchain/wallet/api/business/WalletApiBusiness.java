package com.blockchain.wallet.api.business;

import com.blockchain.wallet.api.WalletApiException;
import com.blockchain.wallet.api.dto.WalletApiRechargeRequestDTO;
import com.blockchain.wallet.api.dto.WalletApiRechargeResponseDTO;
import com.blockchain.wallet.api.dto.WalletApiRegisterRequestDTO;
import com.blockchain.wallet.api.dto.WalletApiRegisterResponseDTO;
import com.blockchain.wallet.api.request.WalletApiResponse;

/**
 *
 * @author Administrator
 * @date 2018/6/29
 */
public interface WalletApiBusiness {

     WalletApiResponse<WalletApiRegisterResponseDTO> generateWalletAddress(WalletApiRegisterRequestDTO request) throws WalletApiException;
     WalletApiResponse<WalletApiRegisterResponseDTO> restWalletPass(WalletApiRegisterRequestDTO request) throws WalletApiException;


}
