package com.example.noginger.db.Member

public data class MemberEntity(
    public val id: Long,
    public val name: String,
    public val cantEat: String?,
    public val diet: String?
) {
    override fun toString(): String = """
    |MemberEntity [
    |   id: $id
    |   name: $name
    |   cantEat: $cantEat
    |   diet: $diet
    """.trimMargin()
}