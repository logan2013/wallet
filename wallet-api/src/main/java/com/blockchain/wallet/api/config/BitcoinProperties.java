package com.blockchain.wallet.api.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = com.blockchain.wallet.api.config.BitcoinProperties.Bit_PREFIX)
public class BitcoinProperties {

    public static final String Bit_PREFIX = "bitcoin";

    private String username = "zk";

    private String password = "zk";

    private String url = "http://192.168.2.68:8332/";

    private String apiUrl="https://blockchain.info/unspent";

    public static String getBit_PREFIX() {
        return Bit_PREFIX;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
