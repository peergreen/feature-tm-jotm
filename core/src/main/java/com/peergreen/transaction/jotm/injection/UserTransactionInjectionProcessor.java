/**
 * Copyright 2013 Peergreen S.A.S.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.transaction.jotm.injection;

import javax.transaction.UserTransaction;

import org.ow2.util.ee.metadata.common.api.struct.IJAnnotationResource;
import org.ow2.util.ee.metadata.common.api.view.ICommonView;
import org.ow2.util.scan.api.metadata.IFieldMetadata;
import org.ow2.util.scan.api.metadata.IMetadata;
import org.ow2.util.scan.api.metadata.IMethodMetadata;

import com.peergreen.metadata.adapter.Binding;
import com.peergreen.metadata.adapter.InjectionContext;
import com.peergreen.metadata.adapter.InjectionProcessor;

/**
 * Allows to inject UserTransaction
 * @author Florent Benoit
 */
@InjectionProcessor("javax.annotation.Resource")
public class UserTransactionInjectionProcessor {

    /**
     * User transaction.
     */
    private final UserTransaction userTransaction;

    /**
     *
     * @param userTransaction
     */
    public UserTransactionInjectionProcessor(UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }


    public Binding<UserTransaction> handle(InjectionContext injectionContext) {

        // Get metadata
        IMetadata metadata = injectionContext.getMetadata();

        // get Common view
        ICommonView commonView = metadata.as(ICommonView.class);

        IJAnnotationResource annotationResource = commonView.getJAnnotationResource();

        if (annotationResource == null) {
            return null;
        }

        String name = annotationResource.getName();
        if (name == null || "".equals(name)) {
            name = metadata.getMember().getName();
        }

        if (metadata instanceof IFieldMetadata) {
            IFieldMetadata fieldMetadata = (IFieldMetadata) metadata;
            if (UserTransaction.class.getName().equals(fieldMetadata.getType())) {
                // matching interface of the field
                return injectionContext.createBinding(name, userTransaction);
            }
        } else if (metadata instanceof IMethodMetadata) {
            IMethodMetadata methodMetadata = (IMethodMetadata) metadata;
            String[] params = methodMetadata.getParametersClassName();
            if (params.length == 1 && UserTransaction.class.getName().equals(params[0])) {
                // matching interface of the field
                return injectionContext.createBinding(name, userTransaction);
            }
        }

        return null;
    }

}
