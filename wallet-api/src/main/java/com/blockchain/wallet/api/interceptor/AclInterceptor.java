package com.blockchain.wallet.api.interceptor;

import com.alibaba.druid.support.http.util.IPAddress;
import com.alibaba.druid.support.http.util.IPRange;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.blockchain.wallet.api.config.AclConfig;
import com.blockchain.wallet.api.internal.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @author shujun
 * @desc 实现黑白名单拦截
 */
@Slf4j
public class AclInterceptor implements HandlerInterceptor {

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    public static String getMultistageReverseProxyIp(String ip) {
        // 多级反向代理检测
        if (ip != null && ip.indexOf(",") > 0) {
            final String[] ips = ip.trim().split(",");
            for (String subIp : ips) {
                if (false == isUnknow(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }


    public static boolean isUnknow(String checkString) {
        return StringUtils.isEmpty(checkString) || "unknown".equalsIgnoreCase(checkString);
    }
    public static String getClientIP(HttpServletRequest request) {
        String[] headers = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
        String ip;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (false == isUnknow(ip)) {
                return getMultistageReverseProxyIp(ip);
            }
        }
        ip = request.getRemoteAddr();
        return getMultistageReverseProxyIp(ip);
    }

    public boolean isPermittedRequest(HttpServletRequest request) {
        String remoteAddress = getClientIP(request);
        return isPermittedRequest(remoteAddress);
    }


    public boolean isPermittedRequest(String remoteAddress) {
        boolean ipV6 = remoteAddress != null && remoteAddress.indexOf(':') != -1;
        if (ipV6) {
            return "0:0:0:0:0:0:0:1".equals(remoteAddress) || (config.getIpDenys().size() == 0 && config.getIpAllows().size() == 0);
        }
        IPAddress ipAddress = new IPAddress(remoteAddress);
        for (String item : config.getIpDenys()) {
            if (item == null || item.length() == 0) {
                continue;
            }
            IPRange ipRange = new IPRange(item);
            if (ipRange.isIPAddressInRange(ipAddress)) {
                log.warn("remoteAddress[{}] in ip denys list." , remoteAddress);
                return false;
            }
        }
        if (config.getIpAllows().size() > 0) {
            for (String item  : config.getIpAllows()) {
                if (item == null || item.length() == 0) {
                    continue;
                }
                IPRange ipRange = new IPRange(item);
                if (ipRange.isIPAddressInRange(ipAddress)) {
                    return true;
                }
            }
            log.warn("remoteAddress[{}] not in ip allows list." , remoteAddress);
            return false;
        }
        return true;
    }


    @Autowired
    private AclConfig config;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(config.getEnable() == false){
            return true;
        }
        if(CollectionUtils.isEmpty(config.getIpAllows())){
            log.warn("can not config IpAllows paramters.");
            return false;
        }
        if( isPermittedRequest(request) == false ){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}