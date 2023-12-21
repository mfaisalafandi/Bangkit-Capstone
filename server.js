//mongodb
require("./config/db");
const app = require("express")();
const router = require("./api/user");
const port = 8080;

const UserRouter = require("./api/user");
// For accepting post from data
const bodyParser = require("express").json;
app.use(bodyParser());
app.use("/user", router);

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
