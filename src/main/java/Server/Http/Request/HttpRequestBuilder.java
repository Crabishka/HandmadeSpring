package Server.Http.Request;

import java.util.Collection;

public interface HttpRequestBuilder {

    /**
     *
     * @param type is type of request
     */
    default void setRequestType(RequestType type) {

    }

    /**
     * @param s is path excluded host
     */
    default void setPath(String s) {

    }

    /**
     * @param s is Host
     */
    default void setHost(String s) {

    }

    /**
     * @param s is "?$Parameters"
     */
    default void setParameters(String s) {

    }

    /**
     * @param s is "HTTP:$Version"
     */
    default void setVersion(String s) {

    }

    /**
     * @param s pair header: value
     */
    default void setHeader(String s) {

    }

    /**
     * @param collection of strings (header: value)
     */
    default void setHeader(Collection<String> collection) {

    }

    /**
     * @param s is anything you want
     */
    default void setBody(String s) {

    }




}
