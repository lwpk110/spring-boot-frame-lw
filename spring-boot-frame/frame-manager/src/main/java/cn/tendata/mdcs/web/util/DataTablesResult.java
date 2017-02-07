package cn.tendata.mdcs.web.util;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonView;

import cn.tendata.mdcs.web.jackson.WebDataView;

public class DataTablesResult<T> {
    
    @JsonView(WebDataView.UI.class)
    private long iTotalRecords;
    @JsonView(WebDataView.UI.class)
    private long iTotalDisplayRecords;
    @JsonView(WebDataView.UI.class)
    private String sEcho;
    @JsonView(WebDataView.UI.class)
    private Collection<T> aaData;

    public DataTablesResult() {
    }

    public DataTablesResult(long iTotalRecords, long iTotalDisplayRecords, String sEcho, Collection<T> aaData) {
        this.iTotalRecords = iTotalRecords;
        this.iTotalDisplayRecords = iTotalDisplayRecords;
        this.sEcho = sEcho;
        this.aaData = aaData;
    }

    public long getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public Collection<T> getAaData() {
        return aaData;
    }

    public void setAaData(Collection<T> aaData) {
        this.aaData = aaData;
    }

    public void fillData(Page<T> page, DataTablesParameter parameter) {
        this.setAaData(page.getContent());
        this.setiTotalDisplayRecords(page.getTotalElements());
        this.setiTotalRecords(page.getTotalElements());
        this.setsEcho(parameter.getsEcho());
    }

    public void fillData(List<T> list, DataTablesParameter parameter, long totalElements) {
        this.setAaData(list);
        this.setiTotalDisplayRecords(totalElements);
        this.setiTotalRecords(totalElements);
        this.setsEcho(parameter.getsEcho());
    }
}
