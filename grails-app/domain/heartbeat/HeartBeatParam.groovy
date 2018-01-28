package heartbeat

/**
 * Optional parameters applied to a HeartBeat.
 */
class HeartBeatParam {

    String name      // name of the parameter
    String value     // the value to apply
    Date dateCreated  // grails auto-managed

    static belongsTo = [
            heartBeat: HeartBeat,
    ]

    static constraints = {
        name   blank: false
        value  blank: false
    }

    @Override
    String toString() {
        name
    }


}
