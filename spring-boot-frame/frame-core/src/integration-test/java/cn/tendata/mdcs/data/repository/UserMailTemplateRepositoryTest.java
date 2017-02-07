package cn.tendata.mdcs.data.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import cn.tendata.mdcs.data.domain.UserMailTemplate;
public class UserMailTemplateRepositoryTest extends JpaRepositoryTestCase{
    
	@Autowired UserMailTemplateRepository repository;
	
	@Transactional
	@Test
	@DatabaseSetup({"userData.xml", "userMailTemplateData.xml"})
	public void testBatchApproveTemplate(){
		Long[] ids={1L,2L};
		repository.batchApproveTemplate(4, ids, 1);
		List<UserMailTemplate> list=(List<UserMailTemplate>) repository.findAll(Arrays.asList(ids));
		for (UserMailTemplate userMailTemplate : list) {
			assertThat(userMailTemplate.getApproveStatus(), is(4));
		}
		
	}
	
	@Transactional
	@Test
	@DatabaseSetup({"userData.xml", "userMailTemplateData.xml"})
	public void testApproveTemplate(){
		repository.approveTemplate(4, 1L, 1);
		UserMailTemplate template=repository.findOne(1L);
		assertThat(template.getApproveStatus(), is(4));		
	}
	
	@Transactional
	@Test
	@DatabaseSetup({"userData.xml", "userMailTemplateData.xml"})
	public void testGetAllWaittingApprove(){
		DateTimeFormatter format=new DateTimeFormatterFactory("yyyy-MM-dd hh:mm:ss").createDateTimeFormatter();
		DateTime time=DateTime.parse("2016-09-29 10:41:23",format);
		List<UserMailTemplate> templates=repository.getAllWaittingApprove(1, time);
		assertThat(templates.size(), is(2));		
	}
}
