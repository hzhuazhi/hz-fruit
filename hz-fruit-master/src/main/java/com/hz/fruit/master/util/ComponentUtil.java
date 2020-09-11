package com.hz.fruit.master.util;


import com.hz.fruit.master.core.common.redis.RedisIdService;
import com.hz.fruit.master.core.common.redis.RedisService;
import com.hz.fruit.master.core.common.utils.constant.LoadConstant;
import com.hz.fruit.master.core.service.*;

/**
 * 工具类
 */
public class ComponentUtil {
    public static RedisIdService redisIdService;
    public static RedisService redisService;
    public static LoadConstant loadConstant;
    public static RegionService regionService;
    public static StrategyService strategyService;
    public static OrderService orderService;


    public static MerchantService merchantService;
    public static MerchantBankCollectionService merchantBankCollectionService;
    public static MerchantRechargeService merchantRechargeService;
    public static MerchantBankStrategyService merchantBankStrategyService;
    public static MerchantDataService merchantDataService;
    public static BankTypeService bankTypeService;
    public static IssueService issueService;
    public static ChannelService channelService;
    public static ChannelBankService channelBankService;
    public static StatisticsClickPayService statisticsClickPayService;
    public static ShortChainService shortChainService;


    public static MobileCardService mobileCardService;
    public static MobileCardShortMsgService mobileCardShortMsgService;
    public static ShortMsgStrategyService shortMsgStrategyService;
    public static ShortMsgArrearsService shortMsgArrearsService;


}
