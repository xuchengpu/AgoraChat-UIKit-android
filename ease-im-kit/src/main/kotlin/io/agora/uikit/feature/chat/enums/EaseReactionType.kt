package io.agora.uikit.feature.chat.enums

enum class EaseReactionType(val viewType: Int) {
    /**
     * Default reaction emoji for message menu.
     */
    DEFAULT(1),

    /**
     * Normal reaction emoji for message reaction.
     */
    NORMAL(2),

    /**
     * Add reaction emoji icon.
     */
    ADD(3),

    /**
     * More reaction emoji icon.
     */
    MORE(4)
}