function createGradleInvocationStatistics() {
    $.ajax({
        type: "GET",
        dataType: "jsonp",
        url: "http://localhost:8080/api/invocations/statistics/10",
        success: function (stats) {
            var dates= [];
            var nrOfInvocations = [];

            for (i = 0; i < stats.length; i++) {
                dates.push(new Date(stats[i].date).getDate().toLocaleString());
                nrOfInvocations.push(stats[i].nrOfInvocations);
            }

            var data = {
                labels : dates.reverse(),
                datasets : [
                    {
                        fillColor : "black",
                        strokeColor : "white",

                        data : nrOfInvocations.reverse()
                    }
                ]
            }


            var canvas = document.getElementById("gradle-invocations-chart")
            var context = canvas.getContext("2d");

            context.clearRect(0, 0, canvas.width, canvas.height);
            context.beginPath();
            canvas.width = $("#gradle-invocations-box").width()*0.9;
            canvas.height = 100;//$("#gradle-invocations-box").height();
            new Chart(context).Bar(data,options);

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error!')
        }
    });
}

function createGradleInvocations() {
    $.ajax({
        type: "GET",
        dataType: "jsonp",
        url: "http://localhost:8080/api/invocations/10",
        success: function (restInvocations) {

            var invocations = document.getElementById('gradle-invocations');
            invocations.innerHTML = '';
            for (i = 0; i < restInvocations.length; i++) {
                invocations.innerHTML += createInvocationHTMLString(restInvocations[i]);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error!')
        }
    });
}

function createUsers() {
    $.ajax({
        type: "GET",
        dataType: "jsonp",
        url: "http://localhost:8080/api/user",
        success: function (users) {
            document.getElementById('user-list').innerHTML = '';
            for (i = 0; i < users.length; i++) {
                document.getElementById('user-list').innerHTML += createUserHTMLString(users[i]);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error!')
        }
    });
}

function createTasks() {
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

function createInvocationHTMLString(restInvocation){
    return '<li class="gradle-invocation">'+new Date(restInvocation.date).toLocaleString() + ' : ' + restInvocation.userId + ' : ' + restInvocation.commandLineTasks;+'</li>';
}

function createUserHTMLString(user) {
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
    html = html.slice(0,-2)
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

function updateSite(){
    createGradleInvocations()
    createUsers()
    createTasks()
    setTimeout(updateSite, 1000)
}

createGradleInvocationStatistics()
updateSite()
