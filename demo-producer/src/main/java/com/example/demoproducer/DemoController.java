package com.example.demoproducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
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
    public void publish(@RequestBody String body){
        streamBridge.send("myFirstTopic", body);
    }
    
    
}
