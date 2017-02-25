package com.cess.gargotte.log;

import com.cess.gargotte.core.model.sales.PaymentMethod;
import com.cess.gargotte.core.model.sales.Sale;

/**
 * Created by Guillaume on 21/02/2017.
 */
public interface ISaleLogSyntax {
    String applySyntax(Sale sale, PaymentMethod paymentMethod);
}
