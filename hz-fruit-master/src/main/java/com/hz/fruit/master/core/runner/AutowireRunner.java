package com.hz.fruit.master.core.runner;

import com.hz.fruit.master.core.common.redis.RedisIdService;
import com.hz.fruit.master.core.common.redis.RedisService;
import com.hz.fruit.master.core.common.utils.constant.LoadConstant;
import com.hz.fruit.master.core.service.*;
import com.hz.fruit.master.util.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Order(0)
public class AutowireRunner implements ApplicationRunner {
    private final static Logger log = LoggerFactory.getLogger(AutowireRunner.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;


    Thread runThread = null;

    @Autowired
    private RedisIdService redisIdService;
    @Autowired
    private RedisService redisService;

    @Resource
    private LoadConstant loadConstant;

    @Autowired
    private RegionService regionService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantRechargeService merchantRechargeService;

    @Autowired
    private MerchantBankStrategyService merchantBankStrategyService;

    @Autowired
    private MerchantDataService merchantDataService;

    @Autowired
    private BankTypeService bankTypeService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelBankService channelBankService;

    @Autowired
    private StatisticsClickPayService statisticsClickPayService;

    @Autowired
    private ShortChainService shortChainService;



    @Autowired
    private MobileCardService mobileCardService;

    @Autowired
    private MobileCardShortMsgService mobileCardShortMsgService;

    @Autowired
    private ShortMsgStrategyService shortMsgStrategyService;

    @Autowired
    private ShortMsgArrearsService shortMsgArrearsService;

    @Autowired
    private BankService bankService;

    @Autowired
    private BankCollectionService bankCollectionService;








    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AutowireRunner ...");
        ComponentUtil.redisIdService = redisIdService;
        ComponentUtil.redisService = redisService;
        ComponentUtil.loadConstant = loadConstant;
        ComponentUtil.regionService = regionService;
        ComponentUtil.strategyService = strategyService;
        ComponentUtil.orderService = orderService;

        ComponentUtil.merchantService = merchantService;
        ComponentUtil.merchantRechargeService = merchantRechargeService;
        ComponentUtil.merchantBankStrategyService = merchantBankStrategyService;
        ComponentUtil.merchantDataService = merchantDataService;
        ComponentUtil.bankTypeService = bankTypeService;
        ComponentUtil.issueService = issueService;
        ComponentUtil.channelService = channelService;
        ComponentUtil.channelBankService = channelBankService;
        ComponentUtil.statisticsClickPayService = statisticsClickPayService;
        ComponentUtil.shortChainService = shortChainService;



        ComponentUtil.mobileCardService = mobileCardService;
        ComponentUtil.mobileCardShortMsgService = mobileCardShortMsgService;
        ComponentUtil.shortMsgStrategyService = shortMsgStrategyService;
        ComponentUtil.shortMsgArrearsService = shortMsgArrearsService;
        ComponentUtil.bankService = bankService;
        ComponentUtil.bankCollectionService = bankCollectionService;

        runThread = new RunThread();
        runThread.start();






    }

    /**
     * @author df
     * @Description: TODO(模拟请求)
     * <p>1.随机获取当日金额的任务</p>
     * <p>2.获取代码信息</p>
     * @create 20:21 2019/1/29
     **/
    class RunThread extends Thread{
        @Override
        public void run() {
            log.info("启动啦............");
        }
    }




}
