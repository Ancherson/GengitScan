function genChart(type, title, labels, data){
    let canvas = document.createElement("canvas");
    document.body.appendChild(canvas);

    let context = canvas.getContext('2d');

    let parameters = {
        type: type,
        data: {
            labels: labels,
            datasets: [{
                label: title,
                backgroundColor: 'rgb(66, 135, 245)',
                borderColor: 'rgb(66, 135, 245)',
                data: data,
            }]
        },
        options: {}
    }
    if(type == "line") parameters.data.datasets[0].lineTension = 0;
    if(type == "pie"){
        let colors = [];
        for(let i = 0; i < data.length; i++){
            let c = 360*i/data.length;
            colors[i] = "hsl("+c+", 100%, 65%)";
        }
        parameters.data.datasets[0].backgroundColor = colors;
        parameters.data.datasets[0].hoverBackgroundColor = colors;
        parameters.data.datasets[0].borderColor = "white";
    }
    let chart = new Chart(context, parameters);
}


function genChart2(title, labels, datasets) {
    let canvas = document.createElement("canvas");
    document.body.appendChild(canvas);

    let context = canvas.getContext('2d');

    let parameters = {
        type: "bar",
        data: {
            labels: labels,
            datasets: [],
        },
        options: {
            scales: {
                yAxes: [{
                    stacked: true
                }],
                xAxes: [{
                    stacked: true
                }]
            }
        }
    }

    colors = [];
    for(let i = 0; i < datasets.length; i++){
        let c = 360*i/datasets.length;
        colors[i] = "hsl("+c+", 100%, 65%)";
    }

    for(let i = 0; i < datasets.length; i++) {
        console.log(datasets[i]);
        parameters.data.datasets.push({
            label: datasets[i][0],
            backgroundColor: colors[i],
            borderColor: colors[i],
            data: datasets[i][1],
        });
    }

    let chart = new Chart(context, parameters);
}