package com.hz.fruit.master.core.controller.bank;

import com.alibaba.fastjson.JSON;
import com.hz.fruit.master.core.common.exception.ExceptionMethod;
import com.hz.fruit.master.core.common.utils.BeanUtils;
import com.hz.fruit.master.core.common.utils.JsonResult;
import com.hz.fruit.master.core.common.utils.StringUtil;
import com.hz.fruit.master.core.common.utils.constant.ServerConstant;
import com.hz.fruit.master.core.model.bank.BankModel;
import com.hz.fruit.master.core.protocol.request.bank.RequestBank;
import com.hz.fruit.master.util.ComponentUtil;
import com.hz.fruit.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description 银行的Controller层
 * @Author yoko
 * @Date 2020/9/5 19:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/fruit/bank")
public class BankController {

    private static Logger log = LoggerFactory.getLogger(BankController.class);


    /**
     * @Description: 录入第三方的银行卡
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/bank/addSource
     * 请求的属性类:RequestBank
     * 必填字段:{"sourceId":"sourceId1","bankName":"bankName1","bankCard":"bankCard1","accountName":"accountName1","bankCode":"bankCode1","useStatus":1}
     * result={
     *     "resultCode": "256",
     *     "message": "传入的银行卡为空!"
     * }
     *
     * result={
     *     "resultCode": "0",
     *     "message": "success"
     * }
     */
    @RequestMapping(value = "/addSource", method = {RequestMethod.POST})
    public JsonResult<Object> addSource(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestBank requestData) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        try{
            // check校验传入的银行卡信息
            HodgepodgeMethod.checkBank(requestData);

            // 查询是否有重复录入
            BankModel bankQuery = HodgepodgeMethod.assembleBankBySourceQuery(requestData.sourceId);
            BankModel bankModel = (BankModel) ComponentUtil.bankService.findByObject(bankQuery);
            HodgepodgeMethod.checkBankData(bankModel);

            // 正式添加数据
            BankModel bankData = BeanUtils.copy(requestData, BankModel.class);
            int num = ComponentUtil.bankService.add(bankData);
            HodgepodgeMethod.checkAddBank(num);
            // 返回数据给客户端
            return JsonResult.successResult(null);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this BankController.addSource() is error , the all data=%s!", JSON.toJSON(requestData)));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this BankController.addSource() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }


    /**
     * @Description: 银行卡使用状态更新
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/bank/updateUseStatus
     * 请求的属性类:RequestBank
     * 必填字段:{"sourceId":"sourceId1","useStatus":1}
     * result={
     *     "resultCode": "256",
     *     "message": "传入要更新银行卡的使用状态数据为空!"
     * }
     *
     * result={
     *     "resultCode": "0",
     *     "message": "success"
     * }
     */
    @RequestMapping(value = "/updateUseStatus", method = {RequestMethod.POST})
    public JsonResult<Object> updateUseStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestBank requestData) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        try{
            // check校验传入的银行卡更新状态信息
            HodgepodgeMethod.checkBankUpdateUseStatus(requestData);

            // 查询是否有此银行卡数据
            BankModel bankQuery = HodgepodgeMethod.assembleBankBySourceQuery(requestData.sourceId);
            BankModel bankModel = (BankModel) ComponentUtil.bankService.findByObject(bankQuery);
            HodgepodgeMethod.checkBankIsNullData(bankModel);

            // 正式更新数据
            BankModel bankData = BeanUtils.copy(requestData, BankModel.class);
            int num = ComponentUtil.bankService.updateUseStatus(bankData);
            HodgepodgeMethod.checkBankUpdateUseStatus(num);
            // 返回数据给客户端
            return JsonResult.successResult(null);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this BankController.updateUseStatus() is error , the all data=%s!", JSON.toJSON(requestData)));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this BankController.updateUseStatus() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }


    /**
     * @Description: 银行卡成功收款数据回调
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/bank/successData
     * 请求的属性类:RequestBank
     * 必填字段:{"sourceId":"sourceId1","useStatus":1}
     * result={
     *     "resultCode": "256",
     *     "message": "传入数据为空!"
     * }
     *
     * result={
     *     "resultCode": "0",
     *     "message": "success"
     * }
     */
    @RequestMapping(value = "/successData", method = {RequestMethod.POST})
//    @PostMapping(value = "/successData")
    public JsonResult<Object> successData(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> obj) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        try{
            if (obj != null){
                log.info("BankController.successData():" + JSON.toJSON(obj));
            }else {
                return JsonResult.failedResult("传入数据为空!", "001");
            }
            // 返回数据给客户端
            return JsonResult.successResult(null);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this BankController.successData() is error , the all data=%s!", JSON.toJSON(obj)));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this BankController.successData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }

}
