package com.accessone;

/**
 * Hello world!
 *
 */

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.HashMap;

import com.accessone.Messages.RabbitMQEventMessageSender;
import com.accessone.Messages.RabbitMQRequestMessageReceiver;

public class App 
{

    public static void main( String[] args ) throws IOException, TimeoutException
    {
        RabbitMQRequestMessageReceiver consumer = new RabbitMQRequestMessageReceiver("queue");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        RabbitMQEventMessageSender producer = new RabbitMQEventMessageSender("queue");

        for (int i = 0; i < 100000; i++) {
            HashMap message = new HashMap();
            message.put("message number", i);
            producer.sendMessage(message);
            System.out.println("Message Number "+ i +" sent.");
        }

    }
}
