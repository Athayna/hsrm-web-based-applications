package de.hsrm.mi.web.projekt.messaging;

public record BackendInfoMessage(
    String topicname,
    BackendOperation operation,
    long id) {}
