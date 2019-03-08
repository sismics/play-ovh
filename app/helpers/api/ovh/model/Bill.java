package helpers.api.ovh.model;

import java.util.Date;

/**
 * @author jtremeaux
 */
public class Bill {
    public String pdfUrl;
    
    public Date date;
    
    public CurrencyAmount priceWithoutTax;
    
    public CurrencyAmount tax;
    
    public CurrencyAmount priceWithTax;
    
    public String billId;
    
    public String password;
    
    public String orderId;
    
    public String url;
}
