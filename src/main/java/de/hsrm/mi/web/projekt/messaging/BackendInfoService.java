package de.hsrm.mi.web.projekt.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class BackendInfoService {
    @Autowired
    private SimpMessagingTemplate messaging;

    public void sendInfo(String topicname, BackendOperation operation, long id) {
        BackendInfoMessage message = new BackendInfoMessage(topicname, operation, id);
        messaging.convertAndSend("/topic/" + topicname, message);
    }
    
}
