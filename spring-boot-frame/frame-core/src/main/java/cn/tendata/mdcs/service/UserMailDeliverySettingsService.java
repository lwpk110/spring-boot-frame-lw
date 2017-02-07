package cn.tendata.mdcs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliverySettings;

public interface UserMailDeliverySettingsService extends EntityService<UserMailDeliverySettings, Long> {

    void setChecked(UserMailDeliverySettings current);
    
    UserMailDeliverySettings getChecked(User user);
    
    List<UserMailDeliverySettings> getAll(User user);
    
    Page<UserMailDeliverySettings> getAll(User user, Pageable pageable);
}
