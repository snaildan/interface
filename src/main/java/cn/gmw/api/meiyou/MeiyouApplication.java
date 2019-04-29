package cn.gmw.api.meiyou;

import cn.gmw.api.meiyou.thread.MeiyouThread;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
//在项目启动后自动执行的功能 CommandLineRunner
public class MeiyouApplication implements CommandLineRunner {
    @Resource
    private MeiyouThread meiyouThread;

    public static void main(String[] args) {
        SpringApplication.run(MeiyouApplication.class, args);
    }

    @Override
    public void run(String... args){
        //开启业务线程
        new Thread(meiyouThread).start();
    }
}
