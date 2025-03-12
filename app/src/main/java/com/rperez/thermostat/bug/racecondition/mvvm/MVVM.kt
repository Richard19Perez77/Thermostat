package com.rperez.thermostat.bug.racecondition.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rperez.thermostat.bug.racecondition.product.Product
import com.rperez.thermostat.bug.racecondition.product.Repository
import kotlinx.coroutines.launch

/**
 * Sample bug in MVVM where MVI will fix with SSOT with state.
 *
 * load a product list
 * apply a filter
 * add an item
 *
 * without PREDICTABLE state ui may show
 *  old data
 *  incorrect filtered results
 *  items disappearing from the cart unexpectedly
 *
 *  What's Broken?
 *      race conditions between productList and filteredProducts
 *          if productList updates after applyFilter(), the filter resets and          the users sees unfiltered products again.
 *      separate observers for cartItems and filteredProducts
 *          the cart updates without considering the filtered list, causing            items to disappear or duplicate
 *
 *  Results:
 *      user frustration because applying filters causes unexpected UI resets.
 *      cart behaves inconsistently, showing products that were filtered out
 */
class MVVM {
    class ProductViewModel(var repository: Repository) : ViewModel() {

        // state problem is there are 3 lists in 3 different areas of the app
        val productList = MutableLiveData<List<Product>>()  // Observed by UI
        val filteredProducts = MutableLiveData<List<Product>>()  // Observed separately
        val cartItems = MutableLiveData<List<Product>>()  // Observed separately

        fun fetchProducts() {
            viewModelScope.launch {
                val products = repository.getProducts()
                productList.postValue(products)
            }
        }

        fun applyFilter(maxPrice: Double) {
            val products = productList.value ?: return
            filteredProducts.postValue(products.filter { it.price <= maxPrice })
        }

        fun addToCart(product: Product) {
            val currentCart = cartItems.value.orEmpty().toMutableList()
            currentCart.add(product)
            cartItems.postValue(currentCart)
        }
    }
}