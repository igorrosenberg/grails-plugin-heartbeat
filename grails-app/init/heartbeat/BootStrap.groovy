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
                        script: "new Date().toString()",
                ).save()
                new HeartBeat(
                        title: 'demo-graph',
                        refreshRate: 10,
                        type: 'groovy',
                        display: 'graph',
                        orderKey: 'A2',
                        style: "color: green",
                        script: "def t = new Date().time % 1000; [['X',t, 'red'],['Y',t+30, 'yellow'],['Z',t-60, 'blue']]",
                ).save()
                new HeartBeat(
                        title: 'demo-piechart',
                        refreshRate: 3,
                        type: 'groovy',
                        display: 'piechart',
                        orderKey: 'B1',
                        style: "color: brown",
                        script: "[['a',35, '#5DA5DA'],['b',15, '#60BD68'],['c',40, '#B276B2'],['d',10, '#DECF3F']]",
                ).save()

            }
        }

    }
    def destroy = {
    }
}
