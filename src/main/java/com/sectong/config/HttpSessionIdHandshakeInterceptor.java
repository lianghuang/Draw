package com.sectong.config;

import com.sectong.constants.WebConstant;
import com.sectong.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Created by huangliangliang on 2/15/17.
 */
public class HttpSessionIdHandshakeInterceptor implements HandshakeInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(HttpSessionIdHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        HttpHeaders headers= serverHttpRequest.getHeaders();
        logger.info("Headers:{}",JsonUtils.toString(headers));
        if(StringUtils.isNotEmpty(headers.getFirst(WebConstant.USERNAME))){
            map.put(WebConstant.USERNAME,headers.getFirst(WebConstant.USERNAME));
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
    }
}
