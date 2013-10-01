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
package com.peergreen.transaction.extension.naming;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import com.peergreen.naming.JavaContextFactoryListener;

/**
 * Defines a listener for adding UserTransaction object in the java: context
 * @author Florent Benoit
 */
@Component
@Instantiate
@Provides
public class TransactionContextFactoryListener implements JavaContextFactoryListener {

    private TransactionManager transactionManager;

    @Bind
    public void bindTransactionContextFactoryListener(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }



    @Override
    public void handle(Context javaContext) throws NamingException {
        javaContext.bind("comp/UserTransaction", transactionManager);

    }

}
