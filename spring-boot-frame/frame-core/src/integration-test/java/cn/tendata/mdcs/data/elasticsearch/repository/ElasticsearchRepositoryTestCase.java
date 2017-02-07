package cn.tendata.mdcs.data.elasticsearch.repository;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.tendata.mdcs.data.elasticsearch.config.ElasticsearchConfig;

@ContextConfiguration(classes = { ElasticsearchConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public abstract class ElasticsearchRepositoryTestCase {
    
}
