package present.panel.stock;

import bl.user.SelfSelectServiceImpl;
import blservice.user.SelfSelectService;
import org.json.JSONException;
import org.json.JSONObject;
import present.MainFrame;
import present.PanelSwitcher;
import present.component.CardsPanel;
import present.component.TextPlusBtn;
import present.panel.account.LoginPanel;
import present.utils.ImageLoader;
import present.utils.StockJsonInfo;
import util.JsonUtil;
import vo.NowTimeSelectedStockInfoVO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Y481L on 2016/8/28.
 *
 * 股票搜索面板
 */
public class SearchPanel extends JPanel {

    private static final int PADDING = 20;

    //搜索面板高度
    private static final int SEARCH_H = 42;

    //一个股票信息面板的宽度
    private static final int CARD_W = 200;

    //一个股票信息面板的高度
    private static final int CARD_H = 200;

    //字符串切割符
    private static final String separator = "--";

    //背景图片
    private Image background = ImageLoader.search_bg;

    //面板跳转器
    private PanelSwitcher switcher;

    public SearchPanel(PanelSwitcher switcher) {
        this.switcher = switcher;


        //搜索
        TextPlusBtn search = this.createSearchPanel();
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setOpaque(false);
        p.add(search);

//        //热门股票信息
//        hotStockInfo = new JPanel(new FlowLayout(
//                FlowLayout.CENTER, 0, 0
//        ));
//        hotStockInfo.setOpaque(false);
//        loading = new LoadingPanel();
//        loading.setOpaque(false);
//        loading.setPreferredSize(
//                new Dimension(CARD_W, CARD_H)
//        );
//        hotStockInfo.add(loading);
//        getData();

        //自选股票信息
        JPanel selfStockInfo = new JPanel(new FlowLayout(
                FlowLayout.CENTER, 0, 0
        ));
        selfStockInfo.setOpaque(false);
        if (LoginPanel.IS_LOGIN) {
            try {
                SelfSelectService self = new SelfSelectServiceImpl();
                List<NowTimeSelectedStockInfoVO> list = self.getUserSelectedStock(
                        LoginPanel.LOGIN_USER, LoginPanel.LOGIN_PW
                );
                List<JPanel> cards = new ArrayList<>();

                if(list.size() == 0) {
                    background = ImageLoader.search_bg_empty_tip;
                    repaint();
                } else {
                    list.forEach((vo) -> {
                        StockInfoCard card = new StockInfoCard(
                                vo.getName(), vo.getGid(),
                                String.valueOf(vo.getNowPri()),
                                vo.getIncrease(),
                                CARD_W, CARD_H
                        );
                        card.addListener(new MouseAdapter() {
                            @Override
                            public void mouseReleased(MouseEvent e) {
                                super.mouseReleased(e);
                                switcher.jump(new StockPanel(vo.getGid()));
                            }
                        });
                        cards.add(card);
                    });
                    CardsPanel panel = new CardsPanel(
                            cards, ImageLoader.selfTip
                    );
                    selfStockInfo.add(panel);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "网络异常");
            }
        } else {
            background = ImageLoader.search_bg_login_tip;
            repaint();
        }

//        //滚动面板包含表格
//        JPanel container = new JPanel(new FlowLayout(
//                FlowLayout.CENTER, PADDING << 2, 0
//        ));
//        container.setOpaque(false);
//        container.add(createSelfTable());
//        container.add(createHotTable());

        //添加组件到主面板
        Box box = Box.createVerticalBox();
        box.setOpaque(false);
        box.add(Box.createVerticalStrut(PADDING << 2));
        box.add(search);
//        box.add(Box.createVerticalStrut(PADDING << 1));
//        box.add(hotStockInfo);
        box.add(Box.createVerticalStrut(PADDING << 1));
        box.add(selfStockInfo);

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(box, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(box, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(
                background, 0, 0, this.getWidth(), this.getHeight(), null
        );
    }

    private TextPlusBtn createSearchPanel() {
        TextPlusBtn search = new TextPlusBtn(
                "输入股票名称或股票代码", MainFrame.PANEL_W >> 1, SEARCH_H
        );
        search.setOpaque(false);
        //设置字符串匹配规则
        search.setMatcher((key) -> {
            Vector<String> v = new Vector<>();
            List<JSONObject> list = JsonUtil.contains(
                    StockJsonInfo.JSON_KEYS, StockJsonInfo.JSON_PATH, key
            );
            for (JSONObject obj : list) {
                try {
                    v.addElement(
                            obj.getString(StockJsonInfo.KEY_CODE) + separator
                                    + obj.getString(StockJsonInfo.KEY_NAME) + separator
                                    + obj.getString(StockJsonInfo.KEY_INDUSTRY)
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return v;
        });
        //设置下拉提示列表监听
        search.setListClickHandler((text) ->
                switcher.jump(new StockPanel(text.split(separator)[0]))
        );
        search.setListFocusHandler((field, text) -> field.setText(text.split(separator)[1]));
        //设置确定按钮监听
        search.setBtnListener((e) -> {
            try {
                StockPanel p = new StockPanel(JsonUtil.confirm(StockJsonInfo.KEY_NAME
                        , StockJsonInfo.KEY_CODE, search.getText(), StockJsonInfo.JSON_PATH));
                switcher.jump(p);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this, "名为 " + search.getText() + " " + "股票信息不存在"
                );
            }
        });
        return search;
    }



/*==========================================表格创建(废弃)======================================================*/

//    private static final Color TABLE_BG = new Color(0xfaf3f3);
//
//    private static final int TABLE_H = 370;
//
//    private static final int TABLE_W = 320;

//        private void getData() {
//        SwingWorker worker = new SwingWorker<List<HotStockVO>, Void>() {
//            @Override
//            protected List<HotStockVO> doInBackground() throws Exception {
//                try {
//                    return hotStocks.getHotStock();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw e;
//                }
//            }
//
//            @Override
//            protected void done() {
//                try {
//                    List<HotStockVO> hotDatas = get();
//                    List<JPanel> cards = new ArrayList<>();
//                    hotDatas.forEach((vo) -> {
//                        StockInfoCard card = new StockInfoCard(
//                                vo.getName(), vo.getCode(),
//                                vo.getCurrentPrice(),
//                                vo.getPchange(),
//                                CARD_W, CARD_H
//                        );
//                        card.addListener(new MouseAdapter() {
//                            @Override
//                            public void mouseReleased(MouseEvent e) {
//                                super.mouseReleased(e);
//                                switcher.jump(new StockPanel(vo.getCode()));
//                            }
//                        });
//                        cards.add(card);
//                    });
//                    CardsPanel panel = new CardsPanel(
//                            cards, ImageLoader.hotTip
//                    );
//                    PanelSwitcher.jump(
//                            hotStockInfo, loading, panel
//                    );
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    JOptionPane.showMessageDialog(SearchPanel.this, "网络异常");
//                }
//            }
//        };
//
//        worker.execute();
//    }


//    /**
//     * 创建自选股票表格
//     *
//     * @return 自选股票表格
//     */
//    private JPanel createSelfTable() {
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        JLabel label = new JLabel("自选股票");
//        panel.setPreferredSize(new Dimension(
//                TABLE_W - (PADDING << 1), PADDING << 1
//        ));
//
//        Vector<String> header = new Vector<>(4);
//        header.addElement("代码");
//        header.addElement("股票");
//        header.addElement("价格");
//        header.addElement("涨跌幅");
//        Vector<String> data = new Vector<>();
//        DefaultTableModel model = new DefaultTableModel(data, header);
//        try {
//            if (LoginPanel.IS_LOGIN) {
//                List<NowTimeSelectedStockInfoVO> list = self.getUserSelectedStock(
//                        LoginPanel.LOGIN_USER, LoginPanel.LOGIN_PW
//                );
//                list.forEach((vo) -> {
//                    Vector<String> row = new Vector<>();
//                    row.addElement(vo.getGid());
//                    row.addElement(vo.getName());
//                    row.addElement(String.valueOf(vo.getNowPri()));
//                    row.add(String.valueOf(vo.getIncrePer()));
//                    model.addRow(row);
//                });
//                panel.add(label);
//            } else {
//                panel.add(createLoginTip());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "网络异常");
//        }
//
//        JTable self = new MyTable(model);
//        self.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                super.mouseReleased(e);
//                jumpToStockPanel(self);
//            }
//        });
//
//        JScrollPane pane = new JScrollPane(self);
//        pane.setPreferredSize(new Dimension(
//                TABLE_W - (PADDING << 2), TABLE_H - (PADDING << 1)
//        ));
//
//        JPanel container = new JPanel(new BorderLayout());
//        container.add(panel, BorderLayout.SOUTH);
//        container.add(pane, BorderLayout.CENTER);
//        return container;
//    }
//
//    /**
//     * 创建热门股票表格
//     *
//     * @return 热门股票表格
//     */
//    private JPanel createHotTable() {
//        Vector<String> header = new Vector<>(4);
//        header.addElement("代码");
//        header.addElement("股票");
//        header.addElement("价格");
//        header.addElement("涨跌额");
//        Vector<String> data = new Vector<>();
//        DefaultTableModel hotTableModel = new DefaultTableModel(data, header);
//
//        hotTable = new MyTable(hotTableModel);
//        hotTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                super.mouseReleased(e);
//                jumpToStockPanel(hotTable);
//            }
//        });
//
//        JScrollPane pane = new JScrollPane(hotTable);
//        pane.setPreferredSize(new Dimension(
//                TABLE_W - (PADDING << 2), TABLE_H - (PADDING << 1)
//        ));
//
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        JLabel label = new JLabel("热门股票");
//        panel.setPreferredSize(new Dimension(
//                TABLE_W - (PADDING << 1), PADDING << 1
//        ));
//        panel.add(label);
//
//        JPanel container = new JPanel(new BorderLayout());
//        container.add(panel, BorderLayout.SOUTH);
//        container.add(pane, BorderLayout.CENTER);
//        return container;
//    }
//
//    private JPanel wrapTable(JPanel table) {
//        JPanel container = new JPanel(new BorderLayout());
//        container.setBackground(TABLE_BG);
//
//        //北边空白面板
//        JPanel north = new JPanel();
//        north.setOpaque(false);
//        north.setPreferredSize(new Dimension(TABLE_W, PADDING));
//        container.add(north, BorderLayout.NORTH);
//
//        //西边空白面板
//        JPanel west = new JPanel();
//        west.setOpaque(false);
//        west.setPreferredSize(new Dimension(PADDING, TABLE_H - (PADDING << 1)));
//        container.add(west, BorderLayout.WEST);
//
//        //东边空白面板
//        JPanel east = new JPanel();
//        east.setOpaque(false);
//        east.setPreferredSize(new Dimension(PADDING, TABLE_H - (PADDING << 1)));
//        container.add(east, BorderLayout.EAST);
//
//        //南边空白面板
//        JPanel south = new JPanel();
//        south.setOpaque(false);
//        south.setPreferredSize(new Dimension(TABLE_W, PADDING));
//        container.add(south, BorderLayout.SOUTH);
//
//        //中间表格
//        container.add(table, BorderLayout.CENTER);
//
//        return container;
//    }
//
//    private JPanel createLoginTip() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
//
//        JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
//        up.setOpaque(false);
//        JLabel label1 = new JLabel("请先");
//        up.add(label1);
//
//        Link link = new Link("登录");
//        link.setHandler(() -> switcher.jump(new AccountPanel(switcher)));
//        up.add(link);
//
//        panel.add(up);
//        panel.setOpaque(false);
//        return panel;
//    }
//
//    private void jumpToStockPanel(JTable table) {
//        int row = table.getSelectedRow();
//        row = row < 0 ? 0 : row;
//        switcher.jump(new StockPanel((String) table.getValueAt(row, 0)));
//    }

}
