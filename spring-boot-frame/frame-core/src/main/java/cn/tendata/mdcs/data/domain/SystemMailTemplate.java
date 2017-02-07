package cn.tendata.mdcs.data.domain;

import cn.tendata.mdcs.data.jpa.converter.JsonAttributeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Luo Min on 2017/1/17.
 */
@Entity
public class SystemMailTemplate extends AbstractEntityAuditable<Integer> {

    private static final long serialVersionUID = 1L;


    private String name;

    private String htmlContent;

    private Collection<String> portraits; //模板的图片描述

    private long useCount;

    private long version;

    @Id
    @GeneratedValue
    public Integer getId() {
        return super.getId();
    }

    @Column(name = "name",nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob
    @NotEmpty
    @Column(name = "html_content",nullable = false)
    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Column(name = "portraits",nullable = false)
    @Convert(converter = JsonAttributeConverter.class)
    public Collection<String> getPortraits() {
        return portraits;
    }

    public void setPortraits(Collection<String> portraits) {
        this.portraits = portraits;
    }

    @Column(name = "use_count", nullable = false)
    public long getUseCount() {
        return useCount;
    }

    public void setUseCount(long useCount) {
        this.useCount = useCount;
    }

    public void increaseUseCount() {
        this.useCount += 1;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
