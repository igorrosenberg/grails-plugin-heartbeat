# grails-plugin-heartbeat

Grails Plugin exposing user-defined metrics (`HeartBeat`) as strings or graphs.

#### Features

* Requires JQuery and [ChartJS](http://www.chartjs.org)
* Display HeartBeats as String
* Display HeartBeats as Graph
* Auto-refresh
* CRUD for HeartBeats
* AJAX, GSP template, Controller, Service
* _No security_: you MUST control access yourself when invoking the HeartBeat (recommendation: add a SpringSecurity URL rule on /heartBeat)
* (upcoming) &lt;g:heartBeat&gt; TagLib
* (upcoming) HeartBeats with parameters

 
#### GUIs

A CRUD interface is available for your `HeartBeat`:

![Show view](https://raw.githubusercontent.com/igorrosenberg/grails-plugin-heartbeat/documentation/show.png)

Once you have created `HeartBeats`, you can use them anywhere in your application, for example in an admin page: 

![Display view](https://raw.githubusercontent.com/igorrosenberg/grails-plugin-heartbeat/documentation/display.png)

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
