package com.accessone;

/**
 * Hello world!
 *
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import java.util.HashMap;

import com.accessone.Database.MariaDBFacade;
import com.accessone.Messages.RabbitMQEventMessageSender;
import com.accessone.Messages.RabbitMQRequestMessageReceiver;

public class App 
{

    public static void main( String[] args ) throws IOException, TimeoutException, SQLException
    {
        MariaDBFacade dbFacade = new MariaDBFacade();
        dbFacade.GetCardholderRecord(1, null);
        dbFacade.GetCardholderRecord(4, null);

         // Set up the RabbitMQ Request message receiver
        RabbitMQRequestMessageReceiver consumer = new RabbitMQRequestMessageReceiver("queue");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        // Setup the RabbitMQ Event message sender
        RabbitMQEventMessageSender producer = new RabbitMQEventMessageSender("queue");

        for (int i = 0; i < 10; i++) {
            HashMap message = new HashMap();
            message.put("message number", i);
            producer.sendMessage(message);
            System.out.println("Message Number "+ i +" sent.");
        }

    }
}
