package com.accessone.Messages;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.HashMap;
import java.util.Map;

import com.accessone.Messages.Decomposer.MessageDecomposer;
import com.accessone.Messages.MessageProducer.RabbitMQMessageProducer;
import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Created by duncan.browne on 06/08/2016.
 */

public class RabbitMQRequestMessageReceiver extends RabbitMQMessageQueue implements Runnable, Consumer
{
    public RabbitMQRequestMessageReceiver(String queueName) throws IOException, TimeoutException
    {
        super(queueName);
    }

    public void run()
    {
        try {
            //start consuming messages. Auto acknowledge messages.
            channel.basicConsume(queueName, true,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when consumer is registered.
     */
    public void handleConsumeOk(String consumerTag)
    {
        System.out.println("Consumer "+consumerTag +" registered");
    }

    /**
     * Called when new message is available.
     */
    public void handleDelivery(String consumerTag, Envelope env,
                               BasicProperties props, byte[] body) throws IOException
    {
        Map map = (HashMap)SerializationUtils.deserialize(body);
        System.out.println("Message Number "+ map.get("message number") + " received.");
        String strBody = map.get("body").toString();
        if (strBody.isEmpty())
        {
            System.err.println("Body is empty");
            return;
        }

        String strReplyMessage = MessageDecomposer.getInstance().decomposeMessage(strBody);

        // Send the reply message
        RabbitMQMessageProducer.getInstance().sendMsg(strReplyMessage);
    }

    public void handleCancel(String consumerTag) {}
    public void handleCancelOk(String consumerTag) {}
    public void handleRecoverOk(String consumerTag) {}
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {}
}