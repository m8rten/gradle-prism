function createStatistics() {
    $.ajax({
        type: "GET",
        dataType: "jsonp",
        url: "http://localhost:8080/api/invocations/statistics/10",
        success: function (stats) {
            var dates = [];
            var nrOfInvocations = [];
            var nrOfUsers = [];

            for (i = 0; i < stats.length; i++) {
                dates.push(new Date(stats[i].date).getDate().toLocaleString());
                nrOfInvocations.push(stats[i].nrOfInvocations);
                nrOfUsers.push(stats[i].nrOfUsers)
            }

            drawGraph("gradle-invocations-chart", dates, nrOfInvocations)
            drawGraph("gradle-user-chart", dates, nrOfUsers)
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error!')
        }
    });
}


function drawGraph(canvasId, lables, data) {
    var canvas = document.getElementById(canvasId)
    var context = canvas.getContext("2d");
    context.clearRect(0, 0, canvas.width, canvas.height);
    context.beginPath();
    canvas.width = $("#gradle-user-box").width() * 0.9;
    canvas.height = 200;
    new Chart(context).Bar({
        labels: lables,
        datasets: [
            {
                fillColor: "black",
                strokeColor: "white",

                data: data.reverse()
            }
        ]
    }, options);
}


function createListOfGradleInvocations() {
    $.ajax({
        type: "GET",
        dataType: "jsonp",
        url: "http://localhost:8080/api/invocations/10",
        success: function (restInvocations) {

            var invocations = document.getElementById('gradle-invocations');
            invocations.innerHTML = '';
            for (i = 0; i < restInvocations.length; i++) {
                invocations.innerHTML += invocationHTMLString(restInvocations[i]);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error!')
        }
    });
}

function createListOfUsers() {
    $.ajax({
        type: "GET",
        dataType: "jsonp",
        url: "http://localhost:8080/api/user",
        success: function (users) {
            document.getElementById('user-list').innerHTML = '';
            for (i = 0; i < users.length; i++) {
                document.getElementById('user-list').innerHTML += userHTMLString(users[i]);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error!')
        }
    });
}

function createListOfTasks() {
    $.ajax({
        type: "GET",
        dataType: "jsonp",
        url: "http://localhost:8080/api/task",
        success: function (tasks) {
            document.getElementById('task-list').innerHTML = '';
            for (i = 0; i < tasks.length; i++) {
                document.getElementById('task-list').innerHTML += createTaskHTMLString(tasks[i]);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error!')
        }
    });
}

function invocationHTMLString(restInvocation) {
    return '<li class="gradle-invocation">' + new Date(restInvocation.date).toLocaleString() + ' : ' + restInvocation.userId + ' : ' + restInvocation.commandLineTasks;
    +'</li>';
}

function userHTMLString(user) {
    var html = "<li class='big-list-item' id='" + user.userId + "'>" +
        "<dl>" +
        "    <dt>userId:</dt>" +
        "        <dd class='big-list-big-text'>" + user.userId + "</dd>" +
        "        <dt>last invocation:</dt>" +
        "        <dd>" + new Date(user.lastInvocation).toLocaleString() + "</dd>" +
        "        <dt>nr of invocations:</dt>" +
        "        <dd>" + user.nrOfInvocations + "</dd>" +
        "        <dt>tasks:</dt>" +
        "        <dd class='big-list-small-text'>";
    $.each(user.tasks, function (key, value) {
        html += '<b>' + value.name + ':</b> ' + value.nrOfInvocations + ', '
    })
    html = html.slice(0, -2)
    html += "        </dd>" +
        "    </dl>" +
        "</li>";
    return html
}

function createTaskHTMLString(task) {
    var html = "<li class='big-list-item' id='" + task.name + "'>" +
        "<dl>" +
        "    <dt>name:</dt>" +
        "        <dd class='big-list-big-text'>" + task.name + "</dd>" +
        "        <dt>last invocation:</dt>" +
        "        <dd>" + new Date(task.lastInvocation).toLocaleString() + "</dd>" +
        "        <dt>nr of invocations:</dt>" +
        "        <dd>" + task.nrOfInvocations + "</dd>" +
        "    </dl>" +
        "</li>";
    return html
}

function updateSite() {
    createListOfGradleInvocations()
    createListOfUsers()
    createListOfTasks()
    setTimeout(updateSite, 5000)
}

createStatistics()
updateSite()
