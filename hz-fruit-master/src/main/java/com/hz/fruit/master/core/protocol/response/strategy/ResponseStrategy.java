package com.hz.fruit.master.core.protocol.response.strategy;



import com.hz.fruit.master.core.protocol.base.BaseResponse;
import com.hz.fruit.master.core.protocol.response.strategy.qiniu.QiNiu;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 协议：策略数据
 * @Author yoko
 * @Date 2020/5/27 18:49
 * @Version 1.0
 */
public class ResponseStrategy extends BaseResponse implements Serializable {
    private static final long   serialVersionUID = 1233023131151L;

    public QiNiu qiNiu;// 获取七牛的token
    public Integer rowCount;



    public ResponseStrategy(){

    }

    public QiNiu getQiNiu() {
        return qiNiu;
    }

    public void setQiNiu(QiNiu qiNiu) {
        this.qiNiu = qiNiu;
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
