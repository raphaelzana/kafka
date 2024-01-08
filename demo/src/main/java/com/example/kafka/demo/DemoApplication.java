package com.example.kafka.demo;
import java.util.function.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
 	public Consumer<Message<Person>> process() {
    return message -> {
        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        System.out.println(message + " received from partition " + message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION));
        if (acknowledgment != null) {
            
                System.out.println("Acknowledgment provided");
                //acknowledgment.acknowledge();
                //throw new Exception();
            
                System.out.print("Acknowledgment not provided");
                System.out.println(message.getHeaders().get("deliveryAttempt"));
                //System.out.println(message.getHeaders().get("deliv").toString());
                //acknowledgment.nack(java.time.Duration.ofSeconds(5));
                //message.getHeaders().replace("deliveryAttempt", message.getHeaders().get("deliveryAttempt"), Integer.valueOf(message.getHeaders().get("deliveryAttempt").toString())+1);
                
            
        }
    };
}

}
