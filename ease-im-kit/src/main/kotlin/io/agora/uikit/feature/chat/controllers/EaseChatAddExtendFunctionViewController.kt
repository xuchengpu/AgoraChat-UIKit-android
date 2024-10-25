package io.agora.uikit.feature.chat.controllers

import android.view.View
import io.agora.uikit.EaseIM
import io.agora.uikit.R
import io.agora.uikit.common.ChatMessage
import io.agora.uikit.common.ChatMessageDirection
import io.agora.uikit.common.extensions.hasThreadChat
import io.agora.uikit.common.extensions.isReplyMessage
import io.agora.uikit.common.extensions.isSend
import io.agora.uikit.feature.chat.reaction.EaseChatMessageReactionView
import io.agora.uikit.feature.chat.reaction.interfaces.OnEaseChatReactionErrorListener
import io.agora.uikit.feature.chat.reply.EaseChatMessageReplyView
import io.agora.uikit.feature.chat.reply.interfaces.OnMessageReplyViewClickListener
import io.agora.uikit.feature.chat.translation.EaseChatMessageTranslationView
import io.agora.uikit.feature.chat.urlpreview.EaseChatMessageUrlPreview
import io.agora.uikit.feature.thread.widgets.EaseChatMessageThreadView
import io.agora.uikit.feature.thread.interfaces.OnMessageChatThreadClickListener
import io.agora.uikit.interfaces.UrlPreviewStatusCallback
import io.agora.uikit.widget.chatrow.EaseChatRow
import io.agora.uikit.widget.chatrow.EaseChatRowText

object EaseChatAddExtendFunctionViewController{

    /**
     * Add reply view to message.
     */
    fun addReplyViewToMessage(message: ChatMessage?, view: EaseChatRowText, listener: OnMessageReplyViewClickListener?) {
        val targetView = view.getTargetTypeChildView(EaseChatMessageReplyView::class.java)
        if (message?.isReplyMessage() == true) {
            if (targetView == null) {
                addReplyView(view, message, listener)
            } else {
                (targetView as EaseChatMessageReplyView).updateMessageInfo(message)
                targetView.visibility = View.VISIBLE
            }
        } else {
            targetView?.visibility = View.GONE
        }
    }

    private fun addReplyView(view: EaseChatRowText, message: ChatMessage?, listener: OnMessageReplyViewClickListener?) {
        val replyView = EaseChatMessageReplyView(view.context, isSender = message?.isSend() ?: false)
        view.addChildToTopBubbleLayout(replyView)
        replyView.setOnMessageReplyViewClickListener(listener)
        replyView.visibility = View.GONE
        val isLoaded = replyView.updateMessageInfo(message)
        if (isLoaded) {
            replyView.visibility = View.VISIBLE
        }
    }

    /**
     * Add reaction view to message.
     */
    fun addReactionViewToMessage(
        message: ChatMessage?,
        view: EaseChatRow,
        reactionErrorListener: OnEaseChatReactionErrorListener?
    ) {
        EaseIM.getConfig()?.chatConfig?.enableMessageReaction?.let {
            if (!it) {
                return
            }
        }
        val reactionView = view.getTargetTypeChildView(EaseChatMessageReactionView::class.java)
        message?.messageReaction?.let {
            if (it.isNotEmpty()) {
                if (reactionView == null) {
                    EaseChatMessageReactionView(view.context).let { child ->
                        child.setupWithMessage(message)
                        child.showReaction()
                        child.setReactionErrorListener(reactionErrorListener)
                        view.addChildToBottomBubbleLayout(child)
                    }
                } else {
                    (reactionView as EaseChatMessageReactionView).run {
                        setupWithMessage(message)
                        showReaction()
                        setReactionErrorListener(reactionErrorListener)
                        visibility = View.VISIBLE
                    }
                }
            } else {
                reactionView?.visibility = View.GONE
            }
        } ?: run {
            reactionView?.visibility = View.GONE
        }
    }

    /**
     * Add thread view to message.
     */
    fun addThreadRegionViewToMessage(
        message: ChatMessage?,
        view: EaseChatRow,
        threadEventListener: OnMessageChatThreadClickListener?
    ){
        val threadView = view.getTargetTypeChildView(EaseChatMessageThreadView::class.java)
        message?.let {
            if (it.hasThreadChat() && !it.isChatThreadMessage) {
                if (threadView == null) {
                    EaseChatMessageThreadView(view.context).let { child ->
                        child.setupWithMessage(it)
                        child.showThread()
                        child.setThreadEventListener(threadEventListener)
                        view.addChildToBottomBubbleLayout(child,0)
                    }
                } else {
                    (threadView as EaseChatMessageThreadView).run {
                        setupWithMessage(it)
                        showThread()
                        setThreadEventListener(threadEventListener)
                        visibility = View.VISIBLE
                    }
                }
            } else {
                threadView?.visibility = View.GONE
            }
        } ?: run {
            threadView?.visibility = View.GONE
        }
    }

    fun addTranslationViewToMessage(
        view: EaseChatRowText,
        message: ChatMessage?
    ){
        EaseIM.getConfig()?.chatConfig?.enableTranslationMessage?.let {
            if (!it) {
                return
            }
        }
        val isSend = message?.direct() == ChatMessageDirection.SEND
        var translationView: EaseChatMessageTranslationView

        view.getBubbleBottom?.let {
            if (it.childCount == 0){
                translationView = EaseChatMessageTranslationView(isSend,false,view.context)
                translationView.id = R.id.ease_translation_view
            }else{
                translationView = it.findViewById(R.id.ease_translation_view)
            }
            view.addChildToBubbleBottomLayout(translationView)
            translationView.visibility = View.GONE
            val isLoaded = translationView.updateMessageInfo(message)
            if (isLoaded) {
                translationView.visibility = View.VISIBLE
            }
        }
    }

    fun addUrlPreviewToMessage(
        view: EaseChatRowText,
        message: ChatMessage?,
        callback: UrlPreviewStatusCallback?=null
    ){
        EaseIM.getConfig()?.chatConfig?.enableUrlPreview?.let {
            if (!it) {
                return
            }
        }
        val isSend = message?.direct() == ChatMessageDirection.SEND
        var urlPreview: EaseChatMessageUrlPreview?
        view.getBubbleBottom?.let {
            if (it.childCount == 0){
                urlPreview = EaseChatMessageUrlPreview(isSend,view.context)
                urlPreview?.id = R.id.ease_url_preview
            }else{
                urlPreview = it.findViewById(R.id.ease_url_preview)
                if (urlPreview == null){
                    urlPreview = EaseChatMessageUrlPreview(isSend,view.context)
                    urlPreview?.id = R.id.ease_url_preview
                }
            }
            view.addChildToBubbleBottomLayout(urlPreview)
            urlPreview?.checkPreview(message,callback)
        }
    }
}