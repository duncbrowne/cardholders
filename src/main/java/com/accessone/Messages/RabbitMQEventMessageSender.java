package com.accessone.Messages;


import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

/**
 * Class for sending Event RabbitMQ messages
 */
public class RabbitMQEventMessageSender extends RabbitMQMessageQueue
{
    public RabbitMQEventMessageSender(String queueName) throws IOException, TimeoutException
    {
        super(queueName);
    }

    public void sendMessage(Serializable object) throws IOException
    {
        channel.basicPublish("",queueName, null, SerializationUtils.serialize(object));
    }
}
