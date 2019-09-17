package com.olrox.chat;

import com.olrox.chat.controller.nio.Reactor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class WebsocketDemoApplication {

	private final static int PORT = 50000;
	private final static Logger LOGGER = LogManager.getLogger(NioServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebsocketDemoApplication.class, args);

//		LOGGER.info("Server Application is starting.");
//		Reactor reactor = null;
//		try {
//			reactor = new Reactor(PORT, true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		new Thread(reactor).start();
	}
}
