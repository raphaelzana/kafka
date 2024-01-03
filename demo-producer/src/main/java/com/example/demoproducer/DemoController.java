package com.example.demoproducer;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DemoController {

    @Autowired
    private StreamBridge streamBridge;

    public DemoController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @RequestMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void publish(@RequestBody Person body){        
        streamBridge.send("myPartitionedTopic", MessageBuilder.withPayload(body)
                    .setHeader("partitionKey", body)
                    .build());
    }    
}
