package io.agora.uikit.feature.chat.interfaces

import io.agora.uikit.interfaces.OnMenuChangeListener
import io.agora.uikit.menu.EaseMenuHelper

interface IChatMenu {
    /**
     * Clear all menus
     */
    fun clearMenu()

    /**
     * Add item menu
     * @param groupId
     * @param itemId
     * @param order
     * @param title
     */
    fun addItemMenu(itemId: Int, order: Int, title: String, groupId: Int = 0)

    /**
     * Set menu item visibility
     * @param id
     * @param visible
     */
    fun findItemVisible(id: Int, visible: Boolean)

    /**
     * Set menu listener.
     * @param listener
     */
    fun setOnMenuChangeListener(listener: OnMenuChangeListener?)

    /**
     * Return to the menu help category
     * @return
     */
    fun getChatMenuHelper(): EaseMenuHelper?
}