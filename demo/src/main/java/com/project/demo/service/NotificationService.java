package com.project.demo.service;

import com.project.demo.dto.WorkRequestdto;
import com.project.demo.entity.ApprovalHistory;
import com.project.demo.entity.Notification;
import com.project.demo.entity.User;
import com.project.demo.entity.WorkFlowRequest;
import com.project.demo.enums.ResStatus;
import com.project.demo.repository.NotificationRepository;
import com.project.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    public void setNotify(WorkFlowRequest work, ResStatus result, String comments) {
        Notification notify = Notification.builder()
                .userId(work.getRequester().getId())
                .action(result)
                .message(comments)
                .createdAt(work.getCreatedAt())
                .isRead(false)
                .build();
        System.out.println("rohit"+notify.getUserId());
        Notification saved = notificationRepository.save(notify);


        emitNotification(saved);

    }



    public void emitNotification(Notification saved) {
        String socketUrl = "http://localhost:4000/emit-Notifications";

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", saved.getUserId());
        payload.put("notification", saved);

        try {
            System.out.println("Attempting to emit to: " + socketUrl);
            // Using postForEntity gives more debug info than postForObject
            ResponseEntity<String> response = restTemplate.postForEntity(socketUrl, payload, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Notification successfully pushed to Socket server. Response: " + response.getBody());
            } else {
                System.err.println("Socket server returned error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            // This will tell you exactly WHY it's failing (Timeout, Serialization, Connection Refused)
            System.err.println("CRITICAL FAILURE in emitNotification: ");
            e.printStackTrace();
        }
    }



    public List<Notification> getUserNotifications(Integer userId)
    {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
