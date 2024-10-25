package io.agora.uikit.model

import io.agora.uikit.EaseIM
import io.agora.uikit.common.extensions.getFullInfo
import io.agora.uikit.provider.getSyncUser


/**
 * It is a bean for profile provider interface.
 * @param id The id, it can be the userId or the group id.
 * @param name The name of the user or the group.
 * @param avatar The avatar of the user or the group.
 * @param remark The group nickname of the user in the group.
 */
open class EaseProfile(
    val id: String,
    open var name: String? = null,
    open var avatar: String? = null,
    open var remark: String? = null
) {
    private var _timestamp: Long = 0L

    internal fun setTimestamp(timestamp: Long) {
        _timestamp = timestamp
    }

    internal fun getTimestamp(): Long {
        return _timestamp
    }

    fun getRemarkOrName(): String {
        return remark?.ifEmpty { getNotEmptyName() } ?: getNotEmptyName()
    }

    fun getNotEmptyName(): String {
        return name?.ifEmpty { id } ?: id
    }

    companion object {

        /**
         * Get the group member information from the cache or the user provider.
         * @param groupId The group id.
         * @param userId The user id.
         * @return The group member information.
         */
        fun getGroupMember(groupId: String?, userId: String?): EaseProfile? {
            return EaseIM.getUserProvider()?.getSyncUser(userId)
        }
    }
}