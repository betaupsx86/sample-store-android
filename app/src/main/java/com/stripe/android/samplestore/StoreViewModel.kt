package com.stripe.android.samplestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stripe.android.CustomerSession
import com.stripe.android.StripeError
import com.stripe.android.model.Customer

internal class StoreViewModel : ViewModel() {
    private val customerSession: CustomerSession by lazy { CustomerSession.getInstance() }

    fun retrieveCustomer(): LiveData<Result<Customer>> {
        val liveData = MutableLiveData<Result<Customer>>()
        customerSession.retrieveCurrentCustomer(
            object : CustomerSession.CustomerRetrievalListener {
                override fun onCustomerRetrieved(customer: Customer) {
                    liveData.value = Result.success(customer)
                }

                override fun onError(
                    errorCode: Int,
                    errorMessage: String,
                    stripeError: StripeError?
                ) {
                    liveData.value = Result.failure(
                        RuntimeException("Could not retrieve Customer. $errorMessage ($errorCode). $stripeError")
                    )
                }
            }
        )
        return liveData
    }
}
