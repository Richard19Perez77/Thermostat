package com.rperez.thermostat.bug.inconsistentui

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rperez.thermostat.bug.racecondition.product.Product
import com.rperez.thermostat.bug.racecondition.product.Repository
import kotlinx.coroutines.launch
import kotlin.collections.orEmpty

class Example {
    /**
     * Ex showing MVI vs MVVM where multiple live data updates cause outdated information.
     *
     * MVVM manages state with 3x live data objects
     */

    class MVVM {
        /**
         * What goes wrong?
         *  productList and cartItems update separately
         *      the cart updates, but the product list doesn't refresh instantly
         *      when navigating back, the user sees stale stock information
         *  UI screens observe different live data objects
         *      cartItems updates but productList is not notified
         *      the product screen still shows the old available stock, causing confusion
         *
         * Result:
         *  user frustration because stock numbers are outdated
         *  data inconsistencies between screens
         */
        class ProductViewModel(var repository: Repository) : ViewModel() {
            val productList = MutableLiveData<List<Product>>()  // Observed by product screen
            val cartItems = MutableLiveData<List<Product>>()   // Observed by cart screen

            fun fetchProducts() {
                viewModelScope.launch {
                    val products = repository.getProducts()
                    productList.postValue(products)  // Updates product screen
                }
            }

            fun addToCart(product: Product) {
                val currentCart = cartItems.value.orEmpty().toMutableList()
                currentCart.add(product)
                cartItems.postValue(currentCart)  // Updates cart screen
            }
        }
    }

    /**
     * What we can fix?
     *  instead of 3x live data objects, mvi ensures that all UI screens rely on a SSOT.
     *
     *  SSOT is ProductViewState
     *      instead of a productList and cartItems updating separately, one state holds both.
     *      when cartItems updates, the product stock also updates in the same state
     *
     * State updates are immutable
     *      instead of modifying data separately, a new state is emitted each time, keeping UI consistent.
     *
     * All screens observe one state
     *      the cart and product screen both listen to state:
     *          StateFlow<ProductViewState>
     *      no race conditions between UI observers
     *
     * Result:
     *      stock updates instantly when adding a card
     *      no stale data on the product screen.
     *      data consistency across screens
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
            data class AddToCart(val product: Product) : ProductIntent()
        }

        class ProductViewModel(private val repository: Repository) : ViewModel() {
            private val _state = MutableStateFlow<ProductViewState>(ProductViewState.Loading)
            val state: StateFlow<ProductViewState> = _state.asStateFlow()

            private var allProducts: List<Product> = emptyList()
            private var cartItems: MutableList<Product> = mutableListOf()

            // may be called on app start up or refresh button press
            // add item may be click on item from screen to add to cart
            // handleIntent(ProductIntent.LoadProducts)
            // handleIntent(ProductIntent.AddToCart(product))
            fun handleIntent(intent: ProductIntent) {
                when (intent) {
                    is ProductIntent.LoadProducts -> fetchProducts()
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

            private fun addToCart(product: Product) {
                cartItems.add(product)
                val updatedProducts = allProducts.map { p ->
                    if (p.id == product.id) p.copy(stock = p.stock - 1) else p
                }
                _state.value = ProductViewState.Success(updatedProducts, cartItems)
            }
        }
    }
}
/**
 *  Architecture
 *
 *  MVVM Architecture
 *
 * +--------------------------------+
 * |        ProductViewModel        |
 * |--------------------------------|
 * | - repository: Repository       |
 * | - productList: MutableLiveData |
 * | - cartItems: MutableLiveData   |
 * |--------------------------------|
 * | + fetchProducts()              |
 * | + addToCart(product)           |
 * +--------------------------------+
 *            |
 *            ▼
 * +--------------------------------+
 * |         Repository             |
 * |--------------------------------|
 * | + getProducts(): List<Product> |
 * +--------------------------------+
 *            |
 *            ▼
 * +--------------------------------+
 * |          Product               |
 * |--------------------------------|
 * | - id: Int                      |
 * | - name: String                 |
 * | - stock: Int                   |
 * |--------------------------------|
 * | + copy(stock: Int)             |
 * +--------------------------------+
 *
 * [UI Layer Observes]
 *   ↳ productList (Product Screen)
 *   ↳ cartItems (Cart Screen)
 *
 * Issue: Data inconsistency between productList and cartItems updates separately.
 *
 *
 * MVI Architecture
 *
 *+--------------------------------+
 * |        ProductViewModel        |
 * |--------------------------------|
 * | - repository: Repository       |
 * | - _state: MutableStateFlow     |
 * | - state: StateFlow             |
 * | - allProducts: List<Product>   |
 * | - cartItems: MutableList       |
 * |--------------------------------|
 * | + handleIntent(intent)         |
 * | + fetchProducts()              |
 * | + addToCart(product)           |
 * +--------------------------------+
 *            |
 *            ▼
 * +--------------------------------+
 * |         Repository             |
 * |--------------------------------|
 * | + getProductsFlow(): Flow<List<Product>> |
 * +--------------------------------+
 *            |
 *            ▼
 * +--------------------------------+
 * |          Product               |
 * |--------------------------------|
 * | - id: Int                      |
 * | - name: String                 |
 * | - stock: Int                   |
 * |--------------------------------|
 * | + copy(stock: Int)             |
 * +--------------------------------+
 *
 * [Single Source of Truth]
 *   ↳ StateFlow<ProductViewState> (All Screens Observe)
 *
 * +--------------------------------+
 * |     ProductViewState (Sealed)  |
 * |--------------------------------|
 * | - Loading                      |
 * | - Success(products, cart)      |
 * | - Error(message)               |
 * +--------------------------------+
 *
 * +--------------------------------+
 * |     ProductIntent (Sealed)     |
 * |--------------------------------|
 * | - LoadProducts                 |
 * | - AddToCart(product)           |
 * +--------------------------------+
 *
 * Fix: Ensures state consistency by updating stock and cart together.
 *
 */