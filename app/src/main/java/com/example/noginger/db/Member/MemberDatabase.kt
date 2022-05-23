package com.example.noginger.db.Member

import com.example.noginger.db.newInstance
import com.example.noginger.db.schema
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver

public interface MemberDatabase: Transacter {
    public val memberEntityQueries: MemberEntityQueries

    public companion object {
        public val Schema: SqlDriver.Schema
            get() = MemberDatabase::class.schema

        public operator  fun invoke(driver: SqlDriver): MemberDatabase =
            MemberDatabase::class.newInstance(driver)
    }
}