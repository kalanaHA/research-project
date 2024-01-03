var area_1_Options = {
    series: [{
        name: "series1",
        data: [31, 40, 28, 51, 42, 109, 100],
    }],
    chart: {
        height: 350,
        type: "area",
    },
    dataLabels: {
        enabled: false,
    },
    stroke: {
        curve: "smooth",
    },
    xaxis: {
        type: "datetime",
        categories: [
            "2018-09-19T00:00:00.000Z",
            "2018-09-19T01:30:00.000Z",
            "2018-09-19T02:30:00.000Z",
            "2018-09-19T03:30:00.000Z",
            "2018-09-19T04:30:00.000Z",
            "2018-09-19T05:30:00.000Z",
            "2018-09-19T06:30:00.000Z",
        ],
    },
    tooltip: {
        x: {
            format: "dd/MM/yy HH:mm",
        },
    },
};

var area_2_Options = {
    series: [{
        name: "series1",
        data: [31, 40, 28, 51, 42, 109, 100],
    }],
    chart: {
        height: 350,
        type: "area",
    },
    dataLabels: {
        enabled: false,
    },
    stroke: {
        curve: "smooth",
    },
    xaxis: {
        type: "datetime",
        categories: [
            "2018-09-19T00:00:00.000Z",
            "2018-09-19T01:30:00.000Z",
            "2018-09-19T02:30:00.000Z",
            "2018-09-19T03:30:00.000Z",
            "2018-09-19T04:30:00.000Z",
            "2018-09-19T05:30:00.000Z",
            "2018-09-19T06:30:00.000Z",
        ],
    },
    tooltip: {
        x: {
            format: "dd/MM/yy HH:mm",
        },
    },
};

var area_3_Options = {
    series: [{
        name: "series1",
        data: [31, 40, 28, 51, 42, 109, 100],
    }],
    chart: {
        height: 350,
        type: "area",
    },
    dataLabels: {
        enabled: false,
    },
    stroke: {
        curve: "smooth",
    },
    xaxis: {
        type: "datetime",
        categories: [
            "2018-09-19T00:00:00.000Z",
            "2018-09-19T01:30:00.000Z",
            "2018-09-19T02:30:00.000Z",
            "2018-09-19T03:30:00.000Z",
            "2018-09-19T04:30:00.000Z",
            "2018-09-19T05:30:00.000Z",
            "2018-09-19T06:30:00.000Z",
        ],
    },
    tooltip: {
        x: {
            format: "dd/MM/yy HH:mm",
        },
    },
};



function getCharts() {

    var area1 = new ApexCharts(document.querySelector("#area1"), area_1_Options);
    var area2 = new ApexCharts(document.querySelector("#area2"), area_2_Options);
    var area3 = new ApexCharts(document.querySelector("#area3"), area_3_Options);

    area1.render();
    area2.render();
    area3.render();

    area1.destroy();
    area2.destroy();
    area3.destroy();

    let start_date = document.getElementById('datepicker1');
    let end_date = document.getElementById('datepicker2');
    let data = {
        "start_date": start_date.value,
        "end_date": end_date.value,
    }
    let jsonData = JSON.stringify(data);
    let req = new XMLHttpRequest();
    req.onreadystatechange = () => {
        if (req.readyState == 4) {
            let respJson = JSON.parse(req.responseText);
            let temperature = [];
            let oxygen = [];
            let heartrate = [];
            let dateList = [];
            console.log(respJson[0]);

            respJson.forEach(element => {
                temperature.push(element['BodyTemperature'])
                oxygen.push(element['BloodOxygenLevel'])
                heartrate.push(element['HeartRate'])
                dateList.push(element['created_time'])
            });

            area_1_Options['series'][0]['data'] = temperature;
            area_2_Options['series'][0]['data'] = oxygen;
            area_3_Options['series'][0]['data'] = heartrate;

            area_1_Options['xaxis']['categories'] = dateList;
            area_2_Options['xaxis']['categories'] = dateList;
            area_3_Options['xaxis']['categories'] = dateList;

            var area1 = new ApexCharts(document.querySelector("#area1"), area_1_Options);
            var area2 = new ApexCharts(document.querySelector("#area2"), area_2_Options);
            var area3 = new ApexCharts(document.querySelector("#area3"), area_3_Options);

            area1.render();
            area2.render();
            area3.render();
        }
    }

    req.open('POST', 'http://127.0.0.1/api/latest-readings', true)
    req.setRequestHeader("Content-Type", "application/json");
    req.send(jsonData);

}

getCharts();
