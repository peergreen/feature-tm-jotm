/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 * Proprietary and confidential.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peergreen.transaction.jotm.injection;


import javax.annotation.Resource;
import javax.transaction.UserTransaction;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import com.peergreen.metadata.adapter.Binding;
import com.peergreen.metadata.adapter.HandlerInjectionProcessor;
import com.peergreen.metadata.adapter.InjectionContext;

@Component
@Instantiate
@Provides
public class UserTransactionHandlerInjectionProcessor implements HandlerInjectionProcessor {

    private final UserTransactionInjectionProcessor wrapped;

    public UserTransactionHandlerInjectionProcessor(@Requires UserTransaction userTransaction) {
        this.wrapped = new UserTransactionInjectionProcessor(userTransaction);
    }

    @Override
    public String getAnnotation() {
        return Resource.class.getName();
    }

    @Override
    public Binding<?> handle(InjectionContext injectionContext) {
        return wrapped.handle(injectionContext);
    }

}
