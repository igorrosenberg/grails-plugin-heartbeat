package heartbeat

class HeartBeat {

    String title      // name of the metric
    int refreshRate   // in seconds, typically 60
    String type       // sql or groovy
    String display    // graph or text
    String orderKey   // anything which could be used to sort/filter
    String style      // css
    String script     // code to execute
    Date dateCreated  // grails auto-managed

    static constraints = {
        title        blank: false
        refreshRate  min: 3
        type         inList: ['sql', 'groovy']
        display      inList: ['graph', 'text']
        orderKey     blank: true
        style        blank:true
        script       widget: 'textarea'
    }

    static mapping = {
        script type: 'text'
        style  type: 'text'
    }
}
