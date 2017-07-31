package com.mercadopago.resources.datastructures.payment;

/**
 * Mercado Pago SDK
 * Source class
 *
 * Created by Eduardo Paoletta on 12/2/16.
 */
public class Source {

    private String id = null;
    private String name = null;
    private Type type = null;
    public enum Type {
        collector,
        operator,
        admin,
        bpp
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }


}
