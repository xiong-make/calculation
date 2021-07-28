package demo1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

public class Calculator2 {
    // 记录表达式
    StringBuffer str = new StringBuffer();
    JFrame frame = new JFrame("Calculator");
    //设置放数字键的面板
    JPanel pan1 = new JPanel();
    //设置放显示结果与清除键的面板
    JPanel pan2 = new JPanel();
    //显示表达式与结果
    JTextField result_TextField = new JTextField(str.toString(), 20);
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
    JButton button_leftkuo = new JButton("(");
    JButton button_rightkuo = new JButton(")");

    public Calculator2() {
        display_swing();
        addlistener();
    }

    public void display_swing() {
        //设置在屏幕的位置
        frame.setLocation(300, 300);
        //设置大小不可变
        frame.setResizable(false);

        //面板一布局为网格布局
        pan1.setLayout(new GridLayout(5, 4, 5, 5));
        //面板二设置为边界布局
        pan2.setLayout(new BorderLayout());

        //在面板上添加数字键
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
        pan1.add(button_leftkuo);
        pan1.add(button_rightkuo);

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

    public void addlistener() {
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
        button_jia.addActionListener(jt);
        button_jian.addActionListener(jt);
        button_cheng.addActionListener(jt);
        button_chu.addActionListener(jt);
        button_leftkuo.addActionListener(jt);
        button_rightkuo.addActionListener(jt);
        button_Dian.addActionListener(jt);
        clear_button.addActionListener(new Listener_clear());
        button_dy.addActionListener(new Listener_result());
    }

    class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            str.append(((JButton) e.getSource()).getText());
            result_TextField.setText(str.toString());
        }
    }

    class Listener_clear implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            str.delete(0, str.length());
            result_TextField.setText(str.toString());
        }
    }

    class Listener_result implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // String str=((JButton)e.getSource()).getText();
            str = Trans(str);
            // System.out.println(str);
            str = calculate(str.toString());
            result_TextField.setText(str.toString());
        }
    }

    int prio(char op) {
        int priority = -1;
        if (op == '*' || op == '/') priority = 2;
        if (op == '+' || op == '-') priority = 1;
        //左括号低
        if (op == '(') priority = 0;
        return priority;
    }

    //将中缀表达式转换成后缀表达式
    StringBuffer Trans(StringBuffer str) {
        //System.out.println(str);
        StringBuffer str1 = new StringBuffer();
        Stack<Character> s = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            //如果是数字直接输出
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9'||str.charAt(i)=='.') {
                str1.append(str.charAt(i));
            } else {
                //使不同的数字隔开
                str1.append(" ");
                //栈空则直接入栈
                if (s.empty()) s.push(str.charAt(i));
                else if (str.charAt(i) == '(') s.push('(');
                else if (str.charAt(i) == ')') {
                    while (s.peek() != '(') {
                        str1.append(s.peek()).append(" ");
                        s.pop();
                    }
                    //弹出左括号
                    s.pop();
                } else {
                    while (prio(str.charAt(i)) <= prio(s.peek())) {
                        str1.append(s.peek()).append(" ");
                        s.pop();
                        if (s.empty()) break;
                    }
                    s.push(str.charAt(i));
                }
            }
        }
        while (!s.empty()) {
            str1.append(" ").append(s.peek()).append(" ");
            s.pop();
        }
        //System.out.println(str1);
        return str1;
    }

    StringBuffer calculate(String str) {
        Stack<Double> s = new Stack<>();
        double x = 0.0, x1, x2;
        boolean flag = false;
        StringBuffer num=new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case '+':
                    flag = false;
                    x1 = s.peek();
                    s.pop();
                    x2 = s.peek();
                    s.pop();
                    s.push(x1 + x2);
                    break;
                case '-':
                    flag = false;
                    x1 = s.peek();
                    s.pop();
                    x2 = s.peek();
                    s.pop();
                    s.push(x2 - x1);
                    break;
                case '*':
                    flag = false;
                    x1 = s.peek();
                    s.pop();
                    x2 = s.peek();
                    s.pop();
                    s.push(x1 * x2);
                    break;
                case '/':
                    flag = false;
                    x1 = s.peek();
                    s.pop();
                    x2 = s.peek();
                    s.pop();
                    s.push(x2 / x1);
                    break;
                case ' ':
                    if (flag) {
                        x= Double.valueOf(num.toString());
                        num=new StringBuffer();
                        s.push(x);
                        flag = false;
                    }
                    break;
                default:
                    flag = true;
                   // x = x * 10 + str.charAt(i) - '0';
                    num.append(str.charAt(i));
                    break;
            }
        }

        return new StringBuffer((s.peek() + ""));
    }


    public static void main(String[] args) {
        new Calculator2();
    }
}
