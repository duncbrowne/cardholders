package com.accessone.Messages;


import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

/**
 * Created by duncan.browne on 06/08/2016.
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
