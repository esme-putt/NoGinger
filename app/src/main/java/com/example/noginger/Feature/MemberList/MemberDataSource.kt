package com.example.noginger.Feature.MemberList

import com.example.noginger.db.Member.MemberEntity
import kotlinx.coroutines.flow.Flow

interface MemberDataSource {

    fun getAllMembers(): Flow<List<MemberEntity>>

    suspend fun insertMember(name: String, cantEat: String?, diet: String?, intolerances: String?, id: Long? = null)
}