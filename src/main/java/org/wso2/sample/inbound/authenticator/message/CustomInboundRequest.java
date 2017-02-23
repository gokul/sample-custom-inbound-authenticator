package org.wso2.sample.inbound.authenticator.message;

import org.wso2.carbon.identity.application.authentication.framework.inbound.FrameworkClientException;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents a subclass IdentityRequest, which can be used to inject additional properties and parameters
 * from the HTTP request coming from the servlet to the IdentityRequest bound for the authentication framework.
 */
public class CustomInboundRequest extends IdentityRequest {

    private transient HttpServletRequest request;
    private transient HttpServletResponse response;

    protected CustomInboundRequest(IdentityRequestBuilder builder) throws FrameworkClientException {
        super(builder);
        this.request = ((CustomInboundRequestBuilder) builder).request;
        this.response = ((CustomInboundRequestBuilder) builder).response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * The builder for the request class is maintained here as a subclass. The builder is required because once it
     * is built, the IdentityRequest object is treated as immutable within the framework and cannot be used for adding
     * additional custom properties from the HTTP request.
     */
    public static class CustomInboundRequestBuilder extends IdentityRequestBuilder {
        private HttpServletRequest request;
        private HttpServletResponse response;

        public CustomInboundRequestBuilder(HttpServletRequest request, HttpServletResponse response) {
            super(request, response);
            this.request = request;
            this.response = response;
        }

        @Override
        public CustomInboundRequest build() throws FrameworkClientException {
            return new CustomInboundRequest(this);
        }

    }
}
