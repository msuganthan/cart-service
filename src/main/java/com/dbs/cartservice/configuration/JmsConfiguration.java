package com.dbs.cartservice.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;
import javax.jms.Session;

@EnableJms
@Configuration
public class JmsConfiguration {

    @Value("${shopping-cart.queue.name}")
    private String shoppingCartQueueName;

    @Bean
    public Queue queue() {
        return new ActiveMQQueue(shoppingCartQueueName);
    }

    @Bean
    public Queue reDeliveryQueue() {
        return new ActiveMQQueue("QUEUE_REDELIVERY");
    }

    /**
     * A redelivery policy comes into play as soon as the delivery of a message fails.
     * ActiveMQ will automatically try to deliver the message again.
     * The number of retries as well as the time period between every redelivery can be configured.
     * @return connectionFactory;
     */
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(Queue reDeliveryQueue) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        /**
         * Configuration options for a messageConsumer used to control how messages are re-delivered when they
         * are rolled back.
         */
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setInitialRedeliveryDelay(1000L); //is the early first redelivery delay. It’s the “startup” delay of the backoff policy. By default it’s 1000L, meaning 1 second
        redeliveryPolicy.setRedeliveryDelay(1000L);
        redeliveryPolicy.setUseExponentialBackOff(true);  //enables an exponential backoff. It’s disabled by default
        redeliveryPolicy.setMaximumRedeliveries(1); //is the max delay we can have. By default, it’s -1, meaning there’s not max

        RedeliveryPolicyMap rdMap = connectionFactory.getRedeliveryPolicyMap();
        rdMap.put((ActiveMQDestination) reDeliveryQueue(), redeliveryPolicy);
        connectionFactory.setRedeliveryPolicyMap(rdMap);

        return connectionFactory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsTransactionalContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory(reDeliveryQueue()));
        factory.setSessionTransacted(true);
        /**
         * With this acknowledgment mode, the client acknowledges a consumed message by calling the message's method. Acknowledging a consumed message acknowledges all messages that the session has consumed.
         */
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setErrorHandler(throwable -> {
            //do nothing
        });
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(activeMQConnectionFactory(reDeliveryQueue()));
        return template;
    }
}
