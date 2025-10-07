package com.liy;

import com.liy.im.WebSocketChanneInitializer;
import com.liy.im.WebSocketConstant;
import com.liy.im.WebSocketInfoService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * springboot启动类
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.liy.mapper"})
//@EsMapperScan("com.liy.mapper")
@ServletComponentScan
@EnableAsync
@EnableFileStorage
public class Application {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        System.out.println("\n----------------------------------------------------------\n\t" +
                "blog is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "Knife4j-ui: \thttp://" + ip + ":" + port + path + "/doc.html\n\t" +
                "----------------------------------------------------------");
        startNettyMsgServer();

    }

    private static void startNettyMsgServer() {
        // 使用多Reactor多线程模型，EventLoopGroup相当于线程池，内部维护一个或多个线程（EventLoop），每个EventLoop可处理多个Channel（单线程处理多个IO任务）
        // 创建主线程组EventLoopGroup，专门负责建立连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 创建子线程组，专门负责IO任务的处理
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new WebSocketChanneInitializer());
            System.out.println("服务端开启等待客户端连接....");
            Channel ch = b.bind(WebSocketConstant.WEB_SOCKET_PORT).sync().channel();

            //创建一个定长线程池，支持定时及周期性任务执行
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
            WebSocketInfoService webSocketInfoService = new WebSocketInfoService();
            //定时任务:扫描所有的Channel，关闭失效的Channel
            executorService.scheduleAtFixedRate(webSocketInfoService::scanNotActiveChannel,
                    3, 60, TimeUnit.SECONDS);

            //定时任务:向所有客户端发送Ping消息
            executorService.scheduleAtFixedRate(webSocketInfoService::sendPing,
                    3, 10, TimeUnit.SECONDS);

            ch.closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            //退出程序
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
