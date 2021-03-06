/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.adrianohrl.stile.model.production;

import tech.adrianohrl.stile.model.order.ProductionOrder;
import tech.adrianohrl.stile.model.order.VariantOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrianohrl
 */
public class DataBeanList {

    List<DataBean> getDataBeanList(ProductionOrder order) {
        List<DataBean> list = new ArrayList<>();
        Model model = order.getModel();
        for (ModelPart part : model.getParts()) {
            for (ChartSize size: model.getChart()) {
                List<SubDataBean> variants = new ArrayList<>();
                for (VariantOrder variant : order.getVariantOrders()) {
                    int quantity = variant.getQuantity(size);
                    if (quantity > 0)
                    {
                        variants.add(new SubDataBean(variant.getVariant().getName(), quantity));
                    }
                }    
                if (!variants.isEmpty())
                {
                    list.add(new DataBean(part.getName(), size.getName(), part.getProgram(), part.getObservation(), variants));
                }
            }
        }
        return list;
    }
    
}
