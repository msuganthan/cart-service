package com.dbs.cartservice.service.consumer;

import com.dbs.cartservice.db.repo.ProductRepo;
import com.dbs.cartservice.dto.ProductDetails;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.transaction.Transactional;

@Component
public class ShoppingCartConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartConsumer.class);

    @Autowired
    private ProductRepo productRepo;

    /**
     * JmsListener is gonna create connection factory for the destination queue.
     */
    @JmsListener(destination = "${shopping-cart.queue.name}", containerFactory = "jmsTransactionalContainerFactory")
    @Transactional
    public void shoppingCartQueueListener(Message message, Session session) throws JMSException {
        logger.info("Received product details {} ", ((TextMessage)message).getText());

        try {
            Gson gson                     = new Gson();
            ProductDetails productDetails = gson.fromJson(((TextMessage)message).getText(), ProductDetails.class);
            productRepo.save(productDetails.toProduct());
            message.acknowledge();
        } catch (Exception e) {
            logger.error("Problems for consuming message from queue {}.",e.getStackTrace());
            session.rollback();
        }
    }

}
