(function() {
    "use strict";
    var ws;
    if (!Modernizr.websockets) {
        alert("Your browser doesn't support Web Sockets! Check http://caniuse.com/#search=websockets for the supported browser!");
        return;
    }
    ws = new WebSocket("ws://bolgame.herokuapp.com/game");
    ws.onopen = function(event) {
        console.log("Connection is open");
    };

    ws.onmessage = function(event) {
        console.log(event.data);
        var data = JSON.parse(event.data),
            action = data.action;
        if (action === "join") {
            join(data.board, data.player, data.id);
        } else if (action === "go" || action === "wait") {
            turn(action, data.board, data.player, data.over, data.won, data.id);
        } else {
            console.error("Unknown action " + action);
        }
    };

    ws.onclose = function(event) {
        console.log("Connection is closed");
        var $waitingmsg = $("#waitingmsg");
        $waitingmsg.text("Connection to server is closed, either your opponent has left or due inactivity timeout. " +
            "Better to refresh the page to start a new game!");
    };

    function join(board, player, id) {
        console.log("Joined a new game " + board + " as player " + player + " for game " + id);

        drawBoard(board, player, id);
        $("#board").show();
    }

    function drawBoard(board, player, id) {
        var opponent = board[(player + 1) % 2],
            me = board[player];

        drawPlayer("opponent", opponent, id);
        drawPlayer("me", me, id);
    }

    function drawPlayer(who, player, id) {
        var $who = $("#" + who);
        $who.empty();
        var score = "<button class='score score-" + who + "' disabled>" + player[player.length - 1] + "</button>";
        if (who !== "me") {
            $who.append(score);
        } else {
            $who.append("&nbsp;<strong>-&gt;</strong>&nbsp;")
        }
        $.each(player.slice(0, -1), function (idx, stones) {
            var pit = who + idx;
            var $btn = $("<button class='pit' value='" + idx + "' id='" + pit + "' disabled>" + stones + "</button>");
            $btn.click(function() {
                pick($(this).val(), id);
            });
            $who.append($btn);
        });
        if (who === "me") {
            $who.append(score);
        } else {
            $who.append("&nbsp;<strong>&lt;-</strong>&nbsp;")
        }
    }

    function turn(action, board, player, over, won, id) {
        console.log("Turn '" + action + "' for game " + board + " as player " + player + " for game " + id);
        var $waitingmsg = $("#waitingmsg"),
            messages = {
                "Wait!": "Go!",
                "Go!": "Go Again!",
                "Go Again!": "Well done! One More!",
                "Well done! One More!": "You are superb!"},
            message;
        drawBoard(board, player, id);
        if (over) {
            console.log("Game " + id + "is over. You " + (won ? "won" : "didn't win" + "."));
            $waitingmsg.text((won ? "Congratulations! You won!" : "Keep trying!") + " (for a new game refresh a page)");
            return;
        }
        if (action === "go") {
            message = messages[$waitingmsg.text()] || "Go!";
            $waitingmsg.text(message);
            $("button[id^=me]").removeAttr("disabled");
        } else if (action === "wait") {
            $waitingmsg.text("Wait!");
        }
    }

    function pick(pit, id) {
        console.log("pick " + pit + " for " + id);
        ws.send(JSON.stringify({action: "pick", pit: pit, id: id}));
    }
})();