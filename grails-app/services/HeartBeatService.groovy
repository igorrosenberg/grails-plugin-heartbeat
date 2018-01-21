package heartbeat

import groovy.sql.Sql

class HeartBeatService {

    def getData(HeartBeat metric) {
        if (metric.type == 'sql' && metric.display == 'text')
            return getSqlOne(metric.script)
        if (metric.type == 'sql' && metric.display == 'graph')
            return getSqlData(metric.script)
        if (metric.type == 'groovy')
            return getGroovyData(metric.script)
        throw new RuntimeException("Wrong metric config for $metric.id, type=$metric.type, display=$metric.display")
    }

    private getGroovyData(String script) {
        GroovyShell groovyShell = new GroovyShell()
        def data = groovyShell.evaluate script
        data
    }

    private getSqlData(String sqlStatement) {

        def data = []
        def columnCount
        HeartBeat.withNewSession { session ->
            def sql = new Sql(session.connection())
            sql.eachRow(sqlStatement) { row ->
                if (!columnCount) {
                    def metaData = row.getMetaData()
                    columnCount = metaData.columnCount -1
                }
                data << (0..columnCount).collect { row[it] }
            }
            sql.close()
        }
        data
    }

    private getSqlOne(String sqlStatement) {

        def data
        HeartBeat.withNewSession { session ->
            def sql = new Sql(session.connection())
            def res = sql.firstRow sqlStatement
            data = res[0]
            sql.close()
        }
        data as String
    }

}