package heartbeat

class BootStrap {

    def init = { servletContext ->
        environments {
            development {
                new HeartBeat(
                        title: 'demo-string',
                        refreshRate: 5,
                        type: 'groovy',
                        display: 'text',
                        orderKey: 'A1',
                        style: "color: blue",
                        script: "new Date().seconds",
                ).save()
                new HeartBeat(
                        title: 'demo-graph',
                        refreshRate: 10,
                        type: 'groovy',
                        display: 'graph',
                        orderKey: 'A2',
                        style: "color: green",
                        script: "[[1,1],[2,4],[3,-1]]",
                ).save()
                new HeartBeat(
                        title: 'demo-piechart',
                        refreshRate: 3,
                        type: 'groovy',
                        display: 'piechart',
                        orderKey: 'B1',
                        style: "color: brown",
                        script: "[[1,35],[2,15],[3,40],[4,10]]",
                ).save()
            }
        }

    }
    def destroy = {
    }
}
