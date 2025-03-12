package com.rperez.thermostat.bug.racecondition.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class Product(var price: Double = 0.0, var id : Int = 0, var stock : Int = 0)

class Repository {

    fun getProducts(): List<Product> {
        return listOf()
    }

    fun getProductsFlow(): Flow<List<Product>> {
        return flow {
            listOf<Product>()
        }
    }
}