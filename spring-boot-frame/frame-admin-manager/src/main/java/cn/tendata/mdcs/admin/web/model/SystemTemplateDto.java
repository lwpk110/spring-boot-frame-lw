package cn.tendata.mdcs.admin.web.model;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by Luo Min on 2017/1/17.
 */
public class SystemTemplateDto {

    private String name;

    private String htmlContent;

    private Collection<String> portraits;

    public SystemTemplateDto() {
    }

    public SystemTemplateDto(String name, String htmlContent) {
        this.name = name;
        this.htmlContent = htmlContent;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Collection<String> getPortraits() {
        return portraits;
    }

    public void setPortraits(Collection<String> portraits) {
        this.portraits = portraits;
    }
}
