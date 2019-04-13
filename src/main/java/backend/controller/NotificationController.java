package backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.mapper.NotificationMapper;
import backend.model.Notification;
import backend.repository.NotificationRepository;
import backend.request.NotificationAdd;
import backend.response.APIResponse;

@RestController
@RequestMapping("/notifications")
public class NotificationController
{
    @Autowired
    private NotificationRepository notificationRepository;


    @PostMapping("")
    public ResponseEntity<APIResponse> addNotification(@RequestBody NotificationAdd notificationAdd)
    {
        Notification notification   = NotificationMapper.MAPPER.notificationAddToNotification(notificationAdd);
        notificationRepository.save(notification);
        APIResponse res  = new APIResponse(HttpStatus.OK, "A notification has been added to userID : " + notificationAdd.getUserID());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{notificationID}")
    public ResponseEntity<APIResponse> updateNotification(@PathVariable("notificationID") int notificationID)
    {
        Notification notification   = notificationRepository.findById(notificationID);
        notification.setHasRead(true);
        notificationRepository.save(notification);
        APIResponse res  = new APIResponse(HttpStatus.OK, "Notification " + notificationID + " has been marked as read");
        return ResponseEntity.ok(res);
    }

}