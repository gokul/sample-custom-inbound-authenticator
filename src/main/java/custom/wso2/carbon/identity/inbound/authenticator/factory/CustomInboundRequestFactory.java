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

import custom.wso2.carbon.identity.inbound.authenticator.message.CustomInboundRequest;
import custom.wso2.carbon.identity.inbound.authenticator.util.CustomInboundConstants;
import org.wso2.carbon.identity.application.authentication.framework.inbound.HttpIdentityRequestFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents a factory for custom IdentityRequest instances which will be passed to the framework for
 * authentication.
 *
 * Essentially, the conversion of the protocol-specific HTTP request to the framework-understood IdentityRequest takes
 * place here.
 */
public class CustomInboundRequestFactory extends HttpIdentityRequestFactory {

    /**
     *  Checks whether or not an incoming request hitting the "/identity" servlet should be handled by this
     *  particular custom IdentityRequest factory.
     * @param request the request parameter coming from the servlet
     * @param response the response parameter coming from the servlet
     * @return true if the request is of a type which can be handled by this particular IdentityRequest factory
     */
    @Override
    public boolean canHandle(HttpServletRequest request, HttpServletResponse response) {
        //Return true if the incoming request to the identity servlet contains the custom URL param
        return request.getParameter(CustomInboundConstants.HTTP_PARAM_CUSTOM_AUTH_REQUEST) != null;
    }

    /**
     * Returns a new instance of the custom IdentityRequest object, which will then be passed to the processor.
     * @param request the HTTP request from the servlet
     * @param response the response parameter coming from the servlet
     * @return a builder for CustomInboundRequest, which is a subclass of IdentityRequest
     */
    @Override
    public CustomInboundRequest.CustomInboundRequestBuilder create(HttpServletRequest request, HttpServletResponse response) {
        return new CustomInboundRequest.CustomInboundRequestBuilder(request, response);
    }
}

