package com.accessone.Messages;

/**
 * Created by duncan.browne on 06/08/2016.
 */
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQMessageQueue
{
    protected Channel channel;
    protected Connection connection;
    protected String queueName;

    /**
     * RabbitMQMessageQueue constructor creates a connection and a message queue
     * @param queueName
     * @throws IOException
     * @throws TimeoutException
     */
    public RabbitMQMessageQueue(String queueName) throws IOException, TimeoutException
    {
        this.queueName = queueName;

        //Create a connection factory
        ConnectionFactory factory = new ConnectionFactory();

        //hostname of your rabbitmq server
        factory.setHost("localhost");

        //getting a connection
        connection = factory.newConnection();

        //creating a channel
        channel = connection.createChannel();

        //declaring a queue for this channel. If queue does not exist,
        //it will be created on the server.  Make it persistent
        channel.queueDeclare(queueName, true, false, false, null);
    }


    /**
     * Close channel and connection. Not necessary as it happens implicitly any way.
     * @throws IOException
     * @throws TimeoutException
     */
    public void close() throws IOException, TimeoutException
    {
        this.channel.close();
        this.connection.close();
    }
}
