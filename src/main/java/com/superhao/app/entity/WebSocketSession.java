package com.superhao.app.entity;

import org.apache.tomcat.websocket.WsRemoteEndpointImplBase;
import org.apache.tomcat.websocket.WsSession;
import org.apache.tomcat.websocket.WsWebSocketContainer;

import javax.websocket.*;
import java.io.Serializable;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: super
 * @Date: 2019/11/6 21:22
 * @email:
 */
public class WebSocketSession extends WsSession implements Serializable {
    public WebSocketSession(Endpoint localEndpoint, WsRemoteEndpointImplBase wsRemoteEndpoint, WsWebSocketContainer wsWebSocketContainer, URI requestUri, Map<String, List<String>> requestParameterMap, String queryString, Principal userPrincipal, String httpSessionId, List<Extension> negotiatedExtensions, String subProtocol, Map<String, String> pathParameters, boolean secure, EndpointConfig endpointConfig) throws DeploymentException {
        super(localEndpoint, wsRemoteEndpoint, wsWebSocketContainer, requestUri, requestParameterMap, queryString, userPrincipal, httpSessionId, negotiatedExtensions, subProtocol, pathParameters, secure, endpointConfig);
    }

}
