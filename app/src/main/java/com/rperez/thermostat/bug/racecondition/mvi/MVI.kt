package com.rperez.thermostat.bug.racecondition.mvi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.rperez.thermostat.bug.racecondition.product.Product
import com.rperez.thermostat.bug.racecondition.product.Repository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

/**
 * MVI as solution to the race condition we identified in MVVM, with three lists as state we have no way of having a unified SSOT.
 *
 * Here we will use a immutable state object, this will avoid race conditions.
 *
 * Why?
 *  SSOT for ProductViewState
 *      the ui always updates based on one state,
 *      instead of three different separate live data values
 *      no chance of outdated UI updates due to separate observers
 *  State updates are immutable
 *      instead of modifying ProductLists, filteredProducts and cartItems
 *      separately, a new state is emitted each time.
 *  Filtering and Cart updates Work Together
 *      The cartItems list os always passed with the latest product state.
 *      No chance of filtered products disappearing from the cart
 *
 *  Result:
 *      Consistent UI, filtered products stay filtered
 *      Cart works as expected, no disappearing items
 *      Predictable behaviour
 *      State transitions are clear and controlled.
 */
class MVI {
    sealed class ProductViewState {
        object Loading : ProductViewState()
        data class Success(val products: List<Product>, val cart: List<Product>) :
            ProductViewState()

        data class Error(val message: String) : ProductViewState()
    }

    sealed class ProductIntent {
        object LoadProducts : ProductIntent()
        data class ApplyFilter(val maxPrice: Double) : ProductIntent()
        data class AddToCart(val product: Product) : ProductIntent()
    }

    class ProductViewModel(private val repository: Repository) : ViewModel() {
        private val _state = MutableStateFlow<ProductViewState>(ProductViewState.Loading)
        val state: StateFlow<ProductViewState> = _state.asStateFlow()

        private var allProducts: List<Product> = emptyList()
        private var cartItems: MutableList<Product> = mutableListOf()

        fun handleIntent(intent: ProductIntent) {
            when (intent) {
                is ProductIntent.LoadProducts -> fetchProducts()
                is ProductIntent.ApplyFilter -> applyFilter(intent.maxPrice)
                is ProductIntent.AddToCart -> addToCart(intent.product)
            }
        }

        private fun fetchProducts() {
            viewModelScope.launch {
                repository.getProductsFlow()
                    .onStart { _state.value = ProductViewState.Loading }
                    .catch { exception ->
                        _state.value = ProductViewState.Error(exception.message ?: "Error")
                    }
                    .collect { products ->
                        allProducts = products
                        _state.value = ProductViewState.Success(products, cartItems)
                    }
            }
        }

        private fun applyFilter(maxPrice: Double) {
            val filteredProducts = allProducts.filter { it.price <= maxPrice }
            _state.value = ProductViewState.Success(filteredProducts, cartItems)
        }

        private fun addToCart(product: Product) {
            cartItems.add(product)
            _state.value = ProductViewState.Success(allProducts, cartItems)
        }
    }
}