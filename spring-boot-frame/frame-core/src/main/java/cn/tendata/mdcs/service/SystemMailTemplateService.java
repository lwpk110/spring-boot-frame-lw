package cn.tendata.mdcs.service;

import cn.tendata.mdcs.data.domain.SystemMailTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by Luo Min on 2017/1/17.
 */
public interface SystemMailTemplateService extends EntityService<SystemMailTemplate,Integer>{

    Page<SystemMailTemplate> getAll(Pageable pageable);

    List<SystemMailTemplate> getAll(Sort sort);

    String[] getAllSystemTemplateImage();
}
