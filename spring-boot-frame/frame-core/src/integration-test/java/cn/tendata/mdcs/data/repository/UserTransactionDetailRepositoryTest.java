package cn.tendata.mdcs.data.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserTransactionDetail;
import cn.tendata.mdcs.service.model.SearchKeywordType;

public class UserTransactionDetailRepositoryTest extends JpaRepositoryTestCase {

    @Autowired UserTransactionDetailRepository repository;
    
    @Test
    @DatabaseSetup({"userData.xml", "userTransactionDetailData.xml"})
    public void testFindAllByUserAndCreatedDateBetween(){
        User user = new User(1, "aaa");
        DateTime start = DateTime.parse("2015-11-19T00:00:00");
        DateTime end = DateTime.parse("2015-11-20T00:00:00");
        Pageable pageable = new PageRequest(0, 10);
        Page<UserTransactionDetail> page = repository.findAllByUserAndCreatedDateBetween(user, start, end, pageable);
        assertThat(page.getTotalElements(), is(3L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "userTransactionDetailData.xml"})
    public void testFindAllByUserId(){
        SearchKeywordType type = SearchKeywordType.USER_ID;
        String keyword = "1";
        DateTime start = null;
        DateTime end = null;
        Pageable pageable = new PageRequest(0, 10);
        Page<UserTransactionDetail> page = repository.findAll(UserTransactionDetailPredicates.list(type, keyword, start, end), pageable);
        assertThat(page.getTotalElements(), is(3L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "userTransactionDetailData.xml"})
    public void testFindAllByUsername(){
        SearchKeywordType type = SearchKeywordType.USERNAME;
        String keyword = "aaa";
        DateTime start = null;
        DateTime end = null;
        Pageable pageable = new PageRequest(0, 10);
        Page<UserTransactionDetail> page = repository.findAll(UserTransactionDetailPredicates.list(type, keyword, start, end), pageable);
        assertThat(page.getTotalElements(), is(3L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "userTransactionDetailData.xml"})
    public void testFindAllByCreateDateBetween(){
        SearchKeywordType type = null;
        String keyword = null;
        DateTime start = DateTime.parse("2015-01-01T00:00:00");
        DateTime end = DateTime.parse("2015-12-31T00:00:00");
        Pageable pageable = new PageRequest(0, 10);
        Page<UserTransactionDetail> page = repository.findAll(UserTransactionDetailPredicates.list(type, keyword, start, end), pageable);
        assertThat(page.getTotalElements(), is(3L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml", "userTransactionDetailData.xml"})
    public void testFindAllByUserIdAndCreateDateBetween(){
        SearchKeywordType type = SearchKeywordType.USER_ID;
        String keyword = "1";
        DateTime start = DateTime.parse("2015-01-01T00:00:00");
        DateTime end = DateTime.parse("2015-12-31T00:00:00");
        Pageable pageable = new PageRequest(0, 10);
        Page<UserTransactionDetail> page = repository.findAll(UserTransactionDetailPredicates.list(type, keyword, start, end), pageable);
        assertThat(page.getTotalElements(), is(3L));
        assertThat(page.getTotalPages(), is(1));
    }
}
