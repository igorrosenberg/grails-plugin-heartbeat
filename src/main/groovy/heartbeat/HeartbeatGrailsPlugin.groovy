package heartbeat

import grails.plugins.*

class HeartbeatGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.2.3 > *"

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/views/index.gsp",
            "grails-app/views/notFound.gsp",
            "grails-app/views/layouts/main.gsp",
    ]

    def title = "Heartbeat" // Headline display name of the plugin
    def author = "Igor Rosenberg"
    def authorEmail = ""
    def description = 'Grails Plugin exposing user-defined metrics as numbers or graphs'
    def profiles = ['web']

    // URL to the plugin's documentation
    def documentation = "https://github.com/igorrosenberg/grails-plugin-heartbeat"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE-2.0"

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GITHUB", url: "https://github.com/igorrosenberg/grails-plugin-heartbeat" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/igorrosenberg/grails-plugin-heartbeat" ]

}
