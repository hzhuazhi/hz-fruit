package com.hz.fruit.master.core.common.utils;

import com.hz.fruit.master.core.model.bank.BankModel;
import org.apache.commons.lang.StringUtils;

import java.net.URLEncoder;

/**
 * @Description 短链生成
 * @Author yoko
 * @Date 2020/9/4 16:19
 * @Version 1.0
 */
public class ShortChainUtil {

    /**
     * @Description: 唤起支付宝APP
     * @return
     * @author yoko
     * @date 2020/10/2 11:06
    */
    public static String getShortChainUrl(BankModel bankModel, String shortChainApi, int shortChainMoney, String money){
        String str1 = "";
        try{
            String amount = "0";
            if (shortChainMoney == 2){
                if (!StringUtils.isBlank(money)){
                    amount = money;
                }
            }
            String url1 = "https://www.alipay.com/?appId=09999988&actionType=toCard&sourceId=bill&cardNo="+ bankModel.getBankCard() + "&bankAccount=" + bankModel.getAccountName() + "&money=" + amount + "&amount=" + amount + "&bankMark=" + bankModel.getBankCode() + "&bankName=" + bankModel.getBankName();
            url1 = URLEncoder.encode(url1,"UTF-8");
//            String url = "http://api.6du.in/urls/add?secretkey=555098a19f6ae3b0ICAgICA0c782c0757f395fdgNjA2Mg&lurl="+url1;
            String url = shortChainApi + "&lurl=" + url1;
            return str1 = HttpGetUtil.sendGetUrl(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str1;
    }


    /**
     * @Description: 唤起支付宝H5页面
     * @return
     * @author yoko
     * @date 2020/10/2 11:06
     */
    public static String getShortChainH5Url(String shortChainApi){
        String str1 = "";
        try{
            String url1 = "https://www.alipay.com/?appId=09999988";
            url1 = URLEncoder.encode(url1,"UTF-8");
//            String url = "http://api.6du.in/urls/add?secretkey=555098a19f6ae3b0ICAgICA0c782c0757f395fdgNjA2Mg&lurl="+url1;
            String url = shortChainApi + "&lurl=" + url1;
            return str1 = HttpGetUtil.sendGetUrl(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str1;
    }

    public static String getOutShortChainUtil(BankModel bankModel){
        String str1 = "";
        try{
            String url = "http://47.116.98.162:8087/source/collect/getShortChain?&bankCard="+ bankModel.getBankCard() + "&accountName=" + bankModel.getAccountName() + "&bankCode=" + bankModel.getBankCode() + "&bankName=" + bankModel.getBankName();
            return str1 = HttpGetUtil.sendGetUrl(url);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String [] args){
        String api = "http://api.wzk.im//urls/add?secretkey=555098a19f6ae3b0ICAgICA0c782c0757f395fdgNjA2Mg";
        BankModel bankModel = new BankModel();
        bankModel.setBankCard("6226622108434107");
        bankModel.setAccountName("孟宪宏");
        bankModel.setBankCode("CEB");
        bankModel.setBankName("光大银行");
        String sb = getShortChainUrl(bankModel, api, 2, "100.00");
        System.out.println("sb:" + sb);

        String sb2 = getShortChainH5Url(api);
        System.out.println("sb2:" + sb2);
    }
}
