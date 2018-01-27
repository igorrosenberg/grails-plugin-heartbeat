package heartbeat

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class HeartBeatParamController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond HeartBeatParam.list(params), model:[heartBeatParamCount: HeartBeatParam.count()]
    }

    def show(HeartBeatParam heartBeatParam) {
        respond heartBeatParam
    }

    def create() {
        respond new HeartBeatParam(params)
    }

    @Transactional
    def save(HeartBeatParam heartBeatParam) {
        if (heartBeatParam == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (heartBeatParam.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond heartBeatParam.errors, view:'create'
            return
        }

        heartBeatParam.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'heartBeatParam.label', default: 'HeartBeatParam'), heartBeatParam.id])
                redirect heartBeatParam
            }
            '*' { respond heartBeatParam, [status: CREATED] }
        }
    }

    def edit(HeartBeatParam heartBeatParam) {
        respond heartBeatParam
    }

    @Transactional
    def update(HeartBeatParam heartBeatParam) {
        if (heartBeatParam == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (heartBeatParam.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond heartBeatParam.errors, view:'edit'
            return
        }

        heartBeatParam.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'heartBeatParam.label', default: 'HeartBeatParam'), heartBeatParam.id])
                redirect heartBeatParam
            }
            '*'{ respond heartBeatParam, [status: OK] }
        }
    }

    @Transactional
    def delete(HeartBeatParam heartBeatParam) {

        if (heartBeatParam == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        heartBeatParam.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'heartBeatParam.label', default: 'HeartBeatParam'), heartBeatParam.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'heartBeatParam.label', default: 'HeartBeatParam'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
