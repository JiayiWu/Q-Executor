package present.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 16-8-27.
 * <p>
 * 大单饼图，大单占总成交的比例
 */
public class GeneralPie {

    public JPanel getPieChart(String codeNum) {
        JPanel panel = new ChartPanel(createPieChart(codeNum));

        panel.setPreferredSize(new Dimension(300, 1));

        return panel;
    }

    /**
     * 创建PieChart
     */
    private JFreeChart createPieChart(String codeNum) {
        DefaultPieDataset dataSet = getPieDataSet(codeNum);

        // JFreeChart主要由三个部分构成：title(标题),legend(图释),plot(图表主体)。
        JFreeChart chart = ChartFactory.createPieChart("", dataSet, false, true, false);
        //设置Chart属性
        setChartProperties(chart);

        return chart;
    }

    /**
     * 设置Chart属性，可解决乱码问题
     *
     * @param chart 统计图标
     */
    private void setChartProperties(JFreeChart chart) {
        //三个部分设置字体的方法分别如下:
        TextTitle textTitle = chart.getTitle();
        textTitle.setFont(new Font("宋体", Font.BOLD, 20));
        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setItemFont(new Font("宋体", Font.BOLD, 20));
        }
        PiePlot pie = (PiePlot) chart.getPlot();
        pie.setLabelFont(new Font("宋体", Font.BOLD, 12));
        pie.setNoDataMessage("No data available");
        //设置PieChart是否显示为圆形
        pie.setCircular(true);
        // 间距
        pie.setLabelGap(0.01D);
    }

    /**
     * 获得PieChart的数据集
     *
     * @return pieChart数据集
     */
    private DefaultPieDataset getPieDataSet(String codeNum) {
        DefaultPieDataset dataSet = new DefaultPieDataset();

        dataSet.setValue("大单", 56.4);
        dataSet.setValue("其它", 630.5);

        return dataSet;
    }
}