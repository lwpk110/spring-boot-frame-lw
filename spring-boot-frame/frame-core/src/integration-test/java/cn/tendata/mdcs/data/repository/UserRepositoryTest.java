package cn.tendata.mdcs.data.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.service.model.SearchKeywordType;

public class UserRepositoryTest extends JpaRepositoryTestCase {

    @Autowired UserRepository repository;
    
    @Test
    @DatabaseSetup({"userData.xml"})
    public void testFindAllByUserId(){
        SearchKeywordType type = SearchKeywordType.USER_ID;
        String keyword = "1";
        Pageable pageable = new PageRequest(0, 10);
        Page<User> page = repository.findAll(UserPredicates.search(type, keyword), pageable);
        assertThat(page.getTotalElements(), is(1L));
        assertThat(page.getTotalPages(), is(1));
    }
    
    @Test
    @DatabaseSetup({"userData.xml"})
    public void testFindAllByUsername(){
        SearchKeywordType type = SearchKeywordType.USERNAME;
        String keyword = "aaa";
        Pageable pageable = new PageRequest(0, 10);
        Page<User> page = repository.findAll(UserPredicates.search(type, keyword), pageable);
        assertThat(page.getTotalElements(), is(1L));
        assertThat(page.getTotalPages(), is(1));
    }
}
