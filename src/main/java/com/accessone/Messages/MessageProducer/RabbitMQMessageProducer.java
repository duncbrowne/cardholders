package com.accessone.Messages.MessageProducer;

import com.accessone.Messages.RabbitMQEventMessageSender;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Current Project cardholders
 * Created by duncan.browne on 08/08/2016.
 */
public class RabbitMQMessageProducer
{
    private static RabbitMQMessageProducer ourInstance = new RabbitMQMessageProducer();

    public static RabbitMQMessageProducer getInstance()
    {
        return ourInstance;
    }

    private RabbitMQEventMessageSender producer;
    private RabbitMQMessageProducer()
    {
        createMsgSender();
    }

    public void createMsgSender()
    {
        try
        {
            // Setup the RabbitMQ Event message sender
            producer = new RabbitMQEventMessageSender("response");
        }
        catch (IOException e)
        {
            System.err.println("createMsgSender IOException " + e.toString());
        }
        catch (TimeoutException e)
        {
            System.err.println("createMsgSender TimeoutException " + e.toString());
        }
    }

    public void sendMsg(String strMessage)
    {
        try
        {
            HashMap message = new HashMap();
            message.put("body", strMessage);
            producer.sendMessage(message);
            System.out.println("Message sent : " + strMessage);
        }
        catch (IOException e)
        {
            System.err.println("sendMsg IOException " + e.toString());
        }
    }
}
