const httpServer = require("http").createServer();
const io = require("socket.io")(httpServer, {
  // ...
});

io.on("connection", (socket) => {
  // ...
});

httpServer.listen(3000);