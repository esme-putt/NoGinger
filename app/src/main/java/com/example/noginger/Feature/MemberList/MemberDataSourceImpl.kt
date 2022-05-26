package com.example.noginger.Feature.MemberList

import com.example.noginger.db.Member.MemberDatabase
import com.example.noginger.db.Member.MemberEntity
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MemberDataSourceImpl(db: MemberDatabase): MemberDataSource {

    private val queries = db.memberEntityQueries

    override suspend fun insertMember(name: String, cantEat: String?, diet: String?, intolerances: String?, id: Long?) {
        return withContext(Dispatchers.IO) {
            queries.insertMember(id, name, cantEat, diet, intolerances)
        }
    }

    override fun getAllMembers(): Flow<List<MemberEntity>> {
        return queries.getAllMembers().asFlow().mapToList()
    }
}