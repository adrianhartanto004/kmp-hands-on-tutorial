package com.adrian.kmphandsontutorial

import com.adrian.kmphandsontutorial.cache.Database
import com.adrian.kmphandsontutorial.cache.DatabaseDriverFactory
import com.adrian.kmphandsontutorial.entity.RocketLaunch
import com.adrian.kmphandsontutorial.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}
