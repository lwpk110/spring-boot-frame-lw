package cn.tendata.mdcs.service;

import cn.tendata.mdcs.core.DefaultMessageSource;
import cn.tendata.mdcs.core.IllegalOperationException;
import cn.tendata.mdcs.core.RecipientCountOverLimitException;
import cn.tendata.mdcs.data.domain.MailAgentDomain;
import cn.tendata.mdcs.data.domain.MailDeliveryChannelNode;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask.DeliveryStatus;
import cn.tendata.mdcs.data.domain.UserMailDeliveryTask.UserMailDeliveryTaskBuilder;
import cn.tendata.mdcs.data.domain.UserMailTemplate;
import cn.tendata.mdcs.data.domain.UserTransactionDetail;
import cn.tendata.mdcs.data.domain.UserTransactionDetail.UserTransactionType;
import cn.tendata.mdcs.data.repository.MailAgentDomainRepository;
import cn.tendata.mdcs.data.repository.UserMailDeliveryTaskPredicates;
import cn.tendata.mdcs.data.repository.UserMailDeliveryTaskRepository;
import cn.tendata.mdcs.data.repository.UserMailRecipientGroupRepository;
import cn.tendata.mdcs.data.repository.UserMailTemplateRepository;
import cn.tendata.mdcs.data.repository.UserRepository;
import cn.tendata.mdcs.data.repository.UserTransactionDetailRepository;
import cn.tendata.mdcs.mail.core.MailDeliveryGateway;
import cn.tendata.mdcs.mail.support.MailAgentDeliveryManager;
import cn.tendata.mdcs.service.model.MailDeliveryTaskDetail;
import cn.tendata.mdcs.service.model.SearchKeywordType;
import cn.tendata.mdcs.util.CollectionUtils;
import cn.tendata.mdcs.util.DateTimeQuery;
import cn.tendata.mdcs.util.MailRecipientUtils;
import cn.tendata.mdcs.util.UserQuery;
import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class UserMailDeliveryTaskServiceImpl extends EntityServiceSupport<UserMailDeliveryTask, UUID, UserMailDeliveryTaskRepository>
        implements UserMailDeliveryTaskService, MessageSourceAware {

    protected MessageSourceAccessor messages = DefaultMessageSource.getAccessor();

    private final UserRepository userRepositry;
    private final UserMailRecipientGroupRepository recipientGroupRepository;
    private final UserMailTemplateRepository templateRepository;
    private final UserTransactionDetailRepository transactionDetailRepository;
    private final MailAgentDomainRepository mailAgentDomainRepository;

    private final MailAgentDeliveryManager mailAgentDeliveryManager;
    private final MailDeliveryGateway mailDeliveryGateway;

    @Autowired
    protected UserMailDeliveryTaskServiceImpl(UserMailDeliveryTaskRepository repository, UserRepository userRepository,
                                              UserMailRecipientGroupRepository recipientGroupRepository, UserMailTemplateRepository templateRepository,
                                              UserTransactionDetailRepository transactionDetailRepository, MailAgentDomainRepository mailAgentDomainRepository,
                                              MailAgentDeliveryManager mailAgentDeliveryManager, MailDeliveryGateway mailDeliveryGateway ) {

        super(repository);
        this.userRepositry = userRepository;
        this.recipientGroupRepository = recipientGroupRepository;
        this.templateRepository = templateRepository;
        this.transactionDetailRepository = transactionDetailRepository;
        this.mailAgentDomainRepository = mailAgentDomainRepository;
        this.mailAgentDeliveryManager = mailAgentDeliveryManager;

        this.mailDeliveryGateway = mailDeliveryGateway;

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void submit(MailDeliveryTaskDetail taskDetail) {
        UserMailDeliveryTask task = new UserMailDeliveryTaskBuilder(taskDetail.getName())
                .description(taskDetail.getDescription())
                .scheduled(taskDetail.isScheduled())
                .scheduledDate(taskDetail.getScheduledDate())
                .parentUserId(taskDetail.getParentUserId())
                .parentUsername(taskDetail.getParentUsername())
                .mailDeliverySettings(taskDetail.getUserMailDeliverySettings().getDeliverySettings())
                .mailRecipientCollection(MailRecipientUtils.getRecipientCollection(taskDetail.getUserMailRecipientGroups()))
                .mailTemplate(taskDetail.getMailTemplate())
                .deliveryChannel(taskDetail.getDeliveryChannel())
                .user(taskDetail.getUser())
                .build();

        if (task.getRecipientCollection().getRecipientCount() > task.getDeliveryChannel().getMaxNumLimit()) {
            throw new RecipientCountOverLimitException();
        }

        task.getDeliverySettings().setReplyName(task.getDeliverySettings().getSenderName());

        if (task.getDeliverySettings().isAgentSend()) {
            MailAgentDomain mailAgentDomain = getMailAgentDomain(task.getDeliveryChannel().getId());
            Assert.state(mailAgentDomain != null, "'mailAgentDomain' must be initialized");

            String senderEmail = mailAgentDeliveryManager.getSenderEmail(task.getDeliverySettings().getReplyEmail(),
                    mailAgentDomain.getMailAgent());
            task.getDeliverySettings().setSenderEmail(senderEmail);

            mailAgentDomain.setUseCount(mailAgentDomain.getUseCount() + task.getRecipientCollection().getRecipientCount());
            mailAgentDomainRepository.save(mailAgentDomain);
        }


        User user = task.getUser();
        user.pay(task.getTotalFee());
        userRepositry.save(user);
        UserMailTemplate userMailTemplate = taskDetail.getUserMailTemplate();
        userMailTemplate.increaseUseCount();
        templateRepository.save(userMailTemplate);
        recipientGroupRepository.delete(taskDetail.getUserMailRecipientGroups());
        task = getRepository().save(task);
        UserTransactionDetail transactionDetail = new UserTransactionDetail(UserTransactionType.SEND,
                Math.negateExact(task.getTotalFee()),
                messages.getMessage("UserTransactionDetail.task", new Object[]{task.getName()}, "Task name: {0}"),
                user);
        transactionDetailRepository.save(transactionDetail);
        mailDeliveryGateway.process(task);
    }


    private MailAgentDomain getMailAgentDomain(Integer channelId) {
        MailAgentDomain mailAgentDomain = null;
        List<MailAgentDomain> mailAgentDomainList = mailAgentDomainRepository.findAllByChannelIdAndDisabledOrderByUseCount(channelId,false);
        if (mailAgentDomainList != null && !mailAgentDomainList.isEmpty()) {
            mailAgentDomain = mailAgentDomainList.get(0);
        }
        return mailAgentDomain;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void retry(UserMailDeliveryTask task) {
        if (task.getDeliveryStatus() == DeliveryStatus.SUCCESS || task.getDeliveryStatus() == DeliveryStatus.CANCEL) {
            throw new IllegalOperationException();
        }
        task.setDeliveryStatus(DeliveryStatus.NONE);
        task.setDeliveryDate(DateTime.now());
        task = getRepository().save(task);
        mailDeliveryGateway.process(task);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void cancel(UserMailDeliveryTask task) {
        if (task.getDeliveryStatus() == DeliveryStatus.NONE || task.getDeliveryStatus() == DeliveryStatus.SUCCESS) {
            throw new IllegalOperationException();
        }
        if (task.getDeliveryStatus() == DeliveryStatus.CANCEL) {
            throw new IllegalOperationException();
        }
        User user = task.getUser();
        user.refound(task.getTotalFee());
        userRepositry.save(user);
        task.setDeliveryStatus(DeliveryStatus.CANCEL);
        getRepository().save(task);
        UserTransactionDetail transactionDetail = new UserTransactionDetail(UserTransactionType.SEND_CANCEL,
                task.getTotalFee(),
                messages.getMessage("UserTransactionDetail.task", new Object[]{task.getName()}, "Task: {0}"),
                user);
        transactionDetailRepository.save(transactionDetail);
    }

    @Transactional(readOnly = true)
    public long getCount(User user) {
        return getRepository().count(UserMailDeliveryTaskPredicates.user(user));
    }

    @Transactional(readOnly = true)
    public List<UserMailDeliveryTask> getAll(User user, int number) {
        Pageable pageable = new PageRequest(0, number);
        return getRepository().findAllByUserOrderByCreatedDateDesc(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserMailDeliveryTask> getAll(User user, DateTimeQuery dateTimeQuery, String taskName, UserQuery userQuery, Pageable pageable) {
        return getRepository().findAll(UserMailDeliveryTaskPredicates.listByUser(user, dateTimeQuery, taskName, userQuery), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserMailDeliveryTask> getAll(User user, Pageable pageable) {
        return getRepository().findAllByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserMailDeliveryTask> getAll(SearchKeywordType type, String keyword, DateTime start, DateTime end, Pageable pageable) {
        return getRepository().findAll(UserMailDeliveryTaskPredicates.list(type, keyword, start, end), pageable);
    }

    @Transactional(readOnly = true)
    public List<UserMailDeliveryTask> getAll(DateTime start, DateTime end) {
        return CollectionUtils.toList(getRepository().findAll(UserMailDeliveryTaskPredicates.list(start, end)));
    }

    @Transactional(readOnly = true)
    public Page<UserMailDeliveryTask> getAll(DateTime start, DateTime end, Pageable pageable) {
        return getRepository().findAll(UserMailDeliveryTaskPredicates.list(start, end), pageable);
    }

    @Transactional(readOnly = true)
    public List<UserMailDeliveryTask> getAll(DateTime start, DateTime end, MailDeliveryChannelNode mailDeliveryChannelNode) {
        return CollectionUtils.toList(getRepository().findAll(UserMailDeliveryTaskPredicates.list(start, end, mailDeliveryChannelNode)));
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

}
