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
package com.peergreen.transaction.jotm;

import javax.resource.spi.XATerminator;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Validate;
import org.objectweb.jotm.Current;
import org.objectweb.jotm.RmiConfiguration;
import org.objectweb.jotm.TransactionFactory;
import org.objectweb.jotm.TransactionFactoryImpl;
import org.objectweb.jotm.TransactionSynchronizationRegistryImpl;
import org.objectweb.jotm.rmi.RmiLocalConfiguration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


/**
 * Defines the JTA component based on OW2 JOTM.
 * It will register also 3 OSGi services as specified in OSGi Enterprise specification
 * @author Florent Benoit
 */
@Component
@Instantiate
public class JOTMService {

    /**
     * JOTM instance.
     */
    private Current current;

    /**
     * Enterprise OSGi Transaction Manager.
     */
    private ServiceRegistration<TransactionManager> transactionManagerServiceRegistration;

    /**
     * Enterprise OSGi User Transaction.
     */
    private ServiceRegistration<UserTransaction> userTransactionServiceRegistration;

    /**
     * Enterprise OSGi TransactionSynchronizationRegistry.
     */
    private ServiceRegistration<TransactionSynchronizationRegistry> transactionSynchronizationRegistryServiceRegistration;

    /**
     * XATerminator.
     */
    private ServiceRegistration<XATerminator> xaTerminatorServiceRegistration;

    /**
     * Bundle context used to register services.
     */
    private final BundleContext bundleContext;

    /**
     * Constructor
     * @param bundleContext
     */
    public JOTMService(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /**
     * Creates the Transaction Manager and JTA services.
     * @throws Exception if registration is failing
     */
    @Validate
    public void start() throws Exception {
        // Initialize JOTM
        RmiConfiguration rmiConfiguration = new RmiLocalConfiguration();
        TransactionFactory transactionFactory = new TransactionFactoryImpl(rmiConfiguration);
        this.current = new Current(transactionFactory);

        // register JTA services
        transactionManagerServiceRegistration = bundleContext.registerService(TransactionManager.class, current, null);
        userTransactionServiceRegistration = bundleContext.registerService(UserTransaction.class, current, null);
        transactionSynchronizationRegistryServiceRegistration = bundleContext.registerService(TransactionSynchronizationRegistry.class, TransactionSynchronizationRegistryImpl.getInstance(), null);

        // Also export XATerminator
        xaTerminatorServiceRegistration = bundleContext.registerService(XATerminator.class, current.getXATerminator(), null);

    }

    /**
     * Unregister JTA services
     */
    @Invalidate
    public void stop() {
        // Also needs to unexport RMI object


        // Well, just to know that we're unregistering services from now
        transactionManagerServiceRegistration.unregister();
        userTransactionServiceRegistration.unregister();
        transactionSynchronizationRegistryServiceRegistration.unregister();
        xaTerminatorServiceRegistration.unregister();
    }

}
