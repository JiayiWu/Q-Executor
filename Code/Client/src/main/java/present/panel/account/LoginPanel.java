package present.panel.account;

import bl.UserServiceImpl;
import blservice.UserService;
import config.MsgInfo;
import present.PanelSwitcher;
import present.component.QTextField;
import present.component.QPasswordField;
import present.panel.home.NavPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Y481L on 2016/8/25.
 *
 * 用户登录界面
 */
public class LoginPanel extends JPanel {

    private UserService service = new UserServiceImpl();

    private PanelSwitcher switcher;

    private QTextField name = new QTextField("邮箱/用户名");

    private QPasswordField password = new QPasswordField("密码");

    private JButton login = new JButton("登录");

    private JButton register = new JButton("注册");

    private JButton findPW = new JButton("找回密码");

    private static final int COMPONENT_NUM = 5;

    private static final int WIDTH = 300;

    private static final int HEIGHT = 56;

    private static final int PADDING = 20;

    public static String LOGIN_USER = null;

    public static String LOGIN_PW = null;

    public static boolean IS_LOGIN = false;

    public LoginPanel(PanelSwitcher switcher) {
        this.switcher = switcher;
        this.addComponents();
    }

    private void addComponents() {
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(PADDING << 1));
        JLabel title = new JLabel("登录");
        title.setFont(new Font("宋体", Font.PLAIN, 30));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(title);
        box.add(panel);
        box.add(Box.createVerticalStrut(PADDING << 1));
        box.add(this.wrapComponent(name));
        box.add(Box.createVerticalStrut(PADDING));
        box.add(this.wrapComponent(password));
        box.add(Box.createVerticalStrut(PADDING));
        box.add(this.wrapComponent(login));
        box.add(Box.createVerticalStrut(PADDING));
        box.add(this.wrapComponent(register));
        box.add(Box.createVerticalStrut(PADDING));
        box.add(this.wrapComponent(findPW));
        box.add(Box.createVerticalStrut(
                NavPanel.PANEL_H - (HEIGHT + PADDING) * COMPONENT_NUM
        ));
        this.addListeners();
        this.setLayout(new BorderLayout());
        this.add(box);
    }

    private JPanel wrapComponent(JComponent c) {
        c.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        c.setFont(new Font("宋体", Font.PLAIN, 22));
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(c);
        return panel;
    }

    private void addListeners() {
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                String user = name.getText();
                String pw = new String(password.getPassword());
                try {
                    MsgInfo result = service.login(user, pw);
                    if(result.isState()) {
                        LOGIN_USER = user;
                        LOGIN_PW = pw;
                        IS_LOGIN = true;
                        JOptionPane.showMessageDialog(LoginPanel.this, "登录成功");
                    }else {
                        JOptionPane.showMessageDialog(LoginPanel.this, result.getInfo());
                        System.out.println("LoginPanel.addListeners");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginPanel.this, "网络异常");
                }
            }
        });

        register.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                switcher.jump(new RegisterPanel());
            }
        });

        findPW.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                switcher.jump(new FindPWPanel());
            }
        });
    }
}
