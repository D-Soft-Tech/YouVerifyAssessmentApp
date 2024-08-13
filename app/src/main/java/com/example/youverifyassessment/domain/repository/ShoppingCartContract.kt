package com.example.youverifyassessment.domain.repository

import com.example.youverifyassessment.data.local.db.entities.ShoppingCartEntity
import com.example.youverifyassessment.domain.model.ShoppingItemDomain
import kotlinx.coroutines.flow.Flow

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
     * @return [Int] which is the number of rows the operation affected
     * */
    suspend fun updateShoppingItemQuantityOrRemove(shoppingItem: ShoppingItemDomain): Int

    /**
     * Retrieves all items in the shopping cart [ShoppingCartEntity]
     *
     * @return List<[ShoppingItemDomain]>
     * */
    fun fetchShoppingItems(): Flow<List<ShoppingItemDomain>>
}