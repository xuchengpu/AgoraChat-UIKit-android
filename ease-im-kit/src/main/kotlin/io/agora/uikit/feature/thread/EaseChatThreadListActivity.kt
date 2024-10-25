package io.agora.uikit.feature.thread

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import io.agora.uikit.EaseIM
import io.agora.uikit.base.EaseBaseActivity
import io.agora.uikit.common.ChatThread
import io.agora.uikit.common.EaseConstant
import io.agora.uikit.common.extensions.hasRoute
import io.agora.uikit.databinding.EaseActivityChatThreadListBinding
import io.agora.uikit.feature.thread.fragment.EaseChatThreadListFragment
import io.agora.uikit.feature.thread.interfaces.OnChatThreadListItemClickListener

open class EaseChatThreadListActivity: EaseBaseActivity<EaseActivityChatThreadListBinding>() {
    private var fragment: EaseChatThreadListFragment? = null
    override fun getViewBinding(inflater: LayoutInflater): EaseActivityChatThreadListBinding? {
        return EaseActivityChatThreadListBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val conversationId = intent.getStringExtra(EaseConstant.EXTRA_CONVERSATION_ID)

        val builder = EaseChatThreadListFragment.Builder(conversationId)
            .useTitleBar(true)
            .enableTitleBarPressBack(true)
            .setOnChatThreadListItemClickListener(object : OnChatThreadListItemClickListener{
                override fun onChatThreadItemClick(view: View?, thread: ChatThread) {
                    EaseChatThreadActivity.actionStart(
                        context = mContext,
                        conversationId = thread.parentId,
                        threadId = thread.chatThreadId,
                        topicMsgId = thread.messageId,
                    )
                }
            })
        setChildSettings(builder)
        fragment = builder.build()
        fragment?.let {
            supportFragmentManager.beginTransaction().add(binding.root.id, it).commit()
        }
    }

    protected open fun setChildSettings(builder: EaseChatThreadListFragment.Builder) {}

    companion object {
        fun actionStart(context: Context,conversationId:String?) {
            val intent = Intent(context, EaseChatThreadListActivity::class.java)
            conversationId?.let {
                intent.putExtra(EaseConstant.EXTRA_CONVERSATION_ID,it)
            }
            EaseIM.getCustomActivityRoute()?.getActivityRoute(intent.clone() as Intent)?.let {
                if (it.hasRoute()) {
                    context.startActivity(it)
                    return
                }
            }
            context.startActivity(intent)
        }
    }
}