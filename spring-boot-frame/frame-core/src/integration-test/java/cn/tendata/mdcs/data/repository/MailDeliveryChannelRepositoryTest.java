package cn.tendata.mdcs.data.repository;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static cn.tendata.mdcs.data.repository.MailDeliveryChannelOrderSpecifiers.*;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import cn.tendata.mdcs.data.domain.MailDeliveryChannel;

public class MailDeliveryChannelRepositoryTest extends JpaRepositoryTestCase {

    @Autowired MailDeliveryChannelRepository repository;
    
    @Test
    @DatabaseSetup({"mailDeliveryChannelData.xml"})
    public void testFindAllAvailableChannels(){
        boolean disabled = false;
        Iterable<MailDeliveryChannel> channels = repository.findAll(MailDeliveryChannelPredicates.channels(disabled), sequenceDesc(), idDesc());
        assertThat(channels, IsIterableWithSize.iterableWithSize(2));
        assertThat(channels, IsIterableContainingInOrder.contains(
                Arrays.asList(
                        Matchers.<MailDeliveryChannel>allOf(hasProperty("id", is(3)), hasProperty("name", is("channel3"))),
                        Matchers.<MailDeliveryChannel>allOf(hasProperty("id", is(1)), hasProperty("name", is("channel1"))))
                ));
    }
}
