package cn.edu.nju.software.dao;

import cn.edu.nju.software.model.StockBasicInfo;
import cn.edu.nju.software.utils.ColumnName;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by 王栋 on 2016/8/2 0002.
 */

@Repository
public class StockBasicInfoDao {
    @Resource
    BaseDao baseDao;

    public List<StockBasicInfo> getAllStocksBasicInfo() {
        try{
        List<StockBasicInfo> basicInfos = (List<StockBasicInfo>) baseDao.getAll(StockBasicInfo.class);
            return basicInfos;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<StockBasicInfo>();
        }
    }

    //提供一个完整的股票代码返回股票的基本信息对象
    //完整股票代码有两种形式 sh600000 或者 600000
    public StockBasicInfo getStockBasicInfo(String code){
        //如果没有查询到的话就返回一个null
        code = getRealCode(code);
        StockBasicInfo stockBasicInfo = null;
        try {
            stockBasicInfo = (StockBasicInfo) baseDao.findByPrimaryKey(StockBasicInfo.class,code);
            return stockBasicInfo;
        }catch (Exception e){
            e.printStackTrace();
            return stockBasicInfo;
        }

    }

    public List<StockBasicInfo> getStocksBasicInfoByCode(String partOfCode){
        List<StockBasicInfo> stockBasicInfos = new ArrayList<StockBasicInfo>();

        try {
        List<Object[]> lists = baseDao.execSqlQuery("select * from StocksInfo where code like \'%"+partOfCode+"%\'");
        for(Object[] objects : lists){
            StockBasicInfo temp = new StockBasicInfo(objects[0].toString(),objects[1].toString(),objects[2].toString()
                    ,objects[3].toString(),Long.parseLong(objects[4].toString()));
            stockBasicInfos.add(temp);
        }
        return stockBasicInfos;
        }catch (Exception e){
            e.printStackTrace();
            return stockBasicInfos;
        }

    }

    public List<StockBasicInfo> getStocksBasicInfoByName(String partOfName){
        List<StockBasicInfo> stockBasicInfos = new ArrayList<StockBasicInfo>();

        try {
            List<Object[]> lists = baseDao.execSqlQuery("select * from StocksInfo where name like \'%"+partOfName+"%\'");
            for(Object[] objects : lists){
                StockBasicInfo temp = new StockBasicInfo(objects[0].toString(),objects[1].toString(),objects[2].toString()
                        ,objects[3].toString(),Long.parseLong(objects[4].toString()));
                stockBasicInfos.add(temp);
            }
            return stockBasicInfos;
        }catch (Exception e){
            e.printStackTrace();
            return stockBasicInfos;
        }

    }

    public List<StockBasicInfo> getStocksBasicInfoByArea(String area){
        List<StockBasicInfo> stockBasicInfos = new ArrayList<StockBasicInfo>();

        try {
            stockBasicInfos = (List<StockBasicInfo>) baseDao.findByProperty(StockBasicInfo.class,"area",area);
            return stockBasicInfos;
        }catch (Exception e){
            e.printStackTrace();
            return stockBasicInfos;
        }

    }
    private String getRealCode(String code){
        if(code.length()==6){
            if(code.charAt(0)=='6'){
                code = "sh"+code;
            }else{
                code = "sz"+code;
            }
            return code;
        }else {
            return code;
        }
    }
}
