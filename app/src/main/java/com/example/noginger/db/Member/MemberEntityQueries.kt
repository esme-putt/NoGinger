package com.example.noginger.db.Member

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter

public interface MemberEntityQueries: Transacter {
    public fun <T: Any> getMemberById(id: Long, mapper: (
        id: Long,
        name: String,
        cantEat: String?,
        diet: String?
    ) -> T): Query<T>

    public fun getMemberById(id: Long): Query<MemberEntity>

    public fun <T: Any> getAllMembers(mapper: (
        id: Long,
        name: String,
        cantEat: String?,
        diet: String?
    ) -> T): Query<T>

    public fun getAllMembers(): Query<MemberEntity>

    public fun insertMember(
        id: Long?,
        name: String,
        cantEat: String?,
        diet: String?
    ): Unit

    public fun deletePersonById(id: Long): Unit
}