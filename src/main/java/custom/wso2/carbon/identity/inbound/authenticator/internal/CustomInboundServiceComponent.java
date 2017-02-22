package custom.wso2.carbon.identity.inbound.authenticator.internal;


import custom.wso2.carbon.identity.inbound.authenticator.factory.CustomInboundIdentityResponseFactory;
import custom.wso2.carbon.identity.inbound.authenticator.factory.CustomInboundRequestFactory;
import custom.wso2.carbon.identity.inbound.authenticator.processor.CustomInboundRequestProcessor;
import custom.wso2.carbon.identity.inbound.authenticator.util.CustomInboundAuthConfig;
import custom.wso2.carbon.identity.inbound.authenticator.util.CustomInboundUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.wso2.carbon.identity.application.authentication.framework.inbound.HttpIdentityRequestFactory;
import org.wso2.carbon.identity.application.authentication.framework.inbound.HttpIdentityResponseFactory;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityProcessor;
import org.wso2.carbon.identity.application.mgt.AbstractInboundAuthenticatorConfig;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.util.Hashtable;

/**
 * @scr.component name="custom.wso2.carbon.identity.inbound.authenticator.internal" immediate="true"
 * @scr.reference name="config.context.service" immediate="true"
 * interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1" policy="dynamic"
 * bind="setConfigurationContextService" unbind="unsetConfigurationContextService"
 * @scr.reference name="user.realmservice.default" interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 * @scr.reference name="osgi.httpservice" interface="org.osgi.service.http.HttpService" cardinality="1..1"
 * policy="dynamic" bind="setHttpService" unbind="unsetHttpService"
 */

/*
This is the OSGi service component for the custom inbound authenticator. This will enable the bundle (jar) to
 activate the specified service and register themselves so that the IS is able to see them and use them when a
 matching request arrives.
*/
public class CustomInboundServiceComponent {

    private static Log log = LogFactory.getLog(CustomInboundServiceComponent.class);

    protected void activate(ComponentContext ctxt) {
        try {
            CustomInboundAuthConfig customInboundAuthConfig = new CustomInboundAuthConfig();
            Hashtable<String, String> props = new Hashtable<>();
            ctxt.getBundleContext().registerService(AbstractInboundAuthenticatorConfig.class,
                    customInboundAuthConfig, props);

            ctxt.getBundleContext().registerService(IdentityProcessor.class.getName(),
                    new CustomInboundRequestProcessor(customInboundAuthConfig), null);

            ctxt.getBundleContext().registerService(HttpIdentityResponseFactory.class.getName(),
                    new CustomInboundIdentityResponseFactory(), null);

            ctxt.getBundleContext().registerService(HttpIdentityRequestFactory.class.getName(),
                    new CustomInboundRequestFactory(), null);
        } catch (Exception e) {
            log.error("Error Activating Custom Inbound Auth Package");
            throw new RuntimeException(e);
        }

    }

    protected void deactivate(ComponentContext ctxt) {
        CustomInboundUtil.setBundleContext(null);
        if (log.isDebugEnabled()) {
            log.info("Custom inbound authenticator bundle is deactivated");
        }
    }

    protected void setRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Realm Service is set in the Custom inbound authenticator bundle");
        }
        CustomInboundUtil.setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Realm Service is set in the Custom inbound authenticator bundle");
        }
        CustomInboundUtil.setRealmService(null);
    }

    protected void setConfigurationContextService(ConfigurationContextService configCtxService) {
        if (log.isDebugEnabled()) {
            log.debug("Configuration Context Service is set in the Custom inbound authenticator bundle");
        }
        CustomInboundUtil.setConfigCtxService(configCtxService);
    }

    protected void unsetConfigurationContextService(ConfigurationContextService configCtxService) {
        if (log.isDebugEnabled()) {
            log.debug("Configuration Context Service is unset in the Custom inbound authenticator bundle");
        }
        CustomInboundUtil.setConfigCtxService(null);
    }

    protected void setHttpService(HttpService httpService) {
        if (log.isDebugEnabled()) {
            log.debug("HTTP Service is set in the Custom inbound authenticator bundle");
        }
        CustomInboundUtil.setHttpService(httpService);
    }

    protected void unsetHttpService(HttpService httpService) {
        if (log.isDebugEnabled()) {
            log.debug("HTTP Service is unset in the Custom inbound authenticator bundle");
        }

        CustomInboundUtil.setHttpService(null);
    }
}
