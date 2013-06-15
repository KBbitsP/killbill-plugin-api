/*
 * Copyright 2010-2013 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.billing.payment.plugin.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.ning.billing.catalog.api.Currency;
import com.ning.billing.payment.api.PaymentMethodPlugin;
import com.ning.billing.util.callcontext.CallContext;
import com.ning.billing.util.callcontext.TenantContext;

public interface PaymentPluginApi {

    /**
     * Charge a specific amount in the Gateway. Required.
     *
     * @param kbAccountId       killbill accountId
     * @param kbPaymentId       killbill payment id (for reference)
     * @param kbPaymentMethodId killbill payment method id
     * @param amount            amount to charge
     * @param currency          currency
     * @param context           call context
     * @return information about the payment in the gateway
     * @throws PaymentPluginApiException
     */
    public PaymentInfoPlugin processPayment(UUID kbAccountId, UUID kbPaymentId, UUID kbPaymentMethodId, BigDecimal amount, Currency currency, CallContext context)
            throws PaymentPluginApiException;


    /**
     * Retrieve information about a given payment. Optional (not all gateways will support it).
     *
     * @param kbAccountId killbill accountId
     * @param kbPaymentId killbill payment id (for reference)
     * @param context     call context
     * @return information about the payment in the gateway
     * @throws PaymentPluginApiException
     */
    public PaymentInfoPlugin getPaymentInfo(UUID kbAccountId, UUID kbPaymentId, TenantContext context)
            throws PaymentPluginApiException;

    /**
     * Process a refund against a given payment. Required.
     *
     * @param kbAccountId  killbill accountId
     * @param kbPaymentId  killbill payment id (for reference)
     * @param refundAmount refund amount
     * @param currency     currency
     * @param context      call context
     * @return information about the refund in the gateway
     * @throws PaymentPluginApiException
     */
    public RefundInfoPlugin processRefund(UUID kbAccountId, UUID kbPaymentId, BigDecimal refundAmount, Currency currency, CallContext context)
            throws PaymentPluginApiException;


    /**
     * @param kbAccountId killbill account id
     * @param kbPaymentId killbill payment id
     * @param context     call context
     * @return information about the refunds in the gateway
     * @throws PaymentPluginApiException
     */
    public List<RefundInfoPlugin> getRefundInfo(UUID kbAccountId, UUID kbPaymentId, TenantContext context)
            throws PaymentPluginApiException;


    /**
     * Add a payment method for a Killbill account in the gateway. Optional.
     * <p/>
     * Note: the payment method doesn't exist yet in Killbill when receiving the call in
     * the plugin (kbPaymentMethodId is a placeholder).
     *
     * @param kbAccountId        killbill accountId
     * @param paymentMethodProps payment method details
     * @param setDefault         set it as the default payment method in the gateway
     * @param context            call context
     * @throws PaymentPluginApiException
     */
    public void addPaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, PaymentMethodPlugin paymentMethodProps, boolean setDefault, CallContext context)
            throws PaymentPluginApiException;

    /**
     * Delete a payment method in the gateway. Optional.
     *
     * @param kbAccountId       killbill accountId
     * @param kbPaymentMethodId killbill payment method id
     * @param context           call context
     * @throws PaymentPluginApiException
     */
    public void deletePaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, CallContext context)
            throws PaymentPluginApiException;

    /**
     * Get payment method details for a given payment method. Optional.
     *
     * @param kbAccountId       killbill account id
     * @param kbPaymentMethodId killbill payment method id.
     * @param context           call context
     * @return PaymentMethodPlugin info for the payment method
     * @throws PaymentPluginApiException
     */
    public PaymentMethodPlugin getPaymentMethodDetail(UUID kbAccountId, UUID kbPaymentMethodId, TenantContext context)
            throws PaymentPluginApiException;

    /**
     * Set a payment method as default in the gateway. Optional.
     *
     * @param kbAccountId       killbill accountId
     * @param kbPaymentMethodId killbill payment method id
     * @param context           call context
     * @throws PaymentPluginApiException
     */
    public void setDefaultPaymentMethod(UUID kbAccountId, UUID kbPaymentMethodId, CallContext context)
            throws PaymentPluginApiException;

    /**
     * This is used to see the view of paymentMethods kept by the plugin or the view of
     * existing payment method on the gateway.
     * <p/>
     * Sometimes payment methods have to be added directly to the gateway for PCI compliance issues
     * and so Killbill needs to refresh its state.
     *
     * @param kbAccountId        killbill accountId
     * @param refreshFromGateway fetch the list of existing  payment methods from gateway-- if supported
     * @param context            call context
     * @return
     */
    public List<PaymentMethodInfoPlugin> getPaymentMethods(UUID kbAccountId, boolean refreshFromGateway, CallContext context)
            throws PaymentPluginApiException;

    /**
     * This is used after Killbill decided to refresh its state from the gateway
     *
     * @param kbAccountId    killbill accountId
     * @param paymentMethods the list of payment methods
     */
    public void resetPaymentMethods(UUID kbAccountId, List<PaymentMethodInfoPlugin> paymentMethods)
            throws PaymentPluginApiException;
}
