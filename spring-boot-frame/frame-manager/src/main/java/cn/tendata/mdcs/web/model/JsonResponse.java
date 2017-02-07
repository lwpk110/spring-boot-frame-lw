package cn.tendata.mdcs.web.model;

public class JsonResponse {
    public final static int SUCCESS = 200;

    public final static int Failure = 400;

    private int status;
    private String msg;
    private Object content;

    public JsonResponse() {
        this.status = SUCCESS;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
