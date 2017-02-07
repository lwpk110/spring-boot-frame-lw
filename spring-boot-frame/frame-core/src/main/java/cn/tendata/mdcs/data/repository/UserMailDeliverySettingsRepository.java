package cn.tendata.mdcs.data.repository;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliverySettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserMailDeliverySettingsRepository extends PagingAndSortingRepository<UserMailDeliverySettings, Long> {

    UserMailDeliverySettings findTopByUserAndChecked(User user, boolean checked);
    
    List<UserMailDeliverySettings> findAllByUser(User user);

    Page<UserMailDeliverySettings> findAllByUser(User user, Pageable pageable);
}
