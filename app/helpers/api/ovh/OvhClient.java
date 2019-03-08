package helpers.api.ovh;

import com.sismics.sapparot.function.CheckedConsumer;
import com.sismics.sapparot.function.CheckedFunction;
import com.sismics.sapparot.okhttp.OkHttpHelper;
import helpers.api.ovh.service.DedicatedOvhService;
import helpers.api.ovh.service.MeOvhService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import play.Play;
import play.libs.Codec;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * @author jtremeaux
 */
public class OvhClient {
    private static Map<Product, OvhClient> ovhClient = new HashMap<>();

    private Product product;

    private OkHttpClient client;

    private DedicatedOvhService dedicatedService;

    private MeOvhService meService;

    private String consumerKey;

    public enum Product {
        OVH,
        SOYOUSTART,
        KIMSUFI,
    }

    public static OvhClient get(Product product) {
        OvhClient ovhClient = OvhClient.ovhClient.get(product);
        if (ovhClient == null) {
            ovhClient = new OvhClient(product);
            OvhClient.ovhClient.put(product, ovhClient);
        }
        return ovhClient;
    }

    public OvhClient(Product product) {
        this.product = product;
        client = createClient();
        if (isMock()) {
            dedicatedService = mock(DedicatedOvhService.class);
            meService = mock(MeOvhService.class);
        } else {
            dedicatedService = new DedicatedOvhService(this);
            meService = new MeOvhService(this);
        }
    }

    private boolean isMock() {
        return Boolean.parseBoolean(Play.configuration.getProperty( product.name().toLowerCase() + ".mock", "false"));
    }

    private static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    public String getOvhUrl() {
        return Play.configuration.getProperty(product.name().toLowerCase() + ".url") + "/1.0";
    }

    public String getOvhAppKey() {
        return Play.configuration.getProperty(product.name().toLowerCase() + ".appKey");
    }

    public String getOvhAppSecret() {
        return Play.configuration.getProperty(product.name().toLowerCase() + ".appSecret");
    }

    public String getOvhConsumerKey() {
        if (consumerKey == null) {
            consumerKey = Play.configuration.getProperty(product.name().toLowerCase() + ".consumerKey");
        }
        return consumerKey;
    }

    public String getUrl(String url) {
        return getOvhUrl() + url;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public DedicatedOvhService getDedicatedService() {
        return dedicatedService;
    }

    public MeOvhService getMeService() {
        return meService;
    }

    public Request signRequest(Request request) {
        Long timestamp = new Date().getTime() / 1000;
        String body = request.body() == null ? "" : request.body().toString();
        String preHashSignature = getOvhAppSecret() + "+" + getOvhConsumerKey() + "+" + request.method() + "+" + request.url() + "+" + body + "+" + timestamp;
        String hash = "$1$" + Codec.hexSHA1(preHashSignature);
        return request.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("X-Ovh-Application", getOvhAppKey())
                .addHeader("X-Ovh-Timestamp", timestamp.toString())
                .addHeader("X-Ovh-Signature", hash)
                .addHeader("X-Ovh-Consumer", getOvhConsumerKey())
                .build();
    }

    public <T> T execute(Request request, CheckedFunction<Response, T> onSuccess, CheckedConsumer<Response> onFailure) {
        return OkHttpHelper.execute(getClient(), request, onSuccess, onFailure);
    }
}
