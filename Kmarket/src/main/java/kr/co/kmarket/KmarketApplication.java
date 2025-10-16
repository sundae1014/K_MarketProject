package kr.co.kmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //스케줄러 -> 하루 지나면 db 상태 업데이트(채용 기간 마감 시 상태 '종료'로 업데이트용)
@SpringBootApplication
public class KmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmarketApplication.class, args);
    }

}
