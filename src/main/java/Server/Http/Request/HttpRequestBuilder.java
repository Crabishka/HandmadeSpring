package Server.Http.Request;

import java.util.Collection;

public interface HttpRequestBuilder {

    /**
     * @param type is type of request
     */
    default HttpRequestBuilder setRequestType(RequestType type) {
        return this;
    }

    /**
     * @param s is path excluded host
     */
    default HttpRequestBuilder setPath(String s) {
        return this;
    }

    /**
     * @param s is Host
     */
    default HttpRequestBuilder setHost(String s) {
        return this;
    }

    /**
     * @param s is "?$Parameters"
     */
    default HttpRequestBuilder setParameters(String s) {
        return this;
    }

    /**
     * @param s is "HTTP:$Version"
     */
    default HttpRequestBuilder setVersion(String s) {
        return this;
    }

    /**
     * @param s pair header: value
     */
    default HttpRequestBuilder setHeader(String s) {
        return this;
    }

    /**
     * @param collection of strings (header: value)
     */
    default HttpRequestBuilder setHeader(Collection<String> collection) {
        return this;
    }

    /**
     * @param s is anything you want
     */
    default HttpRequestBuilder setBody(String s) {
        return this;
    }



    default HttpRequest build() {
        return null;
    }


}
