package backend.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import backend.request.NotificationAdd;
import backend.dto.NotificationDTO;
import backend.model.Notification;


@Mapper (uses = {AdminMapper.class})
public interface NotificationMapper {

    NotificationMapper MAPPER = Mappers.getMapper( NotificationMapper.class);

    @Mapping(source = "profile.userID", target = "userID")
    NotificationDTO notificationToNotificationDTO(Notification notification);

    @InheritInverseConfiguration
    Notification notificationDTOToNotification(NotificationDTO notificationDTO);

    @Mapping(source = "userID", target = "profile.userID")
    Notification notificationAddToNotification(NotificationAdd notificationAdd);
}