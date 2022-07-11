package de.hsrm.mi.web.projekt.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class BackendInfoServiceImpl implements BackendInfoService {
    @Autowired
    private SimpMessagingTemplate messaging;

    @Override
    public void sendInfo(String topicname, BackendOperation operation, long id) {
        messaging.convertAndSend("/topic/" + topicname, new BackendInfoMessage(topicname, operation, id));
    }
    
}
