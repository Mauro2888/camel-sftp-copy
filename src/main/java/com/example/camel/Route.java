package com.example.camel;


import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Route extends RouteBuilder {
    private final Processor setFileName = exchange -> {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String filename = "Input#" + timeStamp + "_#" + ".txt";
        exchange.getIn().setHeader("CamelFilename",filename);
    };


    @Override
    public void configure() throws Exception {
        //disabilitare i know hosts &useUserKnownHostsFile=false
        from("sftp://demo@test.rebex.net:22/?password=dev")
                .choice()
                .when(header("CamelFilename").endsWith(".txt"))
                .process(setFileName)
                .log("msg: ${body}")
                .to("file:C:/test/output");
    }
}
