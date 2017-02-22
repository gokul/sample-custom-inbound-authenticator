/*
*  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package custom.wso2.carbon.identity.inbound.authenticator.factory;

import custom.wso2.carbon.identity.inbound.authenticator.message.CustomInboundResponse;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.application.authentication.framework.inbound.HttpIdentityResponse;
import org.wso2.carbon.identity.application.authentication.framework.inbound.HttpIdentityResponseFactory;
import org.wso2.carbon.identity.application.authentication.framework.inbound.IdentityResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * This class represents a factory for custom IdentityResponse instances which will result from the framework,
 * after the authentication step.
 */
public class CustomInboundIdentityResponseFactory extends HttpIdentityResponseFactory {

    public String getName() {
        return "CustomInboundIdentityResponseFactory";
    }

    /**
     * Checks if an incoming IdentityResponse from the framework can be handled by this particular factory.
     * @param identityResponse incoming IdentityResponse from the identity framework
     * @return true if the incoming response is of the type handled by this factory
     */
    @Override
    public boolean canHandle(IdentityResponse identityResponse) {
        return identityResponse instanceof CustomInboundResponse;
    }

    /**
     * Converts the received IdentityResponse instance to an HTTPResponse so that it could be sent to the calling party.
     * This is where the logic for picking up and setting any custom parameters/headers/cookies etc is written.
     * @param identityResponse the received (and handle-able IdentityResponse instance
     * @return a corresponding HTTPResponse in the form of a builder, so that it could be built on demand
     */
    @Override
    public HttpIdentityResponse.HttpIdentityResponseBuilder create(IdentityResponse identityResponse) {

        HttpIdentityResponse.HttpIdentityResponseBuilder builder
                = new HttpIdentityResponse.HttpIdentityResponseBuilder();

        CustomInboundResponse customInboundResponse = (CustomInboundResponse) identityResponse;

        if (StringUtils.isBlank(customInboundResponse.getToken())) {
            builder.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            builder.setStatusCode(HttpServletResponse.SC_FOUND);
            builder.addParameter("token", customInboundResponse.getToken());
        }
        builder.setRedirectURL(customInboundResponse.getRedirectUrl());
        return builder;
    }

    @Override
    public void create(
            HttpIdentityResponse.HttpIdentityResponseBuilder builder, IdentityResponse identityResponse) {
        this.create(identityResponse);
    }

}
