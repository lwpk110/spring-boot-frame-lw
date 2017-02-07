package cn.tendata.mdcs.data.repository;

import cn.tendata.mdcs.data.domain.MailRecipient;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailRecipientGroup;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UserMailRecipientGroupRepositoryTest extends JpaRepositoryTestCase {

    @Autowired
    UserMailRecipientGroupRepository repository;

    @Test
    @DatabaseSetup("userData.xml")
    public void testCRUD() {
        User user = new User(1, "aaa");
        Set<MailRecipient> recipients = new HashSet<>();
        recipients.add(new MailRecipient("p1", "p1@1.com"));
        recipients.add(new MailRecipient("p2", "p2@1.com"));
        UserMailRecipientGroup recipientGroup = new UserMailRecipientGroup("recipients1", recipients, user);
        recipientGroup = repository.save(recipientGroup);
        assertNotNull(repository.findOne(recipientGroup.getId()));
        repository.delete(recipientGroup);
        assertNull(repository.findOne(recipientGroup.getId()));
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailRecipientGroupData.xml"})
    public void testFindAllByUser() {
        User user = new User(1, "aaa");
        List<UserMailRecipientGroup> recipientGroups = repository.findAllByUserOrderByCreatedDateDesc(user);
        assertThat(recipientGroups, hasSize(3));
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailRecipientGroupData.xml"})
    public void testFindAllByUserAndIdIn() {
        User user = new User(1, "aaa");
        Collection<UUID> ids = new ArrayList<>(3);
        ids.add(UUID.fromString("4b2f02ca-d857-4c95-b7fa-ee6283ce0d64"));
        ids.add(UUID.fromString("4b2f02ca-d857-4c95-b7fa-ee6283ce0d65"));
        ids.add(UUID.fromString("4b2f02ca-d857-4c95-b7fa-ee6283ce0d66"));
        List<UserMailRecipientGroup> recipientGroups = repository.findAllByUserAndIdIn(user, ids);
        assertThat(recipientGroups, hasSize(2));
    }

    @Test
    @DatabaseSetup({"userData.xml", "userMailRecipientGroupData.xml"})
    public void testFindAllByUserByPaging() {
        User user = new User(1, "aaa");
        Pageable pageable = new PageRequest(0, 10);
        Page<UserMailRecipientGroup> page = repository.findAllByUser(user, pageable);
        assertThat(page.getTotalElements(), is(3L));
        assertThat(page.getTotalPages(), is(1));
    }
}
