package com.hz.fruit.master.core.controller.issue;

import com.alibaba.fastjson.JSON;
import com.hz.fruit.master.core.common.exception.ExceptionMethod;
import com.hz.fruit.master.core.common.utils.BeanUtils;
import com.hz.fruit.master.core.common.utils.JsonResult;
import com.hz.fruit.master.core.common.utils.SignUtil;
import com.hz.fruit.master.core.common.utils.StringUtil;
import com.hz.fruit.master.core.common.utils.constant.ServerConstant;
import com.hz.fruit.master.core.model.RequestEncryptionJson;
import com.hz.fruit.master.core.model.ResponseEncryptionJson;
import com.hz.fruit.master.core.model.issue.IssueModel;
import com.hz.fruit.master.core.protocol.request.issue.RequestIssue;
import com.hz.fruit.master.util.ComponentUtil;
import com.hz.fruit.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * @Description 下发的Controller层
 * @Author yoko
 * @Date 2020/9/23 13:58
 * @Version 1.0
 */
@RestController
@RequestMapping("/fruit/issue")
public class IssueController {

    private static Logger log = LoggerFactory.getLogger(IssueController.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    /**
     * 30分钟.
     */
    public long THIRTY_MIN = 30;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;



    /**
     * @Description: 获取下发数据-集合
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/issue/getDataList
     * 请求的属性类:RequestIssue
     * 必填字段:{"noticeType":1,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","pageNumber":1,"pageSize":3,"token":"111111"}
     * 加密字段:{"jsonData":"eyJub3RpY2VUeXBlIjoxLCJhZ3RWZXIiOjEsImNsaWVudFZlciI6MSwiY2xpZW50VHlwZSI6MSwiY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwiY2N0aW1lIjoyMDE5MTEwNzE4MDI5NTksInNpZ24iOiJhYmNkZWZnIiwicGFnZU51bWJlciI6MSwicGFnZVNpemUiOjMsInRva2VuIjoiMTExMTExIn0="}
     * 客户端加密字段:ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTGlzdCI6W3siY29udGVudCI6IiIsImNyZWF0ZVRpbWUiOiIyMDIwLTAxLTIwIDE4OjMwOjMwIiwiaWNvbkFkcyI6Imh0dHA6Ly9xNmxta3U4dTcuYmt0LmNsb3VkZG4uY29tL3VwbG9hZCUyRmltYWdlJTJGMjAyMF8wM18wMyUyRjY3NmNmNTE0NTFjMDY0ZTkzMTc2ZTM5NmZjZThlOTcyNjM2MTlhZjYuanBnIiwiaWQiOjExLCJwYWdlQWRzIjoiaHR0cDovL3E2bG1rdTh1Ny5ia3QuY2xvdWRkbi5jb20vdXBsb2FkJTJGaW1hZ2UlMkYyMDIwXzAzXzAzJTJGZWU3NTIwZWQyMzcwNTFiMDg2MmExZjkzNmIwMzFkNjU3YTBiMmU4Yi5wbmciLCJza2V0Y2giOiLnrKzkuIDmnaHlhazlkYoiLCJ0aXRsZSI6IjUwMOeQhui0oiDor5Xov5DooYzpgJrnn6UifSx7ImNvbnRlbnQiOiLllabllabllaYiLCJjcmVhdGVUaW1lIjoiMjAyMC0wMy0wMyAxNTo0ODo0NCIsImljb25BZHMiOiJodHRwOi8vcTZsbWt1OHU3LmJrdC5jbG91ZGRuLmNvbS91cGxvYWQlMkZpbWFnZSUyRjIwMjBfMDNfMDMlMkY2NzZjZjUxNDUxYzA2NGU5MzE3NmUzOTZmY2U4ZTk3MjYzNjE5YWY2LmpwZyIsImlkIjoxMiwicGFnZUFkcyI6Imh0dHA6Ly9xNmxta3U4dTcuYmt0LmNsb3VkZG4uY29tL3VwbG9hZCUyRmltYWdlJTJGMjAyMF8wM18wMyUyRmVlNzUyMGVkMjM3MDUxYjA4NjJhMWY5MzZiMDMxZDY1N2EwYjJlOGIucG5nIiwic2tldGNoIjoi5rWL6K+V5ZOmMzMiLCJ0aXRsZSI6Iua1i+ivlTIyIn0seyJjb250ZW50IjoiMzEzMTMxMiIsImNyZWF0ZVRpbWUiOiIyMDIwLTAzLTAzIDE2OjE5OjM2IiwiaWNvbkFkcyI6Imh0dHA6Ly9xNmxta3U4dTcuYmt0LmNsb3VkZG4uY29tL3VwbG9hZCUyRmltYWdlJTJGMjAyMF8wM18wMyUyRjcwMzVjODQxZTAzNWQ0MjVlNjU0NzdjMjk1ZjMzZjQ5MDdhYmFmY2EuanBnIiwiaWQiOjEzLCJwYWdlQWRzIjoiIiwic2tldGNoIjoiMzEzMSIsInRpdGxlIjoiMzEzMSJ9XSwicm93Q291bnQiOjUsInNpZ24iOiIwZGRlZTZkOTNhZjliZWMxMzY1OGFhM2ZiY2U3YWM4ZSIsInN0aW1lIjoxNTkzODYxNjczODMyfQ=="
     *     },
     *     "sgid": "202007041921130000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getDataList", method = {RequestMethod.POST})
    public JsonResult<Object> getDataList(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        RequestIssue requestModel = new RequestIssue();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestIssue.class);
            // check校验数据
            HodgepodgeMethod.checkIssueGetDataList(requestModel);

            // 下发数据
            IssueModel issueQuery = BeanUtils.copy(requestModel, IssueModel.class);
            List<IssueModel> dataList = ComponentUtil.issueService.queryByList(issueQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleIssueListResult(stime, sign, dataList, issueQuery.getRowCount());
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // #添加流水
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this IssueController.getDataList() is error , the all data=%s!", data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this IssueController.getDataList() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }


    /**
     * @Description: 获取下发数据-详情
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/issue/getData
     * 请求的属性类:RequestIssue
     * 必填字段:{"id":12,"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 加密字段:{"jsonData":"eyJpZCI6MTIsImFndFZlciI6MSwiY2xpZW50VmVyIjoxLCJjbGllbnRUeXBlIjoxLCJjdGltZSI6MjAxOTExMDcxODAyOTU5LCJjY3RpbWUiOjIwMTkxMTA3MTgwMjk1OSwic2lnbiI6ImFiY2RlZmciLCJ0b2tlbiI6IjExMTExMSJ9"}
     * 客户端加密字段:id+ctime+cctime+秘钥=sign
     * 服务端加密字段:stime+秘钥=sign
     * result={
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJkYXRhTW9kZWwiOnsiY29udGVudCI6IuWVpuWVpuWVpiIsImNyZWF0ZVRpbWUiOiIyMDIwLTAzLTAzIDE1OjQ4OjQ0IiwiaWNvbkFkcyI6Imh0dHA6Ly9xNmxta3U4dTcuYmt0LmNsb3VkZG4uY29tL3VwbG9hZCUyRmltYWdlJTJGMjAyMF8wM18wMyUyRjY3NmNmNTE0NTFjMDY0ZTkzMTc2ZTM5NmZjZThlOTcyNjM2MTlhZjYuanBnIiwiaWQiOjEyLCJwYWdlQWRzIjoiaHR0cDovL3E2bG1rdTh1Ny5ia3QuY2xvdWRkbi5jb20vdXBsb2FkJTJGaW1hZ2UlMkYyMDIwXzAzXzAzJTJGZWU3NTIwZWQyMzcwNTFiMDg2MmExZjkzNmIwMzFkNjU3YTBiMmU4Yi5wbmciLCJzZWF0IjoxLCJza2V0Y2giOiLmtYvor5Xlk6YzMyIsInRpdGxlIjoi5rWL6K+VMjIifSwic2lnbiI6IjJiYWUyMjk4YWFkNGIzMjJkMTNlOWI5NTU0MWQ4MjM5Iiwic3RpbWUiOjE1OTM4NjI5MTM0Njl9"
     *     },
     *     "sgid": "202007041941530000001",
     *     "cgid": ""
     * }
     */
    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    public JsonResult<Object> getData(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        RequestIssue requestModel = new RequestIssue();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestIssue.class);
            // check校验数据
            HodgepodgeMethod.checkIssueGetData(requestModel);

            // 下发数据
            IssueModel issueQuery = BeanUtils.copy(requestModel, IssueModel.class);
            IssueModel dataModel = (IssueModel)ComponentUtil.issueService.findByObject(issueQuery);
            // 组装返回客户端的数据
            long stime = System.currentTimeMillis();
            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
            String strData = HodgepodgeMethod.assembleIssueResult(stime, sign, dataModel);
            // 数据加密
            String encryptionData = StringUtil.mergeCodeBase64(strData);
            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
            resultDataModel.jsonData = encryptionData;
            // #添加流水
            // 返回数据给客户端
            return JsonResult.successResult(resultDataModel);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // 添加异常
            log.error(String.format("this IssueController.getData() is error , the all data=%s!", data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this IssueController.getData() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }


    /**
     * @Description: 支付平台上报提现
     * <p>
     *     新增下发数据
     * </p>
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8089/fruit/issue/actionAdd
     * 请求的属性类:RequestIssue
     * 必填字段:{"outTradeNo":"outTradeNo1","orderMoney":"5000","bankName":"bankName1","bankCard":"bankCard1","accountName":"accountName1"}
     * 加密值:{"jsonData":"eyJvdXRUcmFkZU5vIjoib3V0VHJhZGVObzEiLCJvcmRlck1vbmV5IjoiNTAwMCIsImJhbmtOYW1lIjoiYmFua05hbWUxIiwiYmFua0NhcmQiOiJiYW5rQ2FyZDEiLCJhY2NvdW50TmFtZSI6ImFjY291bnROYW1lMSJ9"}
     * result=ok/no
     */
    @RequestMapping(value = "/actionAdd", method = {RequestMethod.POST})
    public void actionAdd(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String data = "";
        RequestIssue requestModel = new RequestIssue();
        try{
            // 解密
            data = StringUtil.decoderBase64(requestData.jsonData);
            requestModel  = JSON.parseObject(data, RequestIssue.class);
            HodgepodgeMethod.checkIssueAdd(requestModel);

            if (requestModel.orderMoney.indexOf(".") <= -1){
                requestModel.orderMoney = requestModel.orderMoney + ".00";
            }

            // 判断上报订单号是否存在
            IssueModel issueQuery = HodgepodgeMethod.assembleIssueQuery(0, null, requestModel.outTradeNo, 0, 0,0,0,0,0);
            IssueModel issueModel = (IssueModel)ComponentUtil.issueService.findByObject(issueQuery);
            HodgepodgeMethod.checkIssueIsNotNull(issueModel);

            // 添加上报的下发数据
            IssueModel issueAdd = HodgepodgeMethod.assembleIssueAdd(requestModel, sgid);
            int num = ComponentUtil.issueService.add(issueAdd);
            String resStr = "";
            if (num > 0){
                resStr = "ok";
            }else {
                resStr = "no";
            }

            // 返回数据给客户端
            PrintWriter out = response.getWriter();
            out.print(resStr);
            out.flush();
            out.close();
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this IssueController.actionAdd() is error , the all data=%s!", data));
            if (!StringUtils.isBlank(map.get("dbCode"))){
                log.error(String.format("this IssueController.actionAdd() is error codeInfo, the dbCode=%s and dbMessage=%s !", map.get("dbCode"), map.get("dbMessage")));
            }
            e.printStackTrace();

            PrintWriter out = response.getWriter();
            out.print("no");
            out.flush();
            out.close();
        }
    }


}
