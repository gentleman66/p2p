package com.bd.p2p.system;

import com.alibaba.dubbo.common.Constants;
import common.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * ClassName:JCaptcha
 * Package:com.bd.p2p.system
 * Description: 描述信息
 *
 * @date:2020/12/22 12:16
 * @author:动力节点
 */
@Controller
public class JCaptchaController {
    public static final int WIDTH = 120;
    /**
     * 图片高度：50px
     */
    public static final int HEIGHT = 50;

    /**
     * 处理图片验证码方法
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/jcaptcha/captcha")
    public void handleCaptchaRequest(HttpServletRequest request, HttpServletResponse response) {
        //生成 6 位随机验证码
        String captcha = this.getRandomCode(3);
        try {
            //创建字节数组输出流
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            //创建图片缓存对象,BufferedImage.TYPE_INT_RGB ： 表示一个图像，该图像具有整数像素的 8 位 RGB 颜色
            BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            //获取图片的画布
            Graphics graphics = bufferedImage.getGraphics();
            //设置画布背景色
            graphics.setColor(Color.GREEN);
            //设置画布填充区域
            graphics.fillRect(0, 0, WIDTH, HEIGHT);
            //边框区域
            graphics.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
            //设置字体颜色
            graphics.setColor(Color.BLACK);
            //设置字体样式
            graphics.setFont(new Font("微软雅黑", Font.ITALIC,
                    32));
            //填充数据
            graphics.drawString(captcha, 10, 38);
            //将生成的验证码存放到 session 中

            request.getSession().setAttribute(Constant.CAPTCHA, captcha);
            ImageIO.write(bufferedImage, "jpeg", jpegOutputStream);
            byte[] captchaChallengeAsJpeg =
                    jpegOutputStream.toByteArray();
            //将验证码输出到页面
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("image/jpeg");
            ServletOutputStream respOs = response.getOutputStream();
            respOs.write(captchaChallengeAsJpeg);
            respOs.flush();
            respOs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成 0-9，a-z,A-Z 的随机字符串
     *
     * @param count
     * @return
     */
    private String getRandomCode(int count) {
        String[] array =
                {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                        "b", " c"," d"," e"," f"," g"," h"," i"," j"," k"," l"," m"
                        ," n"," o"," p"," q "," r"," s"," t"," u"," v"," w"," x"," y","" +
                        " z","0","1","2","3","4"
                        , "5", "6", "7", "8", "9"};
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            //生成一个下标 0-61 之间一个整数
            int index = (int) Math.round(Math.random() * 61);
            //从 array 数组中获取该下标字符放到 result 中
            result.append(array[index]);
        }
        return result.toString();
    }
}
