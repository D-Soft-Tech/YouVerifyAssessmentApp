package com.example.youverifyassessment.domain.repository

import com.example.youverifyassessment.data.local.db.entities.ProductEntity
import com.example.youverifyassessment.data.local.db.entities.ShoppingCartEntity
import com.example.youverifyassessment.domain.model.ProductsDomain
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import kotlinx.coroutines.flow.Flow

/**
 * Manages all operations involving Shopping Carts
 *
 * @author Adebayo Oloyede
 *
 * @since August, 2024
 *
 * [Github](https://www.github.com/D-Soft-Tech)
 *
 * [LinkedIn](https://www.linkedin.com/in/adebayo-oloyede)
 * */
interface ShoppingCartContract {
    /**
     * Uses the value of quantity of the [shoppingItem] to update the shopping item in the shopping cart, [ShoppingCartEntity] table.
     *
     * If the value of quantity of the [shoppingItem] is greater than 0 & is also greater or less than the the one in the table, [ShoppingCartEntity] it updates the table with this new shopping item.
     * However if the value of the quantity parameter in the incoming shopping item [shoppingItem], it deletes the item completely from table.
     *
     * Likewise, if the item doesn't exist in the table before, it adds it afresh.
     *
     * @param [shoppingItem] which is the new shopping item to be added, removed or updated as the case may be.
     * @param [isIncrease] tells if the operation is to increase the [ShoppingCartEntity.quantity] column or to decrease it.
     * @return [Int] which is the number of rows the operation affected
     * */
    suspend fun insertUpdateOrRemoveShoppingItem(
        shoppingItem: ShoppingItemDomain,
        isIncrease: Boolean
    ): Int

    /**
     * Retrieves the total number of items in the shopping cart
     * */
    fun getTotalItemsInShoppingCart(): Flow<Long?>

    /**
     * Deletes all items in the shopping cart [ShoppingCartEntity]
     *
     * @param [shoppingItems] Products to be removed from shopping cart. The values of field [ProductEntity.isInCart] for these products would be re-set back to false.
     * @return [Int] number of rows affected by this operation wrapped in a Coroutine flow.
     * */
    suspend fun clearShoppingCart(shoppingItems: List<ProductsDomain>): Flow<Int>

    /**
     * Retrieves all items in the shopping cart [ShoppingCartEntity]
     *
     * @return List<[ShoppingItemDomain]> wrapped in a Kotlin Coroutines Flow
     * */
    fun fetchShoppingItems(): Flow<List<ShoppingItemDomain>>
}