// insertion d'un graph ou texte auto-rafraîchit dans la page

/*global Chart */      // fourni par chartjs
/*global refresh_rate */   // fourni par la GSP

/* TODO: deduplicate */
/* TODO: soft alert on error, hide when resolved */
/* TODO: websocket */


// ajax load strings
(function () {
    'use strict';

    Chart.defaults.global.animation.duration = 0;

    $(document).ready(function () {

        function error($this, message) {
            var previous;
            var $error = $this.siblings('.error');
            previous = $error.text();
            if (previous == message) {
                return;
            }
            $error.text(message)
            if (message != '') {
                console.log(new Date().toISOString() + " ERROR in " +$this.attr('data-heart-beat-title')+ ": " + message);
                $this.children('.data').css('background-color', 'red');
            } else {
                console.log(new Date().toISOString() + " INFO  in " +$this.attr('data-heart-beat-title')+ ": fixed");
                $this.children('.data').css('background-color', '');
            }
        }

        // draw a graph (via chartjs)
        var plot = function (labels, data, $canvas_parent) {
            var ctx;
            var $canvas ;
            $canvas = $canvas_parent.children('canvas');
            if ($canvas.length == 0) {
                $canvas_parent.empty();
                $canvas_parent.append('<canvas></canvas>');
                $canvas = $canvas_parent.children('canvas');
            }
            var ctx = $canvas[0].getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                options: {
                    scales: {yAxes: [{ticks: {beginAtZero: true}}]},
                    legend: {display: false},
                    tooltips: {enabled: true}
                },
                data: {
                    labels: labels,
                    datasets: [{
                        data: data,
                        /*
                         backgroundColor: [
                         'rgba(255, 99, 132, 0.2)',
                         'rgba(54, 162, 235, 0.2)',
                         'rgba(255, 206, 86, 0.2)',
                         'rgba(75, 192, 192, 0.2)',
                         'rgba(153, 102, 255, 0.2)',
                         'rgba(255, 159, 64, 0.2)'
                         ],
                         borderColor: [
                         'rgba(255,99,132,1)',
                         'rgba(54, 162, 235, 1)',
                         'rgba(255, 206, 86, 1)',
                         'rgba(75, 192, 192, 1)',
                         'rgba(153, 102, 255, 1)',
                         'rgba(255, 159, 64, 1)'
                         ],
                         */
                        borderWidth: 1
                    }]
                }
            });

        };



        function update($data, remoteData, display) {
            var labels = [];
            var data = [];
            if (display == 'graph') {
                remoteData.forEach(
                    function (item) {
                        labels.push(Object.values(item)[0]);
                        data.push(Object.values(item)[1]);
                    }
                );
                plot(labels, data, $data);
            } else {
                //$data.clear();
                $data.text(remoteData); // html-injection safe
            }
        }

        function fetchWaitLoop($this, infinite, url) {
            var refreshRate;
            infinite = infinite === undefined ? true : infinite; // defaults to true, ie infinite loop
            console.log("huhu, infinite ? " + (infinite));
            url = url || $this.attr('data-heart-beat-url'); // use attr if no url provided
            if (!url) {
                error($this, "Could not read URL")
            }
            $.ajax({
                url: url,
                timeout: 5000   // fail if not returned after 5s
            }).done(
                function (data) {
                    var $data;
                    $this.attr('data-heart-beat-refresh-rate', data.refreshRate);   // not null-safe
                    $data = $this.children('.data');
                    if ($data.attr('style') !== data.style) {
                        $data.attr('style', data.style); // probably not html-injection safe
                    }
                    update($data, data.data, $this.attr('data-heart-beat-display'))
                    error($this, ''); // clear error
                }
            ).fail(function (xhr, textStatus) {
                if (textStatus === 'error')
                    error($this, 'http status ' + xhr.status);
                else
                    error($this, textStatus);
            }).always(function(){
                console.log("huhu, stop? " + (!infinite));
                if (!infinite)
                    return;
                // call again after AJAX call = infinite loop
                refreshRate = $this.attr('data-heart-beat-refresh-rate');
                if (!refreshRate) {
                    error($this, "Could not read refresh rate")
                } else {
                    setTimeout(
                        function () {
                            fetchWaitLoop($this)
                        },
                        refreshRate * 1000
                    )
                }
            });


        }

        $('.heartBeat .box').each(
            function () {
                fetchWaitLoop($(this))
            }
        );

        // edit view (how can I limit this?)
        (function () {
            'use strict';
            $(document).ready(function () {
                $('.test.button').click(
                    function () {
                        var $this = $('.box');
                        $this.attr('data-heart-beat-display', $('#display').val());
                        var url = $this.attr('data-heart-beat-url');
                        url += '?1';
                        url += '&type=' + encodeURIComponent($('#type').val());
                        url += '&display=' + encodeURIComponent($('#display').val());
                        url += '&refreshRate=' + encodeURIComponent($('#refreshRate').val());
                        url += '&script=' + encodeURIComponent($('#script').val());
                        console.log('url=' + url);
                        fetchWaitLoop($this, false, url);
                        console.log('done');
                        false;
                    });
            });
        }());


    });
}());
