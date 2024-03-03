package com.embedcraft.embedcraftcore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.embedcraft.embedcraftcore.mapper") // Mapper package path
public class EmbedcraftCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmbedcraftCoreApplication.class, args);
	}

}
