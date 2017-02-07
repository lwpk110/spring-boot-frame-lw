package cn.tendata.mdcs.service;

import cn.tendata.mdcs.data.domain.SystemMailTemplate;
import cn.tendata.mdcs.data.repository.SystemMailTemplateRepository;
import cn.tendata.mdcs.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * Created by Luo Min on 2017/1/17.
 */
@Service
public class SystemMailTemplateServiceImpl extends EntityServiceSupport<SystemMailTemplate,Integer,SystemMailTemplateRepository> implements SystemMailTemplateService{

    public static final String filePath = "tendata-mdcs-manager\\src\\main\\resources\\static\\img\\template\\portraits";

    @Autowired
    public SystemMailTemplateServiceImpl(SystemMailTemplateRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemMailTemplate> getAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemMailTemplate> getAll(Sort sort) {
        return CollectionUtils.toList(getRepository().findAll(sort));
    }

    @Override
    public String[] getAllSystemTemplateImage() {
        File file = new File(filePath);
        String[] imageNames = new String[]{};
        if (file.isDirectory()) {
            imageNames = file.list();
        }
        return imageNames;
    }
}
