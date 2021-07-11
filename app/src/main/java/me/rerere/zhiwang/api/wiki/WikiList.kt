package me.rerere.zhiwang.api.wiki


import com.google.gson.annotations.SerializedName

class WikiList : ArrayList<WikiList.WikiListItem>(){
    data class WikiListItem(
        @SerializedName("description")
        val description: String,
        @SerializedName("title")
        val title: String
    )
}