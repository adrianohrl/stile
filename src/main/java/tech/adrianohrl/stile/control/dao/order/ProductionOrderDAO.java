/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.adrianohrl.stile.control.dao.order;

import tech.adrianohrl.stile.control.dao.DAO;
import tech.adrianohrl.stile.model.order.ProductionOrder;
import javax.persistence.EntityManager;

/**
 *
 * @author adrianohrl
 */
public class ProductionOrderDAO extends DAO<ProductionOrder, String> {

    public ProductionOrderDAO(EntityManager em) {
        super(em, ProductionOrder.class);
    }

    @Override
    public boolean isRegistered(ProductionOrder entity) {
        return super.find(entity.getReference()) != null;
    }
    
}