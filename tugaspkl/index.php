<script src="js/jquery.js"></script>
<script src="js/socket.io.js"></script>

<form onsubmit="return enterName();">
    <input id="name" placeholder="Masukkan Nama">
    <input type="submit">
</form>

<ul id="users"></ul>

<form onsubmit="return sendMessage();">
    <input id="message" placeholder="Masukkan Pesan">
    <input type="submit">
</form>

<ul id="messages"></ul>

<script>
    var io = io("http://localhost:3000");

    var receiver = "";
    var sender = "";

    function enterName() {
        var name = document.getElementById("name").value;

        io.emit("user_connected", name);

        sender = name;

        return false;
    }

    io.on("user_connected", (username) => {
        var html = "";
        html += "<li><button onclick='onUserSelected(this.innerHTML);'>" + username + "</button></li>";

        document.getElementById("users").innerHTML += html;
    });

    function onUserSelected(username) {
        receiver = username;

        $.ajax({
            url: "http://localhost:3000/get_messages",
            method: "POST",
            data: {
                sender: sender,
                receiver: receiver
            },
            success: function(res) {
                console.log(res);

                var messages = JSON.parse(res);
                var html = "";

                for(var a = 0; a < messages.length; a++) {
                    if(messages[a].sender == sender) {
                        html += "<li>You said: " + messages[a].message + "</li>";
                    }else if(messages[a].sender == receiver) {
                        html += "<li>" + messages[a].sender + " says: " + messages[a].message + "</li>";
                    }
                }

                document.getElementById("messages").innerHTML += html;
            }
        });
    }

    function sendMessage() {
        var message = document.getElementById("message").value;

        io.emit("send_message", sender, receiver, message);

        var html = "";
        html += "<li>You said: " + message + "</li>";

        document.getElementById("messages").innerHTML += html;

        return false;
    }

    io.on("new_message", (msg) => {
        var html = "";
        html += "<li>" + msg.sender + " says: " + msg.message + "</li>";

        document.getElementById("messages").innerHTML += html;
    });
</script>