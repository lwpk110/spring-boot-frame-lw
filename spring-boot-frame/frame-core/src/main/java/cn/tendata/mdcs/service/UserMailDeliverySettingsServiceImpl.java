package cn.tendata.mdcs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliverySettings;
import cn.tendata.mdcs.data.repository.UserMailDeliverySettingsRepository;

@Service
public class UserMailDeliverySettingsServiceImpl extends EntityServiceSupport<UserMailDeliverySettings, Long, UserMailDeliverySettingsRepository>
    implements UserMailDeliverySettingsService {

    @Autowired
    protected UserMailDeliverySettingsServiceImpl(UserMailDeliverySettingsRepository repository) {
        super(repository);
    }
    
    @Transactional(isolation=Isolation.SERIALIZABLE)
    public void setChecked(UserMailDeliverySettings current) {
        List<UserMailDeliverySettings> entities = new ArrayList<>(2);
        UserMailDeliverySettings previous = getChecked(current.getUser());
        if(previous != null){
            previous.setChecked(false);
            entities.add(previous);
        }
        current.setChecked(true);
        entities.add(current);
        getRepository().save(entities);
    }
    
    @Transactional(readOnly=true)
    public UserMailDeliverySettings getChecked(User user) {
        return getRepository().findTopByUserAndChecked(user, true);
    }
    
    @Transactional(readOnly=true)
    public List<UserMailDeliverySettings> getAll(User user) {
        List<UserMailDeliverySettings> items = getRepository().findAllByUser(user);
        if(items != null){
            Collections.sort(items, Collections.<UserMailDeliverySettings>reverseOrder((e1, e2) -> Boolean.compare(e1.isChecked(), e2.isChecked())));
        }
        return items;
    }

    @Transactional(readOnly=true)
    public Page<UserMailDeliverySettings> getAll(User user, Pageable pageable) {
        return getRepository().findAllByUser(user, pageable);
    }
}
