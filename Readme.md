Adding products to the cart:
============================

Request:
========
POST http://localhost:8081/api/cart
[
    {"name":"Dettol", "description": "Hand santi"},
    {"name":"Lizol", "description":"Floor Cleaner"},
    {"name":"Ariel", "description":"Stain remover"}
]

Response:
=========
Http Status: 201