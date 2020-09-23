package com.hz.fruit.master.core.protocol.response.issue;

import com.hz.fruit.master.core.protocol.base.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：下发
 * @Author yoko
 * @Date 2020/9/23 14:20
 * @Version 1.0
 */
public class ResponseIssue extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023331141L;

    public ResponseIssue(){

    }

    public List<Issue> dataList;
    public Issue dataModel;
    public Integer rowCount;

    public List<Issue> getDataList() {
        return dataList;
    }

    public void setDataList(List<Issue> dataList) {
        this.dataList = dataList;
    }

    public Issue getDataModel() {
        return dataModel;
    }

    public void setDataModel(Issue dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public Integer getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
}
