package clojarmeria.support;

import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpParameters;
import com.linecorp.armeria.common.MediaType;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FormParams {

    public static HttpParameters formParams(AggregatedHttpRequest aggregatedHttpRequest, MediaType mediaType) {
        Charset charset = mediaType.charset().orElse(StandardCharsets.US_ASCII);
        String body = aggregatedHttpRequest.content(charset);
        return HttpParameters.copyOf(new QueryStringDecoder(body, false).parameters());
    }
}
