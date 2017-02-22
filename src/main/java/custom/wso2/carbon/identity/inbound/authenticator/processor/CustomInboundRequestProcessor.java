package custom.wso2.carbon.identity.inbound.authenticator.processor;

import custom.wso2.carbon.identity.inbound.authenticator.message.CustomInboundRequest;
import custom.wso2.carbon.identity.inbound.authenticator.message.CustomInboundResponse;
import custom.wso2.carbon.identity.inbound.authenticator.util.CustomInboundAuthConfig;
import custom.wso2.carbon.identity.inbound.authenticator.util.CustomInboundConstants;
import custom.wso2.carbon.identity.inbound.authenticator.util.CustomInboundUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.application.authentication.framework.exception.FrameworkException;
import org.wso2.carbon.identity.application.authentication.framework.inbound.*;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticationResult;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The processor class represents the core functionality od the inbound authenticator. Being a subclass of the
 * IdentityProcessor class, the developer is required to override certain methods which dictate the functional
 * elements.
 *
 * In the process method, the developer is able to leverage standard methods offered by the framework to send the
 * request on to the framework (buildResponseForFrameworkLogin()) as well as handle the response after going through
 * the authentication step (processResponseFromFrameworkLogin()).
 *
 * The getRelyingPartyId() method is used for correlating the protocol of the incoming authentication request
 * (i.e. the protocol represented by this processor) against a particular SP in the Identity Server which contains the
 * information related to the actual authentication, either through local and/or federated authenticators.
 */
public class CustomInboundRequestProcessor extends IdentityProcessor {

    private CustomInboundAuthConfig customInboundAuthConfig = null;

    private String relyingParty;

    public CustomInboundRequestProcessor(CustomInboundAuthConfig customInboundAuthConfig) {
        this.customInboundAuthConfig = customInboundAuthConfig;
    }

    /**
     * This method represents the bulk of the functionality, where the developer chooses what should take place when
     * the authentication request reaches the processor. In this instance, a check is done first to determine if the
     * request is coming new from the /identity servlet.
     * @param identityRequest the request object (or a subclass of it), which can be coming either from the /identity
     *                        servlet or from the framework after authentication)
     * @return an instance of IdentityResponse which may be further customised (similar to how an IdentityRequest can
     * be customised)
     * @throws FrameworkException if any abnormal conditions are encountered by the framework during authentication.
     */
    public IdentityResponse.IdentityResponseBuilder process(IdentityRequest identityRequest) throws FrameworkException {

        IdentityMessageContext messageContext = new IdentityMessageContext<>(identityRequest, new HashMap<String, String>());
        CustomInboundResponse.CustomInboundResponseBuilder respBuilder =
                new CustomInboundResponse.CustomInboundResponseBuilder(messageContext);

        String sessionId = identityRequest.getParameter(InboundConstants.RequestProcessor.CONTEXT_KEY);
        if (sessionId != null) {
            //A session already exists, which means that this is call is coming from the framework after authentication.
            AuthenticationResult authenticationResult = processResponseFromFrameworkLogin(messageContext, identityRequest);
            if (authenticationResult != null && authenticationResult.isAuthenticated()) {
                String userName = authenticationResult.getSubject().getUserName();
                respBuilder.setToken(generateSampleToken(userName));

            } else {
                //Alternatively, use Error builder to handle non-authenticated scenario
                respBuilder.setToken(null);
            }
            respBuilder.setRedirectUrl(getPropertyValue(identityRequest, CustomInboundConstants.REDIRECT_URL));
            return respBuilder;
        } else {
            //No session exists, so we will need to send the request through to the identity framework.
            return buildResponseForFrameworkLogin(messageContext);
        }
    }

    @Override
    public String getCallbackPath(IdentityMessageContext identityMessageContext) {
        return IdentityUtil.getServerURL("identity", false, false);
    }

    @Override
    public String getRelyingPartyId() {
        return this.relyingParty;
    }

    @Override
    public String getRelyingPartyId(IdentityMessageContext identityMessageContext) {
        return this.relyingParty;
    }

    @Override
    public boolean canHandle(IdentityRequest identityRequest) {
        if (identityRequest instanceof CustomInboundRequest) {
            this.relyingParty = ((CustomInboundRequest) identityRequest).
                    getRequest().getParameter(CustomInboundConstants.SP_ID);
        } else if (StringUtils.isNotBlank(
                identityRequest.getParameter(CustomInboundConstants.SP_ID))) {
            this.relyingParty =
                    identityRequest.getParameter(CustomInboundConstants.SP_ID);
        }
        return StringUtils.isNotBlank(this.relyingParty);
    }

    @Override
    public String getName() {
        return "sample-custom-inbound-type";
    }

    private String getPropertyValue(IdentityRequest request, String property) {
        String propertyValue = null;
        Map<String, Property> props = getInboundAuthenticatorPropertyArray(request);
        for (Object obj : props.entrySet()) {
            Map.Entry pair = (Map.Entry) obj;
            if (property.equals(pair.getKey())) {
                Property prop = (Property) pair.getValue();
                propertyValue = prop.getValue();
            }
        }
        return propertyValue;
    }

    /**
     * This method is used for retrieving the values for the properties from {@link CustomInboundAuthConfig} which will
     * have been set in the Identity Server's Service Provider settings.
     * @param request the identity request object
     * @return a map of available properties and their values
     */
    private Map<String, Property> getInboundAuthenticatorPropertyArray(IdentityRequest request) {
        try {
            ApplicationManagementService appInfo = ApplicationManagementService.getInstance();
            ServiceProvider application = appInfo.getServiceProviderByClientId(this.relyingParty, this.getName(), request.getTenantDomain());
            Map<String, Property> properties = new HashMap<>();
            for (InboundAuthenticationRequestConfig authenticationRequestConfig : application
                    .getInboundAuthenticationConfig().getInboundAuthenticationRequestConfigs()) {
                if (StringUtils.equals(authenticationRequestConfig.getInboundAuthType(), getName())
                        && StringUtils.equals(authenticationRequestConfig.getInboundAuthKey(), relyingParty)) {
                    for (Property property : authenticationRequestConfig.getProperties()) {
                        properties.put(property.getName(), property);
                    }
                }
            }
            return properties;
        } catch (IdentityApplicationManagementException e) {
            throw new RuntimeException("Error while reading inbound authenticator properties");
        }
    }

    /** This method is used to generate a sample token for the sample authenticator use-case
     * @param param1 the String value for which a token is generated
     * @return the Base 64 encoded input
     */
    private String generateSampleToken(String param1) {
        return CustomInboundUtil.newString(Base64.encodeBase64(param1.getBytes()), StandardCharsets.UTF_8);
    }

}