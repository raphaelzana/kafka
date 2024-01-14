package com.example.kafka.demo;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.kafka.utils.DlqPartitionFunction;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class DemoApplication {

    @Autowired
    private StreamBridge streamBridge;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    /*
     * Método listener do binding
     * O nome do método concatenado com "-in-0" é o nome do binding (Exemplo: process-in-0)
     * Para setar manualmente o nome do tópico, criar a propriedade spring.cloud.stream.bindings.process-in-0.destination=myTopicName
     * Caso nao seja criada a propriedade, é utilizado o nome para o tópico "process-in-0"
     */
	@Bean
 	public Consumer<Message<Person>> process() {
    return message -> {
        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        System.out.println(message + " received from partition " + message.getHeaders().get(KafkaHeaders.RECEIVED_PARTITION));
        if (acknowledgment != null) {
                try {
                    System.out.println("Acknowledgment provided");
                    //System.out.println(message.getHeaders().get("deliv").toString());
                    throw new IllegalStateException();
                } catch (NullPointerException e){
                    streamBridge.send("myBusinessDlq", MessageBuilder.withPayload(message.getPayload())
                    .setHeader("partitionKey", message.getPayload())
                    .setHeader("messageKey", UUID.randomUUID().toString())
                    .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
                    .build());
                    acknowledgment.acknowledge();
                }
                //throw new Exception();
            
                System.out.print("Acknowledgment not provided");
                System.out.println(message.getHeaders().get("deliveryAttempt"));
                System.out.println(message.getHeaders().get(KafkaHeaders.RECEIVED_KEY).toString());
                System.out.println(message.getHeaders().get("deliv").toString());
                Person person = message.getPayload();
                System.out.println(person.getName());
                //acknowledgment.nack(java.time.Duration.ofSeconds(20));
                //message.getHeaders().replace("deliveryAttempt", message.getHeaders().get("deliveryAttempt"), Integer.valueOf(message.getHeaders().get("deliveryAttempt").toString())+1);
                
            }
        };
    }


    /*
     * Necessário quando a DLQ possui mais que 1 partição
     * Neste caso está sendo postada na mesma partição que a mensagem foi lida
     */
    @Bean
    public DlqPartitionFunction partitionFunction() {
        return (group, record, ex) -> record.partition();
    }

}
