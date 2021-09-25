package Server.Http.Response;

import Server.Http.Request.HttpRequestBuilder;

import java.util.Collection;

public interface HttpResponseBuilder {

    /**
     * @param s is "HTTP:$Version"
     */
    default HttpResponseBuilder setVersion(String s) {
        return this;
    }

    default HttpResponseBuilder setStatus(String s){
        return this;
    }

    /**
     * @param s pair header: value
     */
    default HttpResponseBuilder addHeader(String s) {
        return this;
    }

    /**
     * @param collection of strings (header: value)
     */
    default HttpResponseBuilder addHeader(Collection<String> collection) {
        return this;
    }

    default HttpResponseBuilder setBody(String s) {
        return this;
    }


}
