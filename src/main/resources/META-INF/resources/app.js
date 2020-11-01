// ./app.js

window.addEventListener('load', () => {
    var loc = window.location;
    var baseUrl = loc.protocol + "//" + loc.hostname + (loc.port? ":"+loc.port : "");

    console.log(baseUrl);
    var chart= null;
    var app = {
        pollLogo: document.querySelectorAll('.poll-logo'),
        items: ['Nutella', 'Peanut butter', 'Marmite', 'Jam'],
        // chart: null,
    }
    var dataPoints = [];
    //{label:'Nutella', y:0}, {label:'Peanut butter', y:0}, {label:'Marmite', y:0}, {label:'Jam', y:0}]
    app.items.forEach(item =>  dataPoints.push({label:item, y:0}));

    // Sends a POST request to the server
    app.handlePollEvent = function (event, index) {
        const item = this.items[index];
        $.post(baseUrl+'/vote', item, function(data) {
            console.log(`Voted ${item}`);
        });
    }

    // Sets up click events for
    // all the cards on the DOM
    app.setup = function () {
        this.pollLogo.forEach((pollBox, index) => {
            pollBox.addEventListener('click', (event) => {
                // Calls the event handler
                this.handlePollEvent(event, index)
            }, true)
        });

        this.setupChart();
    }

    //app.addData = function (data) { // Doesn't work ?!
    addData = function (data) {
        for (var i = 0; i < data.length; i++) {
            var item = data[i];
            var newValue = {
                label: item.name,
                y: item.tally
            }
            var idx = dataPoints.findIndex( function(dp) {
                return dp.label == item.name;
            });
            if (idx < 0) {
                dataPoints.push(newValue);
            } else {
                dataPoints[idx] = newValue;
            }

        }
        chart.render();
    }

    app.onEvent = function (event) {
        addData([ JSON.parse(event.data)]);
    }

    app.setupChart = function() {
        const chartContainer = document.querySelector('#chartContainer');

        if (chartContainer) {
            chart = new CanvasJS.Chart("chartContainer",
                {
                    animationEnabled: true,
                    theme: "theme2",
                    data: [
                        {
                            type: "column",
                            dataPoints: dataPoints
                        }
                    ]
                });
            chart.render();

            $.getJSON(baseUrl+'/votes', addData);

            var source = new EventSource("/vote-stream");
            source.onmessage = this.onEvent;
        }
    }

    app.setup();

})

