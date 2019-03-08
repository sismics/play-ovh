package helpers.api.ovh.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import helpers.api.ovh.OvhClient;
import okhttp3.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jtremeaux
 */
public class DedicatedOvhService {
    public OvhClient ovhClient;

    public DedicatedOvhService(OvhClient ovhClient) {
        this.ovhClient = ovhClient;
    }

    /**
     * Get the servers.
     *
     * @return The list of service names
     */
    public List<String> getServer() {
        Request request = ovhClient.signRequest(new Request.Builder()
                .url(ovhClient.getUrl("/dedicated/server"))
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
                    throw new RuntimeException("Error getting servers, response was: " + response.body().string());
                });
    }

    /**
     * Get a server.
     *
     * @param serverName The server name
     */
    public void getServer(String serverName) {
        Request request = ovhClient.signRequest(new Request.Builder()
                .url(ovhClient.getUrl("/dedicated/server/" + serverName))
                .get()
                .build());
        ovhClient.execute(request,
                null,
                (response) -> {
                    throw new RuntimeException("Error getting server" + serverName + ", response was: " + response.body().string());
                });
    }
}
