package demo1;
//未使用后缀表达式

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.Vector;

public class Calculator {
    // 运算结果
    String result = "";
    JFrame frame = new JFrame("Calculator");
    //设置放数字键的面板
    JPanel pan1 = new JPanel();
    //设置放显示结果与清除键的面板
    JPanel pan2 = new JPanel();
    //显示计算结果
    JTextField result_TextField = new JTextField(result, 20);
    //设置按钮
    JButton clear_button = new JButton("Clear");
    JButton button0 = new JButton("0");
    JButton button1 = new JButton("1");
    JButton button2 = new JButton("2");
    JButton button3 = new JButton("3");
    JButton button4 = new JButton("4");
    JButton button5 = new JButton("5");
    JButton button6 = new JButton("6");
    JButton button7 = new JButton("7");
    JButton button8 = new JButton("8");
    JButton button9 = new JButton("9");
    JButton button_Dian = new JButton(".");
    JButton button_jia = new JButton("+");
    JButton button_jian = new JButton("-");
    JButton button_cheng = new JButton("*");
    JButton button_chu = new JButton("/");
    JButton button_dy = new JButton("=");

    //设置响应逻辑
    // 操作数1，为了程序的安全，初值一定设置，这里我们设置为0。
    String str1 = "0";
    // 操作数2
    String str2 = "0";
    // 运算符
    String signal = "+";

    // 以下k1至k5为状态开关
    // 开关1用于选择输入方向，将要写入str1或str2
    // 为 1 时写入 str1，为 2 时写入 str2
    int k1 = 1;

    // 开关 2 用于记录符号键的次数
    // 如果 k2>1 说明进行的是 2+3-9+8 这样的多符号运算
    int k2 = 1;

    // 开关3用于标识 str1 是否可以被清 0
    // 等于 1 时可以，不等于1时不能被清0
    int k3 = 1;

    // 开关4用于标识 str2 是否可以被清 0
    // 等于 1 时可以，不等于1时不能被清0
    int k4 = 1;

    // 开关5用于控制小数点可否被录入
    // 等于1时可以，不为1时，输入的小数点被丢掉
    int k5 = 1;

    JButton store;
    //忽略类型 vt储存之前输入的运算符
    @SuppressWarnings("rawtypes")
    Vector vt = new Vector(20, 10);

    public Calculator() {
        display_swing();
        addListener();
    }
    //放置组件
    public void display_swing() {
        //设置在屏幕的位置
        frame.setLocation(300, 300);
        //设置大小不可变
        frame.setResizable(false);

        //面板一(数字键)布局为网格布局
        pan1.setLayout(new GridLayout(4, 4, 5, 5));
        //面板二设置为边界布局
        pan2.setLayout(new BorderLayout());

        // 将用于计算的按钮添加到容器内
        pan1.add(button7);
        pan1.add(button8);
        pan1.add(button9);
        pan1.add(button_chu);
        pan1.add(button4);
        pan1.add(button5);
        pan1.add(button6);
        pan1.add(button_cheng);
        pan1.add(button1);
        pan1.add(button2);
        pan1.add(button3);
        pan1.add(button_jian);
        pan1.add(button0);
        pan1.add(button_Dian);
        pan1.add(button_dy);
        pan1.add(button_jia);
        //设置 pan 对象的边距
        pan1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        //在pan2上添加内容
        pan2.add(result_TextField, BorderLayout.WEST);
        pan2.add(clear_button, BorderLayout.EAST);
        //在框架中添加面板
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(pan2, BorderLayout.NORTH);
        frame.getContentPane().add(pan1, BorderLayout.CENTER);
        //调整大小
        frame.pack();
        frame.setVisible(true);
        // 窗体关闭事件的响应程序
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    //设置监听
    public void addListener() {
        // 监听等于键
        Listener_dy jt_dy = new Listener_dy();
        button_dy.addActionListener(jt_dy);

        // 监听数字键
        Listener jt = new Listener();
        //给每个数字按钮注册监听
        button0.addActionListener(jt);
        button1.addActionListener(jt);
        button2.addActionListener(jt);
        button3.addActionListener(jt);
        button4.addActionListener(jt);
        button5.addActionListener(jt);
        button6.addActionListener(jt);
        button7.addActionListener(jt);
        button8.addActionListener(jt);
        button9.addActionListener(jt);


        // 监听符号键
        Listener_signal jt_signal = new Listener_signal();
        button_jia.addActionListener(jt_signal);
        button_jian.addActionListener(jt_signal);
        button_cheng.addActionListener(jt_signal);
        button_chu.addActionListener(jt_signal);

        // 监听清除键
        Listener_clear jt_c = new Listener_clear();
        clear_button.addActionListener(jt_c);

        // 监听小数点键
        Listener_xiaos jt_xs = new Listener_xiaos();
        button_Dian.addActionListener(jt_xs);


    }

    //局部内部类
    // 数字键
    class Listener implements ActionListener {
        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent e) {
            // 获取事件源，并从事件源中获取输入的数据
            String ss = ((JButton) e.getSource()).getText();

            store = (JButton) e.getSource();
            vt.add(store);

            if (k1 == 1) {
                if (k3 == 1) {
                    str1 = "";
                    // 还原开关k5状态
                    k5 = 1;
                }
                str1 = str1 + ss;
                //运算数不能清零
                k3 = k3 + 1;
                // 显示结果
                result_TextField.setText(str1);
            } else if (k1 == 2) {
                if (k4 == 1) {
                    str2 = "";
                    // 还原开关k5状态
                    k5 = 1;
                }
                str2 = str2 + ss;
                k4 = k4 + 1;
                result_TextField.setText(str2);
            }
        }
    }

    // 小数点的处理
    class Listener_xiaos implements ActionListener {
        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent e) {
            store = (JButton) e.getSource();
            vt.add(store);
            if (k5 == 1) {
                String ss2 = ((JButton) e.getSource()).getText();
                if (k1 == 1) {
                    if (k3 == 1) {
                        str1 = "";
                        // 还原开关k5状态
                        k5 = 1;
                    }
                    str1 = str1 + ss2;
                    k3 = k3 + 1;
                    // 显示结果
                    result_TextField.setText(str1);
                } else if (k1 == 2) {
                    if (k4 == 1) {
                        str2 = "";
                        // 还原开关k5的状态
                        k5 = 1;
                    }
                    str2 = str2 + ss2;
                    k4 = k4 + 1;
                    result_TextField.setText(str2);
                }
            }
            k5 = k5 + 1;
        }
    }

    //监听符号键
    class Listener_signal implements ActionListener {
        @SuppressWarnings("unchecked")
        @Override
        public void actionPerformed(ActionEvent e) {
            String ss2 = ((JButton) e.getSource()).getText();
            store = (JButton) e.getSource();
            vt.add(store);

            if (k2 == 1) {
                //改变写入
                k1 = 2;
                k5 = 1;

            } else {
                int a = vt.size();
                JButton c = (JButton) vt.get(a - 2);
                if (!(c.getText().equals("+")) && !(c.getText().equals("-"))
                        && !(c.getText().equals("*")) && !(c.getText().equals("/"))) {
                    cal();
                    str1 = result;
                    //
                    k1 = 2;
                    k5 = 1;
                    k4 = 1;
                }
            }
            k2 = k2 + 1;//按符号键的次数
            signal = ss2;
        }
    }

    // 等于按键的逻辑，即在输入完成后开始计算
    class Listener_dy implements ActionListener {
        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent e) {

            store = (JButton) e.getSource();
            vt.add(store);
            cal();

            // 还原开关k1状态
            k1 = 1;

            // 还原开关k2状态
            k2 = 1;

            // 还原开关k3状态
            k3 = 1;

            // 还原开关k4状态
            k4 = 1;
            // 为 7+5=12 +5=17 这种计算做准备
            str1 = result;
        }
    }

    // 清除键的逻辑（Clear）
    class Listener_clear implements ActionListener {
        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent e) {
            store = (JButton) e.getSource();
            vt.add(store);
            k5 = 1;
            k2 = 1;
            k1 = 1;
            k3 = 1;
            k4 = 1;
            str1 = "0";
            str2 = "0";
            signal = "";
            result = "";
            result_TextField.setText(result);
            vt.clear();
        }
    }

    // 计算逻辑
    public void cal() {
        // 操作数1
        double a2;
        // 操作数2
        double b2;
        // 运算符
        String c = signal;
        // 运算结果
        double result2 = 0;

        if (c.equals("")) {
            result_TextField.setText("Please input operator");
        } else {
            // 手动处理小数点的问题
            if (str1.equals("."))
                str1 = "0.0";
            if (str2.equals("."))
                str2 = "0.0";

            a2 = Double.valueOf(str1).doubleValue();
            b2 = Double.valueOf(str2).doubleValue();

            if (c.equals("+")) {
                result2 = a2 + b2;
            }
            if (c.equals("-")) {
                result2 = a2 - b2;
            }
            if (c.equals("*")) {
                BigDecimal m1 = new BigDecimal(Double.toString(a2));
                BigDecimal m2 = new BigDecimal(Double.toString(b2));
                result2 = m1.multiply(m2).doubleValue();
            }
            if (c.equals("/")) {
                if (b2 == 0) {
                    result2 = 0;
                } else {
                    result2 = a2 / b2;
                }

            }

            result = ((new Double(result2)).toString());
            result_TextField.setText(result);
        }
    }

    public static void main(String args[]) {
        new Calculator();
    }
}
