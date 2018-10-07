# grails-plugin-heartbeat

Grails Plugin exposing user-defined metrics (`HeartBeat`) as strings, graphs or piecharts.

#### Requirements

* Requires [Grails 3.x](https://grails.org/)
* Requires [JQuery](https://jquery.com/) and [ChartJS](http://www.chartjs.org)


#### Features

* Auto-refresh
* Display HeartBeats as String, graphs, or piecharts
* HeartBeats with parameters
* CRUD for HeartBeats
* Live configuration test within the CRUD GUI ("Test" button)
* AJAX, GSP template, Controller, Service
* _No security_: you MUST control access yourself when invoking the HeartBeat (recommendation: add a SpringSecurity URL rule on `/heartBeat`)
* _No security_: you MUST make sure the `script` invoked do no nasty things, like in groovy `System.exit()` 
* ~~(rejected) &lt;g:heartBeat&gt; TagLib~~

#### GUIs

A CRUD interface is available at `/heartbeat` :

![Show view](https://raw.githubusercontent.com/igorrosenberg/grails-plugin-heartbeat/documentation/show.png)

Once you have created `HeartBeats`, you can use them anywhere in your application, for example in an admin page: 

![Display view](https://raw.githubusercontent.com/igorrosenberg/grails-plugin-heartbeat/documentation/display.png)

The code used to insert `HeartBeats` like in the previous picture:

```
<g:set var="list" value="${heartbeat.HeartBeat.list().sort { it.orderKey }}"/>
<g:set var="hideButtons" value="${params.boolean('hideButtons')}"/>
<g:set var="hideTitle" value="${params.boolean('hideTitle')}"/>
<g:set var="hideButtons" value="${params.boolean('hideButtons')}"/>

<div class="my_data_line">
    <g:render 
        template="/heartBeat" 
        collection="${list.findAll { it.orderKey.startsWith 'A' }}"
        />
</div>

<div class="my_data_line">
    <g:render 
        template="/heartBeat" 
        collection="${list.findAll { it.orderKey.startsWith 'B' }}"
        />
</div>
```

#### Configuration

The `<g:render template="/heartBeat"/>` template reads the following variables:
- REQUIRED `it` (of type HeartBeat)
- OPTIONAL `hideButtons` if set to `true`, will hide the edit/delete buttons
- OPTIONAL `hideData` if set to `true`, will not show the `HeartBeat` data. It is hard to see why one would want to do this.
- OPTIONAL `hideErrors` if set to `true`, errors while retrieving `HeartBeat` data will not appear.
- OPTIONAL `hideTitle` if set to `true`, will not show the `HeartBeat` title

Within the `HeartBeat` database table, the available fields are:

* `title`: the name of the metric, which will be shown in HTML as the title of the block.
* `refreshRate`: how often to refresh the metric, in seconds. A typical value would be 60.
* `type`: currently only sql or groovy. It names the language used to interpret the script field.
* `display`: currently graph or text. In HTML, the former will show as a ChartJs canvas, while the later will show as a simple text.
* `orderKey`: used to sort or filter the HeartBeats. You may want to use `X-Y` matrix positioning, as in '5-3'. 
* `style`: a css set of properties, applied to the HTML div surronding the rendered HeartBeat, you may want to add coloring here, like `"text-color:green;"`.      // css
* `script`: back-slash escaped code to execute to fetch the data, written in SQL or Groovy. Examples provided below.

When using `HeartBeatParam`, theses are simply 
* `name`: the name of the parameters, used in the EL script. 
* `value`: value that replaces the parameter.

Scripts may come with parameters, using [Groovy's SimpleTemplateEngine](http://docs.groovy-lang.org/latest/html/documentation/template-engines.html#_simpletemplateengine).


#### HeartBeat SQL examples

```
-- Number of pets
select count(*) from Pet
```

```
-- Progession of number of pets on last 24h   
SELECT 
  to_char(ts, 'YYYY-MM-DD HH24:mm'),
  (select count(*) from pet where dateCreated < ts)
FROM
  generate_series(now () - interval '1 day', now(), interval '1 hour') ts
```

```
-- number of pets by keeper
SELECT 
  keeper.name, count(*)
FROM
  pets 
  inner join keeper USING keeper_id
```

#### HeartBeat Groovy examples

```
// Number of pets
Pet.count()
```


#### HeartBeatParam Groovy examples

```
// Number of pets older than parameter age
Pet.createCriteria().count { gt 'age', $age}
```

#### Installation

Within your own grails application:

* Add the dependency to the plugin, in `/build.gradle`
```
dependencies {
    compile "org.grails.plugins:heartbeat:2.1"
```

* Add the JavaScript code, in `/grails-app/assets/javascripts/application.js`
```
//= require jquery-2.2.0.min
//= require chartjs.min
//= require heartbeat
```

* Show the `HeartBeats`, by inserting within an access-protected GSP
```
<g:set var="list" value="${heartbeat.HeartBeat.list().sort { it.orderKey }}"/>

<div class="my_data_line">
    <g:render template="/heartBeat" collection="${list}" />
</div>
<g:link controller="heartBeat">HeartBeat Admin</g:link>
```

#### Development

```
git clone https://github.com/igorrosenberg/grails-plugin-heartbeat.git
gradle bootRun
```

A webserver then is running.
* Add a new `HeartBeat` at `http://localhost:8080/heartBeat/create`
* See it displayed at `http://localhost:8080/`

#### Help wanted

If you want to contribute, here are some ideas:

* (very easy) in ‘_hearbeat.gsp‘, add a "fetch now" button, see [issue #4](https://github.com/igorrosenberg/grails-plugin-heartbeat/issues/4)
* (easy) make CRUD-dedicated JS only applicable to heartbeat CRUD pages (search for `$('.test.button').click`), see [issue #5](https://github.com/igorrosenberg/grails-plugin-heartbeat/issues/5)
* (easy) Bug: "Test" button does not show http 500 when it Occurs within the "show" view, see [issue #6](https://github.com/igorrosenberg/grails-plugin-heartbeat/issues/6)
* (easy) in ‘_heartbeat.gsp‘, add a clock, showing how long before the data gets updated, see [issue #7](https://github.com/igorrosenberg/grails-plugin-heartbeat/issues/7)
* (easy) remove useless files from the plugin code, like `grails-app/assets`, see [issue #8](https://github.com/igorrosenberg/grails-plugin-heartbeat/issues/8)
* (easy) add pie-style charts, see [issue #3](https://github.com/igorrosenberg/grails-plugin-heartbeat/issues/3)
* (medium) remove jquery dependency, reverting to vanilla JavaScript, see [issue #9](https://github.com/igorrosenberg/grails-plugin-heartbeat/issues/9)
* (medium) Optimize JS, see [issue #10](https://github.com/igorrosenberg/grails-plugin-heartbeat/issues/10)
* (medium) Improve add param GUI (using default grails generate-view/controller feels clunky), see [issue #11](https://github.com/igorrosenberg/grails-plugin-heartbeat/issues/11)
* (hard) Make the js graph renderer configurable, so another chart library can be used, like [D3](https://github.com/d3/d3/wiki/Gallery
* (hard) auto-generate the doc: the README file is essentially copy-pasting the source code. It would be nice to simply dump the relevant code comments.
* (hard) caching results - is this possible without adding heavy dependencies, maintaining genericity and configurability? Is caching even desireable?

