// insertion d'un graph ou texte auto-rafraîchit dans la page

/*global Chart*/

/* TODO: websocket ? */

(function () {
    'use strict';

    Chart.defaults.global.animation.duration = 0; // this should be set as specific for each HeartBeat graph

    $(document).ready(function () {

        // trace downtime
        function my_log(level, message) {
            console.log(new Date().toISOString() + ' ' + level + ": " + message);
        }

        // report something unexpected; pass '' to clear
        function error($this, message) {
            var $error = $this.siblings('.error');
            var previous = $error.text();
            if (previous === message) {
                return;
            }
            $error.text(message);
            if (message === '') {
                my_log("INFO ", $this.attr('data-heart-beat-title') + ", fixed");
                $this.children('.data').css('background-color', '');
            } else {
                my_log('ERROR', $this.attr('data-heart-beat-title') + ", " + message);
                $this.children('.data').css('background-color', 'red');
            }
        }

        // draw a graph (via chartjs)
        var plot_graph = function (labels, data, backgroundColors, $canvas_parent) {
            var chartConfig = {
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
                        backgroundColor: backgroundColors,
                        borderWidth: 1
                    }]
                }
            };
            plot_chart(chartConfig, $canvas_parent);
        };

        // draw a piechart (via chartjs)
        var plot_piechart = function (labels, data, backgroundColors, $canvas_parent) {
            var chartConfig = {
                type: 'pie',
                options: {
                    responsive: true
                },
                data: {
                    labels: labels,
                    datasets: [{
                        data: data,
                        backgroundColor: backgroundColors,
                        borderWidth: 1
                    }]
                }
            };
            plot_chart(chartConfig, $canvas_parent);
        };

        // draw any chart (via chartjs)
        var plot_chart = function (chartConfig, $canvas_parent) {
            var ctx;
            var $canvas;
            $canvas = $canvas_parent.children('canvas');
            if ($canvas.length === 0) {
                $canvas_parent.empty(); // could possibly optimize these 3 lines
                $canvas_parent.append('<canvas></canvas>');
                $canvas = $canvas_parent.children('canvas');
            }
            ctx = $canvas[0].getContext('2d');
            new Chart(ctx, chartConfig);// is a new needed each time ?
        };

        function plot_data($data, remoteData, plot_function) {
            var labels = [];
            var data = [];
            var backgroundColors = [];
            remoteData.forEach(
                function (item) {
                    var val = Object.values(item);
                    labels.push(val[0]);
                    data.push(val[1]);
                    if (val[2] !== undefined)
                        backgroundColors.push(val[2]);
                }
            );
            plot_function(labels, data, backgroundColors, $data);
        }

        function update($data, remoteData, display) {
            if (display === 'graph') {
                plot_data($data, remoteData, plot_graph);
            } else if (display === 'piechart') {
                plot_data($data, remoteData, plot_piechart);
            } else {
                $data.text(remoteData); // html-injection safe
            }
        }

        function fetchWaitLoop($this, infinite, url) {
            var refreshRate;
            infinite = (infinite === undefined ? true : infinite); // defaults to true, ie infinite loop
            if (!url) {
                // if url not provided, build url via attr & input params
                url = $this.attr('data-heart-beat-url');
                if (!url) {
                    error($this, "Could not read URL");
                    return;
                }
                // append params as found in named input
                url += '?' + $this.children('input').map(
                        function () {
                            var $sub_this = $(this);
                            return 'heartBeatParams.' + $sub_this.attr('name') + '=' + $sub_this.val();
                        }
                    ).get().join('&');
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
                    update($data, data.data, $this.attr('data-heart-beat-display'));
                    error($this, ''); // clear error
                }
            ).error(function (xhr, textStatus) {
                var message = (textStatus === 'error') ? 'http status ' + xhr.status : textStatus;
                error($this, message);
            }).always(function () {
                if (!infinite)
                    return;
                // call again after AJAX call = infinite loop
                refreshRate = $this.attr('data-heart-beat-refresh-rate');
                if (!refreshRate) {
                    error($this, "Could not read refresh rate");
                } else {
                    setTimeout(
                        function () {
                            fetchWaitLoop($this)
                        },
                        refreshRate * 1000
                    );
                }
            });


        }

        $('.heartBeat .box').each(
            function () {
                fetchWaitLoop($(this));
            }
        );

        // Only used in show/edit CRUD views
        $('.test.button').click(
            function () {
                // uselessly complicated - deals with show and create GSPs
                var get = function (name) {
                    return $('#' + name).val();
                };
                if (get('display') === undefined) {
                    get = function (name) {
                        return $('div[aria-labelledby=' + name + '-label]').text();
                    };
                }
                var getParams = function () {
                    return $('.fieldcontain ul li a').map(function () {
                        var array = $(this).attr('href').split('/');
                        return array[array.length - 1];
                    }).get().join();
                };

                var $this = $('.box');
                $this.attr('data-heart-beat-display', get('display'));
                var url = $this.attr('data-heart-beat-url');
                url += '?1';
                url += '&type=' + encodeURIComponent(get('type'));
                url += '&display=' + encodeURIComponent(get('display'));
                url += '&refreshRate=' + encodeURIComponent(get('refreshRate'));
                url += '&script=' + encodeURIComponent(get('script'));
                url += '&heartBeatParams=' + getParams();
                fetchWaitLoop($this, false, url);
                return false;
            });

    });
}());
