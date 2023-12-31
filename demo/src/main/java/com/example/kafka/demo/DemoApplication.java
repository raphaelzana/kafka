package com.example.kafka.demo;
import java.util.function.Consumer;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public Consumer<KStream<Object, String>> process(){
		return input -> {
			input.peek(((key, value) -> System.out.println(" value: "+value.toString())));
			
		};
	}

}
