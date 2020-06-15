package com.dbs.cartservice.service.consumer;

import com.dbs.cartservice.db.repo.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartConsumerTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ShoppingCartConsumer shoppingCartConsumer;
    
    @Test
    public void shouldAcknowledgeForTheMessage() throws JMSException {
        Message message = Mockito.mock(TextMessage.class);
        Mockito.when(((TextMessage)message).getText()).thenReturn("{\"name\":\"Dettol\", \"description\": \"Hand santi\"}");

        Session session = Mockito.mock(Session.class);
        shoppingCartConsumer.shoppingCartQueueListener(message, session);

        Mockito.verify(message, Mockito.times(1)).acknowledge();
    }
}
