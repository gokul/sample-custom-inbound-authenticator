package custom.wso2.carbon.identity.inbound.authenticator.util;

import org.wso2.carbon.identity.application.common.model.InboundProvisioningConnector;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.mgt.AbstractInboundAuthenticatorConfig;

/**
 * This class is a subclass of {@link AbstractInboundAuthenticatorConfig} which can be used for populating the GUI
 * elements for the custom inbound authenticator in the Identity Server's SP configuration
 */
public class CustomInboundAuthConfig extends AbstractInboundAuthenticatorConfig
        implements InboundProvisioningConnector {

    private static final String NAME = "sample-custom-inbound-type";

    public CustomInboundAuthConfig() {

    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getConfigName() {
        return NAME;
    }


    @Override
    public String getFriendlyName() {
        // The human-readable name that gets printed in the SP config
        return "WSO2 Sample - Custom Inbound Configuration";
    }

    /**
     * Defines which field should be used against the value from the request in picking up what SP config to use against
     * the request (see document).
     * @return the name of the field in the property map defined in getConfigurationProperties() whose value will be
     * unique for a protocol.
     */
    @Override
    public String getRelyingPartyKey() {
        return "relying-party-field";
    }

    /**
     * This method helps define all property fields that will be shown in the SP config page.
     * @return an array of properties to be populated in the SP GUI.
     */
    @Override
    public Property[] getConfigurationProperties() {

        Property relParty = new Property();
        relParty.setName("relying-party-field");
        relParty.setDisplayName("Relying Party");

        Property redirectUrl = new Property();
        redirectUrl.setName("redirect-url");
        redirectUrl.setDisplayName("Redirect URL");

        return new Property[]{relParty, redirectUrl};
    }
}
