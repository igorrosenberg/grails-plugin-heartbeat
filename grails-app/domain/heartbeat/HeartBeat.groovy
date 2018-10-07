package heartbeat

class HeartBeat {

    String title      // name of the metric
    int refreshRate   // in seconds, typically 60
    String type       // sql or groovy
    String display    // graph, piechart or text
    String orderKey   // anything which could be used to sort/filter
    String style      // css
    String script     // back-slash escaped code to execute
    Date dateCreated  // grails auto-managed

    static constraints = {
        title        blank: false
        refreshRate  min: 3
        type         inList: ['sql', 'groovy']
        display      inList: ['graph', 'text', 'piechart']
        orderKey     blank: true
        style        blank:true, maxSize: 64000 // 64K is the max for SimpleTemplateEngine
        script       widget: 'textarea'
    }

    static hasMany = [
            heartBeatParams: HeartBeatParam, // optional parameters to apply
    ]

    static mapping = {
        script type: 'text'
        style  type: 'text'
    }

    @Override
    String toString() {
        title
    }

}
