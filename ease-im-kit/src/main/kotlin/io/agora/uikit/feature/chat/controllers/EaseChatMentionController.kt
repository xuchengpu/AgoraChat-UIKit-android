package io.agora.uikit.feature.chat.controllers

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import io.agora.uikit.EaseIM
import io.agora.uikit.R
import io.agora.uikit.configs.EaseCustomMentionItemConfig
import io.agora.uikit.feature.contact.interfaces.OnHeaderItemClickListener
import io.agora.uikit.feature.contact.interfaces.OnMentionResultListener
import io.agora.uikit.feature.group.EaseGroupMentionBottomSheet
import io.agora.uikit.feature.chat.widgets.EaseChatLayout
import java.util.regex.Pattern

class EaseChatMentionController(
    private val context: Context,
    private val chatLayout: EaseChatLayout?,
) {

    fun setPickAtContentStyle(editable: Editable) {
        val pattern = Pattern.compile("@([^\\s]+)")
        val matcher = pattern.matcher(editable)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            editable.setSpan(
                ForegroundColorSpan(
                    context.resources.getColor(R.color.ease_chat_mention_text_color)
                ), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    fun removePickAt(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && event?.action == KeyEvent.ACTION_DOWN && v is EditText) {
            val selectionStart = v.selectionStart
            val selectionEnd = v.selectionEnd
            val text = v.text as SpannableStringBuilder
            val spans = text.getSpans(
                0, text.length,
                ForegroundColorSpan::class.java
            )
            for (span in spans) {
                val spanStart = text.getSpanStart(span)
                val spanEnd = text.getSpanEnd(span)
                if (selectionStart >= spanStart && selectionEnd <= spanEnd) {
                    if (spanStart != -1 && spanEnd != -1) {
                        text.delete(spanStart + 1, spanEnd)
                    }
                }
            }
        }
        return false
    }

    fun showMentionDialog(conversationId: String){
        if ( EaseIM.getConfig()?.chatConfig?.enableMention == true){
            val defaultMentionItemList = EaseCustomMentionItemConfig(context).getDefaultMentionItemList()
            val easeGroupMentionBottomSheet = EaseGroupMentionBottomSheet(
                groupId = conversationId,
                headerList = defaultMentionItemList ,
                itemListener = object : OnMentionResultListener {
                    override fun onMentionItemClick(view: View?, position: Int, userId: String?) {
                        chatLayout?.inputAtUsername(userId,false)
                    }
                },
                headerItemListener = object : OnHeaderItemClickListener {
                    override fun onHeaderItemClick(v: View, itemIndex: Int,itemId:Int?) {
                        itemId?.let {
                            if (it == R.id.ease_mention_at_all){
                                chatLayout?.inputAtUsername(
                                    context.resources.getString(R.string.ease_all_members), false
                                )
                            }
                        }
                    }
                }
            )
            (context as FragmentActivity).supportFragmentManager.let {  easeGroupMentionBottomSheet.show(it,"group_mention") }
        }
    }


}