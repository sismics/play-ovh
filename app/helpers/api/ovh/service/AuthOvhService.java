package helpers.api.ovh.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import helpers.api.ovh.OvhClient;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author jtremeaux
 */
public class AuthOvhService {
    public OvhClient ovhClient;

    public AuthOvhService(OvhClient ovhClient) {
        this.ovhClient = ovhClient;
    }

    /**
     * Generate a consumer key.
     * Or exec:
     * curl -v -X POST -d '{ "accessRules": [ { "method": "GET", "path": "/*" }, { "method": "DELETE", "path": "/*" }, { "method": "POST", "path": "/*" }, { "method": "PUT", "path": "/*" } ]}' -H "X-Ovh-Timestamp: 1502195671" -H "X-Ovh-Application: 12345678" https://eu.api.soyoustart.com/1.0/auth/credential -H "Content-Type: application/json;charset=utf-8"
     *
     * @return The consumer key
     */
    public String postCredential() {
        JsonObject requestBody = new JsonObject();
        JsonArray accessRules = new JsonArray();
        accessRules.add(getAccessRule("GET", "/*"));
        accessRules.add(getAccessRule("POST", "/*"));
        accessRules.add(getAccessRule("PUT", "/*"));
        accessRules.add(getAccessRule("DELETE", "/*"));
        Request request = ovhClient.signRequest(new Request.Builder()
                .url(ovhClient.getUrl("/auth/credential"))
                .post(RequestBody.create(MediaType.parse("application/json"), new GsonBuilder().create().toJson(requestBody)))
                .build());
        return ovhClient.execute(request,
                (response) -> {
                    JsonObject json = new JsonParser().parse(response.body().string()).getAsJsonObject();
                    return json.get("consumerKey").getAsString();
                },
                (response) -> {
                    throw new RuntimeException("Error posting credentials, response was: " + response.body().string());
                });
    }

    private JsonObject getAccessRule(String method, String path) {
        JsonObject accessRule = new JsonObject();
        accessRule.addProperty("method", method);
        accessRule.addProperty("path", path);
        return accessRule;
    }
}
