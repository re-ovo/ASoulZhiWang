package me.rerere.zhiwang.api.bilibili


import com.google.gson.annotations.SerializedName

data class BilibiliUser(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("ttl")
    val ttl: Int
) {
    data class Data(
        @SerializedName("birthday")
        val birthday: String,
        @SerializedName("coins")
        val coins: Int,
        @SerializedName("face")
        val face: String,
        @SerializedName("fans_badge")
        val fansBadge: Boolean,
        @SerializedName("is_followed")
        val isFollowed: Boolean,
        @SerializedName("jointime")
        val jointime: Int,
        @SerializedName("level")
        val level: Int,
        @SerializedName("live_room")
        val liveRoom: LiveRoom,
        @SerializedName("mid")
        val mid: Int,
        @SerializedName("moral")
        val moral: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("nameplate")
        val nameplate: Nameplate,
        @SerializedName("official")
        val official: Official,
        @SerializedName("pendant")
        val pendant: Pendant,
        @SerializedName("rank")
        val rank: Int,
        @SerializedName("sex")
        val sex: String,
        @SerializedName("sign")
        val sign: String,
        @SerializedName("silence")
        val silence: Int,
        @SerializedName("sys_notice")
        val sysNotice: SysNotice,
        @SerializedName("theme")
        val theme: Theme,
        @SerializedName("top_photo")
        val topPhoto: String,
        @SerializedName("user_honour_info")
        val userHonourInfo: UserHonourInfo,
        @SerializedName("vip")
        val vip: Vip
    ) {
        data class LiveRoom(
            @SerializedName("broadcast_type")
            val broadcastType: Int,
            @SerializedName("cover")
            val cover: String,
            @SerializedName("liveStatus")
            val liveStatus: Int,
            @SerializedName("online")
            val online: Int,
            @SerializedName("roomStatus")
            val roomStatus: Int,
            @SerializedName("roomid")
            val roomid: Int,
            @SerializedName("roundStatus")
            val roundStatus: Int,
            @SerializedName("title")
            val title: String,
            @SerializedName("url")
            val url: String
        )

        data class Nameplate(
            @SerializedName("condition")
            val condition: String,
            @SerializedName("image")
            val image: String,
            @SerializedName("image_small")
            val imageSmall: String,
            @SerializedName("level")
            val level: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("nid")
            val nid: Int
        )

        data class Official(
            @SerializedName("desc")
            val desc: String,
            @SerializedName("role")
            val role: Int,
            @SerializedName("title")
            val title: String,
            @SerializedName("type")
            val type: Int
        )

        data class Pendant(
            @SerializedName("expire")
            val expire: Int,
            @SerializedName("image")
            val image: String,
            @SerializedName("image_enhance")
            val imageEnhance: String,
            @SerializedName("image_enhance_frame")
            val imageEnhanceFrame: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("pid")
            val pid: Int
        )

        class SysNotice(
        )

        class Theme(
        )

        data class UserHonourInfo(
            @SerializedName("colour")
            val colour: Any,
            @SerializedName("mid")
            val mid: Int,
            @SerializedName("tags")
            val tags: Any
        )

        data class Vip(
            @SerializedName("avatar_subscript")
            val avatarSubscript: Int,
            @SerializedName("avatar_subscript_url")
            val avatarSubscriptUrl: String,
            @SerializedName("due_date")
            val dueDate: Int,
            @SerializedName("label")
            val label: Label,
            @SerializedName("nickname_color")
            val nicknameColor: String,
            @SerializedName("role")
            val role: Int,
            @SerializedName("status")
            val status: Int,
            @SerializedName("theme_type")
            val themeType: Int,
            @SerializedName("type")
            val type: Int,
            @SerializedName("vip_pay_type")
            val vipPayType: Int
        ) {
            data class Label(
                @SerializedName("bg_color")
                val bgColor: String,
                @SerializedName("bg_style")
                val bgStyle: Int,
                @SerializedName("border_color")
                val borderColor: String,
                @SerializedName("label_theme")
                val labelTheme: String,
                @SerializedName("path")
                val path: String,
                @SerializedName("text")
                val text: String,
                @SerializedName("text_color")
                val textColor: String
            )
        }
    }
}