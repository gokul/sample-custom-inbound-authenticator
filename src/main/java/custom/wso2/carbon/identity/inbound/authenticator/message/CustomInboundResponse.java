package custom.wso2.carbon.identity.inbound.authenticator.message;

import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityMessageContext;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityResponse;

/**
 * Similar to the class for the custom request, this class represents a subclass of IdentityResponse, which results from
 * the Identity Framework after the authentication steps(s).
 */
public class CustomInboundResponse extends IdentityResponse {

    private String redirectUrl;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    protected CustomInboundResponse(IdentityResponseBuilder builder) {
        super(builder);
        this.redirectUrl = ((CustomInboundResponseBuilder) builder).redirectUrl;
        this.token = ((CustomInboundResponseBuilder) builder).token;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * Here also, the builder class for the IdentityResponse subclass can be found within the class itself.
     * Here, the parameters found in the HTTP response can be picked up and set to the response object as per the need.
     */
    public static class CustomInboundResponseBuilder extends IdentityResponseBuilder {

        private String redirectUrl;
        private String token;

        public CustomInboundResponseBuilder(IdentityMessageContext context) {
            super(context);
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public CustomInboundResponse build() {
            return new CustomInboundResponse(this);
        }
    }
}
