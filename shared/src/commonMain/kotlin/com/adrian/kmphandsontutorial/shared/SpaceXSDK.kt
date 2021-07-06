package com.adrian.kmphandsontutorial.shared

import com.adrian.kmphandsontutorial.shared.cache.Database
import com.adrian.kmphandsontutorial.shared.cache.DatabaseDriverFactory
import com.adrian.kmphandsontutorial.shared.entity.RocketLaunch
import com.adrian.kmphandsontutorial.shared.network.SpaceXApi

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
