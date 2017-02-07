package cn.tendata.mdcs.data.elasticsearch.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import cn.tendata.mdcs.data.elasticsearch.domain.MailDeliveryChannelDocument;
import cn.tendata.mdcs.data.elasticsearch.domain.MailDeliveryTaskReportDocument;
import cn.tendata.mdcs.data.elasticsearch.domain.UserDocument;

public class MailDeliveryTaskReportRepositoryTest extends ElasticsearchRepositoryTestCase {

    @Autowired MailDeliveryTaskReportRepository repository;
    
    @After
    public void clean() {
        repository.deleteAll();
    }
    
    @Test
    public void testCRUD(){
        MailDeliveryTaskReportDocument taskReportDocument = createMailDeliveryTaskReport("1", 
                "task1", new UserDocument(1, "test1", 0, null));
        taskReportDocument = repository.save(taskReportDocument);
        assertNotNull(repository.findOne(taskReportDocument.getId()));
        repository.delete(taskReportDocument);
        assertNull(repository.findOne(taskReportDocument.getId()));
    }
    
    private MailDeliveryTaskReportDocument createMailDeliveryTaskReport(String id, String name, UserDocument user) {
        MailDeliveryTaskReportDocument taskReportDocument = new MailDeliveryTaskReportDocument();
        taskReportDocument.setId(id);
        taskReportDocument.setTaskName(name);
        taskReportDocument.setSenderName("send1");
        taskReportDocument.setSenderEmail("send1@tendata.cn");
        taskReportDocument.setReplyEmail("reply1");
        taskReportDocument.setReplyEmail("reply1@tendata.cn");
        taskReportDocument.setSendDate(DateTime.now());
        taskReportDocument.setTotal(100);
        taskReportDocument.setSent(100);
        taskReportDocument.setCreatedDate(DateTime.now());
        taskReportDocument.setLastModifiedDate(DateTime.now());
        taskReportDocument.setUser(user);
        taskReportDocument.setDeliveryChannel(new MailDeliveryChannelDocument(1, "channel1", 1, "channelNode1"));
        return taskReportDocument;
    }
    
    @Test
    public void testFindAllByUser_UserId(){
        MailDeliveryTaskReportDocument taskReportDocument1 = createMailDeliveryTaskReport("1",
                "task1", new UserDocument(1, "test1", 0, null));
        repository.save(taskReportDocument1);
        MailDeliveryTaskReportDocument taskReportDocument2 = createMailDeliveryTaskReport("2",
                "task2", new UserDocument(2, "test2", 1, "test1"));
        repository.save(taskReportDocument2);
        
        long userId = 1;
        Pageable pageable = new PageRequest(0, 10);
        Page<MailDeliveryTaskReportDocument> page = repository.findAllByUser_UserId(userId, pageable);
        assertThat(page.getTotalElements(), is(1L));
        assertThat(page.getTotalPages(), is(1));
    }
}
