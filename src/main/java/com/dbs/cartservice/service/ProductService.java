package com.dbs.cartservice.service;

import com.dbs.cartservice.dto.ProductDetails;
import com.google.gson.Gson;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;
import java.util.List;


@Service
public class ProductService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    public List<ProductDetails> addProduct(List<ProductDetails> productDetails) {
        Gson gson = new Gson();
        StreamEx.of(productDetails)
                .forEach(details -> jmsTemplate.convertAndSend(queue, gson.toJson(details)));
        return productDetails;
    }
}
