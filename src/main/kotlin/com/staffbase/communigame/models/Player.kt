package com.staffbase.communigame.models

import kotlinx.serialization.Serializable

@Serializable
data class Player(val id: String, val name: String, val createTime: String, val topScore: Int) {

}
