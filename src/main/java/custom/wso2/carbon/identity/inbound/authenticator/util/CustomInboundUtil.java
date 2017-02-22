package custom.wso2.carbon.identity.inbound.authenticator.util;

import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.nio.charset.Charset;

/**
 * Class for storing OSGi services and their related context information.
 */
public class CustomInboundUtil {

    private static BundleContext bundleContext;
    private static RealmService realmService;
    private static ConfigurationContextService configCtxService;
    private static HttpService httpService;

    public static BundleContext getBundleContext() {
        return bundleContext;
    }

    public static RealmService getRealmService() {
        return realmService;
    }

    public static ConfigurationContextService getConfigCtxService() {
        return configCtxService;
    }

    public static HttpService getHttpService() {
        return httpService;
    }

    public static void setBundleContext(BundleContext bundleContext) {
        CustomInboundUtil.bundleContext = bundleContext;

    }

    public static void setRealmService(RealmService realmService) {
        CustomInboundUtil.realmService = realmService;

    }

    public static void setConfigCtxService(ConfigurationContextService configCtxService) {
        CustomInboundUtil.configCtxService = configCtxService;
    }

    public static void setHttpService(HttpService httpService) {
        CustomInboundUtil.httpService = httpService;
    }

    public static String newString(final byte[] bytes, final Charset charset) {
        return bytes == null ? null : new String(bytes, charset);
    }

}
