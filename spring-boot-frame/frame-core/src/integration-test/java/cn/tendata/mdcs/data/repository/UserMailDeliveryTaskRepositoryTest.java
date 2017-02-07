package cn.tendata.mdcs.data.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.mysema.query.types.Predicate;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;
import cn.tendata.mdcs.data.domain.MailDeliverySettings;
import cn.tendata.mdcs.data.domain.MailRecipient;
import cn.tendata.mdcs.data.domain.MailRecipientCollection;
import cn.tendata.mdcs.data.domain.MailTemplate;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask.DeliveryStatus;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask.UserMailDeliveryTaskBuilder;
import cn.tendata.mdcs.service.model.SearchKeywordType;
import cn.tendata.mdcs.util.DateTimeQuery;
import cn.tendata.mdcs.util.UserQuery;

public class UserMailDeliveryTaskRepositoryTest extends JpaRepositoryTestCase {

    @Autowired UserMailDeliveryTaskRepository repository;
    @Autowired MailDeliveryChannelRepository deliveryChannelRepository;
    
    @Transactional
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml"})
    public void testCRUD(){
        MailDeliveryChannel deliveryChannel = deliveryChannelRepository.findOne(1);
        UserMailDeliveryTask task = new UserMailDeliveryTaskBuilder("task1")
                .mailDeliverySettings(new MailDeliverySettings("test", "test@test.com"))
                .mailTemplate(new MailTemplate("111", "test", false))
                .mailRecipientCollection(new MailRecipientCollection(Arrays.asList(new MailRecipient("1@1.com"))))
                .deliveryChannel(deliveryChannel)
                .user(new User(1, "aaa"))
                .build();
        task = repository.save(task);
        assertNotNull(repository.findOne(task.getId()));
        task.setDeliveryStatus(DeliveryStatus.SUCCESS);
        task = repository.save(task);
        assertEquals(DeliveryStatus.SUCCESS, repository.findOne(task.getId()).getDeliveryStatus());
        repository.delete(task);
        assertNull(repository.findOne(task.getId()));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUserOrderByCreatedDateDesc(){
        User user = new User(1, "aaa");
        Pageable pageable = new PageRequest(0, 5);
        List<UserMailDeliveryTask> items = repository.findAllByUserOrderByCreatedDateDesc(user, pageable);
        assertThat(items.size(), is(2));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUser(){
        User user = new User(1, "aaa");
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAllByUser(user, pageable);
        assertThat(page.getTotalElements(), is(2L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUserByTaskName(){
        User user = new User(2, "aaa");
        DateTimeQuery dateTimeQuery = null;
        String taskName = "task";
        UserQuery userQuery = new UserQuery(0, 1);
        Predicate predicate = UserMailDeliveryTaskPredicates.listByUser(user, dateTimeQuery, taskName, userQuery);
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(predicate, pageable);
        assertThat(page.getTotalElements(), is(1L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUserByDateTimeQuery(){
        User user = new User(2, "aaa");
        DateTimeQuery dateTimeQuery = new DateTimeQuery(new DateTime(2015,1,11,0,0),new DateTime(2016,1,11,0,0));
        String taskName = null;
        UserQuery userQuery = new UserQuery(0, 1);
        Predicate predicate = UserMailDeliveryTaskPredicates.listByUser(user, dateTimeQuery, taskName, userQuery);
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(predicate, pageable);
        assertThat(page.getTotalElements(), is(1L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUserByTargetUserIdEqualsUserId(){
        User user = new User(1, "aaa");
        DateTimeQuery dateTimeQuery = null;
        String taskName = null;
        UserQuery userQuery = new UserQuery(1, 0);
        Predicate predicate = UserMailDeliveryTaskPredicates.listByUser(user, dateTimeQuery, taskName, userQuery);
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(predicate, pageable);
        assertThat(page.getTotalElements(), is(2L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUserByTargetUserIdNotEqualsUserId(){
        User user = new User(1, "aaa");
        DateTimeQuery dateTimeQuery = null;
        String taskName = null;
        UserQuery userQuery = new UserQuery(2, 0);
        Predicate predicate = UserMailDeliveryTaskPredicates.listByUser(user, dateTimeQuery, taskName, userQuery);
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(predicate, pageable);
        assertThat(page.getTotalElements(), is(1L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUserByPredicate(){
        User user = new User(1, "aaa");
        DateTimeQuery dateTimeQuery = new DateTimeQuery(new DateTime(2015,1,11,0,0),new DateTime(2016,1,11,0,0));
        String taskName = "task";
        UserQuery userQuery = new UserQuery(0, 0);
        Predicate predicate = UserMailDeliveryTaskPredicates.listByUser(user, dateTimeQuery, taskName, userQuery);
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(predicate, pageable);
        assertThat(page.getTotalElements(), is(3L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUserId(){
        SearchKeywordType type = SearchKeywordType.USER_ID;
        String keyword = "1";
        DateTime start = null;
        DateTime end = null;
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(UserMailDeliveryTaskPredicates.list(type, keyword, start, end), pageable);
        assertThat(page.getTotalElements(), is(2L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUsername(){
        SearchKeywordType type = SearchKeywordType.USERNAME;
        String keyword = "aaa";
        DateTime start = null;
        DateTime end = null;
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(UserMailDeliveryTaskPredicates.list(type, keyword, start, end), pageable);
        assertThat(page.getTotalElements(), is(2L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByCreateDateBetween(){
        SearchKeywordType type = null;
        String keyword = null;
        DateTime start = DateTime.parse("2015-01-01T00:00:00");
        DateTime end = DateTime.parse("2015-11-30T00:00:00");
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(UserMailDeliveryTaskPredicates.list(type, keyword, start, end), pageable);
        assertThat(page.getTotalElements(), is(2L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUserIdAndCreateDateBetween(){
        SearchKeywordType type = SearchKeywordType.USER_ID;
        String keyword = "1";
        DateTime start = DateTime.parse("2015-01-01T00:00:00");
        DateTime end = DateTime.parse("2015-11-30T00:00:00");
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(UserMailDeliveryTaskPredicates.list(type, keyword, start, end), pageable);
        assertThat(page.getTotalElements(), is(1L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "mailDeliveryChannelData.xml", "userMailDeliveryTaskData.xml"})
    public void testFindAllByUsernameAndCreateDateBetween(){
        SearchKeywordType type = SearchKeywordType.USERNAME;
        String keyword = "aaa";
        DateTime start = DateTime.parse("2015-01-01T00:00:00");
        DateTime end = DateTime.parse("2015-11-30T00:00:00");
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailDeliveryTask> page = repository.findAll(UserMailDeliveryTaskPredicates.list(type, keyword, start, end), pageable);
        assertThat(page.getTotalElements(), is(1L));
        assertThat(page.getTotalPages(), is(1));
    }
}
