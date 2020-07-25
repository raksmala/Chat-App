const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const { Client } = require('pg');
const { send } = require('process');

const http = require('http').createServer(app);
const io = require('socket.io')(http);

var users = [];

io.on("connection", (socket) => {
    console.log("User terhubung", socket.id);

    socket.on("user_connected", (username) => {
        users[username] = socket.id;

        io.emit("user_connected", username);
    });

    socket.on("send_message", (sender, receiver, message) => {
        var socketId = users[receiver];
        console.log(sender);
        console.log(receiver);
        console.log(message);

        let msg = {"sender": sender, "receiver": receiver, "message": message};

        io.to(socketId).emit("new_message", msg);

        client.query("insert into messages (sender, receiver, message) values ($1, $2, $3)", [sender, receiver, message], (err, results) => {

        });
    })
});

app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

const client = new Client({
  user: 'raksmala',
  host: 'localhost',
  database: 'tugas2pkl',
  password: 'raksmala',
  port: 5432,
});
client.connect();

    app.use(function(req, res, next){
        res.setHeader("Access-Control-Allow-Origin", "*");
        next();
    });

    app.post("/get_messages", (req, res) => {
        client.query("select * from messages where (sender = $1 and receiver = $2) or (sender = $2 and receiver = $1)", [req.body.sender, req.body.receiver], (err, messages) => {
            res.end(JSON.stringify(messages.rows));
        })
    });

    app.post('/register', (req, res) => {
        var name = req.body.name;
        var email = req.body.email;
        var password = req.body.password;
        var password2 = req.body.password2;
        
        console.log({
            name,
            email,
            password,
            password2
        });

        if (!name || !email || !password || !password2) {
            console.log("Semua kolom harus diisi");
        }
    
        if (password.length < 6) {
            console.log("Password harus lebih dari 6 karakter");
        }
    
        if (password !== password2) {
            console.log("Password tidak sama");
        }

        if(name !== null && email !== null && password !==null && password2 !==null ) {
            client.query(`select * from akun where email = $1`, [email], (err, result) => {
                if(result.rows.length > 0) {
                    console.log("Akun sudah terdaftar");
                    res.status(400).send();
                } else {
                    client.query(`insert into akun (name, email, password) values ($1, $2, $3) returning id, password`, [name, email, password], (err, results) => {
                        console.log("Berhasil Registrasi");
                        res.status(200).send();
                    });
                }
            });
        }
        
    });

    app.post('/login', (req, res) => {
        var email = req.body.email;
        var password = req.body.password;

        if(email && password) {
            client.query('SELECT * FROM akun WHERE email = $1 AND password = $2', [email, password], (err, result) => {
                if(result.rows.length > 0) {

                    const objToSend = {
                        name: result.rows[0]['name'],
                        email: result.rows[0]['email']
                    };
                    console.log("Berhasil Login");
                    console.log(objToSend);
                    res.status(200).send(JSON.stringify(objToSend));
                } else {
                    console.log("Gagal Login");
                    res.status(404).send();
                }
            }) ;
        } else {
            console.log("Harap masukkan email dan password");
        }
    });

    app.get('/listUser', (req, res) => {
        const akun = [];
        client.query('SELECT * from akun', (err, result) => {
            for(var a = 0; a < result.rows.length; a++) {
                akun.push(result.rows[a]);
            }
        console.log(akun);
        res.status(200).send(JSON.stringify(akun));
        });
    })

    app.post('/listChat', (req, res) => {
        var sender = req.body.sender;
        var receiver = req.body.receiver;
        const chat = [];
        client.query('SELECT * from akun a, messages m where (a.email = $1 and m.sender = $1 and m.receiver = $2) or (a.email = $2 and m.sender = $2 and m.receiver = $1)', [sender, receiver], (err, result) => {
            for(var a = 0; a < result.rows.length; a++) {
                chat.push(result.rows[a]);
            }
        console.log("List Chat Terkirim");
        console.log(chat);
        res.status(200).send(JSON.stringify(chat));
        });
    })

app.use(express.json());

http.listen(3000, () => {
    console.log("Server berjalan");
});