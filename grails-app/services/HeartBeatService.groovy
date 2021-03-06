package heartbeat

import grails.transaction.Transactional
import groovy.sql.Sql
import groovy.text.SimpleTemplateEngine

class HeartBeatService {

    @Transactional(readOnly = true)
    def getData(HeartBeat metric) {

        String script = getExecutableScript(metric)

        // find proper execution engine
        if (metric.type == 'sql' && metric.display == 'text')
            return getSqlOne(script)
        if (metric.type == 'sql' && metric.display == 'graph')
            return getSqlData(script)
        if (metric.type == 'groovy')
            return getGroovyData(script)

        throw new RuntimeException("Wrong metric config for $metric.id, type=$metric.type, display=$metric.display")
    }

    private String getExecutableScript(HeartBeat metric) {
        if (!metric.heartBeatParams)
            return metric.script

        // else replace parameters (name -> value)
        def paramMap = metric.heartBeatParams.collectEntries {
            [(it.name): it.value]
        }
        def template = new SimpleTemplateEngine().createTemplate(metric.script)
        template.make(paramMap).toString()
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
                    columnCount = metaData.columnCount - 1
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