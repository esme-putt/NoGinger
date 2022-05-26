package com.example.noginger.db

import com.example.noginger.db.Member.MemberDatabase
import com.example.noginger.db.Member.MemberEntity
import com.example.noginger.db.Member.MemberEntityQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import kotlin.reflect.KClass

internal val KClass<MemberDatabase>.schema: SqlDriver.Schema
    get() = MemberDatabaseImpl.Schema

internal fun KClass<MemberDatabase>.newInstance(driver: SqlDriver): MemberDatabase =
    MemberDatabaseImpl(driver)

private class MemberDatabaseImpl(
    driver: SqlDriver
): TransacterImpl(driver), MemberDatabase {

    public override val memberEntityQueries: MemberEntityQueriesImpl = MemberEntityQueriesImpl(this, driver)

    public object Schema: SqlDriver.Schema {
        public override val version: Int
            get() = 1

        override fun create(driver: SqlDriver): Unit {
            driver.execute(null, """
                |CREATE TABLE memberEntity(
                |   id INTEGER NOT NULL PRIMARY KEY,
                |   name TEXT NOT NULL,
                |   cantEat TEXT,
                |   diet TEXT,
                |   intolerances TEXT
                |)
                """.trimMargin(), 0)
        }

        override fun migrate(
            driver: SqlDriver,
            oldVersion: Int,
            newVersion: Int): Unit {
        }
    }
}

private class MemberEntityQueriesImpl(
    private val database: MemberDatabaseImpl,
    private val driver: SqlDriver
): TransacterImpl(driver), MemberEntityQueries {
    internal val getMemberById: MutableList<Query<*>> = copyOnWriteList()
    internal val getAllMembers: MutableList<Query<*>> = copyOnWriteList()

    public override fun <T: Any> getMemberById(id: Long, mapper: (
        id: Long,
        name: String,
        cantEat: String?,
        diet: String?,
        intolerances: String?
    ) -> T): Query<T> = GetMemberByIdQuery(id) { cursor ->
        mapper(
            cursor.getLong(0)!!,
            cursor.getString(1)!!,
            cursor.getString(2)!!,
            cursor.getString(3)!!,
            cursor.getString(4)!!
        )
    }

    public override fun getMemberById(id: Long): Query<MemberEntity> = getMemberById(id) { id_,
                                                                                           name, cantEat, diet, intolerances ->
       MemberEntity(
           id_,
           name,
           cantEat,
           diet,
           intolerances
       )
    }

    public override fun <T: Any> getAllMembers(mapper: (
        id: Long,
        name: String,
        cantEat: String?,
        diet: String?,
        intolerances: String?
    ) -> T): Query<T> = Query(-1418113920, getAllMembers, driver, "memberEntity.sq", "getAllMembers",
        """
        |SELECT *
        |FROM memberEntity
        """.trimMargin()) { cursor ->
        mapper(
            cursor.getLong(0)!!,
            cursor.getString(1)!!,
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4)
        )
    }

    public override fun getAllMembers(): Query<MemberEntity> = getAllMembers { id, name, cantEat, diet, intolerances ->
        print("Calling get all members - smaller func")
        MemberEntity(
            id,
            name,
            cantEat,
            diet,
            intolerances
        )
    }

    public override fun insertMember(
        id: Long?,
        name: String,
        cantEat: String?,
        diet: String?,
        intolerances: String?
    ): Unit {
        driver.execute(-2037177311, """
        |INSERT OR REPLACE
        |INTO memberEntity
        |VALUES(?, ?, ?, ?, ?)
        """.trimMargin(), 3) {
            bindLong(1, id)
            bindString(2, name)
            bindString(3, cantEat)
            bindString(4, diet)
            bindString(5, intolerances)
        }
        notifyQueries(-2037177311, {
            database.memberEntityQueries.getMemberById + database.memberEntityQueries.getAllMembers
        })
    }

    public override fun deletePersonById(id: Long): Unit {
        driver.execute(-1699858427, """
        | DELETE FROM memberEntity
        | WHERE id = ?
        """.trimMargin(), 1) {
            bindLong(1, id)
        }
        notifyQueries(-1699858427, {
            database.memberEntityQueries.getMemberById + database.memberEntityQueries.getAllMembers
        })
    }

    private inner class GetMemberByIdQuery<out T: Any>(
        public val id: Long,
        mapper: (SqlCursor) -> T
    ): Query<T>(getMemberById, mapper) {
        public override fun execute(): SqlCursor = driver.executeQuery(-1452605846, """
        |SELECT *
        |FROM memberEntity
        |WHERE id = ?
        """.trimMargin(), 1) {
            bindLong(1, id)
        }

        public override fun toString(): String = "memberEntity.sq:getMemberById"
    }
}