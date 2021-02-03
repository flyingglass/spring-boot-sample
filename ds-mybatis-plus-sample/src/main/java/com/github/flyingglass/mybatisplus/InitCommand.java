package com.github.flyingglass.mybatisplus;

import com.github.flyingglass.mybatisplus.config.MyNamedThreadFactory;
import com.github.flyingglass.mybatisplus.service.IDataSourceCheckService;
import com.github.flyingglass.mybatisplus.service.UserInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fly
 */
@Slf4j
@Component
public class InitCommand implements CommandLineRunner {

    /**
     * DB check executor
     */
    private final ThreadPoolExecutor dbCheckExecutor =  new ThreadPoolExecutor(
            2, 4, 0, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1),
            new MyNamedThreadFactory("db-check"),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );


    @Resource
    UserInfoServiceImpl service;

    @Resource
    IDataSourceCheckService checkService;

    @Override
    public void run(String... args) throws Exception {
        checkService.check();

    }

    @Scheduled(cron = "0/10 * * * * ?")
    private void dbCheck() {
        try {
            dbCheckExecutor.execute(() -> checkService.check());
        } catch (Exception ignore) {

        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    private void doSelect() {
//        log.info("doSelect====");
    }
}
