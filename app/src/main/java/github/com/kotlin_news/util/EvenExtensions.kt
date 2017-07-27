package github.com.kotlin_news.util

import org.greenrobot.eventbus.EventBus

fun Any.post(event: Any) {
    EventBus.getDefault().post(event)
}

fun Any.postSticky(event: Any) {
    EventBus.getDefault().postSticky(event)
}
