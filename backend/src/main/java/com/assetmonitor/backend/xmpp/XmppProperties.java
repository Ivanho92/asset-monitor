package com.assetmonitor.backend.xmpp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xmpp")
public record XmppProperties(Server server, Backend backend) {
    public record Server(String host, int port, String domain) {}
    public record Backend(String username, String password) {}
}