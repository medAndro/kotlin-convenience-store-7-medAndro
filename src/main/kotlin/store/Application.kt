package store

import store.controller.StoreController

fun main() {
    val store = StoreController.create()
    store.orderProducts()
}
