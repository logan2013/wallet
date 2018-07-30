package com.blockchain.wallet.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/28.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix="authentication")
public class AclConfig {

    private Boolean enable = Boolean.TRUE ;
    private List<String> ipAllows = new ArrayList<>();
    private List<String> ipDenys =new ArrayList<>();

}
