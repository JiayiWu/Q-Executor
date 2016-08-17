import cn.edu.nju.software.service.StockService;
import cn.edu.nju.software.vo.StockInfoByTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 王栋 on 2016/8/17 0017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/mvc.xml","/applicationContext.xml"})
public class StockInfoByTimeTest {
    @Resource
    StockService stockService;

    @Test
    public void test1(){
        List<StockInfoByTime> stockInfoByTimes = stockService.getStockInfoByTime("sh600000");
        for(StockInfoByTime stockInfoByTime : stockInfoByTimes){
            System.out.println(stockInfoByTime);
        }
    }
}
