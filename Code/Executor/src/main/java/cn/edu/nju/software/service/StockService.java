package cn.edu.nju.software.service;

import cn.edu.nju.software.config.MsgInfo;
import cn.edu.nju.software.model.StockBasicInfo;
import cn.edu.nju.software.model.StockInfoByCom;
import cn.edu.nju.software.model.StockInfoByPer;
import cn.edu.nju.software.vo.*;

import java.util.List;

/**
 * Created by Jiayiwu on 8/9/16.
 * Mail:wujiayi@lgdreamer.com
 * Change everywhere
 */
public interface StockService {
    public MsgInfo getStockNowTime(String Code);

    public List<StockKLineVO> getKLineByDay(String Code);

    public List<StockKLineVO> getKLineByWeek(String Code);

    public List<StockKLineVO> getKLineByMonth(String Code);

    public List<StockInfoByPer> getPerStockInfo(String Code);


    public List<StockInfoByCom> getComStockInfo(String Code);

    public List<StockInfoByCom> getComStockInfo(String Code,double param);

    public List<StockInfoByPrice> getStockInfoByPrice(String Code);

    public List<StockInfoByTime> getStockInfoByTime(String Code);

    public StockBasicInfo getStockBasicInfo(String Code);

    public List<HotStockVO> getHotStocks();

    public List<DeepStockVO> getDeepStock(String Code);

    List<StockKLineVO> getKLineByMinute(String codeNum, int interval);

    List<KDJVO> getKDJ(String codeNum);

    List<IndexVO> getRSI(String codeNum);

    List<IndexVO> getBIAS(String codeNum);

    List<IndexVO> getMACD(String codeNum);
}
