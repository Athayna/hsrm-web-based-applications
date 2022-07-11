package de.hsrm.mi.web.projekt.messaging;

import org.springframework.stereotype.Service;

@Service
public interface BackendInfoService {
    public void sendInfo(String topicname, BackendOperation operation, long id);
}
