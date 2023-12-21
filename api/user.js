const express = require("express");
const router = express.Router();
const multer = require("multer");
const upload = multer();

// mongodb user model
const User = require("./../models/user");

//Password handler
const bcrypt = require("bcrypt");
//signup
router.post("/signup", upload.none(),(req, res) => {
  let { name, email, password } = req.body;
  email = email.trim();
  password = password.trim();

  if (name == "" || email == "" || password == "") {
    res.status(400).json({
      error: true,
      message: "Empty input fields!",
    });
  } else if (!/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(email)) {
    res.status(400).json({
      error: true,
      message: "Invalid email entered",
      });
  } else if (password.length < 8) {
    res.status(400).json({
      error: true,
      message: "Password is to short!",
    });
  } else {
    //checking if user already
    User.find({ email })
      .then((result) => {
        if (result.length) {
          // A user already exists
          res.status(400).json({
            error: true,
            message: "User with the provided email already exists",
          });
        } else {
          // Try to create new user
          // Password handling
          const saltRound = 10;
          bcrypt
            .hash(password, saltRound)
            .then((hashedPassword) => {
              const newUser = new User({
                name,
                email,
                password: hashedPassword,
              });
              newUser
                .save()
                .then((result) => {
                  res.status(201).json({
                    // status: "SUCCESS",
                    // message: "Signup successful",
                    // data: result,
                    error: false,
                    message: "User Created",
                  });
                })
                .catch((err) => {
                  res.status(400).status.json({
                    error: true,
                    message: "An error occurred while saving user account!",
                  });
                });
            })
            .catch((err) => {
              res.status(400).json({
                error: true,
                message: "An error occurred while hashing password!",
              });
            });
        }
      })
      .catch((err) => {
        console.log(err);
        res.status(400).json({
          error: true,
          message: "An error occurred while checking for existing user!",
        });
      });
  }
});
//signin
router.post("/signin", upload.none(), (req, res) => {
  let { email, password } = req.body;
  email = email.trim();
  password = password.trim();

  if (email == "" || password == "") {
    res.status(401).json({
      error: true,
      message: "Empty credentials supplied",
    });
  } else {
    //check if user exist
    User.find({ email })
      .then((data) => {
        if (data) {
          //user exists

          const hashedPassword = data[0].password;
          bcrypt
            .compare(password, hashedPassword)
            .then(async (result) => {
              const saltRound = 10;
              const token = await bcrypt.hash(
                JSON.stringify(data[0]._id),
                saltRound
              );
              if (result) {
                //password match
                res.status(200).json({
                  error: false,
                  message: "success",
                  loginResult: {
                    userId: data[0]._id,
                   name: data[0].name,
                   email: data[0].email,
                    token,
                  },
                });
              } else {
                res.status(401).json({
                 error: true,
                  message: "Invalid password entered!",
                });
              }
            })
            .catch((err) => {
              res.status(401).json({
                error: true,
                message: "An error occurred while comparing password",
              })
            });
        } else {
          res.status(401).json({
            error: true,
            message: "Invalid credentials entered!",
          });
        }
      })
      .catch((err) => {
        res.status(401).json({
          error: true,
          message: "An error occurred while checking for existing user",
        });
      });
  }
});

module.exports = router;
