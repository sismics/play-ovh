package helpers.api.ovh.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import helpers.api.ovh.OvhClient;
import helpers.api.ovh.model.Bill;
import helpers.api.ovh.model.BillDetails;
import helpers.extension.PlayfulJavaExtensions;
import okhttp3.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jtremeaux
 */
public class MeOvhService {
    public OvhClient ovhClient;

    public MeOvhService(OvhClient ovhClient) {
        this.ovhClient = ovhClient;
    }

    /**
     * Get the list of bills.
     *
     * @param dateFrom The date from
     * @return The list of bills
     */
    public List<String> getBill(Date dateFrom) {
        Request request = ovhClient.signRequest(new Request.Builder()
                .url(ovhClient.getUrl("/me/bill?date.from=" + PlayfulJavaExtensions.formatDateTimeIso(dateFrom)))
                .get()
                .build());
        return ovhClient.execute(request,
                (response) -> {
                    JsonArray list = new JsonParser().parse(response.body().string()).getAsJsonArray();
                    List<String> result = new ArrayList<>();
                    for (JsonElement e : list) {
                        result.add(e.getAsString());
                    }
                    return result;
                },
                (response) -> {
                    throw new RuntimeException("Error getting bills, response was: " + response.body().string());
                });
    }

    /**
     * Get the bill info.
     *
     * @param id The Bill ID
     * @return The bill
     */
    public Bill getBill(String id) {
        Request request = ovhClient.signRequest(new Request.Builder()
                .url(ovhClient.getUrl("/me/bill/" + id))
                .get()
                .build());
        return ovhClient.execute(request,
                (response) -> new GsonBuilder().create().fromJson(response.body().string(), Bill.class),
                (response) -> {
                    throw new RuntimeException("Error getting bill: " + id + ", response was: " + response.body().string());
                });
    }

    /**
     * Get the bill PDF.
     *
     * @param pdfUrl The PDF URL
     * @return The bill PDF
     */
    public byte[] getBillPdf(String pdfUrl) {
        Request request = new Request.Builder()
                .url(pdfUrl)
                .get()
                .build();
        return ovhClient.execute(request,
                (response) -> response.body().bytes(),
                (response) -> {
                    throw new RuntimeException("Error getting PDF: " + pdfUrl + ", response was: " + response.body().string());
                });
    }

    /**
     * Get the bill details.
     *
     * @param id The Bill ID
     * @return The list of bill detail IDs
     */
    public List<String> getBillDetails(String id) {
        Request request = ovhClient.signRequest(new Request.Builder()
                .url(ovhClient.getUrl("/me/bill/" + id + "/details"))
                .get()
                .build());
        return ovhClient.execute(request,
                (response) -> {
                    JsonArray list = new JsonParser().parse(response.body().string()).getAsJsonArray();
                    List<String> result = new ArrayList<>();
                    for (JsonElement e : list) {
                        result.add(e.getAsString());
                    }
                    return result;
                },
                (response) -> {
                    throw new RuntimeException("Error getting bill details: " + id + ", response was: " + response.body().string());
                });
    }

    /**
     * Get the bill details.
     *
     * @param billId The bill ID
     * @param billDetailsId The bill details ID
     * @return The bill details
     */
    public BillDetails getBillDetails(String billId, String billDetailsId) {
        Request request = ovhClient.signRequest(new Request.Builder()
                .url(ovhClient.getUrl("/me/bill/" + billId + "/details/" + billDetailsId))
                .get()
                .build());
        return ovhClient.execute(request,
                (response) -> new GsonBuilder().create().fromJson(response.body().string(), BillDetails.class),
                (response) -> {
                    throw new RuntimeException("Error getting bill details: " + billDetailsId + ", response was: " + response.body().string());
                });
    }
}
