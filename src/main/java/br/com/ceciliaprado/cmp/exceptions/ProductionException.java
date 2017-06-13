/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ceciliaprado.cmp.exceptions;

import org.apache.log4j.Logger;

/**
 *
 * @author adrianohrl
 */
public class ProductionException extends CMPException {
    
    private static final Logger logger = Logger.getLogger(ProductionException.class);

    public ProductionException(String message) {
        this(message, logger);
    }
    
    protected ProductionException(String message, Logger logger) {
        super(message);
        logger.fatal(message);
    }
    
}
