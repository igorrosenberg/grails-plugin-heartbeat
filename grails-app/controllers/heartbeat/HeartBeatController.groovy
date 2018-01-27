package heartbeat

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional(readOnly = true)
class HeartBeatController {

    def heartBeatService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond HeartBeat.list(params), model: [heartBeatCount: HeartBeat.count()]
    }

    def fetch(HeartBeat heartBeat) {
        if (!heartBeat) {
            println "params=$params"
            heartBeat = new HeartBeat()
            bindData(heartBeat, params)
        }
        println heartBeat.heartBeatParams.collect { it.name + '/' + it.value}.join()
        def dataX = heartBeatService.getData(heartBeat)
        render(contentType: "text/json") {
            data(dataX)
            style(heartBeat.style)
            refreshRate(heartBeat.refreshRate)
        }
    }

    def show(HeartBeat heartBeat) {
        respond heartBeat
    }

    def create() {
        respond new HeartBeat(params)
    }

    @Transactional
    def save(HeartBeat heartBeat) {
        if (heartBeat == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (heartBeat.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond heartBeat.errors, view: 'create'
            return
        }

        heartBeat.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'heartBeat.label', default: 'HeartBeat'), heartBeat.id])
                redirect heartBeat
            }
            '*' { respond heartBeat, [status: CREATED] }
        }
    }

    def edit(HeartBeat heartBeat) {
        respond heartBeat
    }

    @Transactional
    def update(HeartBeat heartBeat) {
        if (heartBeat == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (heartBeat.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond heartBeat.errors, view: 'edit'
            return
        }

        heartBeat.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'heartBeat.label', default: 'HeartBeat'), heartBeat.id])
                redirect heartBeat
            }
            '*' { respond heartBeat, [status: OK] }
        }
    }

    @Transactional
    def delete(HeartBeat heartBeat) {

        if (heartBeat == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        heartBeat.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'heartBeat.label', default: 'HeartBeat'), heartBeat.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'heartBeat.label', default: 'HeartBeat'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
