package helpers.api.ovh.model;

import java.util.Date;

/**
 * @author jtremeaux
 */
public class BillDetails {
    public CurrencyAmount totalPrice;

    public Date periodStart;

    public Double quantity;

    public CurrencyAmount unitPrice;

    public String description;
    
    public String billDetailId;
    
    public String domain;
    
    public Date periodEnd;
}
