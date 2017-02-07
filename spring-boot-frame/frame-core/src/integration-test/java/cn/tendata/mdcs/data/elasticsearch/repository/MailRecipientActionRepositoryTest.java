package cn.tendata.mdcs.data.elasticsearch.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.elasticsearch.domain.MailDeliveryTaskReportDocument;
import cn.tendata.mdcs.data.elasticsearch.domain.UserDocument;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.mail.MailRecipientAction.MailRecipientActionStatus;

public class MailRecipientActionRepositoryTest extends ElasticsearchRepositoryTestCase {

    @Autowired MailRecipientActionRepository repository;
    @Autowired MailDeliveryTaskReportRepository taskReportRepository;
    
    private MailDeliveryTaskReportDocument createMailDeliveryTaskReport(String id, String name, UserDocument user) {
        MailDeliveryTaskReportDocument taskReportDocument = new MailDeliveryTaskReportDocument();
        taskReportDocument.setId(id);
        taskReportDocument.setTaskName(name);
        taskReportDocument.setUser(user);
        return taskReportDocument;
    }
    
    private MailRecipientActionDocument createMailRecipientAction(String email, DateTime actionDate,
            MailRecipientActionStatus actionStatus, String ip, String taskId, String taskName){
        MailRecipientActionDocument recipientActionDocument = new MailRecipientActionDocument();
        recipientActionDocument.setEmail(email);
        recipientActionDocument.setActionDate(actionDate);
        recipientActionDocument.setActionStatus(actionStatus);
        recipientActionDocument.setIp(ip);
        recipientActionDocument.setTaskId(taskId);
        recipientActionDocument.setTaskName(taskName);
        return recipientActionDocument;
    }
    
    private void addDocuments(String taskId){
        repository.deleteByTaskId(taskId);
        List<MailRecipientActionDocument> recipientActionDocuments = new ArrayList<>();
        recipientActionDocuments.add(createMailRecipientAction("test1@test.com", DateTime.now(), MailRecipientActionStatus.SENT_SUCCESS,
                "127.0.0.1", taskId, "task1"));
        recipientActionDocuments.add(createMailRecipientAction("test2@test.com", DateTime.now(), MailRecipientActionStatus.CLICK,
                "127.0.0.1", taskId, "task1"));
        repository.save(recipientActionDocuments);
    }
    
    @Test
    public void testCountByTaskId(){
        String taskId = "1";
        addDocuments(taskId);
        long count = repository.countByTaskId(taskId);
        assertThat(count, is(2L));
    }
    
    @Test
    public void testDeleteByTaskId(){
        String taskId = "1";
        addDocuments(taskId);
        long deletedCount = repository.deleteByTaskId(taskId);
        assertThat(deletedCount, is(2L));
    }
    
    @Test
    public void testSearchByUserIdAndEmail(){
        MailDeliveryTaskReportDocument taskReportDocument1 = createMailDeliveryTaskReport("1",
                "task1", new UserDocument(1, "test1", 0, null));
        taskReportRepository.save(taskReportDocument1);
        
        List<MailRecipientActionDocument> recipientActionDocuments = new ArrayList<>();
        recipientActionDocuments.add(createMailRecipientAction("test1@test.com", DateTime.now(), MailRecipientActionStatus.SENT_SUCCESS,
                "127.0.0.1", "1", "task1"));
        recipientActionDocuments.add(createMailRecipientAction("test2@test.com", DateTime.now(), MailRecipientActionStatus.CLICK,
                "127.0.0.1", "1", "task1"));
        repository.save(recipientActionDocuments);
        
        User user = new User(1L, "test");
        String email = "test1@test.com";
        Pageable pageable = new PageRequest(0, 10);
        Page<MailRecipientActionDocument> page = repository.search(user, email, pageable);
        assertThat(page.getTotalElements(), is(1L));
        assertThat(page.getTotalPages(), is(1));
    }
}
