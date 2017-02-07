package cn.tendata.mdcs.web.util;

public class DataTablesParameter {
    private static final int MAX_LENGTH = 100;
    public String sEcho;
    public String sSearch;
    public int iDisplayLength;
    public int iDisplayStart;
    public int iColumns;
    public int iSortingCols;
    public String sColumns;

    public int getEndIndex() {
        return getiDisplayStart() + getiDisplayLength() - 1;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getsSearch() {
        return sSearch;
    }

    public void setsSearch(String sSearch) {
        this.sSearch = sSearch;
    }

    public int getiDisplayLength() {
        return iDisplayLength < MAX_LENGTH ? iDisplayLength : MAX_LENGTH;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiColumns() {
        return iColumns;
    }

    public void setiColumns(int iColumns) {
        this.iColumns = iColumns;
    }

    public int getiSortingCols() {
        return iSortingCols;
    }

    public void setiSortingCols(int iSortingCols) {
        this.iSortingCols = iSortingCols;
    }

    public String getsColumns() {
        return sColumns;
    }

    public void setsColumns(String sColumns) {
        this.sColumns = sColumns;
    }

    public int getPage() {
        return iDisplayStart / iDisplayLength;
    }
}
