package com.hz.fruit.master.core.common.utils.constant;

/**
 * @author df
 * @Description:异常状态码
 * @create 2018-07-27 11:13
 **/
public class ErrorCode {

    /**
     * 常量异常
     */
    public final class ERROR_CONSTANT {
        /**
         * 没有被捕捉到的异常
         * 默认系统异常状态码=255
         */
        public static final String DEFAULT_EXCEPTION_ERROR_CODE = "255";

        /**
         * 没有被捕捉到的异常
         * 默认系统异常错误信息=SYS_ERROR
         */
        public static final String DEFAULT_EXCEPTION_ERROR_MESSAGE = "ERROR";

        /**
         * 被捕捉到的异常，并且捕捉的异常错误码为空，则默认异常状态码
         * 默认捕捉的异常状态码=256
         */
        public static final String DEFAULT_SERVICE_EXCEPTION_ERROR_CODE = "256";

        /**
         * 被捕捉到的异常，但是错误信息为空，则默认异常信息提醒
         * 默认捕捉的异常信息提醒=错误
         */
        public static final String DEFAULT_SERVICE_EXCEPTION_ERROR_MESSAGE = "错误";

    }


    /**
     * 异常-枚举类
     */
    public enum ENUM_ERROR {

        /***********************************************
         * B打头表示银行卡的错误
         **********************************************/
        B00001("B00001", "传入的所有数据为空!", "银行卡新增时,所有数据都为空!"),
        B00002("B00002", "传入的银行主键ID为空!", "银行卡新增时,银行主键ID数据为空!"),
        B00003("B00003", "传入的银行名称为空!", "银行卡新增时,银行名称数据为空!"),
        B00004("B00004", "传入的银行卡为空!", "银行卡新增时,银行卡数据为空!"),
        B00005("B00005", "传入的开户人名字为空!", "银行卡新增时,开户人名字数据为空!"),
        B00006("B00006", "银行卡数据已经存在,无需重复录入!", "银行卡新增时,数据已存在!"),
        B00007("B00007", "银行卡数据录入失败,请核对数据后在重新录入!", "银行卡新增时,新增数据的响应行为0!"),

        B00008("B00008", "传入的所有数据为空!", "银行卡使用状态更新时,所有数据都为空!"),
        B00009("B00009", "传入的银行主键ID为空!", "银行卡使用状态更新时,银行主键ID数据为空!"),
        B00010("B00010", "传入要更新银行卡的使用状态数据为空!", "银行卡使用状态更新时,银行使用状态数据为空!"),
        B00011("B00011", "没有此银行卡数据!", "银行卡使用状态更新时,根据银行卡源ID查询数据为空!"),
        B00012("B00012", "银行卡状态更新失败,请核对数据后在重新更新!", "银行卡使用状态更新时,更新数据的响应行为0!"),







        ;

        /**
         * 错误码
         */
        private String eCode;
        /**
         * 给客户端看的错误信息
         */
        private String eDesc;
        /**
         * 插入数据库的错误信息
         */
        private String dbDesc;




        private ENUM_ERROR(String eCode, String eDesc,String dbDesc) {
            this.eCode = eCode;
            this.eDesc = eDesc;
            this.dbDesc  = dbDesc;
        }

        public String geteCode() {
            return eCode;
        }

        public void seteCode(String eCode) {
            this.eCode = eCode;
        }

        public String geteDesc() {
            return eDesc;
        }

        public void seteDesc(String eDesc) {
            this.eDesc = eDesc;
        }

        public String getDbDesc() {
            return dbDesc;
        }

        public void setDbDesc(String dbDesc) {
            this.dbDesc = dbDesc;
        }
    }
}
