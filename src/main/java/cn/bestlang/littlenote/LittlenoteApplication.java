package cn.bestlang.littlenote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"cn.bestlang.littlenote.mapper", "cn.bestlang.littlenote.account.mapper"})
@SpringBootApplication
public class LittlenoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(LittlenoteApplication.class, args);
	}

}
