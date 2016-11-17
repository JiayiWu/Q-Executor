package blservice.vwap;

import org.json.JSONException;
import vo.MLForVWAPPriceVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ZhangYF on 2016/11/17.
 */
public interface MLForVWAPVerifyService {

    /**
     * 获取最新数据下动态预测的均价
     * @param stockID 股票代码
     * @param date 要预测的日期
     * @throws IOException
     * @throws JSONException
     */
    MLForVWAPPriceVO getDynamicPrice(String stockID,Date date) throws IOException, JSONException;

    /**
     * 获取最新数据下静态预测的48个成交量
     * @param stockID 股票代码
     * @param date 要预测的日期
     */
    ArrayList<Integer> getStaticVol(String stockID, Date date) throws IOException, JSONException;
}